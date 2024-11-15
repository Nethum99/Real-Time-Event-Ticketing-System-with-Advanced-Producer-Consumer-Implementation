import java.util.Scanner;

public class Configuration {

    private int totalTickets;
    private int maxCapacity;
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private int activeCustomers;
    private int activeVendors;

    public Configuration(){
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the total number of tickets : ");
        this.totalTickets = scanner.nextInt();

        System.out.print("Enter ticket release rate : ");
        this.ticketReleaseRate = scanner.nextInt();

        System.out.print("Enter customer retrieval rate : ");
        this.customerRetrievalRate = scanner.nextInt();

        System.out.print("Enter max capacity in ticketpool : ");
        this.maxCapacity = scanner.nextInt();

        System.out.print("Enter number of active vendors : ");
        this.activeVendors = scanner.nextInt();

        System.out.print("Enter number of active customers : ");
        this.activeCustomers = scanner.nextInt();

        System.out.print("Do you want to start the system? (yes/no): ");
        String startConfirmation = scanner.next();

        if(!startConfirmation.equalsIgnoreCase("yes")){
            System.out.println("System start aborted");
            System.out.println(0);
        }

        scanner.close();

    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public int getActiveVendors() {
        return activeVendors;
    }

    public void setActiveVendors(int activeVendors) {
        this.activeVendors = activeVendors;
    }

    public int getActiveCustomers() {
        return activeCustomers;
    }

    public void setActiveCustomers(int activeCustomers) {
        this.activeCustomers = activeCustomers;
    }

    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    public void setTicketReleaseRate(int ticketReleaseRate) {
        this.ticketReleaseRate = ticketReleaseRate;
    }

    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    public void setCustomerRetrievalRate(int customerRetrievalRate) {
        this.customerRetrievalRate = customerRetrievalRate;
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }
}
