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
 * File: BaseTest.java
 * Created: 2011-05-06
 * Author: Krzywicki
 * $Id: BaseTest.java 4 2011-12-22 15:10:04Z faber $
 */

package org.jage.utils;

import org.junit.Before;
import org.mockito.MockitoAnnotations;

/**
 * A base class for tests.
 * 
 * @author AGH AgE Team
 */
public abstract class BaseTest {

	/**
	 * Initializes mockito mocks.
	 */
	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}
}
