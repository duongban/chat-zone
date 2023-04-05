import React from 'react'

const Messages = ({ messages, currentUser }) => {

    let renderMessage = (message) => {
        const { sender, content, timestamp } = message;
        const messageFromMe = currentUser.username === message.sender;
        const className = messageFromMe ? "Messages-message currentUser" : "Messages-message";

        // Convert the timestamp to a human-readable format
        const timestampString = new Date(timestamp).toLocaleString();

        return (
            <li className={className}>
                <span
                    className="avatar"
                />
                <div className="Message-content">
                    <div className="username">
                        {sender}
                    </div>
                    <div className="text">
                        {content}
                    </div>
                    <div className="timestamp">
                        {timestampString}
                    </div>
                </div>
            </li>
        );
    };

    return (
        <ul className="messages-list">
            {messages.map(msg => renderMessage(msg))}
        </ul>
    )
}


export default Messages