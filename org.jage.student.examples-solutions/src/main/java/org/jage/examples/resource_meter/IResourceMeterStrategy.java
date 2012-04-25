package org.jage.examples.resource_meter;

import org.jage.strategy.IStrategy;

public interface IResourceMeterStrategy extends IStrategy {

	Long getCpuLoad();

	Long getMemoryLoad();

}
