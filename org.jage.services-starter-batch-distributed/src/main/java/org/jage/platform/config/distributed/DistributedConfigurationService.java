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
 * File: DistributedConfigurationService.java
 * Created: 2012-03-05
 * Author: faber
 * $Id: DistributedConfigurationService.java 139 2012-03-19 22:54:18Z faber $
 */

package org.jage.platform.config.distributed;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.address.node.ComponentAddress;
import org.jage.address.node.IComponentAddress;
import org.jage.address.node.provider.IAgENodeAddressProvider;
import org.jage.address.selector.BroadcastSelector;
import org.jage.communication.ICommunicationService;
import org.jage.communication.message.Header;
import org.jage.communication.message.IHeader;
import org.jage.communication.message.IMessage;
import org.jage.communication.message.Message;
import org.jage.communication.message.Messages;
import org.jage.platform.component.IStatefulComponent;
import org.jage.platform.component.annotation.Inject;
import org.jage.platform.component.definition.IComponentDefinition;
import org.jage.platform.component.exception.ComponentException;
import org.jage.platform.component.provider.IComponentInstanceProvider;
import org.jage.platform.component.provider.IComponentInstanceProviderAware;
import org.jage.platform.config.ConfigurationChangeListener;
import org.jage.platform.starter.IStarter;
import org.jage.services.core.ICoreComponent;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningScheduledExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayList;

/**
 * A configuration service that can work in a distributed environment. It operates on a configuration in the form of a
 * list of {@link IComponentDefinition} instances.
 * <p>
 *
 * It offers two modes of work:
 * <ul>
 * <li>distributing (feeding) - sends the configuration to other configuration services in the environment,
 * <li>receiving - waits for the configuration from another node.
 * </ul>
 *
 * @since 2.6.0
 * @author AGH AgE Team
 */
public class DistributedConfigurationService implements IComponentInstanceProviderAware, IStatefulComponent {

	private static final Logger log = LoggerFactory.getLogger(DistributedConfigurationService.class);

	private static final String COMPONENT_NAME = DistributedConfigurationService.class.getSimpleName();

	private IComponentInstanceProvider instanceProvider;

	@Inject
	private ICommunicationService communicationService;

	@Inject
	private IAgENodeAddressProvider nodeAddressProvider;

	@Inject
	private ICoreComponent coreComponent;

	private final ListeningScheduledExecutorService service = MoreExecutors
	        .listeningDecorator(newSingleThreadScheduledExecutor());

	private IComponentAddress serviceAddress;

	/**
	 * Distributes provided component definitions among other nodes participating in the computation.
	 * <p>
	 *
	 * Note: this implementation does not care whether anyone received the configuration.
	 *
	 * @param componentDefinitions
	 *            the computation configuration to distribute.
	 */
	public void distributeConfiguration(final Collection<IComponentDefinition> componentDefinitions) {
		// Ensure serializable collection.
		final ArrayList<IComponentDefinition> definitions = newArrayList(checkNotNull(componentDefinitions));

		serviceAddress = new ComponentAddress(COMPONENT_NAME, nodeAddressProvider.getNodeAddress());
		final BroadcastSelector<IComponentAddress> receiversSelector = new BroadcastSelector<IComponentAddress>();

		final IHeader<IComponentAddress> header = new Header<IComponentAddress>(serviceAddress, receiversSelector);
		final IMessage<IComponentAddress, ArrayList<IComponentDefinition>> message =
				new Message<IComponentAddress, ArrayList<IComponentDefinition>>(header, definitions);

		communicationService.send(message);

		notifyAboutConfigurationChange(definitions);
		startCoreComponent();
	}

	/**
	 * Receives a configuration from a remote node. It expects the configuration to be an array list of component
	 * definitions.
	 */
	public void receiveConfiguration() {
		final Callable<ArrayList<IComponentDefinition>> query = new Callable<ArrayList<IComponentDefinition>>() {
			@SuppressWarnings("unchecked")
			@Override
			public ArrayList<IComponentDefinition> call() throws Exception {
				IMessage<IComponentAddress, Serializable> message = null;
				while (message == null) {
					message = communicationService.receive(COMPONENT_NAME);
					Thread.sleep(1000);
				}
				return Messages.getPayloadOfTypeOrThrow(message, ArrayList.class);
			}
		};

		final ListenableFuture<ArrayList<IComponentDefinition>> future = service.submit(query);

		// Add an action performed when a message is received.
		Futures.addCallback(future, new FutureCallback<ArrayList<IComponentDefinition>>() {
			@Override
			public void onSuccess(final ArrayList<IComponentDefinition> result) {
				notifyAboutConfigurationChange(result);
				startCoreComponent();
			}

			@Override
			public void onFailure(final Throwable t) {
				log.error("Could not receive a new configuration.", t);
				teardownPlatform();
			}
		});
	}

	@Override
	public void setInstanceProvider(final IComponentInstanceProvider instanceProvider) {
		this.instanceProvider = instanceProvider;
	}

	private void startCoreComponent() {
		try {
			log.info("Starting computation.");
			coreComponent.start();
			log.info("Computation started.");
		} catch (final ComponentException e) {
			log.error("Could not start the core component.", e);
			teardownPlatform();
		}
	}

	private void notifyAboutConfigurationChange(final ArrayList<IComponentDefinition> result) {
		try {
			final Collection<IComponentDefinition> definitions = Collections.unmodifiableList(result);
			log.debug("Received list of components: {}.", result);
			for (final ConfigurationChangeListener listener : instanceProvider
			        .getInstances(ConfigurationChangeListener.class)) {

				listener.computationConfigurationUpdated(definitions);

			}
		} catch (final ComponentException e) {
			log.error("Notifications about a new configuration failed.", e);
			teardownPlatform();
		}
	}

	/**
	 * Initialises the platform teardown.
	 */
	private void teardownPlatform() {
		instanceProvider.getInstance(IStarter.class).shutdown();
	}

	@Override
	public void init() throws ComponentException {
		// Empty
	}

	@Override
	public boolean finish() throws ComponentException {
		service.shutdown();
		try {
			service.awaitTermination(5, TimeUnit.SECONDS);
		} catch (final InterruptedException e) {
			log.error("The executor service could not be properly shutdown.", e);
			Thread.currentThread().interrupt();
		}
		return true;
	}
}
