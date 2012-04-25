package org.jage.examples.resource_meter;

import org.jage.agent.AbstractAgent.Properties;
import org.jage.examples.strategy.IEchoStrategy;
import org.jage.platform.component.annotation.Inject;
import org.jage.property.IPropertyContainer;
import org.jage.property.PropertyField;
import org.jage.property.PropertyGetter;
import org.jage.property.PropertySetter;
import org.jage.workplace.IsolatedSimpleWorkplace;

public class MonitoredIsolatedSimpleWorkplace extends IsolatedSimpleWorkplace {

	IResourceMeterStrategy resourceMeterStr;
	
	@Inject
	public MonitoredIsolatedSimpleWorkplace() {
		
	}
	
	@Override
	public void step() {
		resourceMeterStr.getCpuLoad();
		resourceMeterStr.getMemoryLoad();
		super.step();
	}
	
	@Inject
	@PropertyGetter(propertyName = "resourceMonitorStr")
	public IResourceMeterStrategy getResourceMonitorStr() {
		return resourceMeterStr;
	}

	@Inject
	@PropertySetter(propertyName = "resourceMonitorStr")
	public void setResourceMonitorStr(IResourceMeterStrategy val) {
		resourceMeterStr = val;
	}
	
}
