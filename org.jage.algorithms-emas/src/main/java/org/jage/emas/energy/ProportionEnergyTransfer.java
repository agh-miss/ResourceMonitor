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
 * File: ProportionEnergyTransfer.java
 * Created: 2012-01-30
 * Author: Krzywicki
 * $Id: ProportionEnergyTransfer.java 211 2012-04-08 20:06:03Z krzywick $
 */

package org.jage.emas.energy;

import org.jage.emas.agent.IndividualAgent;

/**
 * Proportion energy transfer strategy. Always transfers a fixed proportion of the source energy to the target.
 *
 * @author AGH AgE Team
 */
public class ProportionEnergyTransfer implements EnergyTransfer<IndividualAgent> {

	private double transferredProportion;

	public void setTransferredProportion(final double transferredProportion) {
		this.transferredProportion = transferredProportion;
	}

	@Override
	public final double transferEnergy(final IndividualAgent source, final IndividualAgent target) {
		final double delta = source.getEnergy() * transferredProportion;

		source.changeEnergyBy(-delta);
		target.changeEnergyBy(delta);

		return delta;
	}
}
