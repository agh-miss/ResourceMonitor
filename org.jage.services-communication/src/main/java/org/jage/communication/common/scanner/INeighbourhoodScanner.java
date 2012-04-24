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
 * File: INeighbourhoodScanner.java
 * Created: 2012-02-07
 * Author: faber
 * $Id: INeighbourhoodScanner.java 58 2012-02-10 14:25:54Z faber $
 */

package org.jage.communication.common.scanner;

import org.jage.platform.component.IStatefulComponent;

/**
 * A neighbourhood scanner of the node.
 * <p>
 * A task of the scanner is to obtain addresses necessary for the local node to communicate with its neighbours. A
 * necessary address may be a AgE address, a physical one (like an IP address) or both.
 * <p>
 * Scanner is expected to fill an IAddressCache with all addresses it finds. It should keep the cache constantly
 * up-to-date.
 * <p>
 * Some scanners will require an IAddressMap (a map) and others will use an IAddressSet (a set).
 * 
 * @author AGH AgE Team
 */
public interface INeighbourhoodScanner extends IStatefulComponent, Runnable {

	/**
	 * Initializes the neighbourhood scanner.
	 */
	@Override
	public void init();

	/**
	 * Will stop the neighbourhood scanner, possibly with a short delay.
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	public boolean finish();

	/**
	 * The working loop of this neighbourhood scanner.
	 */
	@Override
	public void run();

}
