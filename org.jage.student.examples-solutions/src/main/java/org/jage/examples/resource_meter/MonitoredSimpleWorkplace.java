package org.jage.examples.resource_meter;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
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

	private static final long serialVersionUID = 1L;
	private static final UUID uuid = UUID.randomUUID();
	private Map<IAgentAddress, Metric> metrics = new HashMap<IAgentAddress, Metric>();
	private Integer randomLoad = new Random().nextInt(100);
	private boolean isRegisteredInGephyConnector = false;

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
					agentAddress = (IAgentAddress) entry.getAddress();
				} catch (Exception e) {
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
		if (!isRegisteredInGephyConnector) {
			GephiConnector.addNode(getAddress().toString(), getAddress().toString());
			isRegisteredInGephyConnector = true;
		}

		Integer cpuLoad = resourceMeterStr.getCpuLoad();
		Integer memoryLoad = resourceMeterStr.getMemoryLoad();

		sendObjectToAll(new Metric(new Random().nextInt(100),
				new Random().nextInt(100), getAgents().size(), uuid));

		Pair<Object, IAgentAddress> message;

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
						+ ((Metric) object).getCpuLoad());
			}

		} while (true);

		List<MoveAgentTask> moveAgentTasks = new MetricInterpreter(uuid,
				getAddress(), metrics).process().getTasks();
		if (moveAgentTasks != null) {
			for (MoveAgentTask moveAgentTask : moveAgentTasks) {
				sendObject(moveAgentTask, moveAgentTask.getSourceWorkplace());
			}
		}

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
