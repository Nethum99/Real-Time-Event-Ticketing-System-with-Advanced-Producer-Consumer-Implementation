public class Vendor extends Thread{

    private final TicketPool ticketPool;
    private final Ticket ticket;

    public Vendor(TicketPool ticketPool){
        this.ticketPool = ticketPool;
        this.ticket = new Ticket();
    }

    public void run(){
        while (true){
            try{
                int item = ticket.generatedNumber();
                ticketPool.createTicket(item);
                Thread.sleep(500);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
