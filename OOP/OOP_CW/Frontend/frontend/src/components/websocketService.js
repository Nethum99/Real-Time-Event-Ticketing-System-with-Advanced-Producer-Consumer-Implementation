import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";

const websocketService = {
  socket: null,

  connectToWebSocket: function (callback) {
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

        // Subscribe to the topic
        this.socket.subscribe("/logs/ticket", (message) => {
          console.log("Received message:", message.body);
          if (callback) {
            try {
              const parsedMessage = JSON.parse(message.body);
              const logMessage = parsedMessage.message; // Extract the user-friendly message
              callback(logMessage); // Pass user-friendly message to the callback
            } catch (error) {
              console.error("Error parsing WebSocket message:", error.message, message.body);
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