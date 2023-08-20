import React, { useState } from 'react';
import Button from '@mui/material/Button';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import LoginForm from '../LoginForm';
import RegisterForm from '../RegisterForm';

const AuthForm = ({ onLogin, onRegister }) => {
    const [showLoginForm, setShowLoginForm] = useState(true);

    const handleLoginFormSubmit = (username, password) => {
        onLogin(username, password);
    };

    const handleRegisterFormSubmit = (username, password) => {
        onRegister(username, password);
    };

    const toggleForm = () => {
        setShowLoginForm(!showLoginForm);
    };

    return (
        <Box sx={{ maxWidth: 400, margin: '0 auto' }}>
            <Typography variant="h4" align="center" gutterBottom>
                {showLoginForm ? 'Login' : 'Register'}
            </Typography>
            {showLoginForm ? (
                <LoginForm onSubmit={handleLoginFormSubmit} />
            ) : (
                <RegisterForm onSubmit={handleRegisterFormSubmit} />
            )}
            <Box mt={2}>
                <Button onClick={toggleForm} fullWidth>
                    {showLoginForm
                        ? "Don't have an account? Register here"
                        : 'Already have an account? Login here'}
                </Button>
            </Box>
        </Box>
    );
};

export default AuthForm;
