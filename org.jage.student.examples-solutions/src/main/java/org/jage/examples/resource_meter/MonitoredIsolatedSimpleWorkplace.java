package org.jage.examples.resource_meter;

import java.io.Serializable;
import java.util.Collection;
import java.util.logging.Logger;

import org.jage.address.IAgentAddress;
import org.jage.address.selector.UnicastSelector;
import org.jage.agent.AbstractAgent;
import org.jage.agent.AgentException;
import org.jage.agent.IAgent;
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
import org.jage.query.QueryException;
import org.jage.workplace.ConnectedSimpleWorkplace;
import org.jage.workplace.IsolatedSimpleWorkplace;

public class MonitoredIsolatedSimpleWorkplace extends ConnectedSimpleWorkplace {

	IResourceMeterStrategy resourceMeterStr;
	
	@Inject
	public MonitoredIsolatedSimpleWorkplace() {
		
	}
	
	@Override
	public void step() {
		Long cpuLoad = resourceMeterStr.getCpuLoad();
		
		try {
			AgentEnvironmentQuery<AbstractAgent, AbstractAgent> query = new AgentEnvironmentQuery<AbstractAgent, AbstractAgent>();
			Collection<AbstractAgent> answer = queryEnvironment(query);

			for (AbstractAgent entry : answer) {
				
				IAgentAddress agentAddress = null;
				try {
					agentAddress = (IAgentAddress)entry.getProperty("address").getValue();
				} catch(StringIndexOutOfBoundsException e) {
					log.error("Sending load fail", e);
				} catch(QueryException e) {
					log.error("Sending load fail", e);
				} catch(InvalidPropertyPathException e) {
					log.error("Sending load fail", e);
				}
					
				if(agentAddress != null && !agentAddress.equals(getAddress())) {
					Header<IAgentAddress> header = new Header<IAgentAddress>(getAddress(), new UnicastSelector<IAgentAddress>(
							agentAddress));
					Message<IAgentAddress, Long> textMessage = new Message<IAgentAddress, Long>(header, cpuLoad);	
					sendMessage(textMessage);
				}
					
			}
		} catch (AgentException e) {
			log.error("Agent exception", e);
		}
		
		
		Long otherWorkplaceLoad = null;
		IAgentAddress senderAddress = null;
		IMessage<IAgentAddress, Long> message = null;
		do {
			message = receiveMessage();
			if (message != null) {
				senderAddress = message.getHeader().getSenderAddress();
				log.info("Agent: {} received message from: {}:", getAddress(), senderAddress);
				
				otherWorkplaceLoad = message.getPayload();
				
				log.info("    {}", message.getPayload());
				
			} else {
				break;
			}
		} while (true);
		
		
		if (otherWorkplaceLoad != null && senderAddress != null) {
			
			IAgent agentToMove = getAgents().get(0);
			System.out.println(agentToMove.toString());
			
			Header<IAgentAddress> header = new Header<IAgentAddress>(getAddress(), new UnicastSelector<IAgentAddress>(
					senderAddress));
			Message<IAgentAddress, IAgent> textMessage = new Message<IAgentAddress, IAgent>(header, agentToMove);	
			sendMessage(textMessage);
			
			try {
				removeAgent(agentToMove.getAddress());
			} catch (AgentException e) {
				log.error("Agent exception", e);
			}
			
		}
		
		
		IMessage<IAgentAddress, IAgent> obtainedAgent = null;
		do {
			obtainedAgent = receiveMessage();
			if (obtainedAgent != null) {
				
				log.info("Agent: {} received message from: {}:", getAddress(), obtainedAgent.getHeader().getSenderAddress());
				log.info("    {}", obtainedAgent.getPayload());
				
				add(obtainedAgent.getPayload());
				
			} else {
				break;
			}
		} while (true);
		
		
		log.info("CPU LOAD in workplace " + nameInitializer + ": " + cpuLoad.toString() + "%");
		Long memoryLoad = resourceMeterStr.getMemoryLoad();
		log.info("MEMORY LOAD in workplace " + nameInitializer + ": " + memoryLoad.toString() + "%");
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
