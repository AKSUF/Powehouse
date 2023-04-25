package com.powerconsuption.com.serviceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.validation.Valid;

import org.hyperic.sigar.*;

import org.springframework.stereotype.Service;

import com.powerconsuption.com.dto.ApplicationPowerConsumptionDto;
import com.powerconsuption.com.entity.ApplicationPowerConsumption;
import com.powerconsuption.com.service.Powerconsuptionservice;
@Service
public class PowerconsuptionServiceImpl implements Powerconsuptionservice {
	

	@Override
	public double getPowerConsumption(int pid) throws IOException {
	// Get the name of the operating system that this code is running on.
	    String os = System.getProperty("os.name").toLowerCase();
	 // Create a system command to retrieve the RSS value of the process with the specified PID.
	    String cmd = null;
	    if (os.contains("win")) {
	        cmd = "WMIC PROCESS WHERE ProcessId=" + pid + " GET WorkingSetSize";
	    } else if (os.contains("mac") || os.contains("nux")) {
	        cmd = "ps -o pid,rss " + pid;
	    } else {
	        throw new UnsupportedOperationException("Unsupported operating system: " + os);
	    }
	 // Initialize the power consumption value to zero.
	    double powerConsumption = 0;
	 // Run the system command and read the output.
	    Process process = Runtime.getRuntime().exec(cmd);
	    try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
	        String line;
	        boolean isFirstLine = true;
	        while ((line = reader.readLine()) != null) {
	            if (!line.isEmpty()) {
	                String[] tokens = line.trim().split("\\s+");
	                if (isFirstLine) {
	                    // Skip the header row
	                    isFirstLine = false;
	                } else if (tokens.length >= 1) {
	                    long rss = Long.parseLong(tokens[0]);
	                 // Calculate the power consumption in watts for the given RSS value.
	                    double watts = calculateWatts(rss);
	                 // Add the power consumption of this process to the total.
	                    powerConsumption += watts;
	                }
	            }
	        }
	    }

	    return powerConsumption;
	}


	private double calculateWatts(long rss) {
	    // This method calculates the power consumption in watts for a given RSS value (in bytes)
		// the rss value is divided by 1048576, it is the number of bytes in a megabyte
		//the result is multiplied by 0.003, it is a constant that represents the power consumption in watts per megabyte of memory used
	    double watts = rss / 1048576.0 * 0.003;
	 
	    return watts;
	}

//Skip method 1
	@Override
	public ApplicationPowerConsumption addName(@Valid ApplicationPowerConsumptionDto applicationPowerConsumptionDto) {
		
		return null;
	}
	
}


