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

        System.out.println("Enter the total :");
        this.totalTickets = scanner.nextInt();

        System.out.println("Enter ticket release rate :");
        this.ticketReleaseRate = scanner.nextInt();

        System.out.println("Enter customer retrieval rate :");
        this.customerRetrievalRate = scanner.nextInt();

        System.out.println("Enter max capacity in ticketpool");
        this.maxCapacity = scanner.nextInt();

    }


    public void totalTickets(){
        System.out.println("Total number of ticket available for the event :");
    }

}
