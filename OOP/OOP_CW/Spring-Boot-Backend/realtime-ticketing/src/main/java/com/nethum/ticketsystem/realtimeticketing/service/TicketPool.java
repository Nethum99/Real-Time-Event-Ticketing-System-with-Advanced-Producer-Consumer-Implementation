package com.nethum.ticketsystem.realtimeticketing.service;

import com.nethum.ticketsystem.realtimeticketing.controller.TicketLogController;

import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TicketPool {

    private static final Logger logger = Logger.getLogger(TicketPool.class.getName());


    private final ConcurrentLinkedQueue<UUID> ticketpool = new ConcurrentLinkedQueue<UUID>();
    private final int maxTicketCapacity;
    private final int totalTickets;
    private int ticketProduced = 0;
    private int ticketConsumed = 0;
    private volatile boolean soldOut = false;
    private final Lock lock = new ReentrantLock();

    private final TicketLogController ticketLogController;

    public TicketPool(int maxTicketCapacity, int totalTickets, TicketLogController ticketLogController){
        this.maxTicketCapacity = maxTicketCapacity;
        this.totalTickets = totalTickets;
        this.ticketLogController = ticketLogController;

        logger.info("Logger configured for TicketPool.");
        logger.fine("This is a FINE-level log for testing purposes.");
        logger.warning("This is a WARNING-level log for testing purposes.");
    }

    private void broadcastLog(String message) {
        if (ticketLogController != null) {
            if (message.contains("Available total ticket count") || message.contains("Available ticket count in ticket pool")) {
                String[] parts = message.split(":");
                String key = parts[0].trim();
                String value = parts.length > 1 ? parts[1].trim() : "0";

                // Send structured message
                ticketLogController.sendLogToClients(String.format("{\"type\":\"count\",\"key\":\"%s\",\"value\":%s}", key, value));
            } else {
                // Send other log messages as is
                ticketLogController.sendLogToClients(message);
            }
        }
    }

    public boolean createTicket(int numberOfTickets)throws InterruptedException{
        lock.lock();
        try{
            if(soldOut || ticketProduced>=totalTickets){
                logger.warning("Tickets are sold out. No more tickets can be produced.");
                return false;
            }

            int ticketToProduce = Math.min(numberOfTickets,maxTicketCapacity-ticketpool.size());
            ticketToProduce = Math.min(ticketToProduce,totalTickets-ticketProduced);

            if(ticketToProduce>0){
                logger.info("\n>>> Tickets Released :");
                for(int i=0;i<ticketToProduce;i++){
                    UUID ticketID = UUID.randomUUID();
                    ticketpool.add(ticketID);
                    ticketProduced++;
                    logger.info("[TICKET ID : " + ticketID + " ]");

                    // Send real-time log to clients
                    broadcastLog("Ticket Released: " + ticketID);
                }
                logger.info("Available total ticket count: " + (totalTickets - ticketProduced));
                logger.info("Available ticket count in ticket pool: " + ticketpool.size());
                broadcastLog("Available total ticket count : "+(totalTickets-ticketProduced));
                broadcastLog("Available ticket count in ticket pool : "+ticketpool.size());
                return true;
            }
            else {
                logger.warning("Cannot produce tickets. Pool at max capacity or total tickets limit reached.");
                return false;
            }
        }
        finally {
            lock.unlock();

        }
    }



    public boolean consumeTicket(int numberOfTickets) throws InterruptedException{
       lock.lock();
       try{
           if(!ticketpool.isEmpty()){
            logger.info("\n>>> Tickets Consumed :");
           }
           for(int i = 0;i<numberOfTickets && !ticketpool.isEmpty();i++){
               UUID ticket = ticketpool.poll();
               if(ticket!=null){
                   logger.info("[CONSUMER: " + Thread.currentThread().getName() + "] consumed [TICKET ID " + ticket + " ]");
                   broadcastLog("[CONSUMER: "+Thread.currentThread().getName()+ "] consumed [TICKET ID "+ticket+" ]");
                   ticketConsumed++;
                   return true;
               }
               else{
                   logger.warning("Consumer "+Thread.currentThread().getName()+" is waiting");
               }

           }
           if(ticketConsumed>=totalTickets){
               logger.info("All tickets are sold out!. System is stopping");
               return false;
           }
           return true;
       }
       finally {
           lock.unlock();
       }
    }

    public boolean isSoldOut(){
     lock.lock();
     try {
         return soldOut;
     }
     finally {
         lock.unlock();
     }

    }

}
