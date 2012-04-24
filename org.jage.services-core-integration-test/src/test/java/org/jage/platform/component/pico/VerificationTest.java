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
 * File: VerificationTest.java
 * Created: 30-03-2012
 * Author: Krzywicki
 * $Id: VerificationTest.java 172 2012-03-30 16:08:11Z krzywick $
 */

package org.jage.platform.component.pico;

import org.junit.Test;
import org.picocontainer.PicoVerificationException;
import org.picocontainer.visitors.VerifyingVisitor;

import org.jage.platform.component.annotation.Inject;
import org.jage.platform.component.pico.PicoComponentInstanceProvider;

/**
 * Some verification tests.
 *
 * @author AGH AgE Team
 */
public class VerificationTest {

	@Test(expected = PicoVerificationException.class)
	public void shouldDetectCircularMemberDependency() {
		// given
		PicoComponentInstanceProvider container = new PicoComponentInstanceProvider();
		container.addComponent(MemberA.class);
		container.addComponent(MemberB.class);
		container.addComponent(MemberC.class);

		// when
		new VerifyingVisitor().traverse(container);
	}

	public static class MemberA {
        @Inject
		public MemberB memberB;
	}

	public static class MemberB {
		@Inject
		public MemberC memberC;
	}

	public static class MemberC {
		@Inject
		public MemberA memberA;
	}

	@Test(expected = PicoVerificationException.class)
	public void shouldDetectCircularConstructorDependency() {
		// given
		PicoComponentInstanceProvider container = new PicoComponentInstanceProvider();
		container.addComponent(ConstructorA.class);
		container.addComponent(ConstructorB.class);
		container.addComponent(ConstructorC.class);

		// when
		new VerifyingVisitor().traverse(container);
	}

	public static class ConstructorA {
		@Inject
        public ConstructorA(final ConstructorB constructorB) {
        }
	}

	public static class ConstructorB {
		@Inject
        public ConstructorB(final ConstructorC constructorC) {
        }
	}

	public static class ConstructorC {
		@Inject
		public ConstructorC(final ConstructorA constructorA) {
        }
	}



}
