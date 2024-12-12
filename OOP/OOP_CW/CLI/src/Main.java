public class Main {
    /**
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        TicketPool ticketPool = null;

        try {
            Configuration configuration = new Configuration();

            if(!configuration.getConfiramation()){
                System.exit(0);
            }else {
                System.out.println("System Starting");
            }

            System.out.println("Event Total Tickets : " + configuration.getTotalTickets());
            System.out.println("Ticket Release Rate: " + configuration.getTicketReleaseRate());
            System.out.println("Customer Retrieval Rate: " + configuration.getCustomerRetrievalRate());
            System.out.println("Max Capacity: " + configuration.getMaxCapacity());
            System.out.println("Active Vendors: " + configuration.getActiveVendors());
            System.out.println("Active Customers: " + configuration.getActiveCustomers());


            System.out.println("=============================================");
            System.out.println("                                                     ");
            System.out.println("                TICKET SYSTEM                         ");
            System.out.println("                                                     ");
            System.out.println("===============================================");


            ticketPool = new TicketPool(configuration.getMaxCapacity(),configuration.getTotalTickets());



            Thread[] vendors = new Thread[configuration.getActiveVendors()];
            for(int i=0; i<configuration.getActiveVendors(); i++){
                vendors[i] = new Vendor(ticketPool,configuration.getTicketReleaseRate());
                vendors[i].start();
            }

            Thread[] customers  = new Thread[configuration.getActiveCustomers()];
            for(int i=0; i<configuration.getActiveCustomers(); i++){
                customers[i] = new Customer(ticketPool,configuration.getCustomerRetrievalRate());
                customers[i].start();
            }

            for (Thread vendorThread :vendors){
                vendorThread.join();
            }

            for(Thread customerThread : customers){
                customerThread.join();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            System.out.println("System has stopped");
        }
    }
}