import React, { useState } from 'react';
import { TextField, Button } from '@mui/material';

const RegisterForm = ({ onSubmit }) => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const handleUsernameChange = (event) => {
        setUsername(event.target.value);
    };

    const handlePasswordChange = (event) => {
        setPassword(event.target.value);
    };

    const handleSubmit = (event) => {
        event.preventDefault();
        onSubmit(username, password);
    };

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
                Register
            </Button>

        </div>
    )
};

export default RegisterForm;
