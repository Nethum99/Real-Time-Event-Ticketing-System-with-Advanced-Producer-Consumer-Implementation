import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TicketPool {

    private final ConcurrentLinkedQueue<Integer> ticketpool = new ConcurrentLinkedQueue<>();
    private final int maxTicketCapacity;
    private final int totalTickets;
    private int ticketProduced = 0;
    private int ticketToProduce = 0;
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

            ticketToProduce = Math.min(numberOfTickets,maxTicketCapacity-ticketpool.size());
            ticketToProduce = Math.min(ticketToProduce,totalTickets-ticketProduced);

            if(ticketToProduce>0){
                for(int i=0;i<ticketProduced;i++){
                    UUID ticketID = UUID.randomUUID();
                    ticketProduced++;
                    System.out.println("Ticket : "+ticketID+" is released.");
                }
            }
            else {
                System.out.println("Cannot produce tickets. Pool at max capacity or total tickets limit reached.");
            }
        }
        finally {
            lock.unlock();
        }
    }

    public boolean isSoldOut(){
        return  soldOut;
    }

    public synchronized int consumeTicket() throws InterruptedException{
        lock.lock();
        try{
            Integer item;
            while ((item = ticketpool.poll())==null){
                System.out.println("Ticket pool empty. Customer waiting.");
                Thread.sleep(100);
            }
            System.out.println("Consumed ticket: "+item+" Corrent Pool : "+ticketpool);
            return item;
        }
        finally {
            lock.unlock();
        }
    }

}
