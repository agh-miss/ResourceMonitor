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
 * File: BatchModeDistributedStarter.java
 * Created: 2012-03-12
 * Author: faber
 * $Id: BatchModeDistributedStarter.java 133 2012-03-18 20:13:46Z faber $
 */

package org.jage.platform.starter.batch.distributed;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.communication.ICommunicationService;
import org.jage.platform.argument.IRuntimeArgumentsService;
import org.jage.platform.argument.InvalidRuntimeArgumentsException;
import org.jage.platform.component.IStatefulComponent;
import org.jage.platform.component.annotation.Inject;
import org.jage.platform.component.definition.ConfigurationException;
import org.jage.platform.component.definition.IComponentDefinition;
import org.jage.platform.component.exception.ComponentException;
import org.jage.platform.component.provider.IMutableComponentInstanceProvider;
import org.jage.platform.component.provider.IMutableComponentInstanceProviderAware;
import org.jage.platform.config.distributed.DistributedConfigurationService;
import org.jage.platform.config.loader.IConfigurationLoader;
import org.jage.platform.config.xml.ConfigurationLoader;
import org.jage.platform.starter.IStarter;
import org.jage.services.core.ICoreComponent;
import org.jage.services.core.ICoreComponentListener;

/**
 * This is a starter for distributed computations.
 * <p>
 *
 * Command line arguments for this starter are:
 * <ol>
 * <li><tt>-Dage.node.conf=path_to_node_config</tt> - <i>(required)</i> - path to a file containing a configuration of
 * node components;
 * <li><tt>-Dage.computation.conf=path_to_computation_config</tt> - <i>(optional)</i> - path to a file containing a
 * configuration of computation (i.e. configuration of agents).
 * </ol>
 * Only the computation configuration is distributed among nodes.
 * <p>
 *
 * This starter works in two modes: feeding and receiving. The mode is chosen on the basis of existence of the
 * <tt>age.computation.conf</tt> argument. If provided, the starter runs in the "feeding" mode and distributes the
 * configuration among other nodes. If absent, the starter will wait for an incoming configuration.
 * <p>
 *
 * The Starter has an access to component registry so it can register new node components using
 * {@link IMutableComponentInstanceProvider} interface.
 * <p>
 *
 * The Starter has an access to command line arguments through {@link IRuntimeArgumentsService} which can be accessed
 * using injected {@link IMutableComponentInstanceProvider}.
 *
 * @since 2.6.0
 * @author AGH AgE Team
 */
public class BatchModeDistributedStarter implements IStarter, ICoreComponentListener,
        IMutableComponentInstanceProviderAware {

	/**
	 * '-Dage.node.conf' option which defines path to a file with the configuration of node components.
	 */
	public static final String OPT_NODE_CONF = "age.node.conf";

	/**
	 * '-Dage.computation.conf' option which defines path to a file with the configuration of computation components.
	 */
	public static final String OPT_COMPUTATION_CONF = "age.computation.conf";

	private static final Logger log = LoggerFactory.getLogger(BatchModeDistributedStarter.class);

	private IMutableComponentInstanceProvider instanceProvider;

	private ICoreComponent coreComponent;

	@Inject
	private IRuntimeArgumentsService argumentsService;

	// BEGIN State Variables

	/**
	 * A variable which indicates if node received stopped notification from {@link ICoreComponent}.
	 */
	private volatile boolean stopped = false;

	// END State Variables

	@Override
	public void start() {
		log.info("Starter is going to run the platform.");

		try {
			initConfigurationComponents();

			loadNodeConfiguration();

			initRequriedComponents();

			loadComputationConfiguration();

			// NodeBootstrapper requires that start() does not return. It could be done better.
			// TODO: Think how to decouple NodeBootstrapper from the Starter finish.
			// Maybe we should provide something like "PlatformShutdownListener"?
			synchronized (this) {
				while (!stopped) {
					try {
						wait();
					} catch (final InterruptedException e) {
						log.warn("Interrupted.", e);
					}
				}
			}

			log.debug("Starter is finishing its operation.");

			finishComponents();

		} catch (final InvalidRuntimeArgumentsException e) {
			log.error("Runtime arguments exception during startup.", e);
		} catch (final ConfigurationException e) {
			log.error("Configuration exception during startup.", e);
		} catch (final ComponentException e) {
			log.error("Component exception during startup.", e);
		}
	}

	@Override
	public void notifyStarting(final ICoreComponent coreComponent) {
		log.info("Received notification that core component is about to start.");
	}

	@Override
	public void notifyStopped(final ICoreComponent coreComponent) {
		if (this.coreComponent.equals(coreComponent)) {
			log.info("Received notification that core component has just been stopped.");
		}
		synchronized (this) {
			stopped = true;
			notify();
		}
	}

	private void initConfigurationComponents() {
		log.debug("Phase 1: Initializing configuration components.");
		instanceProvider.addComponent(ConfigurationLoader.class);
		instanceProvider.addComponent(DistributedConfigurationService.class);
		log.debug("Phase 1: Configuration components initialized.");
	}

	private void loadNodeConfiguration() throws ConfigurationException, InvalidRuntimeArgumentsException {
		final String configFilePath = argumentsService.getCustomOption(OPT_NODE_CONF);

		if (configFilePath == null) {
			throw new InvalidRuntimeArgumentsException(String.format(
			        "The node config file name parameter is missing. Specify the correct path "
			                + "to the configuration file using -D%s option", OPT_NODE_CONF));

		}

		log.debug("Phase 2: Loading node components from {}", configFilePath);

		final Collection<IComponentDefinition> nodeComponents = instanceProvider.getInstance(IConfigurationLoader.class)
		        .loadConfiguration(configFilePath);

		for (final IComponentDefinition def : nodeComponents) {
			instanceProvider.addComponent(def);
		}
		try {
			instanceProvider.verify();
		} catch (final ComponentException e) {
			throw new ConfigurationException(e);
		}

		log.debug("Phase 2: Nodes componenents loaded (details: {})", nodeComponents.toString());
	}

	/**
	 * Initialises and verifies top-level, required components:
	 * <ol>
	 * <li>stateful components,
	 * <li>the communication service,
	 * <li>the core component.
	 * </ol>
	 *
	 * @throws ComponentException
	 *             when one of the required components cannot be found or configured.
	 */
	private void initRequriedComponents() throws ComponentException {
		log.debug("Phase 3: Initializing stateful and required components.");

		instanceProvider.getInstances(IStatefulComponent.class);

		if (instanceProvider.getInstance(ICommunicationService.class) == null) {
			throw new ComponentException("The communication service is unavailable. This starter requires it to work.");
		}

		coreComponent = instanceProvider.getInstance(ICoreComponent.class);
		if (coreComponent == null) {
			throw new ComponentException("Core component (ICoreComponent) is missing. Cannot run the computation.");
		}
		coreComponent.registerListener(this);

		log.debug("Phase 3: Stateful and required components initialized.");
	}

	private void loadComputationConfiguration() throws ConfigurationException {
		final DistributedConfigurationService configurationService = instanceProvider
		        .getInstance(DistributedConfigurationService.class);
		final String configFilePath = argumentsService.getCustomOption(OPT_COMPUTATION_CONF);

		if (configFilePath == null) {
			log.debug("Phase 4: No configuration provided. I will be waiting for it.");

			configurationService.receiveConfiguration();
		} else {
			log.debug("Phase 4: Computation configuration provided as a file {}.", configFilePath);

			final Collection<IComponentDefinition> computationComponents = instanceProvider.getInstance(
			        IConfigurationLoader.class).loadConfiguration(configFilePath);

			configurationService.distributeConfiguration(computationComponents);
		}

		log.debug("Phase 4: Finished.");
		log.info("Platform startup finished.");
	}

	private void finishComponents() {
		log.debug("Shutting down the platform.");

		try {
			final Collection<IStatefulComponent> statefulComponents = instanceProvider
			        .getInstances(IStatefulComponent.class);
			if (statefulComponents != null) {
				for (final IStatefulComponent statefulComponent : statefulComponents) {
					statefulComponent.finish();
				}
			}
		} catch (final ComponentException e) {
			log.error("Exception during the teardown.", e);
		}
	}

	@Override
	public void setMutableComponentInstanceProvider(final IMutableComponentInstanceProvider instanceProvider) {
		this.instanceProvider = instanceProvider;
	}

	@Override
	public synchronized void shutdown() {
		stopped = true;
		notify();
	}

}
