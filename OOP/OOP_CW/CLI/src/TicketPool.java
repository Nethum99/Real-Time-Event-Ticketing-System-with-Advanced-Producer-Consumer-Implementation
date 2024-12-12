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
    private int ticketProduced = 0;     //How many tickets produced in system according latest user input
    private int ticketConsumed = 0;     //How many tickets consumed in system according latest user input
    private volatile boolean soldOut = false;       // boolean flag to tickets are soldout or not
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
                logMessage("Tickets are sold out. No more tickets can be produced.");
                return false;
            }

            int ticketToProduce = Math.min(numberOfTickets,maxTicketCapacity-ticketpool.size());
            ticketToProduce = Math.min(ticketToProduce,totalTickets-ticketProduced);  //This calculation work for calculate how many rest oftickets have to produce in the iteration

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
                logMessage("\n>>> Tickets Consumed :");
            }
            for(int i = 0;i<numberOfTickets && !ticketpool.isEmpty();i++){
                UUID ticket = ticketpool.poll();        //deleting latest ticeket in queue and assign it ticket
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

    public boolean isSoldOut(){      //notice to other method tickets are soldout or not by returning "soldOut".
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