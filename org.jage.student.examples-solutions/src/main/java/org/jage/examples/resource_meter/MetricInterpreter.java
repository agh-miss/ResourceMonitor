package org.jage.examples.resource_meter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jage.address.IAgentAddress;

public class MetricInterpreter {

	private UUID uuid;
	private IAgentAddress sourceAddress;
	private Map<IAgentAddress, Metric> metrics;

	private List<IAgentAddress> sender = new ArrayList<IAgentAddress>();
	private List<IAgentAddress> receiver = new ArrayList<IAgentAddress>();

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
		boolean isRoot = minUuid.equals(uuid);
		if (isRoot) {
			for (IAgentAddress agentAddress : metrics.keySet()) {
				Metric metric = metrics.get(agentAddress);
				if (metric.getWorkplaceLoad() == WorkplaceLoad.FULL) {
					sender.add(agentAddress);
					sender.add(agentAddress);
				} else if (metric.getWorkplaceLoad() == WorkplaceLoad.HEAVY) {
					sender.add(agentAddress);
				} else if (metric.getWorkplaceLoad() == WorkplaceLoad.LOW) {
					receiver.add(agentAddress);
				} else if (metric.getWorkplaceLoad() == WorkplaceLoad.FREE) {
					receiver.add(agentAddress);
					receiver.add(agentAddress);
				}
			}
		}
		return this;
	}

	public ArrayList<MoveAgentTask> getTasks() {
		ArrayList<MoveAgentTask> tasks = new ArrayList<MoveAgentTask>();
		for (int i = 0; i < Math.min(sender.size(), receiver.size()); i++) {			
			tasks.add(new MoveAgentTask(uuid, sender.get(i), receiver.get(i)));
			GephiConnector.addEdge(sender.get(i).toString(), receiver.get(i).toString());
			System.out.println("NEW TASK");
		}
		return tasks;
	}
}
