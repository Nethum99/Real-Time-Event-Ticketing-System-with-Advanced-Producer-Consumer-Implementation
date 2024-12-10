import React from 'react';
import {
  AppBar,
  Toolbar,
  Box,
  Typography,
  Button
} from '@mui/material';

const Navbar = () => {
  return (
    <div>
      <AppBar position="static" sx={{ background: 'linear-gradient(to right, #ADD8E6, #4B9CD3)' }}>
        <Toolbar variant="dense">
          {/* Title Section */}
          <Typography
            variant="h4"
            component="div"
            sx={{
              flexGrow: 1,
              fontFamily: 'revert',
              fontSize: '1.5rem', // Adjust size for better readability
              color: 'black',
              textAlign: 'left',
              fontWeight: 'bold',
            }}
          >
            Ticketing System
          </Typography>

          {/* Navigation Buttons */}
          <Box sx={{ display: 'flex', gap: 2 }}>
            <Button
              variant="outlined"
              sx={{
                color: 'black',
                borderColor: '#4B9CD3',
                '&:hover': { borderColor: '#ADD8E6', backgroundColor: '#ADD8E6' },
              }}
              href='http://localhost:3001'
            >
              Start
            </Button>
            <Button
              variant="outlined"
              sx={{
                color: 'black',
                borderColor: '#4B9CD3',
                '&:hover': { borderColor: '#ADD8E6', backgroundColor: '#ADD8E6' },
              }}
              href='http://localhost:3008/create'
            >
              Stop
            </Button>
            <Button
              variant="outlined"
              sx={{
                color: 'black',
                borderColor: '#4B9CD3',
                '&:hover': { borderColor: '#ADD8E6', backgroundColor: '#ADD8E6' },
              }}
              href='https://telusko.com/'
            >
              Contact Us
            </Button>
          </Box>
        </Toolbar>
      </AppBar>
    </div>
  );
};

export default Navbar;
