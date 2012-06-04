package org.jage.examples.resource_meter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jage.address.IAgentAddress;

public class MetricInterpreter {

	private UUID uuid;
	private IAgentAddress sourceAddress;
	private Map<IAgentAddress, Metric> metrics;

	private Map<IAgentAddress, Integer> agentsToSend = new HashMap<IAgentAddress, Integer>();
	private Map<IAgentAddress, Integer> agentsToReceive = new HashMap<IAgentAddress, Integer>();

	public MetricInterpreter(UUID uuid, IAgentAddress sourceAddress,
			Map<IAgentAddress, Metric> metrics) {
		this.uuid = uuid;
		this.sourceAddress = sourceAddress;
		this.metrics = metrics;
	}

	public void process() {
		for (IAgentAddress agentAddress : metrics.keySet()) {
			Metric metric = metrics.get(agentAddress);
			
		}
	}

	public ArrayList<MoveAgentTask> getTasks() {
		return null;

	}

}
