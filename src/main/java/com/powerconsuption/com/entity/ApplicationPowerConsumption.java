package com.powerconsuption.com.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table
public class ApplicationPowerConsumption {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @Column(name = "application_name")
	    private String applicationName;

	    @Column(name = "pid")
	    private Integer pid;

	    @Column(name = "power_consumption_rate")
	    private Double powerConsumptionRate;
	    
	    
}
