package org.jage.examples.resource_meter;

import java.util.logging.Logger;

import org.jage.property.ClassPropertyContainer;

public class ResourceMeterStrategy extends ClassPropertyContainer implements
		IResourceMeterStrategy {

	private CpuInfo cpuInfo = new CpuInfo();
	private MemoryInfo memoryInfo = new MemoryInfo();

	@Override
	public Long getCpuLoad() {
		long cpuLoad = cpuInfo.getPercentageValue();
		return cpuLoad;
	}

	@Override
	public Long getMemoryLoad() {
		long memoryLoad = memoryInfo.getPercentageValue();
		return memoryLoad;
	}

}
