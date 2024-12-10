//package com.nethum.ticketsystem.realtimeticketing.config;
//
//import com.nethum.ticketsystem.realtimeticketing.controller.TicketLogController;
//import com.nethum.ticketsystem.realtimeticketing.model.ConfigurationDTO;
//import com.nethum.ticketsystem.realtimeticketing.service.TicketPool;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class TicketPoolConfig {
//
//    @Bean
//    public TicketPool ticketPool(ConfigurationDTO configurationDTO, TicketLogController ticketLogController) {
//        return new TicketPool(configurationDTO.getMaxCapacity(),
//                configurationDTO.getTotalTickets(),
//                ticketLogController);
//    }
//}
