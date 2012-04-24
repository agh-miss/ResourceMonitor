package org.jage.examples.resource_meter;

import java.text.DecimalFormat;

import org.jage.agent.SimpleAgent;

public class ResourceMeterAgent extends SimpleAgent {

	private static final long serialVersionUID = 1429202793426729786L;

	private CpuInfo cpuInfo = new CpuInfo();

	@Override
	public void step() {
		DecimalFormat percentageFormatter = new DecimalFormat("##.##");
		log.info("CPU load is {}%",
				percentageFormatter.format(cpuInfo.getPercentageValue()));
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			log.error("Interrupted", e);
		}
	}

}
