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
 * File: GaussianGenerator.java
 * Created: 2011-11-03
 * Author: Slawek
 * $Id: GaussianGenerator.java 18 2012-01-29 17:29:44Z krzywick $
 */

package org.jage.random;

import org.jage.platform.component.annotation.Inject;
import org.jage.property.ClassPropertyContainer;

/**
 * An implementation of {@link IDoubleSymmetricGenerator} which generates values on the base of a Gaussian distribution.
 *
 * @author AGH AgE Team
 */
public final class GaussianGenerator extends ClassPropertyContainer implements IDoubleSymmetricGenerator {

	private static final double DEFAULT_LOCATION = 0.0;

	private static final double DEFAULT_SCALE = 1.0;

	@Inject
	private INormalizedDoubleRandomGenerator rand;

	@Override
	public double nextDouble() {
		return nextDouble(DEFAULT_LOCATION, DEFAULT_SCALE);
	}

	@Override
	public double nextDouble(double location, double scale) {
		double seed = rand.nextDouble();
		return location + scale * NormalCdf.getValue(seed);
	}

	@Override
	public double getLowerDouble() {
		return Double.MIN_VALUE;
	}

	@Override
	public double getUpperDouble() {
		return Double.MAX_VALUE;
	}

	/**
	 * Helper class, encapsulate the inverse of the cdf of a normal distribution.
	 *
	 * @author AGH AgE Team
	 */
	private static class NormalCdf {
		private static final double RAC2 = 1.41421356237309504880;

		private static final double XBIG = 40.0;

		private static final double[] INV_NORMAL_2_P_1 = { 0.160304955844066229311E2, -0.90784959262960326650E2,
	        0.18644914861620987391E3, -0.16900142734642382420E3, 0.6545466284794487048E2, -0.864213011587247794E1,
	        0.1760587821390590 };

		private static final double[] INV_NORMAL_2_Q_1 = { 0.147806470715138316110E2, -0.91374167024260313396E2,
	        0.21015790486205317714E3, -0.22210254121855132366E3, 0.10760453916055123830E3, -0.206010730328265443E2,
	        0.1E1 };

		private static final double[] INV_NORMAL_2_P_2 = { -0.152389263440726128E-1, 0.3444556924136125216,
	        -0.29344398672542478687E1, 0.11763505705217827302E2, -0.22655292823101104193E2,
	        0.19121334396580330163E2, -0.5478927619598318769E1, 0.237516689024448000 };

		private static final double[] INV_NORMAL_2_Q_2 = { -0.108465169602059954E-1, 0.2610628885843078511,
	        -0.24068318104393757995E1, 0.10695129973387014469E2, -0.23716715521596581025E2,
	        0.24640158943917284883E2, -0.10014376349783070835E2, 0.1E1 };

		private static final double[] INV_NORMAL_2_P_3 = { 0.56451977709864482298E-4, 0.53504147487893013765E-2,
	        0.12969550099727352403, 0.10426158549298266122E1, 0.28302677901754489974E1, 0.26255672879448072726E1,
	        0.20789742630174917228E1, 0.72718806231556811306, 0.66816807711804989575E-1,
	        -0.17791004575111759979E-1, 0.22419563223346345828E-2 };

		private static final double[] INV_NORMAL_2_Q_3 = { 0.56451699862760651514E-4, 0.53505587067930653953E-2,
	        0.12986615416911646934, 0.10542932232626491195E1, 0.30379331173522206237E1, 0.37631168536405028901E1,
	        0.38782858277042011263E1, 0.20372431817412177929E1, 0.1E1 };

		/**
		 * Returns the inverse of the cdf of the normal distribution. Rational approximations giving 16 decimals of
		 * precision. The authors also give an approximation with 23 decimals of precision. J.M. Blair, C.A. Edwards,
		 * J.H. Johnson, "Rational Chebyshev approximations for the Inverse of the Error Function", in Mathematics of
		 * Computation, Vol. 30, 136, pp 827, (1976)
		 *
		 * @param u
		 *            The argument
		 */
		private static double getValue(double u) {
			if (u < 0.0 || u > 1.0) {
				throw new IllegalArgumentException("u is not in [0, 1]");
			}
			if (u == 0.0) {
				return Double.NEGATIVE_INFINITY;
			}
			if (u == 1.0) {
				return Double.POSITIVE_INFINITY;
			}

			// Transform u as argument of InvErf
			double z = u;
			u = 2.0 * u - 1.0;
			if (u >= 1.0) {
				return XBIG;
			}

			boolean negatif;
			if (u < 0.0) {
				u = -u;
				negatif = true;
			} else {
				negatif = false;
			}

			if (u <= 0.75) {
				double v = u * u - 0.5625;
				double numer = 0.0;
				double denom = 0.0;

				// Evaluation of the 2 polynomials by Horner
				for (int i = 6; i >= 0; i--) {
					numer = numer * v + INV_NORMAL_2_P_1[i];
					denom = denom * v + INV_NORMAL_2_Q_1[i];
				}

				z = u * numer / denom;
			} else if (u <= 0.9375) {
				double v = u * u - 0.87890625;
				double numer = 0.0;
				double denom = 0.0;

				for (int i = 7; i >= 0; i--) {
					numer = numer * v + INV_NORMAL_2_P_2[i];
					denom = denom * v + INV_NORMAL_2_Q_2[i];
				}

				z = u * numer / denom;
			} else {
				double v;
				if (z > 0.1) {
					v = 1.0 / Math.sqrt(-Math.log(1.0 - u));
				} else {
					v = 1.0 / Math.sqrt(-Math.log(2.0 * z));
				}
				double numer = 0.0;
				double denom = 0.0;

				for (int i = 10; i >= 0; i--) {
					numer = numer * v + INV_NORMAL_2_P_3[i];
				}

				for (int i = 8; i >= 0; i--) {
					denom = denom * v + INV_NORMAL_2_Q_3[i];
				}

				z = (1.0 / v) * numer / denom;
			}

			return negatif? -z * RAC2: z * RAC2;
		}
	}
}
