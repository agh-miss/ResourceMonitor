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
package org.jage.config;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import org.junit.Before;

import org.jage.platform.component.definition.IComponentDefinition;
import org.jage.platform.component.pico.PicoComponentInstanceProviderFactory;
import org.jage.platform.component.provider.IComponentInstanceProvider;
import org.jage.platform.component.provider.IMutableComponentInstanceProvider;
import org.jage.platform.config.xml.ConfigurationLoader;

/**
 * An abstract test case that provides access to component instances defined in an XML configuration file.
 *
 * The file is passed in the constructor. The, before the test case is used, the configuration is loaded. Afterwards,
 * test cases extending this one can use {@link IComponentInstanceProvider}'s methods (only
 * {@link IComponentInstanceProvider#getInstance(Class)} and {@link IComponentInstanceProvider#getInstance(String)} to
 * get access to components configured as specified in the XML configuration.
 *
 * @author Adam Wos <adam.wos@gmail.com>
 */
public abstract class InstanceProvidingTestCase implements IComponentInstanceProvider {

	private String configurationFile;

	private IMutableComponentInstanceProvider picoContainer;

	public InstanceProvidingTestCase(String configurationFile) {
		this.configurationFile = configurationFile;
	}

	@Before
	public void setUp() throws Exception {
		ConfigurationLoader loader = new ConfigurationLoader();
		List<IComponentDefinition> definitions = loader.loadConfiguration(configurationFile);
		picoContainer = PicoComponentInstanceProviderFactory.createInstanceProvider(definitions);
	}

	public Object getInstance() {
		throw new UnsupportedOperationException(
		        "Only getInstance(Class) and getInstance(String) are supported by this test case");
	}

	@Override
    public <T> T getInstance(Class<T> type) {
		return picoContainer.getInstance(type);
	}

	@Override
    public Object getInstance(String name) {
		return picoContainer.getInstance(name);
	}

	@Override
	public <T> Collection<T> getInstances(Class<T> type) {
		return picoContainer.getInstances(type);
	}

	public String getName() {
		throw new UnsupportedOperationException(
		        "Only getInstance(Class) and getInstance(String) are supported by this test case");
	}

	@Override
    public Class<?> getComponentType(String name) {
		return picoContainer.getComponentType(name);
	}

	@Override
    public <T> T getParametrizedInstance(Class<T> type, Type[] typeParameters) {
		return picoContainer.getParametrizedInstance(type, typeParameters);
	}
}
