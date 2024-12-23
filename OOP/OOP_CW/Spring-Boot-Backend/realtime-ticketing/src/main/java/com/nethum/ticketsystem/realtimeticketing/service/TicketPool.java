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
    private int ticketProduced = 0;     //How many tickets produced in system according latest user input
    private int ticketConsumed = 0;     //How many tickets consumed in system according latest user input
    private volatile boolean soldOut = false;  // boolean flag to tickets are soldout or not
    private final Lock lock = new ReentrantLock();

    private final TicketLogController ticketLogController;      //The class that have responsibilty to pass the logs throght websocket

    public TicketPool(int maxTicketCapacity, int totalTickets, TicketLogController ticketLogController){
        this.maxTicketCapacity = maxTicketCapacity;
        this.totalTickets = totalTickets;
        this.ticketLogController = ticketLogController;

        logger.info("Logger configured for TicketPool.");
        logger.fine("This is a FINE-level log for testing purposes.");
        logger.warning("This is a WARNING-level log for testing purposes.");
    }

    private void broadcastLog(String message) {     //This method declared in TicketLogController class and this is the checker of what data should be passed that class
        if (ticketLogController != null) {
            if (message.contains("Available total ticket count")) {
                String[] parts = message.split(":");
                String value = parts.length > 1 ? parts[1].trim() : "0";
                ticketLogController.sendCountToClients(
                        String.format("{\"type\":\"count\",\"key\":\"Available total ticket count\",\"value\":%s}", value)
                );
                logger.info("Broadcasting Available total ticket count: " + value);
            } else if (message.contains("Available ticket count in ticket pool")) {
                String[] parts = message.split(":");
                String value = parts.length > 1 ? parts[1].trim() : "0";
                ticketLogController.sendCountToClients(
                        String.format("{\"type\":\"count\",\"key\":\"Available ticket count in ticket pool\",\"value\":%s}", value)
                );
                logger.info("Broadcasting Available ticket count in ticket pool: " + value);
            } else {
                ticketLogController.sendLogToClients(message); // For other log messages
                logger.info("Broadcasting log message: " + message);
            }
        }
    }


    /**
     *
     * @param numberOfTickets
     * numberOfTickets similar value that the user enter ticket releasring rate.
     * This is the class create tickets
     * @return
     * @throws InterruptedException
     */
    public boolean createTicket(int numberOfTickets)throws InterruptedException{
        lock.lock();
        try{
            if(soldOut || ticketProduced>=totalTickets){
                logger.warning("Tickets are sold out. No more tickets can be produced.");
                return false;
            }


            int ticketToProduce = Math.min(numberOfTickets,maxTicketCapacity-ticketpool.size());
            ticketToProduce = Math.min(ticketToProduce,totalTickets-ticketProduced);    //This calculation work for calculate how many rest oftickets have to produce in the iteration

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


    /**
     * This is the method the deleting tickets from ticketpool otherwise this is the method that consumers buy tickets
     *
     * @param numberOfTickets
     * @return
     * @throws InterruptedException
     */
    public boolean consumeTicket(int numberOfTickets) throws InterruptedException{
        lock.lock();
        try{
            if(!ticketpool.isEmpty()){      //looking for ticketpool empty or not
                logger.info("\n>>> Tickets Consumed :");
            }
            for(int i = 0;i<numberOfTickets && !ticketpool.isEmpty();i++){
                UUID ticket = ticketpool.poll();    //deleting latest ticeket in queue and assign it ticket
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

    public boolean isSoldOut(){         //notice to other method tickets are soldout or not by returning "soldOut".
        lock.lock();
        try {
            return soldOut; //
        }
        finally {
            lock.unlock();
        }

    }

}