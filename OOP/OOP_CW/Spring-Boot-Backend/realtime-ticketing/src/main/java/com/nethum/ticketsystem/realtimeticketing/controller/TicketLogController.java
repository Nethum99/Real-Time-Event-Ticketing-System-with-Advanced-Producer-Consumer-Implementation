package com.nethum.ticketsystem.realtimeticketing.controller;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

@Controller
public class TicketLogController {

    private final SimpMessagingTemplate messagingTemplate;

    public TicketLogController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendLogToClients(String logMessage) {
        // Wrap the log message in a JSON object
        Map<String, String> message = new HashMap<>();
        message.put("message", logMessage);

        messagingTemplate.convertAndSend("/logs/ticket", message);
    }
    public void sendCountToClients(String countMessage) {
        messagingTemplate.convertAndSend("/counts/ticket", countMessage);
        System.out.println("For debugging int sencCountClients method "+countMessage);

    }


}
