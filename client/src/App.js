import React, { useState, useEffect } from 'react';
import './App.css';
import Input from './components/Input/Input';
import AuthForm from './components/AuthForm';
import Messages from './components/Messages/Messages';
import { randomColor } from './utils/common';
import SockJS from 'sockjs-client';
import Stomp from 'webstomp-client';
import chatHttpApi from './services/chatHttpApi';
import Popup from './components/Popup/Popup'
import Room from './components/Room';

const SOCKET_URL = 'http://localhost:8085/ws/';
let client;

const App = () => {
  const [messages, setMessages] = useState([])
  const [user, setUser] = useState(null)
  const [connectedUsers, setConnectedUsers] = useState([]);
  const [showErrorPopup, setShowErrorPopup] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');
  const [enteredRoom, setEnteredRoom] = useState(false);
  const [roomCode, setRoomCode] = useState(null);
  const [popupKey, setPopupKey] = useState(0);
  const [userRooms, setUserRooms] = useState([]);

  let onConnected = (username, roomCode) => {
    console.log("Connected!!")

    client.subscribe(`/user/queue/load-history`, (message) => {
      onMessageReceived(JSON.parse(message.body));
    });

    client.subscribe(`/topic/group/${roomCode}`, (message) => {
      onMessageReceived(JSON.parse(message.body));
    });

    sendNewUser(username, roomCode);

    client.subscribe(`/topic/newUser/${roomCode}`, (message) => {
      const msg = JSON.parse(message.body);
      if (msg.sender !== user.username) {
        setErrorMessage(`User ${msg.sender} has enter room`);
        setPopupKey((prevKey) => prevKey + 1);
      }
    });
  }

  let onDisconnected = () => {
    console.log("Disconnected!!")
  }

  let onMessageReceived = (msg) => {
    if (msg.content === 'newUser') {
      setConnectedUsers((users) => [...users, msg.sender]);
    } else {
      setMessages((messages) => messages.concat(msg));
    }
  }

  let onSendMessage = (msgText) => {
    let msg = {
      sender: user.username,
      content: msgText
    }
    client.send(`/app/sendMessage/${roomCode}`, JSON.stringify(msg));
  }

  let onError = (error) => {
    console.log('Failed to connect to WebSocket server', error);
  }

  let onErrorInvalidSession = (error) => {
    if (error === 6) {
      setTimeout(() => {
        window.location.reload();
      }, 1000);
    }
  }

  let handleLoginSubmit = (username, password) => {
    chatHttpApi.login(username, password).then(res => {
      if (res.data.err_code !== 0) {
        setErrorMessage(res.data.err_msg);
        setShowErrorPopup(true);
        return;
      }
      setUser({
        username: username,
        color: randomColor(),
        session: res.data.data.session,
      })
      handleGetUserRooms(res.data.data.session);
    }).catch(err => {
      console.log('Error login', err);
    })
  }

  let connectSocket = (roomCode) => {
    const socket = new SockJS(SOCKET_URL);
    client = Stomp.over(socket);
    client.debug = () => { };
    client.connect({}, () => onConnected(user.username, roomCode), onError);
    client.disconnect = onDisconnected;
    console.log(`Sucribe /topic/group/${roomCode}`);
  }

  let handleRegisterSubmit = (username, password) => {
    console.log(`Register username ${username} pass ${password}`)
    chatHttpApi.register(username, password).then(res => {
      if (res.data.err_code !== 0) {
        setErrorMessage(res.data.err_msg);
        setShowErrorPopup(true);
        return;
      }
      setErrorMessage("Register success");
      setShowErrorPopup(true);
    }).catch(err => {
      console.log('Error register', err);
    })
    setShowErrorPopup(false);
  }

  let sendNewUser = (username, roomCode) => {
    const msg = {
      sender: username,
      content: 'newUser',
    };
    client.send(`/app/newUser/${roomCode}`, JSON.stringify(msg));
  };

  const handleCreateRoom = (roomName) => {
    console.log(`Creating room: ${roomName}`);
    chatHttpApi.createRoom(roomName, user.session).then(res => {
      if (res.data.err_code !== 0) {
        setErrorMessage(res.data.err_msg);
        setShowErrorPopup(true);
        return;
      }
      setErrorMessage("Create room success, Room code " + res.data.data.code);
      setShowErrorPopup(true);
      setEnteredRoom(true);
      setRoomCode(res.data.data.code);
      connectSocket(res.data.data.code);
    });
    setShowErrorPopup(false);
  };

  const handleEnterRoom = (roomCode) => {
    chatHttpApi.findRoom(roomCode, user.session).then(res => {
      console.log(res.data);
      if (res.data.err_code !== 0) {
        setErrorMessage(res.data.err_msg);
        setShowErrorPopup(true);
        onErrorInvalidSession(res.data.err_code);
        return;
      }
      setErrorMessage("Enter room success, Room name " + res.data.data.name);
      setShowErrorPopup(true);
      setEnteredRoom(true);
      setRoomCode(res.data.data.code);
      connectSocket(res.data.data.code);
    });
    setShowErrorPopup(false);
  };

  const handleGetUserRooms = (session) => {
    chatHttpApi.getRoom(session).then(res => {
      console.log(res.data);
      if (res.data.err_code !== 0) {
        setErrorMessage(res.data.err_msg);
        setShowErrorPopup(true);
        onErrorInvalidSession(res.data.err_code);
        return;
      }
      setUserRooms(res.data.data);
    });
    setShowErrorPopup(false);
  };

  useEffect(() => {
    console.log(connectedUsers);
  }, [connectedUsers]);

  return (
    <div className="App">
      {!!user && !enteredRoom ? (
        <Room onCreateRoom={handleCreateRoom} onEnterRoom={handleEnterRoom} userRooms={userRooms}/>
      ) : null}

      {!!user && enteredRoom ? (
        <>
          <Messages messages={messages} currentUser={user} />
          <Input onSendMessage={onSendMessage} />
        </>
      ) : null}

      {!user && (
        <AuthForm onLogin={handleLoginSubmit} onRegister={handleRegisterSubmit} />
      )}

      {showErrorPopup && (
        <Popup message={errorMessage} key={popupKey} />
      )}
    </div>
  )
}

export default App;
