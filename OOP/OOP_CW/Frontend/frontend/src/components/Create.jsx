import React, { useState, useEffect, useRef } from 'react';
import axios from 'axios';
import { Typography, TextField, Button, Paper, Box, Snackbar, Grid, List, ListItem, ListItemText, AppBar, Toolbar } from '@mui/material';
import websocketService from '../components/websocketService';

const initial = {
  totalTickets: '50',
  maxCapacity: '5',
  ticketReleaseRate: '2',
  customerRetrievalRate: '3',
  activeCustomers: '2',
  activeVendors: '3',
};

const TicketCountCard = ({ title, count, color }) => {
  return (
    <Paper
      sx={{
        padding: '1.5rem',
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        backgroundColor: color,
        color: 'white',
        borderRadius: '12px',
        boxShadow: '0 6px 10px rgba(0, 0, 0, 0.2)',
      }}
      elevation={3}
    >
      <Typography variant="h6" sx={{ fontWeight: 'bold', marginBottom: '0.5rem' }}>
        {title}
      </Typography>
      <Typography variant="h4" sx={{ fontWeight: 'bold' }}>
        {count}
      </Typography>
    </Paper>
  );
};

const CreateTicketConfig = () => {
  const [form, setForm] = useState(initial);
  const [consoleLogs, setConsoleLogs] = useState([]); // All logs go here
  const [ticketCounts, setTicketCounts] = useState({
    totalAvailable: 0,
    poolAvailable: 0,
  }); // Specific data for ticket counts
  const [snackbarOpen, setSnackbarOpen] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState('');
  const logEndRef = useRef(null); // Reference for auto-scrolling

  // Disable scrolling on load
  useEffect(() => {
    document.body.style.overflow = 'hidden';
    document.documentElement.style.overflow = 'hidden';

    // Scroll to top on first load
    window.scrollTo(0, 0);

    return () => {
      document.body.style.overflow = '';
      document.documentElement.style.overflow = '';
    };
  }, []);

  const handleSnackbarClose = () => setSnackbarOpen(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    const { totalTickets, maxCapacity, ticketReleaseRate, customerRetrievalRate, activeCustomers, activeVendors } = form;

    if (!totalTickets || !maxCapacity || !ticketReleaseRate || !customerRetrievalRate || !activeCustomers || !activeVendors) {
      setSnackbarMessage('Please fill in all required fields.');
      setSnackbarOpen(true);
      return;
    }

    try {
      const response = await axios.post('http://localhost:8004/api/configuration/start', form, {
        headers: { 'Content-Type': 'application/json' },
      });
      console.log(response.data);

      if (websocketService.socket && websocketService.socket.connected) {
        websocketService.sendMessage('/app/configStarted', { type: 'configStarted', config: form });
      } else {
        console.error('WebSocket is not connected.');
      }

      setSnackbarMessage('Configuration started successfully.');
      setSnackbarOpen(true);
    } catch (error) {
      console.error('Error submitting form:', error);
      setSnackbarMessage('An error occurred while submitting the configuration. Please try again.');
      setSnackbarOpen(true);
    }
  };

  const handleFieldChange = (e) => {
    const { name, value } = e.target;
    setForm((prevForm) => ({ ...prevForm, [name]: value }));
  };

  useEffect(() => {
    const updateConsoleLog = (message) => {
      console.log("Incoming message:", message); // Debug incoming message format

      try {
        const parsedMessage = JSON.parse(message); // Parse structured message
        if (parsedMessage.type === "count") {
          if (parsedMessage.key === "Available total ticket count") {
            setTicketCounts((prevCounts) => ({
              ...prevCounts,
              totalAvailable: parseInt(parsedMessage.value, 10),
            }));
          } else if (parsedMessage.key === "Available ticket count in ticket pool") {
            setTicketCounts((prevCounts) => ({
              ...prevCounts,
              poolAvailable: parseInt(parsedMessage.value, 10),
            }));
          }
        } else {
          setConsoleLogs((prevLogs) => [...prevLogs, message]); // Add non-count messages to console logs
        }
      } catch (e) {
        console.warn("Non-JSON message received:", message);
        setConsoleLogs((prevLogs) => [...prevLogs, message]); // Handle plain text messages
      }
    };

    websocketService.connectToWebSocket((message) => {
      updateConsoleLog(message);
    });

    return () => {
      websocketService.disconnectWebSocket();
    };
  }, []);

  useEffect(() => {
    if (logEndRef.current) {
      logEndRef.current.scrollIntoView({ behavior: 'smooth' });
    }
  }, [consoleLogs]); // Scroll to bottom whenever new logs are added

  const getLogStyle = (log) => {
    if (log.toLowerCase().includes('consumed')) {
      return { color: 'yellow' };
    }
    if (log.toLowerCase().includes('ticket created')) {
      return { color: 'red' };
    }
    return { color: 'white' };
  };

  return (
    <Box sx={{ height: '100vh', display: 'flex', flexDirection: 'column', overflow: 'hidden' }}>
      {/* Navigation Bar */}
      <AppBar position="static" sx={{ background: 'linear-gradient(to right, #ADD8E6, #4B9CD3)' }}>
        <Toolbar>
          <Typography
            variant="h6"
            component="div"
            sx={{
              flexGrow: 1,
              fontFamily: 'revert',
              fontSize: '1.5rem',
              fontWeight: 'bold',
              color: 'white',
            }}
          >
            Ticketing System
          </Typography>
          <Button
            variant="outlined"
            sx={{ color: 'white', borderColor: 'white', marginRight: '1rem' }}
            href="/start"
          >
            Start
          </Button>
          <Button variant="outlined" sx={{ color: 'white', borderColor: 'white' }} href="/stop">
            Stop
          </Button>
        </Toolbar>
      </AppBar>

      {/* Main Content */}
      <Box sx={{ flex: 1, display: 'flex', flexDirection: 'row', paddingTop: '2rem', overflow: 'hidden' }}>
        {/* Left Section */}
        <Paper
          sx={{
            width: '30%',
            padding: '1rem',
            display: 'flex',
            flexDirection: 'column',
            boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)',
            borderRadius: '8px',
            backgroundColor: '#ffffff',
          }}
          elevation={3}
        >
          <Typography
            sx={{
              marginBottom: '0.5rem',
              fontWeight: 'bold',
              fontSize: '1.5rem',
              textAlign: 'center',
            }}
          >
            System Configuration
          </Typography>
          <form autoComplete="off" noValidate onSubmit={handleSubmit}>
            <Grid container spacing={2} sx={{ marginTop: '0.5rem' }}>
              {[...Object.entries(initial)].map(([key, value], index) => (
                <Grid item xs={12} key={index}>
                  <TextField
                    type="number"
                    sx={{ width: '100%' }}
                    name={key}
                    onChange={handleFieldChange}
                    label={key}
                    variant="outlined"
                    value={form[key]}
                    required
                    size="small"
                  />
                </Grid>
              ))}
            </Grid>
            <Box sx={{ display: 'flex', justifyContent: 'center', marginTop: '1rem' }}>
              <Button sx={{ width: '80%' }} variant="contained" type="submit">
                Submit
              </Button>
            </Box>
          </form>
        </Paper>

        {/* Right Section */}
        <Box sx={{ width: '70%', display: 'flex', flexDirection: 'column', paddingLeft: '1rem' }}>
          {/* Ticket Counts Section */}
          <Box sx={{ display: 'flex', gap: '1rem', marginBottom: '1rem' }}>
            <TicketCountCard title="Total Tickets" count={ticketCounts.totalAvailable} color="#4CAF50" />
            <TicketCountCard title="Tickets in Pool" count={ticketCounts.poolAvailable} color="#FF9800" />
          </Box>

          {/* Console Logs Section */}
          <Paper
            sx={{
              height: '50%',
              marginBottom: '1rem',
              padding: '1rem',
              backgroundColor: 'black',
              boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)',
              borderRadius: '8px',
              display: 'flex',
              flexDirection: 'column',
            }}
            elevation={3}
          >
            <Typography
              variant="h6"
              sx={{
                marginBottom: '1rem',
                fontWeight: 'bold',
                textAlign: 'center',
                color: 'white',
                position: 'sticky', // Keep heading fixed
                top: 0,
                backgroundColor: 'black',
                zIndex: 1,
                padding: '0.5rem',
              }}
            >
              Console Logs
            </Typography>
            <List
              sx={{
                flex: 1,
                overflowY: 'auto', // Enable scrolling only inside logs
              }}
            >
              {consoleLogs.map((log, index) => (
                <ListItem
                  key={index}
                  sx={{
                    padding: '8px',
                    borderBottom: '1px solid rgba(255, 255, 255, 0.1)',
                  }}
                >
                  <ListItemText
                    primary={log}
                    primaryTypographyProps={{
                      fontSize: '0.9rem',
                      fontFamily: 'monospace',
                      ...getLogStyle(log),
                    }}
                  />
                </ListItem>
              ))}
              <div ref={logEndRef} />
            </List>
          </Paper>

          {/* Graph Section */}
          <Paper
            sx={{
              height: '25%',
              padding: '1rem',
              backgroundColor: '#ffffff',
              boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)',
              borderRadius: '8px',
            }}
            elevation={3}
          >
            <Typography variant="h6" sx={{ textAlign: 'center', fontWeight: 'bold', marginBottom: '1rem' }}>
              Graph
            </Typography>
            <Box sx={{ height: '100%', display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
              <Typography>No data to display</Typography>
            </Box>
          </Paper>
        </Box>
      </Box>

      {/* Snackbar */}
      <Snackbar
        open={snackbarOpen}
        message={snackbarMessage}
        autoHideDuration={3000}
        onClose={handleSnackbarClose}
      />
    </Box>
  );
};

export default CreateTicketConfig;
