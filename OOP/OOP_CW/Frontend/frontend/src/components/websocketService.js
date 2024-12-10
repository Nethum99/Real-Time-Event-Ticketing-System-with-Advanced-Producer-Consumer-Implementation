import { Client } from '@stomp/stompjs';

const websocketService = {
  stompClient: null,
  isConnected: false,

  connectToWebSocket: function (onMessageCallback) {
    if (this.isConnected) {
      console.log("WebSocket is already connected.");
      return;
    }

    this.stompClient = new Client({
      brokerURL: 'ws://localhost:8004/websocket', // STOMP WebSocket URL
      reconnectDelay: 5000, // Attempt to reconnect every 5 seconds if disconnected
      debug: (str) => console.log(str), // Enable debug logs
      onConnect: () => {
        console.log("STOMP connection established.");
        this.isConnected = true;

        // Subscribe to a topic (e.g., "/topic/messages")
        this.stompClient.subscribe('/topic/messages', (message) => {
          console.log("Received STOMP message:", message.body);
          if (onMessageCallback) onMessageCallback(JSON.parse(message.body));
        });
      },
      onDisconnect: () => {
        console.log("STOMP connection disconnected.");
        this.isConnected = false;
      },
      onStompError: (error) => {
        console.error("STOMP error:", error);
      },
    });

    this.stompClient.activate();
  },

  disconnectWebSocket: function () {
    if (this.stompClient && this.isConnected) {
      this.stompClient.deactivate();
      console.log("WebSocket disconnected.");
      this.isConnected = false;
    }
  },

  sendMessage: function (destination, message) {
    if (this.stompClient && this.isConnected) {
      this.stompClient.publish({
        destination, // e.g., "/app/sendMessage"
        body: JSON.stringify(message),
      });
      console.log("Message sent:", message);
    } else {
      console.error("WebSocket is not connected. Unable to send message.");
    }
  },
};

export default websocketService;
