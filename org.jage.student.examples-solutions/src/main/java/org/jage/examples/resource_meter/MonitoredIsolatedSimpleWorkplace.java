package org.jage.examples.resource_meter;

import java.io.Serializable;
import java.util.Collection;
import java.util.logging.Logger;

import org.jage.address.IAgentAddress;
import org.jage.address.selector.UnicastSelector;
import org.jage.agent.AbstractAgent;
import org.jage.agent.AgentException;
import org.jage.agent.SimpleAgent;
import org.jage.agent.AbstractAgent.Properties;
import org.jage.communication.message.Header;
import org.jage.communication.message.IMessage;
import org.jage.communication.message.Message;
import org.jage.examples.strategy.IEchoStrategy;
import org.jage.platform.component.annotation.Inject;
import org.jage.property.IPropertyContainer;
import org.jage.property.InvalidPropertyPathException;
import org.jage.property.PropertyField;
import org.jage.property.PropertyGetter;
import org.jage.property.PropertySetter;
import org.jage.query.AgentEnvironmentQuery;
import org.jage.workplace.ConnectedSimpleWorkplace;
import org.jage.workplace.IsolatedSimpleWorkplace;

public class MonitoredIsolatedSimpleWorkplace extends ConnectedSimpleWorkplace {

	IResourceMeterStrategy resourceMeterStr;
	
	private String receiver = null;

	private IAgentAddress receiverAddress = null;
	
	@Inject
	public MonitoredIsolatedSimpleWorkplace() {
		
	}
	
	@Override
	public void step() {
		
		if (receiverAddress == null) {
			try {
				AgentEnvironmentQuery<AbstractAgent, AbstractAgent> query = new AgentEnvironmentQuery<AbstractAgent, AbstractAgent>();
				Collection<AbstractAgent> answer = queryEnvironment(query);

				for (AbstractAgent entry : answer) {
					IAgentAddress agentAddress = (IAgentAddress)entry.getProperty("address").getValue();
					Header<IAgentAddress> header = new Header<IAgentAddress>(getAddress(), new UnicastSelector<IAgentAddress>(
							agentAddress));
					Message<IAgentAddress, String> textMessage = new Message<IAgentAddress, String>(header, new String("wiadomosc od " + nameInitializer));
					
					sendMessage(textMessage);
					
				}
			} catch (AgentException e) {
				log.error("Agent exception", e);
			} catch (InvalidPropertyPathException e) {
				log.error("Agent exception", e);
			}
		}
		
		IMessage<IAgentAddress, String> message = null;
		do {
			message = receiveMessage();
			if (message != null) {
				log.info("Agent: {} received message from: {}:", getAddress(), message.getHeader().getSenderAddress());
				log.info("    {}", message.getPayload());
			} else {
				break;
			}
		} while (true);
		
		Long cpuLoad = resourceMeterStr.getCpuLoad();
		log.info("CPU LOAD in workplace " + nameInitializer + ": " + cpuLoad + "%");
		Long memoryLoad = resourceMeterStr.getMemoryLoad();
		log.info("MEMORY LOAD in workplace " + nameInitializer + ": " + memoryLoad + "%");
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
