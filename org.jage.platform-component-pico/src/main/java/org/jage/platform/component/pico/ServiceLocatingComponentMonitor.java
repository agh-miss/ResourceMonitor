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
 * File: ServiceLocatingComponentMonitor.java
 * Created: 2011-04-16
 * Author: Faber
 * $Id: ServiceLocatingComponentMonitor.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.platform.component.pico;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.monitors.AbstractComponentMonitor;

import org.jage.platform.component.provider.IComponentInstanceProvider;

/**
 * This is a component monitor that monitors failed attempts to locate a component in the container. When this happens
 * it try to locate this component in components implementing IImplementationProvider interface.
 * 
 * Note about performance: DefaultPicoContainer behaves ridiculously and when it retrieves an instance it tries to
 * "pull" it down to the lowest container. That is not a problem, but when doing this, it goes to the top and calls the
 * monitor as many times as there is levels. (It would be worth to override their implementation in future.)
 * 
 * @author AGH AgE Team
 */
public class ServiceLocatingComponentMonitor extends AbstractComponentMonitor {

	private static final long serialVersionUID = -8387031118893242216L;

	private static Logger log = LoggerFactory.getLogger(ServiceLocatingComponentMonitor.class);

	/**
	 * Handles the noComponentFound event.
	 * 
	 * {@inheritDoc}
	 * 
	 * @param container
	 *            The container that caused the event.
	 * @param componentKey
	 *            A key to look up. This implementation accepts only String or Class instances as keys.
	 * @return The found component or <code>null</code> if no component was found or <code>componentKey</code> was not a
	 *         string or Class instance.
	 * 
	 * @see org.picocontainer.monitors.AbstractComponentMonitor#noComponentFound(org.picocontainer.MutablePicoContainer,
	 *      java.lang.Object)
	 */
	@Override
	public Object noComponentFound(MutablePicoContainer container, Object componentKey) {
		log.debug("No component found: " + componentKey + " in " + container);

		if (!(componentKey instanceof String) && !(componentKey instanceof Class<?>)) {
			return null;
		}

		List<IComponentInstanceProvider> providers = container.getComponents(IComponentInstanceProvider.class);

		log.trace("Providers found: " + providers);

		for (IComponentInstanceProvider provider : providers) {
			Object implementation = null;
			if (componentKey instanceof String) {
				implementation = provider.getInstance((String)componentKey);
			} else if (componentKey instanceof Class) {
				implementation = provider.getInstance((Class<?>)componentKey);
			}
			if (implementation != null) {
				log.trace("Implementation found: " + implementation);
				return implementation;
			}
		}

		return null;
	}

}
