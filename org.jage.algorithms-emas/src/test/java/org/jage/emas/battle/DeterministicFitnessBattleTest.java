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
 * File: DeterministicFitnessBattleTest.java
 * Created: 2012-03-18
 * Author: Krzywicki
 * $Id: DeterministicFitnessBattleTest.java 211 2012-04-08 20:06:03Z krzywick $
 */

package org.jage.emas.battle;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

import org.jage.emas.agent.IndividualAgent;

/**
 * Tests for DeterministicFitnessBattle.
 *
 * @author AGH AgE Team
 */
@RunWith(MockitoJUnitRunner.class)
public class DeterministicFitnessBattleTest {

	@Mock
	IndividualAgent first;

	@Mock
	IndividualAgent second;

	private final Battle<IndividualAgent> battle = new DeterministicFitnessBattle();

	@Test
	public void shouldChooseFirst() {
		// given
		given(first.getEffectiveFitness()).willReturn(10.0);
		given(second.getEffectiveFitness()).willReturn(5.0);

		// when
		IndividualAgent winner = battle.fight(first, second);

		// then
		assertThat(winner, is(first));
	}

	@Test
	public void shouldChooseSecond() {
		// given
		given(first.getEffectiveFitness()).willReturn(5.0);
		given(second.getEffectiveFitness()).willReturn(10.0);

		// when
		IndividualAgent winner = battle.fight(first, second);

		// then
		assertThat(winner, is(second));
	}

	@Test
	public void shouldChooseFirstOnDraw() {
		// given
		given(first.getEffectiveFitness()).willReturn(5.0);
		given(second.getEffectiveFitness()).willReturn(5.0);

		// when
		IndividualAgent winner = battle.fight(first, second);

		// then
		assertThat(winner, is(first));
	}
}
