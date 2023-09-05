import React, { useState } from 'react';
import { Button, TextField, Box, Container } from '@mui/material';

const EnterRoom = ({ onCreateRoom, onEnterRoom }) => {
  const [showCreate, setShowCreate] = useState(false);
  const [showEnter, setShowEnter] = useState(false);
  const [roomName, setRoomName] = useState('');
  const [roomCode, setRoomCode] = useState('');

  const toggleCreate = () => {
    setShowCreate(!showCreate);
    setShowEnter(false);
    setRoomName('');
    setRoomCode('');
  };

  const toggleEnter = () => {
    setShowEnter(!showEnter);
    setShowCreate(false);
    setRoomName('');
    setRoomCode('');
  };

  const handleCreateRoom = () => {
    if (roomName.trim() !== '') {
      onCreateRoom(roomName);
      toggleCreate();
    }
  };

  const handleEnterRoom = () => {
    if (roomCode.trim() !== '') {
      onEnterRoom(roomCode);
      toggleEnter();
    }
  };

  return (
    <Container maxWidth="sm">
      <Box mt={4} display="flex" flexDirection="column" alignItems="center">
        {(!showEnter && !showCreate) && (
          <Button variant="contained" onClick={toggleCreate}>
            Create room
          </Button>
        )}
        {showCreate && (
          <Box mt={2} display="flex" flexDirection="column" alignItems="center">
            <TextField
              label="Enter room name"
              variant="outlined"
              value={roomName}
              onChange={(e) => setRoomName(e.target.value)}
            />
            <Button variant="contained" onClick={handleCreateRoom} style={{ marginTop: '10px' }}>
              Create
            </Button>
          </Box>
        )}
      </Box>

      <Box mt={4} display="flex" flexDirection="column" alignItems="center">
        {(!showEnter && !showCreate) && (
          <Button variant="contained" onClick={toggleEnter}>
            Enter room
          </Button>
        )}
        {showEnter && (
          <Box mt={2} display="flex" flexDirection="column" alignItems="center">
            <TextField
              label="Enter room code"
              variant="outlined"
              value={roomCode}
              onChange={(e) => setRoomCode(e.target.value)}
            />
            <Button variant="contained" onClick={handleEnterRoom} style={{ marginTop: '10px' }}>
              Enter
            </Button>
          </Box>
        )}
      </Box>
    </Container>
  );
};

export default EnterRoom;
