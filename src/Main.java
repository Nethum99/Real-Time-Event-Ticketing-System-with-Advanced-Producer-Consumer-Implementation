
public class Main {

    public static void main(String[] args) {

        Configuration configuration = new Configuration();


        TicketPool ticketPool = new TicketPool(configuration.getMaxCapacity(),configuration.getTotalTickets());

        for(int i=0; i<configuration.getActiveVendors(); i++){
            Vendor vendor = new Vendor(ticketPool,configuration.getTicketReleaseRate());
            vendor.start();
        }

        for(int i=0; i<configuration.getActiveCustomers(); i++){
            Customer customer = new Customer(ticketPool);
            customer.start();
        }


    }
}