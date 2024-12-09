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
    private int maxCapacity;
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private int activeCustomers;
    private int activeVendors;

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
