package org.jage.examples.resource_meter;

import java.io.Serializable;
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

public class MonitoredSimpleWorkplace extends ConnectedSimpleWorkplace {

	IResourceMeterStrategy resourceMeterStr;

	@Inject
	public MonitoredSimpleWorkplace() {

	}

	public void sendObjectToAll(Serializable object) {

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
					Message<IAgentAddress, Serializable> textMessage = new Message<IAgentAddress, Serializable>(
							header, object);
					sendMessage(textMessage);
				}

			}
		} catch (AgentException e) {
			log.error("Agent exception", e);
		}

	}

	public void sendObject(Serializable object, IAgentAddress address) {
		Header<IAgentAddress> header = new Header<IAgentAddress>(getAddress(),
				new UnicastSelector<IAgentAddress>(address));
		Message<IAgentAddress, Serializable> textMessage = new Message<IAgentAddress, Serializable>(
				header, object);
		sendMessage(textMessage);
	}

	public Pair<Object, IAgentAddress> receiveObject() {
		IAgentAddress senderAddress = null;
		IMessage<IAgentAddress, ?> message = null;
		message = receiveMessage();
		if (message == null)
			return null;
		senderAddress = message.getHeader().getSenderAddress();
		return new Pair<Object, IAgentAddress>(message.getPayload(),
				senderAddress);
	}

	@Override
	public void step() {
		Integer cpuLoad = resourceMeterStr.getCpuLoad();

		sendObjectToAll(cpuLoad);

		Pair<Object, IAgentAddress> message;

		do {
			message = receiveObject();
			if (message == null)
				break;

			Object object = message.getKey();
			IAgentAddress senderAddress = message.getValue();

			if (object instanceof Integer) {
				System.out.println("Received cpu load " + (Integer) object);
			}

			if (object instanceof IAgent) {
				add((IAgent) object);
			}

			if (senderAddress != null) {
				if (getAgents().size() > 2) {
					IAgent agentToMove = getAgents().get(0);
					sendObject(agentToMove, senderAddress);
					try {
						removeAgent(agentToMove.getAddress());
					} catch (AgentException e) {
						log.error("Agent exception", e);
					}
				}
			}

			log.info("CPU LOAD in workplace " + nameInitializer + ": "
					+ cpuLoad.toString() + "%");
			Integer memoryLoad = resourceMeterStr.getMemoryLoad();
			log.info("MEMORY LOAD in workplace " + nameInitializer + ": "
					+ memoryLoad.toString() + "%");
		} while (true);
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
