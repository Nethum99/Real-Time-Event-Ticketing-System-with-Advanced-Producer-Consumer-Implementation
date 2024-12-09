package com.nethum.ticketsystem.realtimeticketing.service;

import com.nethum.ticketsystem.realtimeticketing.model.Customer;
import com.nethum.ticketsystem.realtimeticketing.model.Vendor;
import com.nethum.ticketsystem.realtimeticketing.model.ConfigurationDTO;
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
