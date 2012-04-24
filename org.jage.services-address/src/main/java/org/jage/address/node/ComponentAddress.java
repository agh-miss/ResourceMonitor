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
 * File: ComponentAddress.java
 * Created: 2010-04-09
 * Author: kaplita, rzasam
 * $Id: ComponentAddress.java 139 2012-03-19 22:54:18Z faber $
 */

package org.jage.address.node;

import com.google.common.base.Objects;

import org.jage.address.AbstractAddress;
import static com.google.common.base.Preconditions.*;

/**
 * This class provides a default implementation of {@link IComponentAddress}.
 * 
 * @author AGH AgE Team
 */
public class ComponentAddress extends AbstractAddress implements IComponentAddress {

	private static final long serialVersionUID = 4L;

	private final String componentName;

	/**
	 * Constructs a new component address with given parameters.
	 * 
	 * @param componentName
	 *            A name that identifies this component in the node, cannot be null.
	 * @param nodeAddress
	 *            A current node address, cannot be null.
	 * 
	 * @throws IllegalArgumentException
	 *             if either <code>componentName</code> or <code>nodeAddress</code> is <code>null</code>.
	 */
	public ComponentAddress(String componentName, INodeAddress nodeAddress) {
		super(nodeAddress, checkNotNull(componentName));
		this.componentName = componentName;
	}

	/**
	 * Constructs a new component address with a given name. The constructed address represents <b>any</b> component
	 * with this name in the distributed environment.
	 * 
	 * @param componentName
	 *            A name that identifies this component in the node, cannot be null.
	 * 
	 * @throws NullPointerException
	 *             if <code>componentName</code> is <code>null</code>.
	 */
	public ComponentAddress(String componentName) {
		this(componentName, new UnspecifiedNodeAddress());
	}

	@Override
	public String getComponentName() {
		return componentName;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(componentName, getNodeAddress());
	}

	/**
	 * Tests whether this <code>ComponentAddress</code> is equal to another.
	 * <p>
	 * {@inheritDoc}
	 * <p>
	 * This implementation is based on equality of <code>componentName</code> and the attached node address.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}

		final ComponentAddress other = (ComponentAddress)obj;
		return Objects.equal(componentName, other.componentName)
		        && Objects.equal(getNodeAddress(), other.getNodeAddress());
	}

}
