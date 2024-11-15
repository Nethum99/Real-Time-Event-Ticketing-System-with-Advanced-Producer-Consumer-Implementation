public class Customer extends Thread {

    private final TicketPool ticketPool;
    private final int customerRetrievalRate;

    public Customer(TicketPool ticketPool,int customerRetrievalRate){
        this.ticketPool = ticketPool;
        this.customerRetrievalRate = customerRetrievalRate;
    }

    @Override
    public void run(){
        while (!ticketPool.isSoldOut()){
            try{
              if(!ticketPool.consumeTicket(customerRetrievalRate)){
                  break;
              }
              Thread.sleep(500);
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }
        System.out.println("Customer thread ending as tickets are sold out.");
    }
}
