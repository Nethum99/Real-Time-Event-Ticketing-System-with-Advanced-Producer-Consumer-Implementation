package com.nethum.ticketsystem.realtimeticketing.service;

import com.nethum.ticketsystem.realtimeticketing.Configuration;
import com.nethum.ticketsystem.realtimeticketing.Customer;
import com.nethum.ticketsystem.realtimeticketing.TicketPool;
import com.nethum.ticketsystem.realtimeticketing.Vendor;
import com.nethum.ticketsystem.realtimeticketing.controller.ConfigurationController;
import com.nethum.ticketsystem.realtimeticketing.model.ConfigurationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketPoolSystemService {


    public void startSystem(ConfigurationDTO configurationDTO) {

        TicketPool ticketPool = new TicketPool(configurationDTO.getMaxCapacity(), configurationDTO.getTotalTickets());

        for (int i = 0; i < configurationDTO.getActiveVendors(); i++) {
            System.out.println(configurationDTO.getActiveVendors()+" TicketPoolSystem");
            Vendor vendor = new Vendor(ticketPool, configurationDTO.getTicketReleaseRate());
            vendor.start();
        }


        for (int i = 0; i < configurationDTO.getActiveCustomers(); i++) {
            Customer customer = new Customer(ticketPool, configurationDTO.getCustomerRetrievalRate());
            customer.start();
        }
    }
}
