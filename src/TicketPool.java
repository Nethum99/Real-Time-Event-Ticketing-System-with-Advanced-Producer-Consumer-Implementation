import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TicketPool {

    private final ConcurrentLinkedQueue<Integer> ticketpool = new ConcurrentLinkedQueue<>();
    private int maxTicketCapacity;
    private final Lock lock = new ReentrantLock();

    public TicketPool(int maxTicketCapacity){
        this.maxTicketCapacity = maxTicketCapacity;
    }

    public synchronized void createTicket(int item)throws InterruptedException{
        lock.lock();
        try{
            while (ticketpool.size()>=maxTicketCapacity){
                System.out.println("Ticket pool full. Vendor waiting");
                Thread.sleep(100);
            }
            ticketpool.add(item);
            System.out.println("Produced ticket :" + item + "Current Pool : "+ticketpool);
        }
        finally {
            lock.unlock();
        }
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
