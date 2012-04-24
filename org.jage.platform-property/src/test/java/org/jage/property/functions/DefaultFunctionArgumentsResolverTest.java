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
package org.jage.property.functions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.jage.property.IPropertyContainer;
import org.jage.property.functions.DefaultFunctionArgumentsResolver;
import org.jage.property.functions.FunctionArgument;
import org.jage.property.functions.InvalidArgumentsPatternException;
import org.junit.Before;
import org.junit.Test;

public class DefaultFunctionArgumentsResolverTest {

	private FunctionsTestPropertyContainer _rootObject;
	private DefaultFunctionArgumentsResolver _resolver;

	@Before
	public void setUp() {
		_rootObject = new FunctionsTestPropertyContainer();
		_resolver = new DefaultFunctionArgumentsResolver(_rootObject);
	}

	@Test
	public void testSinglePath() throws Exception {
		String path = "propertyA[3].propertyB[2].C";
		FunctionsTestPropertyContainer subContainer1 = new FunctionsTestPropertyContainer();
		FunctionsTestPropertyContainer subContainer2 = new FunctionsTestPropertyContainer();
		subContainer2.addProperty("C", "Value of property C");
		subContainer1.addProperty("propertyB", new IPropertyContainer[] { null,
				null, subContainer2 });
		_rootObject.addProperty("propertyA",
				new FunctionsTestPropertyContainer[] { null, null, null,
						subContainer1, null });

		ArrayList<FunctionArgument> arguments = new ArrayList<FunctionArgument>();
		for (FunctionArgument argument : _resolver.resolveArguments(path)) {
			arguments.add(argument);
		}
		assertEquals(1, arguments.size());
		assertSame(subContainer2, arguments.get(0).getPropertyContainer());
		assertEquals("C", arguments.get(0).getPropertyPath());
	}

	@Test
	public void testSeveralPaths() throws Exception {
		String pattern = "a,b,c,d,e";
		_rootObject.addProperty("a", 1);
		_rootObject.addProperty("b", null);
		_rootObject.addProperty("c", new Object());
		ArrayList<FunctionArgument> arguments = new ArrayList<FunctionArgument>();
		for (FunctionArgument argument : _resolver.resolveArguments(pattern)) {
			arguments.add(argument);
		}
		assertEquals(3, arguments.size());
		String[] expectedPaths = { "a", "b", "c" };
		for (int i = 0; i < 3; i++) {
			assertSame(_rootObject, arguments.get(i).getPropertyContainer());
			assertEquals(expectedPaths[i], arguments.get(i).getPropertyPath());
		}
	}

	@Test
	public void testPathWithIndexStar() throws Exception {
		String pattern = "a[*].c,b";

		FunctionsTestPropertyContainer subContainer1 = new FunctionsTestPropertyContainer();
		subContainer1.addProperty("c", 1);
		FunctionsTestPropertyContainer subContainer2 = new FunctionsTestPropertyContainer();
		subContainer2.addProperty("c", 2);
		_rootObject.addProperty("a", new FunctionsTestPropertyContainer[] {
				subContainer1, subContainer2 });
		_rootObject.addProperty("b", 2.0);

		HashMap<String, Integer> paths = new HashMap<String, Integer>();
		for (FunctionArgument argument : _resolver.resolveArguments(pattern)) {
			if (argument.getPropertyPath().equals("c")) {
				assertTrue(argument.getPropertyContainer() == subContainer1
						|| argument.getPropertyContainer() == subContainer2);
			} else if (argument.getPropertyPath().equals("b")) {
				assertSame(argument.getPropertyContainer(), _rootObject);
			}
			if (!paths.containsKey(argument.getPropertyPath())) {
				paths.put(argument.getPropertyPath(), new Integer(1));
			} else {
				int oldCount = paths.get(argument.getPropertyPath()).intValue();
				paths
						.put(argument.getPropertyPath(), new Integer(
								oldCount + 1));
			}
		}

		assertEquals(2, paths.size());
		assertEquals(2, paths.get("c").intValue());
		assertEquals(1, paths.get("b").intValue());
	}

	@Test
	public void testPathWithNestedIndexStar() throws Exception {
		String pattern = "a[*][*].b";
		FunctionsTestPropertyContainer subContainer11 = new FunctionsTestPropertyContainer();
		subContainer11.addProperty("b", 11);
		FunctionsTestPropertyContainer subContainer12 = new FunctionsTestPropertyContainer();
		subContainer12.addProperty("b", 12);
		FunctionsTestPropertyContainer subContainer21 = new FunctionsTestPropertyContainer();
		subContainer21.addProperty("b", 21);
		FunctionsTestPropertyContainer subContainer22 = new FunctionsTestPropertyContainer();
		subContainer22.addProperty("b", 22);
		_rootObject.addProperty("a", new IPropertyContainer[][] {
				new IPropertyContainer[] { subContainer11, subContainer12 },
				new IPropertyContainer[] { subContainer21, subContainer22 } });

		HashSet<IPropertyContainer> resolvedContainers = new HashSet<IPropertyContainer>();
		for (FunctionArgument argument : _resolver.resolveArguments(pattern)) {
			assertEquals("b", argument.getPropertyPath());
			resolvedContainers.add(argument.getPropertyContainer());
		}

		assertEquals(4, resolvedContainers.size());
		assertTrue("SubContainer11 should be resolved", resolvedContainers
				.contains(subContainer11));
		assertTrue("SubContainer12 should be resolved", resolvedContainers
				.contains(subContainer12));
		assertTrue("SubContainer21 should be resolved", resolvedContainers
				.contains(subContainer21));
		assertTrue("SubContainer22 should be resolved", resolvedContainers
				.contains(subContainer22));
	}

	@Test
	public void testPathWidtNotExistingProperties() throws Exception {
		String pattern = "a,b,c";
		assertEquals(0, _resolver.resolveArguments(pattern).size());
	}

	@Test
	public void testInvalidPattern() {
		String[] invalidPatterns = new String[] { "a[", "a[b]", "b.", "c[0]" };

		for (String pattern : invalidPatterns) {
			try {
				_resolver.resolveArguments(pattern);
				fail("Exception InvalidArgumentsPattern was expected for pattern "
						+ pattern);
			} catch (InvalidArgumentsPatternException ex) {
			}
		}
	}

	/*
	 * This test in turned off as it is dependent on core component.
	 */
	/*
	@Test
	public void testAgentsExtensionForAggregate() throws Exception {
		SimpleAggregate aggregate = new SimpleAggregate(new AgentAddress(
				"aggregateId"));

		IAgentEnvironment agentEnvironment = Mockito
				.mock(IAgentEnvironment.class);
		aggregate.setAgentEnvironment(agentEnvironment);
		Mockito.when(
				agentEnvironment.registerAgent(
						Mockito.any(IAgentAddress.class), Mockito.anyList()))
				.thenReturn(true);

		TestAgent agent1 = new TestAgent("agent1");
		TestAgent agent2 = new TestAgent("agent2");
		// aggregate.addAgent(agent1);
		// aggregate.addAgent(agent2);
		aggregate.add(agent1);
		aggregate.add(agent2);
		DefaultFunctionArgumentsResolver resolver = new DefaultFunctionArgumentsResolver(
				aggregate);

		HashMap<IPropertyContainer, String> resolvedArguments = new HashMap<IPropertyContainer, String>();
		for (FunctionArgument argument : resolver
				.resolveArguments("@Agents[*].intProperty")) {
			assertFalse(
					"resolveFunctionArguments should not return the same agent twice",
					resolvedArguments.containsKey(argument
							.getPropertyContainer()));
			resolvedArguments.put(argument.getPropertyContainer(), argument
					.getPropertyPath());
		}

		verify(agentEnvironment, times(2)).registerAgent(
				(IAgentAddress) anyObject(), (List<IAddress>) anyList());
		verifyNoMoreInteractions(agentEnvironment);
		assertEquals(2, resolvedArguments.size());
		assertEquals("intProperty", resolvedArguments.get(agent1));
		assertEquals("intProperty", resolvedArguments.get(agent2));

	} */

	/*
	private class TestAgent extends SimpleAgent {

		private final static long serialVersionUID = 1;

		@SuppressWarnings("unused")
		@PropertyField(propertyName = "intProperty")
		private int _intProperty = 3;

		public TestAgent(String id) {
			super(new AgentAddress(id));
		}

		@Override
		public void deliverMessage(Message message) {
		}

		@Override
		public void finish() {
		}

		@Override
		public void init() {
		}

		@Override
		public void step() {
		}

		public IAgent getClone(IAgentAddress newAddress) {
			throw new IllegalStateException("NYI");
		}

	}
	*/
}
