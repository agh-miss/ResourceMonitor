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
 * File: AbstractInjector.java
 * Created: 2010-09-13
 * Author: Kamil
 * $Id: AbstractInjector.java 172 2012-03-30 16:08:11Z krzywick $
 */

package org.jage.platform.component.pico.injector;

import java.lang.reflect.Type;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import static java.lang.String.format;

import org.picocontainer.ObjectReference;
import org.picocontainer.PicoCompositionException;
import org.picocontainer.PicoContainer;
import org.picocontainer.adapters.AbstractAdapter;
import org.picocontainer.injectors.AbstractInjector.CyclicDependencyException;

import org.jage.platform.component.definition.IComponentDefinition;

import com.google.common.base.Throwables;

/**
 * This class provides a default injector implementations.
 * <p>
 * Subclasses may override the {@code doVerify} {@code doCreate} or {@code doInject} methods. Default implementations
 * are provided.
 * <p>
 * All these methods are handled in a way which detects circular loops in the container structure. Any exception will be
 * wrapped into a {@link PicoCompositionException}, along with sensible information about the adapted component.
 *
 * @param <T>
 *            the type of components this injector can create or inject into
 *
 * @author AGH AgE Team
 */
public abstract class AbstractInjector<T> extends AbstractAdapter<T> implements Injector<T> {

	private static final long serialVersionUID = 1L;

	private final ThreadLocalCyclicDependencyGuard verificationGuard = new ThreadLocalCyclicDependencyGuard();

	private final ThreadLocalCyclicDependencyGuard instantiationGuard = new ThreadLocalCyclicDependencyGuard();

	/**
	 * Creates an AbstractInjector from the name and type of the given definition.
	 *
	 * @param definition
	 *            the component definition
	 */
	protected AbstractInjector(final IComponentDefinition definition) {
		super(definition.getName(), definition.getType());
	}

	@Override
	public final void verify(final PicoContainer container) throws PicoCompositionException {
		verificationGuard.run(getComponentImplementation(), new Runnable() {
			@Override
			public void run() {
				try {
					doVerify(container);
				} catch (final PicoCompositionException e) {
					throw rewrittenException("Verification error", e);
				}
			}
		});
	}

	@Override
	public final T getComponentInstance(final PicoContainer container, final Type into) throws PicoCompositionException {
		return instantiationGuard.call(getComponentImplementation(), new Callable<T>() {
			@Override
			public T call() throws Exception {
				try {
					return doCreate(container);
				} catch (final PicoCompositionException e) {
					throw rewrittenException("Instantiation error", e);
				}
			}
		});
	}

	@Override
	public final T decorateComponentInstance(final PicoContainer container, final Type into, final T instance)
	        throws PicoCompositionException {

		return instantiationGuard.call(getComponentImplementation(), new Callable<T>() {
			@Override
			public T call() throws Exception {
				try {
					doInject(container, instance);
					return instance;
				} catch (final PicoCompositionException e) {
					throw rewrittenException("Injection error", e);
				}
			}
		});
	}

	/**
	 * Performs the actual verification of the injector. Default implementation does nothing.
	 *
	 * @param container
	 *            the verification context
	 */
	protected void doVerify(final PicoContainer container) throws PicoCompositionException {
		// do nothing
	}

	/**
	 * Performs the actual instantiation by the injector. Default implementation throws an
	 * {@link UnsupportedOperationException}.
	 *
	 * @param container
	 *            the verification context
	 * @return the component instance produced by this injector
	 */
	protected T doCreate(final PicoContainer container) throws PicoCompositionException {
		throw new UnsupportedOperationException();

	}

	/**
	 * Performs the actual injection by the injector. Default implementation throws an
	 * {@link UnsupportedOperationException}.
	 *
	 * @param container
	 *            the verification context
	 * @param instance
	 *            the instance to inject into
	 */
	protected void doInject(final PicoContainer container, final T instance) throws PicoCompositionException {
		throw new UnsupportedOperationException();
	}

	/**
	 * Rewrites the exception message to include information about the adapted component.
	 */
	protected PicoCompositionException rewrittenException(final String message, final PicoCompositionException e) {
		throw new PicoCompositionException(format("%1$s for component '%2$s' of type '%3$s': %4$s", message,
		        getComponentKey(), getComponentImplementation(), e.getMessage()), e);
	}

	@Override
	public boolean canProduceParametrizedInstance(final Class<?> type, final Type[] typeParameters) {
		return false;
	}

	@Override
	public <TT> TT getParametrizedInstance(final Class<TT> type, final Type[] typeParameters) {
		return null;
	}

	/**
	 * Wrap any exceptions in {@link PicoCompositionException} ones.
	 */
	protected PicoCompositionException wrappedException(final Exception e) {
		throw new PicoCompositionException(e);
	}

	/**
	 * Abstract utility class to detect recursion cycles.
	 *
	 * @author AGH AgE Team
	 */
	public static final class ThreadLocalCyclicDependencyGuard extends ThreadLocal<Boolean> {

		/**
		 * Creates a {@link ThreadLocalCyclicDependencyGuard} initialized to Boolean.False.
		 */
		public ThreadLocalCyclicDependencyGuard() {
			set(Boolean.FALSE);
		}

		/**
		 * Runs the runnable. This guard will hold the {@link Boolean} value. If the guard is already
		 * <code>Boolean.TRUE</code> a {@link CyclicDependencyException} will be thrown. If the runnable throws an
		 * exception, it will be simply propagated, and the guard will be released.
		 *
		 * @param stackFrame
		 *            the current stack frame
		 * @param runnable
		 *            the runnable to be run
		 * @throws CyclicDependencyException
		 *             if the execution loops back to this instance, while the guard is still held
		 */
		public void run(final Class<?> stackFrame, final Runnable runnable) throws CyclicDependencyException {
			call(stackFrame, Executors.callable(runnable));
		}

		/**
		 * Call the callable. This guard will hold the {@link Boolean} value. If the guard is already
		 * <code>Boolean.TRUE</code> a {@link CyclicDependencyException} will be thrown. If the callable throws an
		 * exception, it will be simply propagated, and the guard will be released.
		 *
		 * @param <V>
		 *            return value type
		 * @param stackFrame
		 *            the current stack frame
		 * @param callable
		 *            the callable to be called
		 * @return the callable's result
		 * @throws CyclicDependencyException
		 *             if the execution loops back to this instance, while the guard is still held
		 */
		public <V> V call(final Class<?> stackFrame, final Callable<V> callable) throws CyclicDependencyException {
			if (Boolean.TRUE.equals(get())) {
				throw new CyclicDependencyException(stackFrame);
			}
			try {
				set(Boolean.TRUE);
				return callable.call();
			} catch (final CyclicDependencyException e) {
				e.push(stackFrame);
				throw e;
			} catch (Exception e) {
				throw Throwables.propagate(e);
			} finally {
				set(Boolean.FALSE);
			}
		}
	}
}
