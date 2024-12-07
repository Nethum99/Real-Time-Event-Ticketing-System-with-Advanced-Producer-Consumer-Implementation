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
                System.out.println("\n>>> Tickets Released :");
                for(int i=0;i<ticketToProduce;i++){
                    UUID ticketID = UUID.randomUUID();
                    ticketpool.add(ticketID);
                    ticketProduced++;
                    System.out.println("[TICKET ID : "+ticketID+" ]");
                }
                System.out.println("Available total ticket count : "+(totalTickets-ticketProduced));
                System.out.println("Available ticket count in ticket pool : "+ticketpool.size());
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



    public boolean consumeTicket(int numberOfTickets) throws InterruptedException{
       lock.lock();
       try{
           if(!ticketpool.isEmpty()){
            System.out.println("\n>>> Tickets Consumed :");
           }
           for(int i = 0;i<numberOfTickets && !ticketpool.isEmpty();i++){
               UUID ticket = ticketpool.poll();
               if(ticket!=null){
                   System.out.println("[CONSUMER: "+Thread.currentThread().getName()+ "] consumed [TICKET ID "+ticket+" ]");
                   ticketConsumed++;
                   return true;
               }
               else{
                   System.out.println("Consumer "+Thread.currentThread().getName()+" is waiting");
               }

           }
           if(ticketConsumed>=totalTickets){
               System.out.println("All tickets are sold out!. System is stopping");
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
