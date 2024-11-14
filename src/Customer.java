public class Customer extends Thread {

    private final TicketPool ticketPool;

    public Customer(TicketPool ticketPool){
        this.ticketPool = ticketPool;
    }

    @Override
    public void run(){
        while (true){
            try{
                ticketPool.consumeTicket();
                Thread.sleep(500);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

    }
}
