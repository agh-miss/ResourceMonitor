package org.jage.examples.resource_meter;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class Metric implements Serializable {

	private static final int LEVEL_HEAVY = 75;
	private static final int LEVEL_MEDIUM = 50;
	private static final int LEVEL_LOW = 30;
	private static final int LEVEL_FREE = 15;

	private static final long serialVersionUID = 1L;

	private UUID uuid;
	private Long timeStamp;

	private Integer cpuLoad;
	private Integer memoryLoad;
	private Integer agentCount;

	public Metric(Integer cpuLoad, Integer memoryLoad, Integer agentCount,
			UUID uuid) {
		this.cpuLoad = cpuLoad;
		this.memoryLoad = memoryLoad;
		this.agentCount = agentCount;
		this.uuid = uuid;
		timeStamp = new Date().getTime();
	}

	public Integer getAgentCount() {
		return agentCount;
	}

	public Integer getCpuLoad() {
		return cpuLoad;
	}

	public Integer getMemoryLoad() {
		return memoryLoad;
	}

	public UUID getUuid() {
		return uuid;
	}

	public Long getTimeStamp() {
		return timeStamp;
	}

	private WorkplaceLoad getWorkplaceLoadFromPercentage(int value) {
		if (value >= 0 && value < LEVEL_FREE) {
			return WorkplaceLoad.FREE;
		} else if (value >= LEVEL_FREE && value < LEVEL_LOW) {
			return WorkplaceLoad.LOW;
		} else if (value >= LEVEL_LOW && value < LEVEL_MEDIUM) {
			return WorkplaceLoad.MEDIUM;
		} else if (value >= LEVEL_MEDIUM && value < LEVEL_HEAVY) {
			return WorkplaceLoad.HEAVY;
		} else if (value >= LEVEL_HEAVY && value <= 100) {
			return WorkplaceLoad.FULL;
		}
		
		System.out.println(memoryLoad);
		System.out.println(cpuLoad);

		throw new IllegalStateException("Percentage value out of 0..100 range");
	}

	private WorkplaceLoad fuzzyLoad(WorkplaceLoad workplaceCpuLoad,
			WorkplaceLoad workplaceMemoryLoad) {

		if (workplaceCpuLoad == WorkplaceLoad.FULL
				|| workplaceMemoryLoad == WorkplaceLoad.FULL) {
			return WorkplaceLoad.FULL;
		} else if (workplaceCpuLoad == WorkplaceLoad.HEAVY) {
			return WorkplaceLoad.HEAVY;
		} else if (workplaceCpuLoad == WorkplaceLoad.MEDIUM
				|| (workplaceCpuLoad == WorkplaceLoad.LOW && (workplaceMemoryLoad == WorkplaceLoad.HEAVY || workplaceMemoryLoad == WorkplaceLoad.MEDIUM))) {
			return WorkplaceLoad.MEDIUM;
		} else if ((workplaceCpuLoad == WorkplaceLoad.LOW && (workplaceMemoryLoad == WorkplaceLoad.LOW || workplaceMemoryLoad == WorkplaceLoad.FREE))
				|| (workplaceCpuLoad == WorkplaceLoad.FREE && (workplaceMemoryLoad == WorkplaceLoad.MEDIUM || workplaceMemoryLoad == WorkplaceLoad.HEAVY))) {
			return WorkplaceLoad.LOW;
		} else if (workplaceCpuLoad == WorkplaceLoad.FREE
				&& (workplaceMemoryLoad == WorkplaceLoad.LOW || workplaceMemoryLoad == WorkplaceLoad.FREE)) {
			return WorkplaceLoad.FREE;
		}

		throw new IllegalStateException("Wrong workplace load combination");
	}

	public WorkplaceLoad getWorkplaceLoad() {
		WorkplaceLoad workplaceMemoryLoad = getWorkplaceLoadFromPercentage(getMemoryLoad());
		WorkplaceLoad workplaceCpuLoad = getWorkplaceLoadFromPercentage(getCpuLoad());
		return fuzzyLoad(workplaceCpuLoad, workplaceMemoryLoad);
	}

}
