package com.nethum.ticketsystem.realtimeticketing;

public class Vendor extends Thread{

    private final TicketPool ticketPool;
    private final int ticketReleaseRate;

    public Vendor(TicketPool ticketPool,int ticketReleaseRate){
        this.ticketPool = ticketPool;
        this.ticketReleaseRate = ticketReleaseRate;

    }

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
