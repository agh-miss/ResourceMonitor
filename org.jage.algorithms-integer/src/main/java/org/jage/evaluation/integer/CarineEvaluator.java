/**
 * Copyright (C) 2006 - 2010
 *   Pawel Kedzior
 *   Tomasz Kmiecik
 *   Kamil Pietak
 *   Krzysztof Sikora
 *   Adam Wos
 *   and other students of AGH University of Science and Technology
 *   as indicated in each file separately.
 *
 * This file is part of jAgE.
 *
 * jAgE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * jAgE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with jAgE.  If not, see http://www.gnu.org/licenses/
 */
/*
 * File: CarineProblem.java
 * Created: 2011-12-02
 * Author: Krzywicki
 * $Id: CarineEvaluator.java 18 2012-01-29 17:29:44Z krzywick $
 */

package org.jage.evaluation.integer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.evaluation.ISolutionEvaluator;
import org.jage.property.ClassPropertyContainer;
import org.jage.solution.IVectorSolution;

import it.unimi.dsi.fastutil.ints.IntList;

/**
 * CarineEvaluator is used for running a step of calculation with carine application. Carine homepage:
 * http://www.atpcarine.com/index2.html
 *
 * @author AGH AgE Team
 */
public final class CarineEvaluator extends ClassPropertyContainer implements
        ISolutionEvaluator<IVectorSolution<Integer>, Double> {

	private static final int MAX_EVALUATION_TIME = 99999;

	private static final int MAX_EXECUTION_TIME = 3;

	private static final Logger LOG = LoggerFactory.getLogger(CarineEvaluator.class);

	private String carinePath = "carine/carine";

	private String geoPath = "carine/geo001-1.t";

	public String getCarinePath() {
		return carinePath;
	}

	public void setCarinePath(String carinePath) {
		this.carinePath = carinePath;
	}

	public String getGeoPath() {
		return geoPath;
	}

	public void setGeoPath(String geoPath) {
		this.geoPath = geoPath;
	}

	@Override
	public Double evaluate(IVectorSolution<Integer> solution) {
		String[] commandString = createCommandString((IntList)solution.getRepresentation());
		LOG.debug("Command string is: " + Arrays.toString(commandString));

		long startTime = System.currentTimeMillis();
		boolean isSuccess = executeCommandString(commandString);

		long timeElapsed = System.currentTimeMillis() - startTime;
		if (!isSuccess || timeElapsed > MAX_EVALUATION_TIME) {
			timeElapsed = MAX_EVALUATION_TIME;
		}
		LOG.debug("Time elapsed: " + timeElapsed);

		return (double)-timeElapsed;
	}

	private String[] createCommandString(IntList representation) {
		// we are in target/classes directory, where files from resources are copied
		String[] cmd = new String[11];
		cmd[0] = carinePath;
		cmd[1] = geoPath;
		cmd[2] = "xo=off";
		cmd[3] = "wt=" + MAX_EXECUTION_TIME; // max execution time
		cmd[4] = "id=" + representation.getInt(0); // 0-1
		cmd[5] = "md=" + representation.getInt(1); // 0-30
		cmd[6] = "uct=" + representation.getInt(2); // 1-50000
		cmd[7] = "mtl=" + representation.getInt(3); // 0-255
		cmd[8] = "ml=" + representation.getInt(4); // 1-48
		cmd[9] = "mtu=" + representation.getInt(5); // 1-64
		cmd[10] = "mtdu=" + representation.getInt(6); // 1-63
		return cmd;
	}

	private boolean executeCommandString(String[] commandString) {
		Process process = null;
		BufferedReader input = null;
		try {
			process = new ProcessBuilder(commandString).redirectErrorStream(true).start();
			input = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;
			while ((line = input.readLine()) != null) {
				if (line.contains("PROOF FOUND!")) {
					return true;
				}
			}
		} catch (IOException e) {
			LOG.error("An error happened when executing command string: " + commandString, e);
		} finally {
			if(input != null) {
				try {
	                input.close();
                } catch (IOException e) {
                	// Nothing we can do now
                }
			}
			if(process != null) {
				process.destroy();
			}
		}
		return false;
	}
}
