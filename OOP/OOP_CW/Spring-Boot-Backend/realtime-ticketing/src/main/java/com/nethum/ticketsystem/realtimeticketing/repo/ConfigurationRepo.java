package com.nethum.ticketsystem.realtimeticketing.repo;

import com.nethum.ticketsystem.realtimeticketing.model.ConfigurationDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigurationRepo extends JpaRepository<ConfigurationDTO,Integer> {

}
