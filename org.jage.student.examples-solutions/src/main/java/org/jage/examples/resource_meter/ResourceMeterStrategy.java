package org.jage.examples.resource_meter;

import org.jage.property.ClassPropertyContainer;

public class ResourceMeterStrategy extends ClassPropertyContainer implements
		IResourceMeterStrategy {

	private CpuInfo cpuInfo = new CpuInfo();
	private MemoryInfo memoryInfo = new MemoryInfo();

	@Override
	public Integer getCpuLoad() {
		Integer cpuLoad = cpuInfo.getPercentageValue();
		return cpuLoad;
	}

	@Override
	public Integer getMemoryLoad() {
		Integer memoryLoad = memoryInfo.getPercentageValue();
		return memoryLoad;
	}

}
