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
 * File: WorkplaceManager.java
 * Created: 2008-10-07
 * Author: awos
 * $Id: WorkplaceManager.java 134 2012-03-18 20:17:01Z faber $
 */

package org.jage.workplace;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.platform.component.provider.IMutableComponentInstanceProvider;
import org.jage.platform.component.provider.IMutableComponentInstanceProviderAware;
import org.jage.platform.config.ConfigurationChangeListener;
import org.jage.property.ClassPropertyContainer;
import org.jage.services.core.ICoreComponentListener;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newLinkedList;

/**
 * A base and abstract implementation of the workplace manager.<p>
 * 
 * Synchronization of this object is done by locking its instance.
 * 
 * @author AGH AgE Team
 */
public abstract class WorkplaceManager extends ClassPropertyContainer implements IWorkplaceManager,
        IWorkplaceEnvironment, IMutableComponentInstanceProviderAware, ConfigurationChangeListener {

	private static Logger log = LoggerFactory.getLogger(WorkplaceManager.class);

	protected IMutableComponentInstanceProvider instanceProvider;

	protected final List<ICoreComponentListener> listeners = newLinkedList();

	@Override
	public final synchronized void registerListener(final ICoreComponentListener listener) {
		checkNotNull(listener);

		if (!listeners.contains(listener)) {
			listeners.add(listener);
			log.debug("Registered listener {} to WorkplaceManager.", listener.toString());
		}
	}

	@Override
	public final synchronized void unregisterListener(final ICoreComponentListener listener) {
		checkNotNull(listener);

		if (listeners.contains(listener)) {
			listeners.remove(listener);
			log.debug("Unregistered listener {} to WorkplaceManager.", listener.toString());
		}
	}

	@Override
	public void setMutableComponentInstanceProvider(final IMutableComponentInstanceProvider provider) {
		this.instanceProvider = provider;
	}

}
