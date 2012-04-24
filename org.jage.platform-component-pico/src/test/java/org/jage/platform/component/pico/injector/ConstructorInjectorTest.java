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
 * File: ConstructorInjectorTest.java
 * Created: 2012-03-03
 * Author: Krzywicki
 * $Id: ConstructorInjectorTest.java 213 2012-04-08 20:21:35Z krzywick $
 */

package org.jage.platform.component.pico.injector;

import java.lang.reflect.Type;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.picocontainer.Injector;
import org.picocontainer.PicoCompositionException;
import org.picocontainer.adapters.InstanceAdapter;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

import org.jage.platform.component.annotation.Inject;
import org.jage.platform.component.definition.ComponentDefinition;
import org.jage.platform.component.definition.ValueDefinition;
import org.jage.platform.component.pico.IPicoComponentInstanceProvider;

/**
 * Tests for ConstructorInjector.
 *
 * @author AGH AgE Team
 */
@RunWith(MockitoJUnitRunner.class)
public class ConstructorInjectorTest extends AbstractBaseInjectorTest {

	@Mock
	private IPicoComponentInstanceProvider container;

	@Test(expected = NullPointerException.class)
	public void shouldThrowNPEForNullDefinition() {
		// when
		new ConstructorInjector<Object>(null);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void shouldNotSupportDecoratingComponents() {
		// given
		final Object instance = new Object();
		final Injector<Object> injector = injectorFor(instance);

		// when
		injector.decorateComponentInstance(container, null, instance);
	}

	@Test(expected = PicoCompositionException.class)
	public void testNoMatchingArguments() throws Exception {
		// given
		final ComponentDefinition definition = definitionFor(DefaultConstructor.class);
		definition.addConstructorArgument(new ValueDefinition(String.class, "value"));
		final Injector<DefaultConstructor> injector = injectorFor(definition);

		// when
		injector.getComponentInstance(container, null);
	}

	@Test(expected = PicoCompositionException.class)
	public void testMultipleMatchingArguments() throws Exception {
		// given
		final ComponentDefinition definition = definitionFor(MultipleMatchingArguments.class);
		definition.addConstructorArgument(new ValueDefinition(String.class, "value"));
		final Injector<MultipleMatchingArguments> injector = injectorFor(definition);

		// when
		injector.getComponentInstance(container, null);
	}

	@Test
	public void testSingleMatchingArgument() throws Exception {
		// given
		final ComponentDefinition definition = definitionFor(SingleMatchingArgument.class);
		final String value = "value";
		definition.addConstructorArgument(new ValueDefinition(String.class, value));
		final Injector<SingleMatchingArgument> injector = injectorFor(definition);

		// when
		final SingleMatchingArgument instance = injector.getComponentInstance(container, null);

		// then
		assertNotNull(instance);
		assertThat(instance.value, is(equalTo(value)));
	}

	@Test
	public void testSingleMatchingSuperArgument() throws Exception {
		// given
		final ComponentDefinition definition = definitionFor(SingleMatchingSuperArgument.class);
		final String value = "value";
		definition.addConstructorArgument(new ValueDefinition(String.class, value));
		final Injector<SingleMatchingSuperArgument> injector = injectorFor(definition);

		// when
		final SingleMatchingSuperArgument instance = injector.getComponentInstance(container, null);

		// then
		assertNotNull(instance);
		assertThat(instance.value, is(String.class));
		assertThat((String)instance.value, is(equalTo(value)));
	}

	@Test
	public void testSingleMatchingPrimitiveArgument() throws Exception {
		// given
		final ComponentDefinition definition = definitionFor(SingleMatchingPrimitiveArgument.class);
		final String value = "123";
		definition.addConstructorArgument(new ValueDefinition(Integer.class, value));
		final Injector<SingleMatchingPrimitiveArgument> injector = injectorFor(definition);

		// when
		final SingleMatchingPrimitiveArgument instance = injector.getComponentInstance(container, null);

		// then
		assertNotNull(instance);
		assertThat(instance.value, is(equalTo(Integer.parseInt(value))));
	}

	@Test(expected = PicoCompositionException.class)
	public void testMultipleAnnotation() throws Exception {
		// given
		final Injector<MultipleAnnotation> injector = injectorFor(MultipleAnnotation.class);

		// when
		injector.getComponentInstance(container, null);
	}

	@Test
	public void testSingleAnnotationNoDependencies() throws Exception {
		// given
		final Injector<SingleAnnotationNoDependencies> injector = injectorFor(SingleAnnotationNoDependencies.class);

		// when
		final SingleAnnotationNoDependencies instance = injector.getComponentInstance(container, null);

		// then
		assertNotNull(instance);
	}

	@Test(expected = PicoCompositionException.class)
	public void testSingleAnnotationWithDependencyUnsatisfied() throws Exception {
		// given
		final Injector<SingleAnnotationWithDependency> injector = injectorFor(SingleAnnotationWithDependency.class);

		// when
		injector.getComponentInstance(container, null);
	}

	@Test
	public void testSingleAnnotationWithDependency() throws Exception {
		// given
		final Injector<SingleAnnotationWithDependency> injector = injectorFor(SingleAnnotationWithDependency.class);
		final Object dependency = new Object();
		providerHasComponent(Object.class, dependency);

		// when
		final SingleAnnotationWithDependency instance = injector.getComponentInstance(container, null);

		// then
		assertNotNull(instance);
		assertThat(instance.dependency, is(sameInstance(dependency)));
	}

	@Test(expected = PicoCompositionException.class)
	public void testNoDefaultConstructor() throws Exception {
		// given
		final Injector<NoDefaultConstructor> injector = injectorFor(NoDefaultConstructor.class);

		// when
		injector.getComponentInstance(container, null);
	}

	@Test
	public void testDefaultConstructor() throws Exception {
		// given
		final Injector<DefaultConstructor> injector = injectorFor(DefaultConstructor.class);

		// when
		final DefaultConstructor instance = injector.getComponentInstance(container, null);

		// then
		assertNotNull(instance);
	}

	@Override
    protected <T> Injector<T> injectorFor(final ComponentDefinition definition) {
		return new ConstructorInjector<T>(definition);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private <T> void providerHasComponent(final Class<T> key, final T component) {
		given(container.getComponent(eq(key), any(Type.class))).willReturn(component);
		given(container.getComponentAdapter(key)).willReturn(new InstanceAdapter(key, component));
	}

	@SuppressWarnings("unused")
	private static class MultipleMatchingArguments {
		public MultipleMatchingArguments(final Object value) {
		}

		public MultipleMatchingArguments(final String value) {
		}
	}

	@SuppressWarnings("unused")
	private static class SingleMatchingArgument {
		private final String value;

		public SingleMatchingArgument(final String value) {
			this.value = value;
		}
	}

	@SuppressWarnings("unused")
	private static class SingleMatchingSuperArgument {
		private final Object value;

		public SingleMatchingSuperArgument(final Object value) {
			this.value = value;
		}
	}

	@SuppressWarnings("unused")
	private static class SingleMatchingPrimitiveArgument {
		private final int value;

		public SingleMatchingPrimitiveArgument(final int value) {
			this.value = value;
		}
	}

	@SuppressWarnings("unused")
	private static class MultipleAnnotation {
		@Inject
		public MultipleAnnotation() {
		}

		@Inject
		public MultipleAnnotation(final String foo) {
		}
	}

	@SuppressWarnings("unused")
	private static class SingleAnnotationNoDependencies {
		@Inject
		public SingleAnnotationNoDependencies() {
		}
	}

	@SuppressWarnings("unused")
	private static class SingleAnnotationWithDependency {
		private final Object dependency;

		@Inject
		public SingleAnnotationWithDependency(final Object dependency) {
			this.dependency = dependency;
		}
	}

	@SuppressWarnings("unused")
	private static class NoDefaultConstructor {
		public NoDefaultConstructor(final String foo) {
		}
	}

	private static class DefaultConstructor {
	}
}
