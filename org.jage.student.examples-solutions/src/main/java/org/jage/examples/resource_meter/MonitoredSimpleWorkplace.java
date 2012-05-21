package org.jage.examples.resource_meter;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

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

	private static final UUID id = UUID.randomUUID();
	private Map<IAgentAddress, Metric> metrics = new HashMap<IAgentAddress, Metric>();
	private Integer randomLoad = new Random().nextInt(100);

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

				if (agentAddress != null) {
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
		Integer memoryLoad = resourceMeterStr.getMemoryLoad();

		sendObjectToAll(new Metric(cpuLoad + randomLoad, memoryLoad));

		Pair<Object, IAgentAddress> message;

		IAgentAddress minLoadAgent = null;
		IAgentAddress maxLoadAgent = null;
		Integer minLoad = Integer.MAX_VALUE;
		Integer maxLoad = Integer.MIN_VALUE;
		System.out.println(metrics.size());
		for (Map.Entry<IAgentAddress, Metric> entry : metrics.entrySet()) {
			Integer value = entry.getValue().getValue();
			if (value > maxLoad) {
				maxLoad = value;
				maxLoadAgent = entry.getKey();
			}
			if (value < minLoad) {
				minLoad = value;
				minLoadAgent = entry.getKey();
			}
		}
		if (minLoadAgent != null && maxLoadAgent != null
				&& minLoadAgent != maxLoadAgent) {
			MoveAgentTask moveAgentTask = new MoveAgentTask(id);
			moveAgentTask.setSourceWorkplace(maxLoadAgent);
			moveAgentTask.setDestinationWorkplace(minLoadAgent);
			sendObject(moveAgentTask, maxLoadAgent);
		}

		do {
			message = receiveObject();
			if (message == null)
				break;

			Object object = message.getKey();
			IAgentAddress senderAddress = message.getValue();

			if (object instanceof Integer) {
				log.info(getAddress() + " received CPU LOAD "
						+ (Integer) object + " from " + senderAddress);
			} else if (object instanceof IAgent) {
				add((IAgent) object);
				log.info(getAddress() + " received AGENT "
						+ ((IAgent) object).getAddress() + " from "
						+ senderAddress);
			} else if (object instanceof MoveAgentTask) {

				MoveAgentTask receivedMoveAgentTask = (MoveAgentTask) object;
				if (getAgents().size() > 1) {
					IAgent agentToMove = getAgents().get(0);
					sendObject(agentToMove,
							receivedMoveAgentTask.getDestinationWorkplace());
					try {
						removeAgent(agentToMove.getAddress());
					} catch (AgentException e) {
						e.printStackTrace();
					}
				}

				log.info(getAddress() + " received MOVE AGENT TASK from "
						+ senderAddress);

			} else if (object instanceof Metric) {
				metrics.put(senderAddress, (Metric) object);
				log.info(getAddress() + " received METRIC from "
						+ senderAddress + " VALUE "
						+ ((Metric) object).getValue());
			}

			/*
			 * if (senderAddress != null) { if (getAgents().size() > 2) { IAgent
			 * agentToMove = getAgents().get(0); sendObject(agentToMove,
			 * senderAddress); try { removeAgent(agentToMove.getAddress()); }
			 * catch (AgentException e) { log.error("Agent exception", e); } } }
			 */
		} while (true);

		log.info("CPU LOAD in workplace " + nameInitializer + ": "
				+ cpuLoad.toString() + "%");
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
