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
 * File: HelloWorldSimpleAgent.java
 * Created: 2011-03-15
 * Author: kpietak
 * $Id: HelloWorldSimpleAgent.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.examples.resource_meter;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.agent.SimpleAgent;
import org.jage.platform.component.annotation.Inject;
import org.jage.platform.component.exception.ComponentException;
import org.jage.property.PropertyGetter;
import org.jage.property.PropertySetter;

public class HardlyWorkingAgent extends SimpleAgent implements Runnable {

	private static final long serialVersionUID = 3L;

	private final Logger log = LoggerFactory
			.getLogger(HardlyWorkingAgent.class);
	private Integer workTime;
	private Integer dallyTime;
	private boolean isWorking;
	private Thread work;

	@Inject
	public HardlyWorkingAgent() {
		log.info("Constructing Hello World Simple Agent");
	}

	@Override
	public void init() throws ComponentException {
		super.init();
		work = new Thread(this);
		setWorking(true);
		work.start();
		log.info("Initializing Hardly Working Agent: {}", getAddress());
	}

	@Override
	public void step() {
		log.info("Working hard period: {}ms doing nothing period: {}ms", workTime, dallyTime);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			log.error("Interrupted", e);
		}
	}

	@Override
	public boolean finish() {
		setWorking(false);
		log.info("Finishing Hardly Working Agent: {}", getAddress());		
		return true;
	}

	@Inject
	@PropertyGetter(propertyName = "workTime")
	public Integer getWorkTime() {
		return workTime;
	}

	@Inject
	@PropertySetter(propertyName = "workTime")
	public void setWorkTime(Integer workTime) {
		this.workTime = workTime;
	}

	@Inject
	@PropertyGetter(propertyName = "dallyTime")
	public Integer getDallyTime() {
		return dallyTime;
	}

	@Inject
	@PropertySetter(propertyName = "dallyTime")
	public void setDallyTime(Integer dallyTime) {
		this.dallyTime = dallyTime;
	}

	@Override
	public void run() {
		while (isWorking) {
			try {
				Thread.sleep(dallyTime);
				long startWorkingTime = System.currentTimeMillis();
				long stopWorkingTime = System.currentTimeMillis();
				int dummy_one;
				int dummy_two;
				while (startWorkingTime + workTime > stopWorkingTime) {
					stopWorkingTime = System.currentTimeMillis();
					dummy_one = new Random().nextInt();
					dummy_two = new Random().nextInt();
					dummy_one *= dummy_two *= dummy_one;
				}
			} catch (InterruptedException e) {
				log.error("Interrupted", e);
			}
		}

	}

	public boolean isWorking() {
		return isWorking;
	}

	public void setWorking(boolean isWorking) {
		this.isWorking = isWorking;
	}
}
