package com.nethum.ticketsystem.realtimeticketing.controller;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class TicketLogController {

    private final SimpMessagingTemplate messagingTemplate;

    public TicketLogController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendLogToClients(String logMessage) {
        // Sends the log message to all clients subscribed to the "/logs/ticketUpdates" topic
        messagingTemplate.convertAndSend("/logs/ticket", logMessage);
    }
}
