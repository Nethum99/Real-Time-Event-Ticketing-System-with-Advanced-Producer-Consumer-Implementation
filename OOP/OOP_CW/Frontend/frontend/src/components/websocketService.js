import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";

const websocketService = {
  socket: null,

  connectToWebSocket: function (logCallback, countCallback) {
    if (this.socket) {
      console.log("WebSocket is already connected.");
      return this.socket;
    }

    // Initialize SockJS and STOMP client
    const socket = new SockJS("http://localhost:8004/websocket");
    this.socket = new Client({
      webSocketFactory: () => socket,
      reconnectDelay: 5000, // Reconnect every 5 seconds
      onConnect: () => {
        console.log("WebSocket connection established.");

        // Subscribe to log messages
        this.socket.subscribe("/logs/ticket", (message) => {
          console.log("Received log message:", message.body);
          if (logCallback) {
            try {
              const parsedMessage = JSON.parse(message.body); // Parse the structured JSON message
              logCallback(parsedMessage); // Pass the entire parsed object
            } catch (error) {
              console.error("Error parsing log message:", error.message, message.body);
            }
          }
        });

        // Subscribe to count updates
        this.socket.subscribe("/counts/ticket",(message)=> {
          console.log("Received count update:", message.body);
          if (countCallback) {
            try {
              const parsedMessage = JSON.parse(message.body); // Parse the structured JSON message
              countCallback(parsedMessage); // Pass the entire parsed object
            } catch (error) {
              console.error("Error parsing count message:", error.message, message.body);
            }
          }
        });
      },
      onStompError: (error) => {
        console.error("WebSocket STOMP error:", error);
      },
      onDisconnect: () => {
        console.log("WebSocket disconnected.");
      },
    });

    this.socket.activate();
    return this.socket;
  },

  sendMessage: function (destination, body) {
    if (this.socket && this.socket.connected) {
      this.socket.publish({
        destination: destination,
        body: JSON.stringify(body),
      });
      console.log(`Message sent to ${destination}:`, body);
    } else {
      console.error("WebSocket is not connected.");
    }
  },

  disconnectWebSocket: function () {
    if (this.socket) {
      this.socket.deactivate();
      console.log("WebSocket disconnected.");
      this.socket = null;
    }
  },
};

export default websocketService;
