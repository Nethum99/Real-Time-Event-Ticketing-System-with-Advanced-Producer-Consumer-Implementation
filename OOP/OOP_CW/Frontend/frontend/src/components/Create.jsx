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

const CreateTicketConfig = () => {
  const [form, setForm] = useState(initial);
  const [consoleLogs, setConsoleLogs] = useState([]); // All logs go here
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
      setConsoleLogs((prevLogs) => [...prevLogs, message]); // Add all messages to console logs
    };

    websocketService.connectToWebSocket((message) => {
      // All WebSocket messages go to the console logs
      updateConsoleLog(message);
    });

    return () => {
      websocketService.disconnectWebSocket();
    };
  }, []);

  const getLogStyle = (log) => {
    if (log.toLowerCase().includes('consumed')) {
      return { color: 'yellow' };
    }
    if (log.toLowerCase().includes('ticket created')) {
      return { color: 'red' };
    }
    return { color: 'white' };
  };

  useEffect(() => {
    if (logEndRef.current) {
      logEndRef.current.scrollIntoView({ behavior: 'smooth' });
    }
  }, [consoleLogs]); // Scroll to bottom whenever new logs are added

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
              {[
                { label: 'Total Tickets', name: 'totalTickets' },
                { label: 'Max Capacity', name: 'maxCapacity' },
                { label: 'Ticket Release Rate', name: 'ticketReleaseRate' },
                { label: 'Customer Retrieval Rate', name: 'customerRetrievalRate' },
                { label: 'Active Customers', name: 'activeCustomers' },
                { label: 'Active Vendors', name: 'activeVendors' },
              ].map((field, index) => (
                <Grid item xs={12} key={index}>
                  <TextField
                    type="number"
                    sx={{ width: '100%' }}
                    name={field.name}
                    onChange={handleFieldChange}
                    label={field.label}
                    variant="outlined"
                    value={form[field.name]}
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
          {/* Console Logs Section */}
          <Paper
            sx={{
              height: '40%',
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
              height: '55%',
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
