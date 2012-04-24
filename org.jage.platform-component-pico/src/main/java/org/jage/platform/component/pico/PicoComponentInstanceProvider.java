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
 * File: PicoComponentInstanceProvider.java
 * Created: 09-09-2010
 * Author: Kamil
 * $Id: PicoComponentInstanceProvider.java 175 2012-03-31 17:21:19Z krzywick $
 */

package org.jage.platform.component.pico;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static java.lang.String.format;

import org.picocontainer.ComponentAdapter;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.PicoCompositionException;
import org.picocontainer.PicoContainer;
import org.picocontainer.PicoVerificationException;
import org.picocontainer.PicoVisitor;
import org.picocontainer.visitors.VerifyingVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.platform.component.definition.ComponentDefinition;
import org.jage.platform.component.definition.IComponentDefinition;
import org.jage.platform.component.pico.injector.Injector;
import org.jage.platform.component.pico.injector.factory.DefaultInjectorFactory;
import org.jage.platform.component.pico.injector.factory.InjectorFactory;

import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;

/**
 * Default implementation of {@link IPicoComponentInstanceProvider}.
 *
 * @author AGH AgE Team
 */
public class PicoComponentInstanceProvider extends DefaultPicoContainer implements IPicoComponentInstanceProvider {

	private static final long serialVersionUID = -8352607280307912901L;

	private static Logger log = LoggerFactory.getLogger(PicoComponentInstanceProvider.class);

	private final InjectorFactory<IComponentDefinition> injectorFactory;

	public PicoComponentInstanceProvider() {
		injectorFactory = new DefaultInjectorFactory();
	}

	public PicoComponentInstanceProvider(final IPicoComponentInstanceProvider parent) {
		super(parent);
		injectorFactory = new DefaultInjectorFactory();
	}

	@Override
	public Object getInstance(final String name) {
		return getComponent(name);
	}

	@Override
	public <T> T getInstance(final Class<T> type) {
		return getComponent(type);
	}

	@Override
	public <T> T getParametrizedInstance(final Class<T> type, final Type[] typeParameters) {
		log.debug("Looking for generic instance of {} with parameters: {}.", type, Arrays.toString(typeParameters));
		final List<T> found = new ArrayList<T>();
		for (final ComponentAdapter<T> adapter : getComponentAdapters(type)) {
			log.debug("Component adapter: {}.", adapter.getClass());
			if (adapter instanceof Injector<?>) {
				final Injector<T> injector = (Injector<T>)adapter;
				final T instance = injector.getParametrizedInstance(type, typeParameters);
				log.debug("Returned {}", instance);
				if (instance != null) {
					found.add(instance);
				}
			}
		}
		if (found.size() == 1) {
			return found.get(0);
		} else if (found.isEmpty()) {
			return null;
		} else {
			throw new PicoCompositionException(String.format(
			        "There are several possible instances of the component: %s", type));
		}
	}

	@Override
	public List<ComponentAdapter<?>> getComponentAdapters(final Class<?> type, final Type[] typeParameters) {
		log.debug("Looking for component adapters for generic instance of {} with parameters: {}.", type,
		        Arrays.toString(typeParameters));
		final List<ComponentAdapter<?>> found = new ArrayList<ComponentAdapter<?>>();
		for (final ComponentAdapter<?> adapter : getComponentAdapters(type)) {
			log.debug("Component adapter: {}.", adapter.getClass());
			if (adapter instanceof Injector<?>) {
				final Injector<?> injector = (Injector<?>)adapter;
				if (injector.canProduceParametrizedInstance(type, typeParameters)) {
					found.add(injector);
				}
			}
		}
		return found;
	}

	@Override
	public void addComponent(final IComponentDefinition definition) {
		IPicoComponentInstanceProvider actualProvider = this;
		if (!definition.getInnerComponentDefinitions().isEmpty()) {
			actualProvider = makeChildContainer();
			for (final IComponentDefinition innerDefinition : definition.getInnerComponentDefinitions()) {
				actualProvider.addComponent(innerDefinition);
			}
		}

		// The adapter will be added to this provider, but will possibly use an inner one for constructing instances
		final ComponentAdapter<Object> adapter = injectorFactory.createAdapter(definition);
		addAdapter(new FixedContainerAdapter<Object>(actualProvider, adapter));
	}

	@Override
	public void addComponent(final Class<?> compImplementation) {
		addComponent(new ComponentDefinition(compImplementation.getName(), compImplementation, true));
	}

	@Override
	public void addComponent(final String key, final Class<?> compType) {
		addComponent(new ComponentDefinition(key, compType, true));
	}

	@Override
	public void addComponentInstance(final Object compInstance) {
		addComponent(compInstance.getClass().getName(), compInstance);
	}

	@Override
	public <T> Collection<T> getInstances(final Class<T> type) {
		return getComponents(type);
	}

	@Override
	public Class<?> getComponentType(final String name) {
		return getComponentAdapter(name).getComponentImplementation();
	}

	@Override
	public void verify() throws PicoCompositionException {
		try {
			new VerifyingVisitor().traverse(this);
		} catch (PicoVerificationException e) {
			List<Throwable> nestedExceptions = e.getNestedExceptions();
			StringBuilder builder = new StringBuilder(format("Verification failed. %1$s verification errors:\n",
			        nestedExceptions.size()));
			for (Throwable throwable : nestedExceptions) {
				builder.append(format("\t %1$s \n", throwable.getMessage()));
			}
			log.error(builder.toString());
			throw new PicoCompositionException("Verification failed");
		}
	}

	@Override
	public void reconfigure(final Collection<IComponentDefinition> defs) {
		// froze the container

		// reconfigure definitions
		for (final IComponentDefinition definition : defs) {
			if (getComponentKeyToAdapterCache().containsKey(definition.getName())) {
				final ComponentAdapter<?> componentAdapter = getComponentKeyToAdapterCache().get(definition.getName());
				if (componentAdapter instanceof Injector) {
//					final Injector<?> injector = (Injector<?>)componentAdapter;
					// injector.reconfigure(definition);
				} else {
					// create new and replace the adapter
				}
			} else {
				// create new adapter and add it
				addComponent(definition);
			}
		}

		// update component instances (reinject)

		// unfroze the container

	}

	@Override
	public IPicoComponentInstanceProvider makeChildContainer() {
		final PicoComponentInstanceProvider child = new PicoComponentInstanceProvider(this);
		addChildContainer(child);
		return child;
	}

	@Override
	public String toString() {
		ToStringHelper helper = Objects.toStringHelper(this).add("id", hashCode());
		if (getParent() != null) {
			helper.add("parent", getParent().hashCode());
		}
		return helper.toString();
	}

	private static class FixedContainerAdapter<T> implements ComponentAdapter<T> {
		private final PicoContainer container;

		private final ComponentAdapter<T> delegate;

		public FixedContainerAdapter(final PicoContainer container, final ComponentAdapter<T> delegate) {
			this.container = container;
			this.delegate = delegate;
		}

		@Override
		public Object getComponentKey() {
			return delegate.getComponentKey();
		}

		@Override
		public Class<? extends T> getComponentImplementation() {
			return delegate.getComponentImplementation();
		}

		@Override
		public T getComponentInstance(final PicoContainer container) throws PicoCompositionException {
			return getComponentInstance(container, ComponentAdapter.NOTHING.class);
		}

		@Override
		public T getComponentInstance(final PicoContainer container, final Type into) throws PicoCompositionException {
			return delegate.getComponentInstance(this.container, into);
		}

		@Override
		public void verify(final PicoContainer container) throws PicoCompositionException {
			delegate.verify(this.container);
		}

		@Override
		public void accept(final PicoVisitor visitor) {
			if(visitor instanceof VerifyingVisitor) {
				// Stop validating visitors here, or else they might get to delegate with a different container
				visitor.visitComponentAdapter(this);
			} else {
				delegate.accept(visitor);
			}
		}

		@Override
		public ComponentAdapter<T> getDelegate() {
			return delegate;
		}

		@SuppressWarnings("rawtypes")
		@Override
		public <U extends ComponentAdapter> U findAdapterOfType(final Class<U> adapterType) {
			return delegate.findAdapterOfType(adapterType);
		}

		@Override
		public String getDescriptor() {
			return delegate.getDescriptor();
		}

		@Override
		public String toString() {
			return getDescriptor() + " " + getComponentKey();
		}
	}
}
