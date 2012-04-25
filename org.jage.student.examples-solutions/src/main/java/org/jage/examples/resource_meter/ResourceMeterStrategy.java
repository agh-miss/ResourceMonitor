package org.jage.examples.resource_meter;

import org.jage.property.ClassPropertyContainer;

public class ResourceMeterStrategy extends ClassPropertyContainer implements
		IResourceMeterStrategy {

	@Override
	public Long getCpuLoad() {
		System.out.println("CPU LOAD");
		return null;
	}

	@Override
	public Long getMemoryLoad() {
		System.out.println("MEMORY LOAD");
		return null;
	}

}
