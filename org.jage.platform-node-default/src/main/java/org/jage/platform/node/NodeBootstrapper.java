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
 * File: NodeBootstrapper.java
 * Created: 2010-06-08
 * Author: kpietak
 * $Id: NodeBootstrapper.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.platform.node;

import org.picocontainer.ComponentMonitorStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.platform.argument.CommandLineArgumentsService;
import org.jage.platform.argument.IRuntimeArgumentsService;
import org.jage.platform.argument.InvalidRuntimeArgumentsException;
import org.jage.platform.component.pico.PicoComponentInstanceProviderFactory;
import org.jage.platform.component.pico.ServiceLocatingComponentMonitor;
import org.jage.platform.component.provider.IMutableComponentInstanceProvider;
import org.jage.platform.starter.IStarter;

/**
 * The node bootstraper is a starting point of the node. It creates initial components and then calls a configured
 * starter.
 * 
 * @author AGH AgE Team
 */
public class NodeBootstrapper {

	// BEGIN Command Line Options
	/**
	 * 'starter' option.
	 */
	public static final String OPT_STARTER_CLASS = "starter";

	/**
	 * 'help' option.
	 */
	public static final String OPT_HELP = "help";

	// END Command Line Options

	private static Logger log = LoggerFactory.getLogger(NodeBootstrapper.class);

	private IMutableComponentInstanceProvider instanceProvider;

	/**
	 * Starting point of the AgE Platform.
	 * 
	 * @param args
	 *            runtime arguments
	 */
	public static void main(final String[] args) {
		NodeBootstrapper bootstrapper = new NodeBootstrapper();
		bootstrapper.start(args);
	}

	/**
	 * Starts the platform.
	 * 
	 * @param args
	 *            runtime arguments
	 */
	public void start(final String[] args) {

		log.info("Starting AgE Node...");

		// create a component registry
		instanceProvider = PicoComponentInstanceProviderFactory.createInstanceProvider();

		log.info("Component registry created.");

		if (instanceProvider instanceof ComponentMonitorStrategy) {
			((ComponentMonitorStrategy)instanceProvider).changeMonitor(new ServiceLocatingComponentMonitor());
		} else {
			log.warn("Created instance provider does not support attaching monitors.");
		}

		// create runtime parameters service and register in the registry
		try {
			instanceProvider.addComponentInstance(new CommandLineArgumentsService(args));
			log.info("Runtime arguments loaded.");

			IRuntimeArgumentsService argsService = instanceProvider.getInstance(IRuntimeArgumentsService.class);

			// check if help mode is turned off
			if (argsService.containsPlatformOption(OPT_HELP)) {
				argsService.printUsage();
			} else {
				// run default services and run starter

				// create, register and initialize logger service
				// TODO

				// read and register given IStarter
				loadStarter();
				runStarter();
			}
		} catch (InvalidRuntimeArgumentsException e) {
			log.error("Invalid runtime arguments", e);
			// componentRegistry.getComponentInstance(IRuntimeArgumentsService.class).printUsage();
		}

		log.info("Shutting down AgE node.");

	}

	private void loadStarter() throws InvalidRuntimeArgumentsException {
		IRuntimeArgumentsService argsService = instanceProvider.getInstance(IRuntimeArgumentsService.class);
		String starterClassName = argsService.getPlatformOption(OPT_STARTER_CLASS);
		if (starterClassName == null) {
			throw new InvalidRuntimeArgumentsException(
			        "There is no Starter defined - cannot run the node. Run node with -h or --help options to see more"
			                + " details about command line options.");
		}

		Class<?> starterClass = null;
		try {
			starterClass = Class.forName(starterClassName);
			instanceProvider.addComponent(starterClass);

		} catch (ClassNotFoundException e) {
			throw new InvalidRuntimeArgumentsException(String.format(
			        "Undefined starter class: %s. Probably there is a mistake in the class name or the class is"
			                + " not attached to classpath. Cannot run the node.", starterClassName), e);
		}
	}

	private void runStarter() {
		// get IStarter from component register and pass processing to it
		IStarter starter = instanceProvider.getInstance(IStarter.class);
		if (starter != null) {
			log.info("Going to run the starter {}", starter.getClass().getName());
			starter.start();
		}
	}
}
