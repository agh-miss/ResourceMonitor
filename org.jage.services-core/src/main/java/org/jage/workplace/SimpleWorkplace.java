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
 * File: SimpleWorkplace.java
 * Created: 2008-10-07
 * Author: awos
 * $Id: SimpleWorkplace.java 166 2012-03-30 08:26:53Z faber $
 */

package org.jage.workplace;

import java.lang.Thread.UncaughtExceptionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.agent.AlreadyExistsException;
import org.jage.agent.IAgent;
import org.jage.agent.IAgentEnvironment;
import org.jage.agent.ISimpleAgent;
import org.jage.agent.SimpleAggregate;
import org.jage.platform.component.exception.ComponentException;
import org.jage.property.PropertyField;

/**
 * This is a simplest possible workplace. Doesn't provide any means to communicate with other workplaces.
 * 
 * @author AGH AgE Team
 */
public class SimpleWorkplace extends SimpleAggregate implements IWorkplace {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3L;

	/** The state. */
	private State state = State.STOPPED;

	/** The workplace environment. */
	private IWorkplaceEnvironment workplaceEnvironment;

	/**
	 * The name of the step property used in "stepped" workplaces.
	 */
	public static final String STEP_PROPERTY_NAME = "step";
	
	/** The step. */
	@PropertyField(propertyName = STEP_PROPERTY_NAME, isMonitorable = true)
	private long step;

	/** Logger. */
	private static final Logger log = LoggerFactory.getLogger(SimpleWorkplace.class);

	/**
	 * Constructor.
	 */
	protected SimpleWorkplace() {
		// empty
		log.debug("Creating instance of {}", SimpleWorkplace.class.getName());
	}

	/**
	 * Gets the agent environment.
	 * 
	 * @return the agent environment
	 * 
	 * @see org.jage.workplace.AbstractAgent#getAgentEnvironment()
	 */
	@Override
	protected IAgentEnvironment getAgentEnvironment() {
		throw new IllegalOperationException("Agent environment is not applicable to workplace.");
	}

	@Override
	public void setAgentEnvironment(final IAgentEnvironment localEnvironment) throws AlreadyExistsException {
		throw new IllegalOperationException("Agent environment is not applicable to workplace.");
	}

	@Override
	public void init() throws ComponentException {
		// init step
		step = 0;
		super.init();
	}

	@Override
	public void start() throws IllegalOperationException {
		log.info("{} is starting...", getAddress());
		synchronized (state) {
			if (!State.STOPPED.equals(state)) {
				throw new IllegalOperationException("Workplace is already started.");
			}
			final Thread thread = new Thread(this);
			thread.setName(getAddress().toString());
			// Won't work if in the future the runnable is being scheduled
			thread.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {

				@Override
				public void uncaughtException(final Thread t, final Throwable e) {
					log.error("Exception caught during run", e);
					setStopped();
				}
			});
			thread.start();
		}
	}

	@Override
	public void stop() throws IllegalOperationException {
		synchronized (state) {
			if (!State.RUNNING.equals(state)) {
				throw new IllegalOperationException("Workplace is not running.");
			}
			state = State.STOPPING;
		}
	}

	@Override
	public boolean finish() throws ComponentException {
		synchronized (state) {
			if (!State.STOPPED.equals(state)) {
				throw new IllegalOperationException("Illegal use of finish. Must invoke stop() first.");
			}
			super.finish();
			log.info("{} shut down", getAddress());
			return true;
		}
	}

	/**
	 * Main loop of a workplace.
	 */
	@Override
	public void run() {
		setRunning();
		log.info("{} has been started.", getAddress());
		while (State.RUNNING.equals(state)) {
			step();
		}
		setStopped();
	}

	/**
	 * Performs step of all agents and processes events.
	 */
	@Override
	public void step() {
		try {
			getAgentsLock().readLock().lockInterruptibly();
			try {
				// running agents
				for (final IAgent agent : agents.values()) {
					final ISimpleAgent simpleAgent = (ISimpleAgent)agent;
					// if (simpleAgent.getStatus() == IAgent.Status.ACTIVE) {
					simpleAgent.step();
				}
			} finally {
				getAgentsLock().readLock().unlock();
			}
		} catch (final InterruptedException e) {
			log.error("Interrupted in run", e);
		}

		processEvents();
		processActions();

		step++;

		//notifyMonitorsForChangedProperties();

	}

	@Override
	public boolean isRunning() {
		return State.RUNNING.equals(state);
	}

	/**
	 * Gets the step.
	 * 
	 * @return the step
	 */
	protected long getStep() {
		return step;
	}

	protected void setRunning() {
		state = State.RUNNING;
	}

	protected void setStopped() {
		log.info("{} stopped", getAddress());
		state = State.STOPPED;
		getWorkplaceEnvironment().notifyStopped(this);
	}

	@Override
	public void setWorkplaceEnvironment(final IWorkplaceEnvironment workplaceEnvironment) throws AlreadyExistsException {
		this.workplaceEnvironment = workplaceEnvironment;

		processEvents();
		processActions();
		if (temporaryAgentsList != null) {
			addAll(temporaryAgentsList);
			temporaryAgentsList = null;
		}
	}

	public IWorkplaceEnvironment getWorkplaceEnvironment() {
		return workplaceEnvironment;
	}

}
