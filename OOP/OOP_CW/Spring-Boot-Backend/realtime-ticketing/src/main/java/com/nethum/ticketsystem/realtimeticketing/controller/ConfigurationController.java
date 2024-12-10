package com.nethum.ticketsystem.realtimeticketing.controller;


import com.nethum.ticketsystem.realtimeticketing.model.ConfigurationDTO;
import com.nethum.ticketsystem.realtimeticketing.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3005")
@RequestMapping("api/configuration")
public class ConfigurationController {

    @Autowired
    private ConfigurationService configurationService;

    @PostMapping("start")
    public void startSystem(@RequestBody ConfigurationDTO configurationDTO){
        configurationService.readBody(configurationDTO);
        configurationService.saveBody(configurationDTO);
    }

}
