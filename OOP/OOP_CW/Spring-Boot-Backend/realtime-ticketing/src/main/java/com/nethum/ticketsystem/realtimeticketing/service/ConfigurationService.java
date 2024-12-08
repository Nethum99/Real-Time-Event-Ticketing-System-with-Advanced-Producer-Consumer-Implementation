package com.nethum.ticketsystem.realtimeticketing.service;

import com.nethum.ticketsystem.realtimeticketing.model.ConfigurationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfigurationService {

    @Autowired
    TicketPoolSystemService ticketPoolSystemService;


    public void readBody(ConfigurationDTO configurationDTO) {
        ticketPoolSystemService.startSystem(configurationDTO);
    }
}
