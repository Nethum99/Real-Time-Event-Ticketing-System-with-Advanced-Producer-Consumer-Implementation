import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.Buffer;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    private final BufferedWriter logWriter;

    public TicketPool(int maxTicketCapacity,int totalTickets) throws IOException{
        this.maxTicketCapacity = maxTicketCapacity;
        this.totalTickets = totalTickets;

        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String logFileName = "ticket_system_log_" + timestamp + ".txt";
        logWriter = new BufferedWriter(new FileWriter(logFileName));

    }

    private void logMessage(String message) {
        try {
            // Write log message to the file
            logWriter.write(message);
            logWriter.newLine();
            logWriter.flush();
            // Also print to the console
            System.out.println(message);
        } catch (IOException e) {
            System.err.println("Error writing log: " + e.getMessage());
        }
    }

    public boolean createTicket(int numberOfTickets)throws InterruptedException{
        lock.lock();
        try{
            if(soldOut || ticketProduced>=totalTickets){
                logMessage("Tickets are sold out. No more tickets can be produced.");
                return false;
            }

            int ticketToProduce = Math.min(numberOfTickets,maxTicketCapacity-ticketpool.size());
            ticketToProduce = Math.min(ticketToProduce,totalTickets-ticketProduced);

            if(ticketToProduce>0){
                logMessage("\n>>> Tickets Released :");
                for(int i=0;i<ticketToProduce;i++){
                    UUID ticketID = UUID.randomUUID();
                    ticketpool.add(ticketID);
                    ticketProduced++;
                    logMessage("[TICKET ID : "+ticketID+" ]");
                }
                logMessage("Available total ticket count : "+(totalTickets-ticketProduced));
                logMessage("Available ticket count in ticket pool : "+ticketpool.size());
                return true;
            }
            else {
                logMessage("Cannot produce tickets. Pool at max capacity or total tickets limit reached.");
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
                logMessage("\n>>> Tickets Consumed :");
            }
            for(int i = 0;i<numberOfTickets && !ticketpool.isEmpty();i++){
                UUID ticket = ticketpool.poll();
                if(ticket!=null){
                    logMessage("[CONSUMER: "+Thread.currentThread().getName()+ "] consumed [TICKET ID "+ticket+" ]");
                    ticketConsumed++;
                    return true;
                }
                else{
                    logMessage("Consumer "+Thread.currentThread().getName()+" is waiting");
                }

            }
            if(ticketConsumed>=totalTickets){
                logMessage("All tickets are sold out!. System is stopping");
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

    public void closeLog() {
        try {
            logMessage("Closing log file.");
            logWriter.close();
        } catch (IOException e) {
            System.err.println("Error closing log file: " + e.getMessage());
        }
    }


}