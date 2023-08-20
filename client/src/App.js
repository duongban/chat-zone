import React, { useState, useEffect } from 'react';
import './App.css';
import Input from './components/Input/Input';
import AuthForm from './components/AuthForm';
import Messages from './components/Messages/Messages';
import { randomColor } from './utils/common';
import SockJS from 'sockjs-client';
import Stomp from 'webstomp-client';
import chatAPI from './services/chatapi';

const SOCKET_URL = 'http://localhost:8085/ws/';
let client;

const App = () => {
  const [messages, setMessages] = useState([])
  const [user, setUser] = useState(null)
  const [connectedUsers, setConnectedUsers] = useState([]);


  let onConnected = (username) => {
    console.log("Connected!!")

    client.subscribe('/topic/group', (message) => {
      onMessageReceived(JSON.parse(message.body));
    });

    sendNewUser(username);
  }

  let onDisconnected = () => {
    console.log("Disconnected!!")
  }

  let onMessageReceived = (msg) => {
    console.log('New Message Received!!', msg);
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
    client.send('/app/sendMessage', JSON.stringify(msg));
  }

  let onError = (error) => {
    console.log('Failed to connect to WebSocket server', error);
  }

  let handleLoginSubmit = (username, password) => {
    console.log(username, " Logged in..");
    console.log(`Login pass ${password}`)

    setUser({
      username: username,
      color: randomColor()
    })

    const socket = new SockJS(SOCKET_URL);
    client = Stomp.over(socket);
    client.debug = () => { };
    client.connect({}, () => onConnected(username), onError);
    client.disconnect = onDisconnected;

  }

  let handleRegisterSubmit = (username, password) => {
    console.log(`Register username ${username} pass ${password}`)
    chatAPI.register(username, password).then(res => {
      console.log('Register', res);
    }).catch(err => {
      console.log('Error register', err);
    })
  }

  let sendNewUser = (username) => {
    const msg = {
      sender: username,
      content: 'newUser',
    };
    client.send('/app/newUser', JSON.stringify(msg));
  };

  useEffect(() => {
    console.log(connectedUsers);
  }, [connectedUsers]);

  return (
    <div className="App">
      {!!user ?
        (
          <>
            <Messages
              messages={messages}
              currentUser={user}
            />
            <Input onSendMessage={onSendMessage} />
          </>
        ) :
        <AuthForm onLogin={handleLoginSubmit} onRegister={handleRegisterSubmit} />
      }
    </div>
  )
}

export default App;
