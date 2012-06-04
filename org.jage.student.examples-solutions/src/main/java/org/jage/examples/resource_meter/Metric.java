package org.jage.examples.resource_meter;

import java.io.Serializable;

public class Metric implements Serializable {

	private static final long serialVersionUID = 1L;
	
	Integer cpuLoad;
	Integer memoryLoad;
	Integer agentCount;

	public Metric(Integer cpuLoad, Integer memoryLoad, Integer agentCount) {
		this.cpuLoad = cpuLoad;
		this.memoryLoad = memoryLoad;
		this.agentCount = agentCount;
	}

	public Integer getAgentCount() {
		return agentCount;
	}

	public Integer getCpuLoad() {
		return cpuLoad;
	}

	public Integer getMemoryLoad() {
		return memoryLoad;
	}

}
