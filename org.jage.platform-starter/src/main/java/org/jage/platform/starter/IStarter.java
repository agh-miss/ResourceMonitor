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
 * File: IStarter.java
 * Created: 2010-06-08
 * Author: kpietak
 * $Id: IStarter.java 134 2012-03-18 20:17:01Z faber $
 */

package org.jage.platform.starter;


/**
 * <p>
 * Starter is a node component which is responsible for managing node lifecycle. Depending on the implementation, the
 * starter can create and register all required node components (such as configuration loader, core component), load a
 * configuration, start a computation, close the node.
 *
 * <p>
 * The Starter is run just after loading command line arguments, so than there is no other node component registered
 * (apart from command line arguments service).
 *
 * @since 2.5.0
 *
 * @author AGH AgE Team
 */
public interface IStarter {

	/**
	 * Starts the starter processing.
	 */
	void start();
	
	/**
	 * Forces the shutdown of the stater.<p>
	 * 
	 * This method should be used in the case when the platform need to be shut down due to an error. If the computation
	 * finishes correctly, the starter implementation should notice it and perform the shutdown automatically. 
	 */
	void shutdown();
}
