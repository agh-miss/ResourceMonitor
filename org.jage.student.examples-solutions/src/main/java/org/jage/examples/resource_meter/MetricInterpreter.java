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

	public MetricInterpreter(UUID uuid, IAgentAddress currentAgentAddress,
			Map<IAgentAddress, Metric> metrics) {
		this.uuid = uuid;
		this.sourceAddress = currentAgentAddress;
		this.metrics = metrics;
	}

	public MetricInterpreter process() {
		/**
		 * Check root
		 */
		UUID minUuid = uuid;
		for (IAgentAddress agentAddress : metrics.keySet()) {
			Metric metric = metrics.get(agentAddress);
			if (metric.getUuid().compareTo(minUuid) > 0) {
				minUuid = metric.getUuid();
			}
		}
		if (minUuid == uuid) {

		}
		return this;
	}

	public ArrayList<MoveAgentTask> getTasks() {
		return null;
	}

}
