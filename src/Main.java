
public class Main {

    public static void main(String[] args) {
        TicketPool ticketPool = new TicketPool(5);

        for(int i=3; i<5; i++){
            Vendor vendor = new Vendor(ticketPool);
            vendor.start();
        }

        Customer customer = new Customer(ticketPool);
        customer.start();kk
    }
}