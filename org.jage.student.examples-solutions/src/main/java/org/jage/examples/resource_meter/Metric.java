package org.jage.examples.resource_meter;

import java.io.Serializable;

public class Metric implements Serializable {

	Integer cpuLoad;
	Integer memoryLoad;
	
	public Metric(Integer cpuLoad, Integer memoryLoad) {
		this.cpuLoad = cpuLoad;
		this.memoryLoad = memoryLoad;
	}
	
	public Integer getValue() {
		//TODO Fuzzylogic super algorithm
		return cpuLoad + memoryLoad;
	}
	
}
