package com.powerconsuption.com.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hyperic.sigar.SigarException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.powerconsuption.com.dto.ApplicationPowerConsumptionDto;
import com.powerconsuption.com.entity.ApplicationPowerConsumption;
import com.powerconsuption.com.service.Powerconsuptionservice;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/powerconsumption")
public class powercontroller {
	@Autowired
	private Powerconsuptionservice powerConsumptionService;
	@Autowired
	private ModelMapper modelMapper;
	@GetMapping("/pid")
	public int getPid(@RequestParam("appName") String appName) throws IOException {
	    String processName = appName;
	    String os = System.getProperty("os.name").toLowerCase();
	    String cmd = null;
	    if (os.contains("win")) {
	        cmd = "tasklist /fo csv /nh /fi \"imagename eq " + processName + ".exe\"";
	    } else if (os.contains("mac")) {
	        cmd = "pgrep " + processName;
	    } else if (os.contains("nux")) {
	        cmd = "pidof " + processName;
	    } else {
	        throw new UnsupportedOperationException("Unsupported operating system: " + os);
	    }

	    Process process = Runtime.getRuntime().exec(cmd);
	    try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
	        String line = reader.readLine();
	        if (line != null) {
	            if (os.contains("win")) {
	                Pattern pattern = Pattern.compile("\"" + processName + "\\.exe\",\"\\d+\",");
	                Matcher matcher = pattern.matcher(line);
	                if (matcher.find()) {
	                    String[] split = matcher.group().split(",");
	                    String pidString = split[1].trim();
	                    return Integer.parseInt(pidString.replaceAll("\"", ""));
	                }
	            } else {
	                String pidString = line.trim();
	                return Integer.parseInt(pidString);
	            }
	        }
	    }
	    return -1;
	}

	
	@GetMapping("/power-consumption/{pid}")
	public double getPowerConsumption(@PathVariable("pid") int pid) throws Exception {
	    return powerConsumptionService.getPowerConsumption(pid);
	}
@PostMapping("/applicationname")
	public ResponseEntity<ApplicationPowerConsumptionDto>addapplication(@Valid @RequestBody ApplicationPowerConsumptionDto applicationPowerConsumptionDto){
	ApplicationPowerConsumption applicationPowerConsumption=this.powerConsumptionService.addName(applicationPowerConsumptionDto);
	ApplicationPowerConsumptionDto newConsumptionDto=this.modelMapper.map(applicationPowerConsumption,ApplicationPowerConsumptionDto.class);
	
		return new ResponseEntity<ApplicationPowerConsumptionDto>(newConsumptionDto,HttpStatus.CREATED);
		
	}
	
	
}
