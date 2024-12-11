// import React, { useState, useEffect } from "react";
// import axios from "axios";
// import { Typography, TextField, Button, Paper, Box, Snackbar, Grid, List, ListItem, ListItemText } from "@mui/material";
// import websocketService from "../components/websocketService";

// const initial = {
//   totalTickets: "",
//   maxCapacity: "",
//   ticketReleaseRate: "",
//   customerRetrievalRate: "",
//   activeCustomers: "",
//   activeVendors: "",
// };

// const CreateTicketConfig = () => {
//   const [form, setForm] = useState(initial);
//   const [consoleLogs, setConsoleLogs] = useState([]); // All logs go here
//   const [snackbarOpen, setSnackbarOpen] = useState(false);
//   const [snackbarMessage, setSnackbarMessage] = useState("");

//   const handleSnackbarClose = () => setSnackbarOpen(false);

//   const handleSubmit = async (e) => {
//     e.preventDefault();
//     const { totalTickets, maxCapacity, ticketReleaseRate, customerRetrievalRate, activeCustomers, activeVendors } = form;

//     if (!totalTickets || !maxCapacity || !ticketReleaseRate || !customerRetrievalRate || !activeCustomers || !activeVendors) {
//       setSnackbarMessage("Please fill in all required fields.");
//       setSnackbarOpen(true);
//       return;
//     }

//     try {
//       const response = await axios.post("http://localhost:8004/api/configuration/start", form, {
//         headers: { "Content-Type": "application/json" },
//       });
//       console.log(response.data);

//       if (websocketService.socket && websocketService.socket.connected) {
//         websocketService.sendMessage("/app/configStarted", { type: "configStarted", config: form });
//       } else {
//         console.error("WebSocket is not connected.");
//       }

//       setSnackbarMessage("Configuration started successfully.");
//       setSnackbarOpen(true);
//     } catch (error) {
//       console.error("Error submitting form:", error);
//       setSnackbarMessage("An error occurred while submitting the configuration. Please try again.");
//       setSnackbarOpen(true);
//     }
//   };

//   const handleFieldChange = (e) => {
//     const { name, value } = e.target;
//     setForm((prevForm) => ({ ...prevForm, [name]: value }));
//   };

//   useEffect(() => {
//     const updateConsoleLog = (message) => {
//       setConsoleLogs((prevLogs) => [...prevLogs, message]); // Add all messages to console logs
//     };

//     websocketService.connectToWebSocket((message) => {
//       // All WebSocket messages go to the console logs
//       updateConsoleLog(message);
//     });

//     return () => {
//       websocketService.disconnectWebSocket();
//     };
//   }, []);

//   return (
//     <Box sx={{ display: "flex", flexDirection: "row", height: "100vh" }}>
//       {/* Left Section */}
//       <Paper
//         sx={{
//           width: "40%",
//           padding: "1%",
//           display: "flex",
//           flexDirection: "column",
//           justifyContent: "flex-start",
//           height: "auto",
//         }}
//         elevation={1}
//       >
//         <Typography
//           sx={{
//             marginBottom: "0.5rem",
//             fontWeight: "bold",
//             fontSize: "1.2rem",
//             textAlign: "center",
//           }}
//         >
//           Create Ticket Configuration
//         </Typography>
//         <form autoComplete="off" noValidate onSubmit={handleSubmit}>
//           <Grid container spacing={2} sx={{ marginTop: "0.5rem" }}>
//             {[
//               { label: "Total Tickets", name: "totalTickets" },
//               { label: "Max Capacity", name: "maxCapacity" },
//               { label: "Ticket Release Rate", name: "ticketReleaseRate" },
//               { label: "Customer Retrieval Rate", name: "customerRetrievalRate" },
//               { label: "Active Customers", name: "activeCustomers" },
//               { label: "Active Vendors", name: "activeVendors" },
//             ].map((field, index) => (
//               <Grid item xs={6} key={index}>
//                 <TextField
//                   type="number"
//                   sx={{ width: "100%" }}
//                   name={field.name}
//                   onChange={handleFieldChange}
//                   label={field.label}
//                   variant="outlined"
//                   value={form[field.name]}
//                   required
//                   size="small"
//                 />
//               </Grid>
//             ))}
//           </Grid>
//           <Box sx={{ display: "flex", justifyContent: "center", marginTop: "0.5rem" }}>
//             <Button sx={{ width: "50%" }} variant="contained" type="submit">
//               Submit
//             </Button>
//           </Box>
//         </form>
//       </Paper>

//       {/* Right Section */}
//       <Box sx={{ width: "60%", display: "flex", flexDirection: "column", justifyContent: "space-between", paddingLeft: "1%" }}>
//         {/* Console Logs Section */}
//         <Paper sx={{ height: "35%", marginBottom: "1rem", padding: "1rem", overflow: "hidden" }} elevation={3}>
//           <Typography variant="h6" sx={{ marginBottom: "1rem", fontWeight: "bold", textAlign: "center" }}>
//             Console Logs
//           </Typography>
//           <List
//             sx={{
//               height: "calc(100% - 2rem)",
//               overflowY: "auto",
//               borderRadius: "4px",
//               backgroundColor: "#f9f9f9",
//               boxShadow: "inset 0px 2px 5px rgba(0, 0, 0, 0.1)",
//             }}
//           >
//             {consoleLogs.map((log, index) => (
//               <ListItem
//                 key={index}
//                 sx={{
//                   marginBottom: "0.5rem",
//                   padding: "8px",
//                   backgroundColor: "#e3f2fd",
//                   borderRadius: "4px",
//                   border: "1px solid #bbdefb",
//                   boxShadow: "1px 1px 3px rgba(0, 0, 0, 0.1)",
//                 }}
//               >
//                 <ListItemText
//                   primary={log}
//                   primaryTypographyProps={{
//                     fontSize: "0.9rem",
//                     fontFamily: "monospace",
//                     color: "#1565c0",
//                   }}
//                 />
//               </ListItem>
//             ))}
//           </List>
//         </Paper>

//         {/* Graph Section */}
//         <Paper sx={{ height: "60%", padding: "1rem" }} elevation={3}>
//           <Typography variant="h6" sx={{ textAlign: "center", fontWeight: "bold", marginBottom: "1rem" }}>
//             Graph
//           </Typography>
//           <Box sx={{ height: "100%", display: "flex", justifyContent: "center", alignItems: "center" }}>
//             <Typography>No data to display</Typography>
//           </Box>
//         </Paper>
//       </Box>

//       {/* Snackbar */}
//       <Snackbar
//         open={snackbarOpen}
//         message={snackbarMessage}
//         autoHideDuration={3000}
//         onClose={handleSnackbarClose}
//       />
//     </Box>
//   );
// };

// export default CreateTicketConfig;
