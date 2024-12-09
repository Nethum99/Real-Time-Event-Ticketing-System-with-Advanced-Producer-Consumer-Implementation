package com.nethum.ticketsystem.realtimeticketing.service;

import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TicketPool {

    private static Logger logger = Logger.getLogger(TicketPool.class.getName());

    private final ConcurrentLinkedQueue<UUID> ticketpool = new ConcurrentLinkedQueue<UUID>();
    private final int maxTicketCapacity;
    private final int totalTickets;
    private int ticketProduced = 0;
    private int ticketConsumed = 0;
    private volatile boolean soldOut = false;
    private final Lock lock = new ReentrantLock();

    public TicketPool(int maxTicketCapacity,int totalTickets){
        this.maxTicketCapacity = maxTicketCapacity;
        this.totalTickets = totalTickets;

        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.FINE); // Set the handler's level to FINE
        logger.addHandler(consoleHandler);  // Attach the handler to the logger

        logger.setLevel(Level.FINE); // Set the logger's level to FINE
        logger.setUseParentHandlers(false); // Disable default parent handlers to avoid duplicate logs

        logger.info("Logger configured for TicketPool.");
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
                    logger.log(Level.FINE,"[TICKET ID : "+ticketID+" ]");
                }
                logger.log(Level.INFO,"Available total ticket count : "+(totalTickets-ticketProduced));
                logger.log(Level.INFO,"Available ticket count in ticket pool : "+ticketpool.size());
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
                   logger.log(Level.FINE,"[CONSUMER: "+Thread.currentThread().getName()+ "] consumed [TICKET ID "+ticket+" ]");
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
