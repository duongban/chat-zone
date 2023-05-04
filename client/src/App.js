import React, { useState, useRef } from 'react';
import SockJsClient from 'react-stomp';
import './App.css';
import Input from './components/Input/Input';
import LoginForm from './components/LoginForm';
import Messages from './components/Messages/Messages';
import { randomColor } from './utils/common';


const SOCKET_URL = 'http://localhost:8085/ws/';

const App = () => {
  const [messages, setMessages] = useState([])
  const [user, setUser] = useState(null)
  const clientRef = useRef(null);

  let onConnected = () => {
    console.log("Connected!!")
  }

  let onDisconnected = () => {
    console.log("Disconnected!!")
  }

  let onMessageReceived = (msg) => {
    console.log('New Message Received!!', msg);
    setMessages(messages => messages.concat(msg));
  }

  let onSendMessage = (msgText) => {
    let msg = {
      sender: user.username,
      content: msgText
    }
    clientRef.current.sendMessage('/app/sendMessage', JSON.stringify(msg));
  }

  let handleLoginSubmit = (username) => {
    console.log(username, " Logged in..");

    setUser({
      username: username,
      color: randomColor()
    })
  }

  return (
    <div className="App">
      {!!user ?
        (
          <>
            <SockJsClient
              url={SOCKET_URL}
              topics={['/topic/group']}
              onConnect={onConnected}
              onDisconnect={onDisconnected}
              onMessage={msg => onMessageReceived(msg)}
              ref={(client) => (clientRef.current = client)}
            />
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
