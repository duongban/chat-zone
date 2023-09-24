import React, { useRef, useEffect } from 'react';

const Messages = ({ messages, currentUser }) => {
    const messagesEndRef = useRef(null);

    // Function to scroll to the latest message
    const scrollToLatestMessage = () => {
        if (messagesEndRef.current) {
            messagesEndRef.current.scrollIntoView({ behavior: 'smooth' });
        }
    };

    // Scroll to the latest message when messages change
    useEffect(() => {
        scrollToLatestMessage();
    }, [messages]);

    let renderMessage = (message) => {
        const { sender, content, timestamp } = message;
        const messageFromMe = currentUser.username === message.sender;
        const className = messageFromMe ? "Messages-message currentUser" : "Messages-message";

        // Convert the timestamp to a human-readable format
        const timestampString = new Date(timestamp).toLocaleString();

        return (
            <li className={className} key={message.id}>
                <span className="avatar" />
                <div className="Message-content">
                    <div className="username">{sender}</div>
                    <div className="text">{content}</div>
                    <div className="timestamp">{timestampString}</div>
                </div>
            </li>
        );
    };

    return (
        <ul className="messages-list">
            {messages.map((msg) => renderMessage(msg))}
            <div ref={messagesEndRef} /> {/* Ref to the last message */}
        </ul>
    );
};

export default Messages;
