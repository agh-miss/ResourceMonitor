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
 * File: FixedStepCountStopCondition.java
 * Created: 2009-05-18
 * Author: kpietak
 * $Id: FixedStepCountStopCondition.java 139 2012-03-19 22:54:18Z faber $
 */

package org.jage.workplace;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.event.AbstractEvent;
import org.jage.event.PropertyEvent;
import org.jage.platform.component.annotation.Inject;
import org.jage.platform.component.annotation.Require;
import org.jage.platform.component.exception.ComponentException;
import org.jage.property.InvalidPropertyOperationException;
import org.jage.property.InvalidPropertyPathException;
import org.jage.property.monitors.AbstractPropertyMonitor;
import org.jage.property.monitors.DefaultPropertyMonitorRule;
import org.jage.query.PropertyContainerCollectionQuery;
import org.jage.services.core.ICoreComponent;

import static org.jage.query.ValueFilters.lessThan;
import static org.jage.query.ValueSelectors.property;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Stop condition which stops workplaces when all of them perform fixed steps count. It gets in constructor number of
 * steps after which workplaces are stopped. In no number is given the default value is set.
 * <p>
 *
 * Important: with the version 2.6 semantics of this stop condition changed: now <emph>all</emph> of workplaces must
 * pass at least the provided number of steps for the computation to stop.
 *
 * @author AGH AgE Team
 */
public class FixedStepCountStopCondition extends AbstractPropertyMonitor implements IStopCondition {

	private static Logger log = LoggerFactory.getLogger(FixedStepCountStopCondition.class);

	private static final long DEFAULT_STEP_COUNT = 10;

	@Inject
	private IWorkplaceManager workplaceManager;

	private long stepCount = DEFAULT_STEP_COUNT;

	/**
	 * Creates a new step counting condition with the default maximum step count.
	 */
	@Inject
	public FixedStepCountStopCondition() {
		log.info("Fixed step stop condition created with default step set to: {}", this.stepCount);
	}

	/**
	 * Creates a new step counting condition with the provided maximum step count.
	 *
	 * @param stepCount
	 *            a number of steps before this stop condition is satisfied, must be greater than zero.
	 */
	@Inject
	public FixedStepCountStopCondition(final Long stepCount) {
		if (stepCount != null) {
			checkArgument(stepCount > 0,
			        "FixedStepCountStopCondition: number of steps cannot be equal or less then zero. Given value: %s.",
			        stepCount);
			this.stepCount = stepCount;
		}
		log.info("Fixed step stop condition created with step set to: {}.", this.stepCount);

	}

	@Override
	public void notifyStarting(ICoreComponent coreComponent) {
		assert coreComponent.equals(workplaceManager);
		try {
			for (IWorkplace workplace : workplaceManager.getWorkplaces()) {
				workplace.getProperty(SimpleWorkplace.STEP_PROPERTY_NAME).addMonitor(this,
				        new DefaultPropertyMonitorRule());
			}
		} catch (InvalidPropertyOperationException e) {
			log.error("Step property is set to read-only while it shouldn't", e);
			throw new RuntimeException("'Step' property is read-only.", e);
		} catch (InvalidPropertyPathException e) {
			log.error("Cannot find step property in workplace.", e);
			throw new RuntimeException("Cannot find a 'step' property in a workplace.", e);
		}

	}

	@Override
	public void notifyStopped(ICoreComponent coreComponent) {
		// do nothing
	}

	@Override
	public void init() throws ComponentException {
		if (workplaceManager == null) {
			throw new ComponentException("Workplace manager has not been injected. Is it configured?");
		}
		workplaceManager.registerListener(this);
	}

	@Override
	public boolean finish() throws ComponentException {
		workplaceManager.unregisterListener(this);
		return true;
	}

	@Override
	protected void propertyChanged(PropertyEvent event) {
		final Object value = event.getProperty().getValue();
		checkArgument(value instanceof Long, "Wrong property type. Should be long, but actual type is %s.",
		        value.getClass());

		if (shouldStop()) {
			log.info("The stop condition has been satisfied.");
			workplaceManager.stop();
		}
	}

	@Override
	public void ownerDeleted(AbstractEvent event) {
		// ignore
	}

	private boolean shouldStop() {
		final PropertyContainerCollectionQuery<IWorkplace, Long> query = new PropertyContainerCollectionQuery<IWorkplace, Long>(
		        IWorkplace.class);

		final Collection<Long> results = query.matching(SimpleWorkplace.STEP_PROPERTY_NAME, lessThan(stepCount))
		        .select(property(SimpleWorkplace.STEP_PROPERTY_NAME)).execute(workplaceManager.getWorkplaces());
		return results.isEmpty();
	}

}
