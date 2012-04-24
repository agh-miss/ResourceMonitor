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
 * File: StrategyProvider.java
 * Created: 2011-04-16
 * Author: faber
 * $Id: StrategyProvider.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.examples.delegation;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.platform.component.annotation.Inject;
import org.jage.platform.component.provider.IComponentInstanceProvider;
import org.jage.property.ClassPropertyContainer;
import org.jage.property.PropertyField;

/**
 * Simple provider of strategies that uses an externally configured map with implementations.
 * 
 * @author AGH AgE Team
 */
public class StrategyProvider extends ClassPropertyContainer implements IComponentInstanceProvider {

	@Inject
	@PropertyField(propertyName = "strategies")
	private Map<String, IEchoStrategy> strategies;

	private final Logger log = LoggerFactory.getLogger(StrategyProvider.class);

	@Override
	public Object getInstance(String name) {
		log.info("Strategy lookup: {}", name);

		return strategies.get(name);
	}

	@Override
	public <T> T getInstance(Class<T> type) {
		log.info("Strategy lookup: {}", type);

		for (IEchoStrategy instance : strategies.values()) {
			if (type.isAssignableFrom(instance.getClass())) {
				return type.cast(instance);
			}
		}
		return null;
	}

	@Override
	public <T> Collection<T> getInstances(Class<T> type) {
		log.info("Strategy lookup: {}", type);

		List<T> instances = new ArrayList<T>();

		for (IEchoStrategy instance : strategies.values()) {
			if (type.isAssignableFrom(instance.getClass())) {
				instances.add(type.cast(instance));
			}
		}
		return instances;
	}

	@Override
	public Class<?> getComponentType(String name) {
		return strategies.get(name).getClass();
	}

	@Override
	public <T> T getParametrizedInstance(Class<T> type, Type[] typeParameters) {
		throw new UnsupportedOperationException();
	}

}
