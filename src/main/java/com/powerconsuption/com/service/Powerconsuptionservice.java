package com.powerconsuption.com.service;

import java.io.IOException;

import org.hyperic.sigar.SigarException;

public interface Powerconsuptionservice {

	double getPowerConsumption(int pid) throws IOException, SigarException;

}
