import React, { useState, useEffect } from "react";
import axios from "axios";
import { Typography, TextField, Button, Paper, Box } from "@mui/material";
import websocketService from '../components/websocketService'; 

const initial = {
  totalTickets: "",
  maxCapacity: "",
  ticketReleaseRate: "",
  customerRetrievalRate: "",
  activeCustomers: "",
  activeVendors: "",
};

const CreateTicketConfig = () => {
  const [form, setForm] = useState(initial);
  const [consoleLogs, setConsoleLogs] = useState([]);
  const [ticketData, setTicketData] = useState(null);

  // Handle submit function
  const handleSubmit = async (e) => {
    e.preventDefault();
    const { totalTickets, maxCapacity, ticketReleaseRate, customerRetrievalRate, activeCustomers, activeVendors } = form;

    if (!totalTickets || !maxCapacity || !ticketReleaseRate || !customerRetrievalRate || !activeCustomers || !activeVendors) {
      alert("Please fill in all required fields.");
      return;
    }

    try {
      const response = await axios.post("http://localhost:8004/api/configuration/start", form, {
        headers: { "Content-Type": "application/json" },
      });
      console.log(response.data);
  
      // Sending message to WebSocket via STOMP
      websocketService.sendMessage('/app/configStarted', { type: 'configStarted', config: form });
      alert("Configuration started successfully.");
    } catch (error) {
      console.error("Error submitting form:", error);
      alert("An error occurred while submitting the configuration. Please try again.");
    }
  };

  // Handle change for form fields
  const handleFieldChange = (e) => {
    const { name, value } = e.target;
    setForm((prevForm) => ({ ...prevForm, [name]: value }));
  };

  // WebSocket connection and message handling
  useEffect(() => {
    const updateConsoleLog = (message) => {
      setConsoleLogs((prevLogs) => [...prevLogs, message]);
    };
  
    websocketService.connectToWebSocket((message) => {
      updateConsoleLog(message);
      setTicketData(message); // Assuming the message is JSON formatted
    });
  
    return () => {
      websocketService.disconnectWebSocket();
    };
  }, []);

  return (
    <Box sx={{ display: "flex", flexDirection: "row", height: "100vh" }}>
      {/* Left Section */}
      <Paper sx={{ width: "60%", padding: "2%", display: "flex", flexDirection: "column" }} elevation={0}>
        <Typography sx={{ marginBottom: "1rem" }} align="left" variant="h5">
          Create Ticket Configuration
        </Typography>
        <form autoComplete="off" noValidate onSubmit={handleSubmit}>
          <Box sx={{ display: "flex", flexDirection: "column", alignItems: "flex-start" }}>
            <TextField
              type="number"
              sx={{ width: "80%", marginBottom: "1rem" }}
              name="totalTickets"
              onChange={handleFieldChange}
              label="Total Tickets"
              variant="outlined"
              value={form.totalTickets}
              required
            />
            <TextField
              type="number"
              sx={{ width: "80%", marginBottom: "1rem" }}
              name="maxCapacity"
              onChange={handleFieldChange}
              label="Max Capacity"
              variant="outlined"
              value={form.maxCapacity}
              required
            />
            <TextField
              type="number"
              sx={{ width: "80%", marginBottom: "1rem" }}
              name="ticketReleaseRate"
              onChange={handleFieldChange}
              label="Ticket Release Rate"
              variant="outlined"
              value={form.ticketReleaseRate}
              required
            />
            <TextField
              type="number"
              sx={{ width: "80%", marginBottom: "1rem" }}
              name="customerRetrievalRate"
              onChange={handleFieldChange}
              label="Customer Retrieval Rate"
              variant="outlined"
              value={form.customerRetrievalRate}
              required
            />
            <TextField
              type="number"
              sx={{ width: "80%", marginBottom: "1rem" }}
              name="activeCustomers"
              onChange={handleFieldChange}
              label="Active Customers"
              variant="outlined"
              value={form.activeCustomers}
              required
            />
            <TextField
              type="number"
              sx={{ width: "80%", marginBottom: "1rem" }}
              name="activeVendors"
              onChange={handleFieldChange}
              label="Active Vendors"
              variant="outlined"
              value={form.activeVendors}
              required
            />
            <Button sx={{ width: "80%" }} variant="contained" type="submit">
              Submit
            </Button>
          </Box>
        </form>
      </Paper>

      {/* Right Section */}
      <Box sx={{ width: "40%", display: "flex", flexDirection: "column", justifyContent: "space-between" }}>
        {/* Console Logs Section */}
        <Paper sx={{ height: "30%", marginBottom: "1rem", padding: "1rem" }} elevation={1}>
          <Typography variant="h6">Console Logs</Typography>
          <Box sx={{ overflowY: "scroll", height: "100%" }}>
            {consoleLogs.map((log, index) => (
              <Typography key={index}>{log}</Typography>
            ))}
          </Box>
        </Paper>

        {/* Graph Section */}
        <Paper sx={{ height: "60%", padding: "1rem" }} elevation={1}>
          <Typography variant="h6">Graph</Typography>
          <Box sx={{ height: "100%" }}>
            {/* Graph content from WebSocket goes here */}
            {ticketData && <Typography>Ticket Data: {JSON.stringify(ticketData)}</Typography>}
          </Box>
        </Paper>
      </Box>
    </Box>
  );
};

export default CreateTicketConfig;
