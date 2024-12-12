import java.util.Scanner;

public class Configuration {

    private int totalTickets;
    private int maxCapacity;
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private int activeCustomers;
    private int activeVendors;
    private boolean confirmed;
    private boolean validation =true;

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
