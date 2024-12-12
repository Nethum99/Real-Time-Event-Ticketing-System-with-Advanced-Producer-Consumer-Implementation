import React, { useState, useEffect, useRef } from 'react';
import axios from 'axios';
import { Typography, TextField, Button, Paper, Box, Snackbar, Grid, List, ListItem, ListItemText } from '@mui/material';
import { Line } from 'react-chartjs-2';
import websocketService from '../components/websocketService';
import Navbar from './Navbar'; // Import the Navbar component
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
} from 'chart.js';

ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend);

const initial = {
  totalTickets: '100',
  maxCapacity: '10',
  ticketReleaseRate: '4',
  customerRetrievalRate: '6',
  activeCustomers: '8',
  activeVendors: '2',
};

const TicketCountCard = ({ title, count, color }) => (
  <Paper
    sx={{
      padding: '1rem',
      display: 'flex',
      flexDirection: 'column',
      alignItems: 'center',
      justifyContent: 'center',
      backgroundColor: color,
      color: 'white',
      borderRadius: '8px',
      boxShadow: '0 6px 10px rgba(0, 0, 0, 0.2)',
      width: '100%',
    }}
    elevation={3}
  >
    <Typography variant="h6" sx={{ fontWeight: 'bold', marginBottom: '0.5rem' }}>
      {title}
    </Typography>
    <Typography variant="h4" sx={{ fontWeight: 'bold' }}>
      {count || '0'} {/* Fallback for undefined values */}
    </Typography>
  </Paper>
);

const CreateTicketConfig = () => {
  const [form, setForm] = useState(initial);
  const [consoleLogs, setConsoleLogs] = useState([]);
  const [ticketCounts, setTicketCounts] = useState({
    totalAvailable: 0,
    poolAvailable: 0,
  });
  const [chartData, setChartData] = useState({
    labels: [],
    datasets: [
      {
        label: 'Total Available Tickets',
        data: [],
        borderColor: '#4CAF50',
        backgroundColor: 'rgba(76, 175, 80, 0.2)',
      },
      {
        label: 'Tickets in Pool',
        data: [],
        borderColor: '#FF5722',
        backgroundColor: 'rgba(255, 87, 34, 0.2)',
      },
    ],
  });
  const [snackbarOpen, setSnackbarOpen] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState('');
  const logEndRef = useRef(null);

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

  const getLogStyle = (log) => {
    if (log.toLowerCase().includes('consumed')) {
      return { color: 'white' };
    }
    if (log.toLowerCase().includes('ticket released')) {
      return { color: 'yellow' };
    }
    return { color: 'white' };
  };

  useEffect(() => {
    websocketService.connectToWebSocket(
      (logMessage) => {
        console.log('Log Callback:', logMessage);
        setConsoleLogs((prevLogs) => [...prevLogs, logMessage.message]);
      },
      (countMessage) => {
        console.log('Count Callback:', countMessage);
        if (countMessage.key === 'Available total ticket count') {
          setTicketCounts((prev) => ({ ...prev, totalAvailable: parseInt(countMessage.value, 10) }));
          setChartData((prev) => ({
            ...prev,
            labels: [...prev.labels, new Date().toLocaleTimeString()],
            datasets: [
              {
                ...prev.datasets[0],
                data: [...prev.datasets[0].data, parseInt(countMessage.value, 10)],
              },
              { ...prev.datasets[1] },
            ],
          }));
        } else if (countMessage.key === 'Available ticket count in ticket pool') {
          setTicketCounts((prev) => ({ ...prev, poolAvailable: parseInt(countMessage.value, 10) }));
          setChartData((prev) => ({
            ...prev,
            labels: [...prev.labels, new Date().toLocaleTimeString()],
            datasets: [
              { ...prev.datasets[0] },
              {
                ...prev.datasets[1],
                data: [...prev.datasets[1].data, parseInt(countMessage.value, 10)],
              },
            ],
          }));
        }
      }
    );

    return () => {
      websocketService.disconnectWebSocket();
    };
  }, []);

  useEffect(() => {
    if (logEndRef.current) {
      logEndRef.current.scrollIntoView({ behavior: 'smooth' });
    }
  }, [consoleLogs]);

  return (
    <Box sx={{ height: '100vh', display: 'flex', flexDirection: 'column', overflow: 'hidden' }}>
      {/* Navbar */}
      <Navbar />

      {/* Content Layout */}
      <Box sx={{ flex: 1, display: 'flex', flexDirection: 'row', gap: '1rem', padding: '1rem' }}>
        {/* Left Section - Form */}
        <Box sx={{ flex: '1', display: 'flex', flexDirection: 'column', gap: '0.5rem' }}>
          <Paper
            sx={{
              padding: '1.5rem',
              borderRadius: '8px',
              boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)',
              background: 'linear-gradient(to right, #E0E0E0, #FFFFFF)',
              border: '2px solid #B0BEC5',
            }}
          >
            <Typography sx={{ fontWeight: 'bold', fontSize: '1.5rem', textAlign: 'center', marginBottom: '1rem' }}>
              System Configuration
            </Typography>
            <form onSubmit={handleSubmit}>
              <Grid container spacing={2}>
                {Object.entries(initial).map(([key, value]) => (
                  <Grid item xs={12} key={key}>
                    <TextField
                      fullWidth
                      variant="outlined"
                      name={key}
                      value={form[key]}
                      onChange={handleFieldChange}
                      label={key.replace(/([A-Z])/g, ' $1').toUpperCase()}
                      required
                      sx={{
                        fontSize: '1.2rem',
                        borderRadius: '5px',
                      }}
                    />
                  </Grid>
                ))}
              </Grid>
              <Button
                fullWidth
                type="submit"
                variant="contained"
                sx={{ marginTop: '1rem', backgroundColor: '#4CAF50', color: 'white', '&:hover': { backgroundColor: '#388E3C' } }}
              >
                Submit
              </Button>
            </form>
          </Paper>

          <Paper sx={{ marginTop: '0.5rem', padding: '1rem', borderRadius: '8px', boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)' }}>
            <Typography variant="h6" sx={{ fontWeight: 'bold', marginBottom: '1rem', color: '#4B9CD3', fontSize: '1.2rem' }}>
              Field Descriptions
            </Typography>
            <Typography sx={{ marginBottom: '0.5rem', fontSize: '0.9rem', lineHeight: 1.5 }}>
              <strong>Total Number of Tickets:</strong> Maximum tickets all vendors can produce.
            </Typography>
            <Typography sx={{ marginBottom: '0.5rem', fontSize: '0.9rem', lineHeight: 1.5 }}>
              <strong>Max Capacity in Ticket Pool:</strong> Maximum tickets allowed in the pool at one time.
            </Typography>
            <Typography sx={{ marginBottom: '0.5rem', fontSize: '0.9rem', lineHeight: 1.5 }}>
              <strong>Ticket Release Rate:</strong> Number of tickets each vendor produces per unit time.
            </Typography>
            <Typography sx={{ marginBottom: '0.5rem', fontSize: '0.9rem', lineHeight: 1.5 }}>
              <strong>Customer Retrieval Rate:</strong> Number of tickets each customer retrieves per unit time.
            </Typography>
            <Typography sx={{ marginBottom: '0.5rem', fontSize: '0.9rem', lineHeight: 1.5 }}>
              <strong>Number of Active Customers:</strong> Consumers concurrently retrieving tickets.
            </Typography>
            <Typography sx={{ fontSize: '0.9rem', lineHeight: 1.5 }}>
              <strong>Number of Active Vendors:</strong> Producers concurrently generating tickets.
            </Typography>
          </Paper>
        </Box>

        {/* Right Section */}
        <Box sx={{ flex: '1', display: 'flex', flexDirection: 'column', gap: '1rem' }}>
          {/* Ticket Counts */}
          <Box sx={{ display: 'flex', gap: '1rem', marginBottom: '1rem', justifyContent: 'center' }}>
            <TicketCountCard title="Total Available Tickets" count={ticketCounts.totalAvailable} color="#4CAF50" />
            <TicketCountCard title="Tickets in Pool" count={ticketCounts.poolAvailable} color="#FF5722" />
          </Box>

          {/* Console Logs */}
          <Paper
            sx={{
              flex: 1,
              backgroundColor: '#000',
              color: '#fff',
              borderRadius: '8px',
              padding: '1rem',
              overflowY: 'auto',
              maxHeight: '25vh',
            }}
          >
            <Typography variant="h6" sx={{ textAlign: 'center', marginBottom: '1rem' }}>
              Console Logs
            </Typography>
            <List>
              {consoleLogs.map((log, index) => (
                <ListItem key={index} sx={{ padding: '0.5rem' }}>
                  <ListItemText
                    primary={<Typography sx={{ ...getLogStyle(log), fontSize: '0.875rem' }}>{log}</Typography>}
                  />
                </ListItem>
              ))}
              <div ref={logEndRef} />
            </List>
          </Paper>

          {/* Chart Section */}
          <Paper
            sx={{
              flex: 1,
              borderRadius: '8px',
              padding: '1rem',
              maxHeight: '30vh',
              boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)',
            }}
          >
            <Typography variant="h6" sx={{ textAlign: 'center', marginBottom: '1rem', fontWeight: 'bold' }}>
              Ticket Count Over Time
            </Typography>
            <Line
              data={chartData}
              options={{
                responsive: true,
                plugins: {
                  legend: {
                    position: 'top',
                  },
                  title: {
                    display: true,
                    text: 'Real-Time Ticket Data',
                  },
                },
                scales: {
                  x: {
                    title: {
                      display: true,
                      text: 'Time',
                    },
                  },
                  y: {
                    title: {
                      display: true,
                      text: 'Tickets',
                    },
                  },
                },
              }}
            />
          </Paper>
        </Box>
      </Box>

      <Snackbar open={snackbarOpen} autoHideDuration={4000} onClose={handleSnackbarClose} message={snackbarMessage} />
    </Box>
  );
};

export default CreateTicketConfig;
