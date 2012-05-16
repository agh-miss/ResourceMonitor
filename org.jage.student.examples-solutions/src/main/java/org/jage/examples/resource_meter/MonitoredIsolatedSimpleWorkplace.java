package org.jage.examples.resource_meter;

import java.util.Collection;

import org.jage.address.IAgentAddress;
import org.jage.address.selector.UnicastSelector;
import org.jage.agent.AbstractAgent;
import org.jage.agent.AgentException;
import org.jage.agent.IAgent;
import org.jage.communication.message.Header;
import org.jage.communication.message.IMessage;
import org.jage.communication.message.Message;
import org.jage.platform.component.annotation.Inject;
import org.jage.property.InvalidPropertyPathException;
import org.jage.property.PropertyGetter;
import org.jage.property.PropertySetter;
import org.jage.query.AgentEnvironmentQuery;
import org.jage.query.QueryException;
import org.jage.workplace.ConnectedSimpleWorkplace;

public class MonitoredIsolatedSimpleWorkplace extends ConnectedSimpleWorkplace {

	IResourceMeterStrategy resourceMeterStr;

	@Inject
	public MonitoredIsolatedSimpleWorkplace() {

	}

	@Override
	public void step() {
		Integer cpuLoad = resourceMeterStr.getCpuLoad();

		try {
			AgentEnvironmentQuery<AbstractAgent, AbstractAgent> query = new AgentEnvironmentQuery<AbstractAgent, AbstractAgent>();
			Collection<AbstractAgent> answer = queryEnvironment(query);

			for (AbstractAgent entry : answer) {

				IAgentAddress agentAddress = null;
				try {
					agentAddress = (IAgentAddress) entry.getProperty("address")
							.getValue();
				} catch (StringIndexOutOfBoundsException e) {
					log.error("Sending load fail", e);
				} catch (QueryException e) {
					log.error("Sending load fail", e);
				} catch (InvalidPropertyPathException e) {
					log.error("Sending load fail", e);
				}

				if (agentAddress != null && !agentAddress.equals(getAddress())) {
					Header<IAgentAddress> header = new Header<IAgentAddress>(
							getAddress(), new UnicastSelector<IAgentAddress>(
									agentAddress));
					Message<IAgentAddress, Integer> textMessage = new Message<IAgentAddress, Integer>(
							header, cpuLoad);
					sendMessage(textMessage);
				}

			}
		} catch (AgentException e) {
			log.error("Agent exception", e);
		}

		Long otherWorkplaceLoad = null;
		IAgentAddress senderAddress = null;
		IMessage<IAgentAddress, ?> message = null;
		do {
			message = receiveMessage();
			if (message == null)
				break;
			if (message.getPayload() instanceof Integer) {
				if (message != null) {
					senderAddress = message.getHeader().getSenderAddress();
					log.info("Agent: {} received message from: {}:",
							getAddress(), senderAddress);
					otherWorkplaceLoad = (Long) message.getPayload();
					log.info("    {}", message.getPayload());

				}
			} else if (message.getPayload() instanceof IAgent) {
				if (message != null) {
					log.info("Agent: {} received message from: {}:",
							getAddress(), message.getHeader()
									.getSenderAddress());
					log.info("    {}", message.getPayload());
					add((IAgent) message.getPayload());

				}
			}

		} while (true);

		if (otherWorkplaceLoad != null && senderAddress != null) {

			IAgent agentToMove = getAgents().get(0);
			System.out.println(agentToMove.toString());

			Header<IAgentAddress> header = new Header<IAgentAddress>(
					getAddress(), new UnicastSelector<IAgentAddress>(
							senderAddress));
			Message<IAgentAddress, IAgent> textMessage = new Message<IAgentAddress, IAgent>(
					header, agentToMove);
			sendMessage(textMessage);

			try {
				removeAgent(agentToMove.getAddress());
			} catch (AgentException e) {
				log.error("Agent exception", e);
			}

		}

		log.info("CPU LOAD in workplace " + nameInitializer + ": "
				+ cpuLoad.toString() + "%");
		Integer memoryLoad = resourceMeterStr.getMemoryLoad();
		log.info("MEMORY LOAD in workplace " + nameInitializer + ": "
				+ memoryLoad.toString() + "%");
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
