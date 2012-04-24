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
 * File: BatchModeSingleStarter.java
 * Created: 2010-06-08
 * Author: kpietak
 * $Id: BatchModeSingleStarter.java 134 2012-03-18 20:17:01Z faber $
 */

package org.jage.platform.starter.batch.single;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.platform.argument.IRuntimeArgumentsService;
import org.jage.platform.argument.InvalidRuntimeArgumentsException;
import org.jage.platform.component.IStatefulComponent;
import org.jage.platform.component.definition.ConfigurationException;
import org.jage.platform.component.definition.IComponentDefinition;
import org.jage.platform.component.exception.ComponentException;
import org.jage.platform.component.provider.IMutableComponentInstanceProvider;
import org.jage.platform.component.provider.IMutableComponentInstanceProviderAware;
import org.jage.platform.config.loader.IConfigurationLoader;
import org.jage.platform.config.xml.ConfigurationLoader;
import org.jage.platform.starter.IStarter;
import org.jage.services.core.ICoreComponent;
import org.jage.services.core.ICoreComponentListener;
import org.jage.workplace.IStopCondition;

/**
 * Starter which runs a single computation which is provided in a configuration file. The computation is run in batch
 * mode so that the result should be gathered from output logs.
 * <p>
 * 
 * Command line arguments required by the starter:
 * <ol>
 * <li><tt>-Dage.node.conf=path_to_node_config</tt> - <i>(required)</i> - path to file containing a configuration of
 * node components;
 * <li><tt>-Dage.computation.conf=path_to_computation_config</tt> - <i>(optional)</i> - path to the file containing a
 * configuration of computation (i.e. configuration of agents).
 * </ol>
 * Both configuration files should be the XML files which satisfy the following XSD Schema: <a
 * href="http://age.iisg.agh.edu.pl/xsd/age-2.3.xsd">http://age.iisg.agh.edu.pl/xsd/age-2.3.xsd</a>. The detailed
 * description of the file format can be found at: <a
 * href="https://caribou.iisg.agh.edu.pl/confluence/display/jagedocs/SpecyfikacjaPlikuKonfiguracyjnego"
 * >https://caribou.iisg.agh.edu.pl/confluence/display/jagedocs/SpecyfikacjaPlikuKonfiguracyjnego</a>
 * <p>
 * 
 * The Starter has an access to component registry so it can register new node components using
 * {@link IMutableComponentInstanceProvider} interface.
 * <p>
 * 
 * The Starter has an access to command line arguments through {@link IRuntimeArgumentsService} which can be accessed
 * using injected {@link IMutableComponentInstanceProvider}.
 * <p>
 * 
 * <i>Note:</i> The starter is not intended to be run in a distributed environment, however it can be used as a basic
 * implementation.
 * <p>
 * 
 * <i>This is the default implementation of {@link IStarter}.</i>
 * 
 * @since 2.5.0
 * 
 * @author AGH AgE Team
 */
public class BatchModeSingleStarter implements IMutableComponentInstanceProviderAware, IStarter, ICoreComponentListener {

	/**
	 * '-Dage.node.conf' option which defines path to a file with the configuration of node components.
	 */
	public static final String OPT_NODE_CONF = "age.node.conf";

	/**
	 * '-Dage.computation.conf' option which defines path to a file with the configuration of computation components.
	 */
	@Deprecated
	public static final String OPT_COMPUTATION_CONF = "age.computation.conf";

	private static Logger log = LoggerFactory.getLogger(BatchModeSingleStarter.class);

	private IMutableComponentInstanceProvider instanceProvider;

	private ICoreComponent coreComponent;

	// BEGIN State Variables

	/**
	 * A variable which indicates if node received stopped notification from {@link ICoreComponent}.
	 */
	private volatile boolean stopped = false;

	// END State Variables

	@Override
	public void start() {
		log.info("Starter is going to run the platform");

		try {
			initConfigurationComponents();

			loadNodeConfiguration();

			loadComputationConfiguration();

			initRequriedComponents();

			startCoreComponent();

			finishComponents();

		} catch (final InvalidRuntimeArgumentsException e) {
			log.error("Runtime arguments exception during startup", e);
			return;
		} catch (final ConfigurationException e) {
			log.error("Configuration exception during startup", e);
		} catch (final ComponentException e) {
			log.error("Component exception during startup", e);
		}

	}

	@Override
	public void shutdown() {
		synchronized (this) {
			stopped = true;
			notify();
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

	@Override
	public void setMutableComponentInstanceProvider(final IMutableComponentInstanceProvider instanceProvider) {
		this.instanceProvider = instanceProvider;
	}

	// BEGIN Starter Phases Methods

	protected void initConfigurationComponents() {
		log.debug("Phase 1: Initializing configuration components...");
		// create and register default configuration loader and provider
		instanceProvider.addComponent(ConfigurationLoader.class);
		log.debug("Phase 1: Configuration components initialized");

	}

	protected void loadNodeConfiguration() throws ConfigurationException, InvalidRuntimeArgumentsException {
		// load node configuration

		final String configFilePath = instanceProvider.getInstance(IRuntimeArgumentsService.class).getCustomOption(
		        OPT_NODE_CONF);

		if (configFilePath == null) {
			throw new InvalidRuntimeArgumentsException(String.format(
			        "The node config file name parameter is missing. Specify the correct path to the "
			                + "configuration file using -D%s option", OPT_NODE_CONF));

		}

		log.debug("Phase 2: Loading node components from {}", configFilePath);

		final Collection<IComponentDefinition> nodeComponents = instanceProvider
		        .getInstance(IConfigurationLoader.class).loadConfiguration(configFilePath);

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

	// This action is kept only for displaying a warning. TODO: Remove in 2.6++
	protected void loadComputationConfiguration() {
		final String configFilePath = instanceProvider.getInstance(IRuntimeArgumentsService.class).getCustomOption(
		        OPT_COMPUTATION_CONF);

		if (configFilePath != null) {
			log.warn("BatchModeSingleStarter does not support computation configuration anymore. Use <include />"
			        + " in the node configuration file.");
		}
		log.debug("Phase 3: Skipping...");
	}

	protected void initRequriedComponents() throws ComponentException {

		log.debug("Phase 4: Initializing required components...");

		// initialize state-ful components
		instanceProvider.getInstances(IStatefulComponent.class);

		coreComponent = instanceProvider.getInstance(ICoreComponent.class);
		if (coreComponent == null) {
			throw new ComponentException("Core component (ICoreComponent) is missing. Cannot run the computation.");
		}

		if (instanceProvider.getInstance(IStopCondition.class) != null) {
			throw new ComponentException("A stop condition located in the root level of the configuration file is no "
			        + "longer supported. It have to be moved to the core component.");
		}

		coreComponent.registerListener(this);

		log.debug("Phase 4: Required components initialized");

	}

	protected void startCoreComponent() throws ComponentException {
		log.debug("Phase 5: Starting computation");
		coreComponent.start();
		log.debug("Phase 5: Computation started");

		// wait until all are finished
		synchronized (this) {
			while (!stopped) {
				try {
					wait();
				} catch (final InterruptedException e) {
					log.warn("Starter interrupted", e);
				}
			}
		}

		log.debug("Phase 5: Computation finished.");

	}

	protected void finishComponents() throws ComponentException {
		log.debug("Phase 6: Shutting down the platform");

		// finalize state-ful components
		final Collection<IStatefulComponent> statefulComponents = instanceProvider
		        .getInstances(IStatefulComponent.class);
		if (statefulComponents != null) {
			for (final IStatefulComponent statefulComponent : statefulComponents) {
				statefulComponent.finish();
			}
		}
	}

	// END Starter Phases Methods
}
