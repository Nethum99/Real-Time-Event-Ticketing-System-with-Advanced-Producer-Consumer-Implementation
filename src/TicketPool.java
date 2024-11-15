import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TicketPool {

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
    }

    public boolean createTicket(int numberOfTickets)throws InterruptedException{
        lock.lock();
        try{
            if(soldOut || ticketProduced>=totalTickets){
                System.out.println("Tickets are sold out. No more tickets can be produced.");
                return false;
            }

            int ticketToProduce = Math.min(numberOfTickets,maxTicketCapacity-ticketpool.size());
            ticketToProduce = Math.min(ticketToProduce,totalTickets-ticketProduced);

            if(ticketToProduce>0){
                for(int i=0;i<ticketToProduce;i++){
                    UUID ticketID = UUID.randomUUID();
                    ticketpool.add(ticketID);
                    ticketProduced++;
                    System.out.println("Ticket : "+ticketID+" is released.");
                }
                return true;
            }
            else {
                System.out.println("Cannot produce tickets. Pool at max capacity or total tickets limit reached.");
                return false;
            }
        }
        finally {
            lock.unlock();
        }
    }



    public int consumeTicket(int numberOfTickets) throws InterruptedException{
       lock.lock();
       try{
           int ticketsConsumedInBatch = 0;

           for(int i = 0;i<numberOfTickets && !ticketpool.isEmpty();i++){
               UUID ticket = ticketpool.poll();
               if(ticket!=null){
                   System.out.println("Consumer consumed tocket "+ticket);
                   ticketConsumed++;
                   ticketsConsumedInBatch++;
               }
           }
           if(ticketConsumed>=totalTickets){
               System.out.println("All tickets are sld out!");
           }
           return ticketsConsumedInBatch;
       }
       finally {
           lock.unlock();
       }
    }

    public boolean isSoldOut(){
        return  soldOut;
    }

}
