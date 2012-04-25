package org.jage.examples.resource_meter;

import java.util.logging.Logger;

import org.jage.property.ClassPropertyContainer;

public class ResourceMeterStrategy extends ClassPropertyContainer implements
		IResourceMeterStrategy {

	private CpuInfo cpuInfo = new CpuInfo();
	private MemoryInfo memoryInfo = new MemoryInfo();

	@Override
	public Long getCpuLoad() {
		log.info("CPU LOAD: " + cpuInfo.getPercentageValue() + "%");
		return null;
	}

	@Override
	public Long getMemoryLoad() {
		log.info("MEMORY LOAD: " + memoryInfo.getPercentageValue() + "%");
		return null;
	}

}
