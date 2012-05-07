package org.jage.examples.resource_meter;

import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.cmd.SigarCommandBase;

public class MemoryInfo extends SigarCommandBase {

	@Override
	public void output(String[] arg0) throws SigarException {
	}

	public double getValue() {
		try {
			return this.sigar.getMem().getUsedPercent();
		} catch (SigarException e) {
			e.printStackTrace();
		}

		return -1.0;
	}

	public long getPercentageValue() {
		return (long) getValue();
	}

}
