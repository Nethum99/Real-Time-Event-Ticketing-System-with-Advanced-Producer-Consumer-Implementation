package com.nethum.ticketsystem.realtimeticketing.service;

import com.nethum.ticketsystem.realtimeticketing.model.ConfigurationDTO;
import com.nethum.ticketsystem.realtimeticketing.repo.ConfigurationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfigurationService {

    @Autowired
    TicketPoolSystemService ticketPoolSystemService;

    @Autowired
    ConfigurationRepo configurationRepo;


    public void readBody(ConfigurationDTO configurationDTO) {
        ticketPoolSystemService.startSystem(configurationDTO);
    }

    public void saveBody(ConfigurationDTO configurationDTO) {
        configurationRepo.save(configurationDTO);
    }
}
