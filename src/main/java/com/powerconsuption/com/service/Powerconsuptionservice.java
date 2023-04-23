package com.powerconsuption.com.service;

import java.io.IOException;

import javax.validation.Valid;

import org.hyperic.sigar.SigarException;

import com.powerconsuption.com.dto.ApplicationPowerConsumptionDto;
import com.powerconsuption.com.entity.ApplicationPowerConsumption;

public interface Powerconsuptionservice {

	double getPowerConsumption(int pid) throws IOException, SigarException;

	ApplicationPowerConsumption addName(@Valid ApplicationPowerConsumptionDto applicationPowerConsumptionDto);

}
