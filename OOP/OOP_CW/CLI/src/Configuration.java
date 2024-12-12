import java.util.Scanner;

public class Configuration {

    private int totalTickets;
    // Total Number of Tickets:
    // Defines the maximum number of tickets that can be produced by all vendors combined.
    // Example: If set to 50, vendors can produce a total of 50 tickets. Once this limit is reached, production stops.


    private int maxCapacity;
    // Max Capacity in Ticket Pool:
    // Defines the maximum number of tickets that can exist in the ticket pool at any given time.
    // Example: If set to 25, the ticket pool cannot hold more than 25 tickets. Vendors must wait for tickets to be consumed before producing more.

    private int ticketReleaseRate;
    // Ticket Release Rate:
    // Specifies the number of tickets each vendor produces per unit time (e.g., per cycle or iteration).
    // Example: If set to 10 and there are 2 vendors, each vendor produces 10 tickets per cycle, resulting in 20 tickets per cycle combined.


    private int customerRetrievalRate;
    // Customer Retrieval Rate:
    // Indicates the number of tickets each customer consumes per unit time (e.g., per cycle or iteration).
    // Example: If set to 2 and there are 4 customers, each customer consumes 2 tickets per cycle, for a total of 8 tickets consumed per cycle.

    private int activeCustomers;
    // Number of Active Customers:
    // Specifies the number of consumer threads (customers) working concurrently to retrieve tickets.
    // Example: If set to 4, four customer threads will work simultaneously, consuming tickets based on the retrieval rate.

    private int activeVendors;
    // Number of Active Vendors:
    // Specifies the number of producer threads (vendors) working concurrently to release tickets.
    // Example: If set to 2, two vendor threads will work simultaneously, producing tickets based on the release rate.

    private boolean confirmed;  //User confirm run or not
    private boolean validation =true;       //Controll to while loop

    public Configuration(){
        Scanner scanner = new Scanner(System.in);

        while (validation) {
            int tempTotalTickets = 0;
            try{
                System.out.print("Enter the total of tickets in event : ");
                tempTotalTickets = scanner.nextInt();
                if(tempTotalTickets>0){
                    this.totalTickets = tempTotalTickets;
                    validation = false;
                }
                else {
                    System.out.println("The value should be greater than zero. Please try again!");
                }
            }
            catch (Exception e){
                System.out.println("It seems you entered text. Please enter a number instead");
                scanner.nextLine();
            }
        }
        validation = true;


        while (validation) {
            int tempTicketReleaseRate = 0;
            try{
                System.out.print("Enter ticket release rate : ");
                tempTicketReleaseRate = scanner.nextInt();
                if(tempTicketReleaseRate>0){
                    this.ticketReleaseRate = tempTicketReleaseRate;
                    validation = false;
                }
                else {
                    System.out.println("The value should be greater than zero. Please try again!");
                }
            }
            catch (Exception e){
                System.out.println("It seems you entered text. Please enter a number instead");
                scanner.nextLine();
            }
        }
        validation = true;

        while (validation) {
            int tempCustomerRetrievalRate = 0;
            try{
                System.out.print("Enter customer retrieval rate : ");
                tempCustomerRetrievalRate = scanner.nextInt();
                if(tempCustomerRetrievalRate>0){
                    this.customerRetrievalRate = tempCustomerRetrievalRate;
                    validation = false;
                }
                else {
                    System.out.println("The value should be greater than zero. Please try again!");
                }
            }
            catch (Exception e){
                System.out.println("It seems you entered text. Please enter a number instead");
                scanner.nextLine();
            }
        }
        validation = true;

        while (validation) {
            int tempMaxTicketCapacity = 0;
            try{
                System.out.println("Enter max ticket capacity : ");
                tempMaxTicketCapacity = scanner.nextInt();
                if(tempMaxTicketCapacity>0){
                    this.maxCapacity = tempMaxTicketCapacity;
                    validation = false;
                }
                else {
                    System.out.println("The value should be greater than zero. Please try again!");
                }
            }
            catch (Exception e){
                System.out.println("It seems you entered text. Please enter a number instead");
                scanner.nextLine();
            }
        }
        validation = true;

        while (validation) {
            int tempVendorsCount = 0;
            try{
                System.out.print("Enter number of active vendors : ");
                tempVendorsCount = scanner.nextInt();
                if(tempVendorsCount>0){
                    this.activeVendors = tempVendorsCount;
                    validation = false;
                }
                else {
                    System.out.println("The value should be greater than zero. Please try again!");
                }
            }
            catch (Exception e){
                System.out.println("It seems you entered text. Please enter a number instead");
                scanner.nextLine();
            }
        }
        validation = true;

        while (validation) {
            int tempCustomersCount = 0;
            try{
                System.out.print("Enter number of active customers : ");
                tempCustomersCount = scanner.nextInt();
                if(tempCustomersCount>0){
                    this.activeCustomers = tempCustomersCount;
                    validation = false;
                }
                else {
                    System.out.println("The value should be greater than zero. Please try again!");
                }
            }
            catch (Exception e){
                System.out.println("It seems you entered text. Please enter a number instead");
                scanner.nextLine();
            }
        }
        validation = true;

        while(validation){
            String tempConfirmation;
            try{
                System.out.print("Do you want to start the system? (yes/no): ");
                tempConfirmation = scanner.next();
                if(tempConfirmation.equalsIgnoreCase("yes")) {
                    this.confirmed = true;
                    validation = false;

                }
                else if (tempConfirmation.equalsIgnoreCase("n")) {
                    this.confirmed = false;
                    validation = false;
                    System.out.println("System aborted. Exiting...");
                }
                else {
                    System.out.println("Let’s try that again with a valid input.");
                }
            }
            catch (Exception e){
                System.out.println("Let’s try that again with a valid input.");
            }
        }
        validation = false;
        scanner.close();
    }

    public boolean getConfiramation(){
        return confirmed;
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
