package com.powerconsuption.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.powerconsuption.com.entity.ApplicationPowerConsumption;

public interface PowerConsumptionApplicationRepository extends  JpaRepository <ApplicationPowerConsumption, Long> {

}
