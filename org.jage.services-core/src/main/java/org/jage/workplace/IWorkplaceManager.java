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
 * File: IWorkplaceManager.java
 * Created: 2008-10-07
 * Author: awos
 * $Id: IWorkplaceManager.java 134 2012-03-18 20:17:01Z faber $
 */

package org.jage.workplace;

import java.util.List;

import org.jage.address.IAgentAddress;
import org.jage.services.core.ICoreComponent;

/**
 * Manager of workplaces. The manager is created from configuration. Initial configuration may configure workplaces.
 * Than management by add/removeWorkplace may be done.
 * 
 * @author AGH AgE Team
 */
public interface IWorkplaceManager extends ICoreComponent {

	/**
	 * Provides workplace of given address.
	 * 
	 * @param address
	 *            Address of the workplace
	 * @return A workplace or null if not found
	 */
	public IWorkplace getWorkplace(IAgentAddress address);

	/**
	 * Provides all workplaces in this manager.
	 * 
	 * @return Array of workplaces
	 */
	public List<IWorkplace> getWorkplaces();

}
