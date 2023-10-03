import React, { useState } from 'react';
import {
  Button, TextField, Box, Container,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
} from '@mui/material';

const EnterRoom = ({ onCreateRoom, onEnterRoom, userRooms }) => {
  const [showCreate, setShowCreate] = useState(false);
  const [showEnter, setShowEnter] = useState(false);
  const [roomName, setRoomName] = useState('');
  const [roomCode, setRoomCode] = useState('');
  const [selectedRoom, setSelectedRoom] = useState(null);

  const handleRoomClick = (room) => {
    setSelectedRoom(room);
  };

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

      <h2>Your Rooms:</h2>
        <TableContainer component={Paper}>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>No.</TableCell>
                <TableCell>Room Name</TableCell>
                <TableCell>Room Code</TableCell>
                <TableCell>Enter room</TableCell>
                <TableCell>Copy room code</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {userRooms.map((room, index) => (
                <TableRow
                  key={room.code}
                  onClick={() => handleRoomClick(room)}
                  sx={{
                    backgroundColor:
                      selectedRoom && selectedRoom.code === room.code
                        ? '#e0e0e0'
                        : 'transparent',
                  }}
                >
                  <TableCell>{index + 1}</TableCell>
                  <TableCell>{room.name}</TableCell>
                  <TableCell>{room.code}</TableCell>
                  <TableCell>
                    <Button
                      variant="outlined"
                      onClick={() => onEnterRoom(room.code)}
                    >
                      Enter
                    </Button>
                  </TableCell>
                  <TableCell>
                    <Button
                      variant="outlined"
                      onClick={() => {
                        navigator.clipboard.writeText(room.code);
                      }}
                    >
                      Copy
                    </Button>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
    </Container>
  );
};

export default EnterRoom;
