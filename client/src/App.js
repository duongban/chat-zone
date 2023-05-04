import React, { useState, useEffect } from 'react';
import './App.css';
import Input from './components/Input/Input';
import LoginForm from './components/LoginForm';
import Messages from './components/Messages/Messages';
import { randomColor } from './utils/common';
import SockJS from 'sockjs-client';
import Stomp from 'webstomp-client';

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

  let handleLoginSubmit = (username) => {
    console.log(username, " Logged in..");

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
        <LoginForm onSubmit={handleLoginSubmit} />
      }
    </div>
  )
}

export default App;
