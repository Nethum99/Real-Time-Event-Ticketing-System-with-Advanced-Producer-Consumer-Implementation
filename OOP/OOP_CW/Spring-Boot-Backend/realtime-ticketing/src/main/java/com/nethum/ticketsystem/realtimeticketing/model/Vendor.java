package com.nethum.ticketsystem.realtimeticketing.model;

import com.nethum.ticketsystem.realtimeticketing.service.TicketPool;

public class Vendor extends Thread{

    private final TicketPool ticketPool;
    private final int ticketReleaseRate;

    public Vendor(TicketPool ticketPool,int ticketReleaseRate){
        this.ticketPool = ticketPool;
        this.ticketReleaseRate = ticketReleaseRate;

    }

    /**
     * run method
     */
    @Override
    public void run(){
        while (!ticketPool.isSoldOut()){
            try{
                if(!ticketPool.createTicket(ticketReleaseRate)){
                    break;
                }
                Thread.sleep(500);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        System.out.println("Vendor thread ending as tickets are sold out.");
    }
}
