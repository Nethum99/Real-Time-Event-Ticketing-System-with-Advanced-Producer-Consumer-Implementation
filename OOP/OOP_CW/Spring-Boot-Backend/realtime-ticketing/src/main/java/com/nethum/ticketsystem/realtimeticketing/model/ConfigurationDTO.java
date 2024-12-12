package com.nethum.ticketsystem.realtimeticketing.model;


import jakarta.persistence.*;
import org.springframework.stereotype.Component;

@Entity
@Table
@Component
public class ConfigurationDTO {
    @Id
    @SequenceGenerator(
            name = "input_sequence",
            sequenceName = "input_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "input_sequence"
    )
    private int id;
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


    public ConfigurationDTO() {
    }

    public ConfigurationDTO(int totalTickets, int activeVendors, int activeCustomers, int ticketReleaseRate, int maxCapacity, int customerRetrievalRate) {
        this.totalTickets = totalTickets;
        this.activeVendors = activeVendors;
        this.activeCustomers = activeCustomers;
        this.ticketReleaseRate = ticketReleaseRate;
        this.maxCapacity = maxCapacity;
        this.customerRetrievalRate = customerRetrievalRate;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
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

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    public void setCustomerRetrievalRate(int customerRetrievalRate) {
        this.customerRetrievalRate = customerRetrievalRate;
    }

    public int getActiveVendors() {
        return activeVendors;
    }

    public void setActiveVendors(int activeVendors) {
        this.activeVendors = activeVendors;
    }
}
