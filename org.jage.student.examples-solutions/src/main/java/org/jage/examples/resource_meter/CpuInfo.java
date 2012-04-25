package org.jage.examples.resource_meter;

import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.cmd.SigarCommandBase;

public class CpuInfo extends SigarCommandBase {

	@Override
	public void output(String[] arg0) throws SigarException {
	}

	public double getValue() {
		try {
			return this.sigar.getCpuPerc().getUser();
		} catch (SigarException e) {
			e.printStackTrace();
		}

		return -1.0;
	}

	public int getPercentageValue() {
		return (int) (getValue() * 100);
	}

}
