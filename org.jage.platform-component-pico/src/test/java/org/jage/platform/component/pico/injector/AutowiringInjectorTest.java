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
 * File: AutowiringInjectorTest.java
 * Created: 2012-03-15
 * Author: Krzywicki
 * $Id $
 */

package org.jage.platform.component.pico.injector;

import java.lang.reflect.Type;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.picocontainer.Injector;
import org.picocontainer.PicoContainer;
import org.picocontainer.adapters.InstanceAdapter;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.jage.platform.component.annotation.Inject;
import org.jage.platform.component.definition.ComponentDefinition;
import org.jage.platform.component.definition.ConfigurationException;
import org.jage.platform.component.definition.IArgumentDefinition;

/**
 * Tests for AutowiringInjector.
 *
 * @author AGH AgE Team
 */
@RunWith(MockitoJUnitRunner.class)
public class AutowiringInjectorTest extends AbstractBaseInjectorTest {

	private static final String VALUE = "value";

	@Mock
	private PicoContainer container;

	@Test(expected = NullPointerException.class)
	public void shouldThrowNPEForNullDefinition() {
		// when
		new AutowiringInjector<Object>(null);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void shouldNotSupportCreatingComponents() {
		// given
		final Injector<Object> injector = injectorFor(anyDefinition());

		// when
		injector.getComponentInstance(container, null);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void shouldNotSupportCreatingInstances() throws Exception {
		// given
		final Injector<Object> injector = injectorFor(Object.class);

		// when
		injector.getComponentInstance(container, null);
	}

	@Test
	public void shouldInjectPublicField() throws ConfigurationException {
		// given
		final PublicField instance = new PublicField();
		final Injector<PublicField> injector = injectorFor(instance);
		providerHasComponent(VALUE);

		// when
		injector.decorateComponentInstance(container, null, instance);

		// then
		assertThat(instance.field, is(equalTo(VALUE)));
	}

	@Test
	public void shouldInjectPackageField() throws ConfigurationException {
		// given
		final PackageField instance = new PackageField();
		final Injector<PackageField> injector = injectorFor(instance);
		providerHasComponent(VALUE);

		// when
		injector.decorateComponentInstance(container, null, instance);

		// then
		assertThat(instance.field, is(equalTo(VALUE)));
	}

	@Test
	public void shouldInjectPrivateField() throws ConfigurationException {
		// given
		final PrivateField instance = new PrivateField();
		final Injector<PrivateField> injector = injectorFor(instance);
		providerHasComponent(VALUE);

		// when
		injector.decorateComponentInstance(container, null, instance);

		// then
		assertThat(instance.field, is(equalTo(VALUE)));
	}

	@Test
	public void shouldNotInjectFinalField() throws ConfigurationException {
		// given
		final FinalField instance = new FinalField();
		final Injector<FinalField> injector = injectorFor(instance);

		// when
		injector.decorateComponentInstance(container, null, instance);

		// then
		assertThat(instance.field, is(nullValue()));
	}

	@Test
	public void shouldInjectSuperclassField() throws ConfigurationException {
		// given
		final PrivateFieldSubclass instance = new PrivateFieldSubclass();
		final Injector<PrivateFieldSubclass> injector = injectorFor(instance);
		providerHasComponent(VALUE);

		// when
		injector.decorateComponentInstance(container, null, instance);

		// then
		assertThat(((PrivateField)instance).field, is(equalTo(VALUE)));
	}

	@Test
	public void shouldInjectSuperclassFieldFirst() throws ConfigurationException {
		// given
		final PrivateFieldSubclass instance = new PrivateFieldSubclass();
		final Injector<PrivateFieldSubclass> injector = injectorFor(instance);
		final String first = "first";
		final String second = "second";
		providerHasComponent(String.class, first, second);

		// when
		injector.decorateComponentInstance(container, null, instance);

		// then
		assertThat(((PrivateField)instance).field, is(equalTo(first)));
		assertThat(instance.field2, is(equalTo(second)));
	}

	@Test
	public void shouldNotInjectExplicitlyInitializedField() throws ConfigurationException {
		// given
		final PrivateField instance = new PrivateField();
		final ComponentDefinition definition = definitionFor(instance);
		definition.addPropertyArgument("field", mock(IArgumentDefinition.class));
		final Injector<PrivateField> injector = injectorFor(definition);

		// when
		injector.decorateComponentInstance(container, null, instance);

		// then
		assertThat(instance.field, is(nullValue()));
	}

	@Test
	public void shouldInjectPublicMethod() throws ConfigurationException {
		// given
		final PublicMethod instance = new PublicMethod();
		final Injector<PublicMethod> injector = injectorFor(instance);
		providerHasComponent(VALUE);

		// when
		injector.decorateComponentInstance(container, null, instance);

		// then
		assertThat(instance.field, is(equalTo(VALUE)));
	}

	@Test
	public void shouldInjectPackageMethod() throws ConfigurationException {
		// given
		final PackageMethod instance = new PackageMethod();
		final Injector<PackageMethod> injector = injectorFor(instance);
		providerHasComponent(VALUE);

		// when
		injector.decorateComponentInstance(container, null, instance);

		// then
		assertThat(instance.field, is(equalTo(VALUE)));
	}

	@Test
	public void shouldInjectPrivateMethod() throws ConfigurationException {
		// given
		final PrivateMethod instance = new PrivateMethod();
		final Injector<PrivateMethod> injector = injectorFor(instance);
		providerHasComponent(VALUE);

		// when
		injector.decorateComponentInstance(container, null, instance);

		// then
		assertThat(instance.field, is(equalTo(VALUE)));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldNotInjectAbstractMethod() throws ConfigurationException {
		// given
		final AbstractMethod instance = mock(AbstractMethod.class);
		given((Class<AbstractMethod>)instance.getClass()).willReturn(AbstractMethod.class);
		final Injector<AbstractMethod> injector = injectorFor(instance);
		providerHasComponent(VALUE);

		// when
		injector.decorateComponentInstance(container, null, instance);

		// then
		verify(instance, never()).inject(VALUE);
	}

	@Test
	public void shouldNotInjectMethodWithTypeParameter() throws ConfigurationException {
		// given
		final TypedMethod instance = new TypedMethod();
		final Injector<TypedMethod> injector = injectorFor(instance);
		providerHasComponent(VALUE);

		// when
		injector.decorateComponentInstance(container, null, instance);

		// then
		assertThat(instance.field, is(nullValue()));
	}

	@Test
	public void shouldInjectMethodWithZeroArgs() throws ConfigurationException {
		// given
		final ZeroArgMethod instance = new ZeroArgMethod();
		final Injector<ZeroArgMethod> injector = injectorFor(instance);

		// when
		injector.decorateComponentInstance(container, null, instance);

		// then
		assertThat(instance.field, is(equalTo(VALUE)));
	}

	@Test
	public void shouldInjectMethodWithOneArg() throws ConfigurationException {
		// given
		final OneArgMethod instance = new OneArgMethod();
		final Injector<OneArgMethod> injector = injectorFor(instance);
		providerHasComponent(VALUE);

		// when
		injector.decorateComponentInstance(container, null, instance);

		// then
		assertThat(instance.field, is(equalTo(VALUE)));
	}

	@Test
	public void shouldInjectMethodWithManyArgs() throws ConfigurationException {
		// given
		final ManyArgMethod instance = new ManyArgMethod();
		final Injector<ManyArgMethod> injector = injectorFor(instance);
		final String first = "first";
		final String second = "second";
		final String third = "third";
		providerHasComponent(String.class, first, second, third);

		// when
		injector.decorateComponentInstance(container, null, instance);

		// then
		assertThat(instance.field1, is(equalTo(first)));
		assertThat(instance.field2, is(equalTo(second)));
		assertThat(instance.field3, is(equalTo(third)));
	}

	@Test
	public void shouldInjectSuperclassMethod() throws ConfigurationException {
		// given
		final PrivateMethodSubclass instance = new PrivateMethodSubclass();
		final Injector<PrivateMethodSubclass> injector = injectorFor(instance);
		providerHasComponent(VALUE);

		// when
		injector.decorateComponentInstance(container, null, instance);

		// then
		assertThat(((PrivateMethod)instance).field, is(equalTo(VALUE)));
	}

	@Test
	public void shouldInjectSuperclassMethodFirst() throws ConfigurationException {
		// given
		final PrivateMethodSubclass instance = new PrivateMethodSubclass();
		final Injector<PrivateMethodSubclass> injector = injectorFor(instance);
		final String first = "first";
		final String second = "second";
		providerHasComponent(String.class, first, second);

		// when
		injector.decorateComponentInstance(container, null, instance);

		// then
		assertThat(((PrivateMethod)instance).field, is(equalTo(first)));
		assertThat(instance.field2, is(equalTo(second)));
	}

	@Test
	public void shouldInjectOverridenAnnotatedMethodOnce() throws ConfigurationException {
		// given
		final OverridingAnnotatedMethod instance = new OverridingAnnotatedMethod();
		final Injector<OverridingAnnotatedMethod> injector = injectorFor(instance);
		providerHasComponent(VALUE);

		// when
		injector.decorateComponentInstance(container, null, instance);

		// then
		assertThat(((PublicMethod)instance).field, is(nullValue()));
		assertThat(instance.field, is(equalTo(VALUE)));
	}

	@Test
	public void shouldNotInjectOverridenNotAnnotatedMethod() throws ConfigurationException {
		// given
		final OverridingNotAnnotatedMethod instance = new OverridingNotAnnotatedMethod();
		final Injector<OverridingNotAnnotatedMethod> injector = injectorFor(instance);
		providerHasComponent(VALUE);

		// when
		injector.decorateComponentInstance(container, null, instance);

		// then
		assertThat(((PublicMethod)instance).field, is(nullValue()));
		assertThat(instance.field, is(nullValue()));
	}

	@Test
	public void shouldNotInjectExplicitlyInitializedSetter() throws ConfigurationException {
		// given
		final SetterMethod instance = new SetterMethod();
		final ComponentDefinition definition = definitionFor(instance);
		definition.addPropertyArgument("property", mock(IArgumentDefinition.class));
		final Injector<SetterMethod> injector = injectorFor(definition);

		// when
		injector.decorateComponentInstance(container, null, instance);

		// then
		assertThat(instance.property, is(nullValue()));
	}

	@Test
	public void shouldInjectFieldBeforeMethod() throws ConfigurationException {
		// given
		final FieldMethod instance = new FieldMethod();
		final Injector<FieldMethod> injector = injectorFor(instance);
		final String first = "first";
		final String second = "second";
		providerHasComponent(String.class, first, second);

		// when
		injector.decorateComponentInstance(container, null, instance);

		// then
		assertThat(instance.field, is(first));
		assertThat(instance.field2, is(second));
	}

	@Override
    protected <T> Injector<T> injectorFor(final ComponentDefinition definition) {
		return new AutowiringInjector<T>(definition);
	}

	private static class PublicField {
		@Inject
		public String field;
	}

	private static class PackageField {
		@Inject
		String field;
	}

	private static class PrivateField {
		@Inject
		private String field;
	}

	private static class FinalField {
		@Inject
		private final String field = null;
	}

	private static class PrivateFieldSubclass extends PrivateField {
		@Inject
		private String field2;
	}

	private static class PublicMethod {
		private String field;

		@SuppressWarnings("unused")
		@Inject
		public void inject(final String value) {
			field = value;
		}
	}

	private static class PackageMethod {
		private String field;

		@SuppressWarnings("unused")
		@Inject
		void inject(final String value) {
			field = value;
		}
	}

	private static class PrivateMethod {
		private String field;

		@SuppressWarnings("unused")
		@Inject
		private void inject(final String value) {
			field = value;
		}
	}

	private static abstract class AbstractMethod {
		@Inject
		public abstract void inject(final String value);
	}

	private static class TypedMethod {
		private String field;

		@SuppressWarnings("unused")
		@Inject
		private <T> void inject(final String value) {
			field = value;
		}
	}

	private static class ZeroArgMethod {
		private String field;

		@SuppressWarnings("unused")
		@Inject
		private void inject() {
			field = VALUE;
		}
	}

	private static class OneArgMethod {
		private String field;

		@SuppressWarnings("unused")
		@Inject
		private void inject(final String value) {
			field = value;
		}
	}

	private static class ManyArgMethod {
		private String field1;

		private String field2;

		private String field3;

		@SuppressWarnings("unused")
		@Inject
		private void inject(final String value1, final String value2, final String value3) {
			field1 = value1;
			field2 = value2;
			field3 = value3;
		}
	}

	private static class PrivateMethodSubclass extends PrivateMethod {
		private String field2;

		@SuppressWarnings("unused")
		@Inject
		private void inject2(final String value) {
			field2 = value;
		}
	}

	private static class OverridingAnnotatedMethod extends PublicMethod {
		private String field;

		@Inject
		@Override
		public void inject(final String value) {
			field = value;
		}
	}

	private static class OverridingNotAnnotatedMethod extends PublicMethod {
		private String field;

		@Override
		public void inject(final String value) {
			field = value;
		}
	}

	private static class SetterMethod {
		private String property;

		@SuppressWarnings("unused")
        @Inject
		public void setProperty(final String property) {
			this.property = property;
		}
	}

	private static class FieldMethod {
		@Inject
		private String field;

		private String field2;

		@SuppressWarnings("unused")
        @Inject
		public void setField2(final String value) {
			field2 = value;
		}
	}

	@SuppressWarnings("unchecked")
	private <T> void providerHasComponent(final T component) {
		providerHasComponent((Class<T>)component.getClass(), component);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private <T> void providerHasComponent(final Class<T> key, final T component, final T... components) {
		given(container.getComponent(eq(key), any(Type.class))).willReturn(component, components);
		given(container.getComponentAdapter(key)).willReturn(new InstanceAdapter(key, component), adaptersFor(key, components));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private <T> InstanceAdapter[] adaptersFor(final Class<T> key, final T... components) {
		InstanceAdapter[] adapters = new InstanceAdapter[components.length];
		for (int i = 0; i < adapters.length; i++) {
	        adapters[i] = new InstanceAdapter(key, components[i]);
        }
		return adapters;
	}
}
