package com.nethum.ticketsystem.realtimeticketing.repo;

import com.nethum.ticketsystem.realtimeticketing.model.ConfigurationDTO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Data Base Connectivity Repo to store user Inputs
 */
public interface ConfigurationRepo extends JpaRepository<ConfigurationDTO,Integer> {

}
