import React, { useState } from 'react';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';

const LoginForm = ({ onSubmit }) => {

    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const handleUsernameChange = (event) => {
        setUsername(event.target.value);
    };
    const handlePasswordChange = (event) => {
        setPassword(event.target.value);
    };

    let handleSubmit = () => {
        onSubmit(username, password);
    }

    return (
        <div>
            <TextField
                label="Type your username"
                placeholder="Username"
                onChange={handleUsernameChange}
                margin="normal"
            />
            <TextField
                label="Type your password"
                placeholder="Password"
                onChange={handlePasswordChange}
                margin="normal"
            />
            <br />
            <Button variant="contained" color="primary" onClick={handleSubmit} >
                Login
            </Button>

        </div>
    )
}

export default LoginForm
