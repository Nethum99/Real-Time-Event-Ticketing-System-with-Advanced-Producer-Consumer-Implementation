
public class Main {

    public static void main(String[] args) {

        Configuration configuration = new Configuration();

        if(!configuration.getConfiramation()){
            System.exit(0);
        }

        System.out.println("Total Tickets: " + configuration.getTotalTickets());
        System.out.println("Ticket Release Rate: " + configuration.getTicketReleaseRate());
        System.out.println("Customer Retrieval Rate: " + configuration.getCustomerRetrievalRate());
        System.out.println("Max Capacity: " + configuration.getMaxCapacity());
        System.out.println("Active Vendors: " + configuration.getActiveVendors());
        System.out.println("Active Customers: " + configuration.getActiveCustomers());


        TicketPool ticketPool = new TicketPool(configuration.getMaxCapacity(),configuration.getTotalTickets());

        for(int i=0; i<configuration.getActiveVendors(); i++){
            Vendor vendor = new Vendor(ticketPool,configuration.getTicketReleaseRate());
            vendor.start();
        }

        for(int i=0; i<configuration.getActiveCustomers(); i++){
            Customer customer = new Customer(ticketPool,configuration.getCustomerRetrievalRate());
            customer.start();
        }


    }
}