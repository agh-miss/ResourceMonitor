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
 * File: ChainingContext.java
 * Created: 2012-03-16
 * Author: Krzywicki
 * $Id: ChainingContext.java 199 2012-04-07 09:14:03Z krzywick $
 */

package org.jage.emas.util;

import java.util.List;

import org.jage.action.IActionContext;

import com.google.common.base.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Utility context which allow to chain ChainingAction subclasses. Contains a reference to the next action context in
 * the chain.
 *
 * @author AGH AgE Team
 */
public class ChainingContext implements IActionContext {

	private ChainingContext next;

	/**
	 * Returns an optional next context.
	 *
	 * @return an optional next context
	 */
	public final Optional<ChainingContext> getNextContext() {
		return Optional.fromNullable(next);
	}

	/**
	 * Creates a new chaining context builder.
	 *
	 * @return an empty builder
	 */
	public static ChainingContextBuilder builder() {
		return new ChainingContextBuilder();
	}

	/**
	 * Helper builder class for {@link ChainingContext} chains.
	 *
	 * @author AGH AgE Team
	 */
	public static final class ChainingContextBuilder {

		private ChainingContext first;

		private ChainingContext last;

		/**
		 * Appends the given context to the chain. If the chain is empty, this context will be made the head of the
		 * chain. This context next one will be set to null.
		 *
		 * @param next
		 *            the next context
		 * @return this builder
		 */
		public ChainingContextBuilder append(final ChainingContext next) {
			checkNotNull(next);
			if (first == null) {
				first = next;
				last = next;
			} else {
				last.next = next;
				last = next;
			}
			last.next = null;
			return this;
		}

		/**
		 * Appends all the given contexts to this chain.
		 *
		 * @param nexts
		 *            a list of contexts
		 * @return this builder
		 */
		public ChainingContextBuilder appendAll(final List<ChainingContext> nexts) {
			for (ChainingContext next : nexts) {
				append(next);
			}
			return this;
		}

		/**
		 * Appends the given context to this chain if the provided condition is true.
		 *
		 * @param condition
		 *            some boolean condition
		 * @param next
		 *            the next context
		 * @return this builder
		 *
		 * @see ChainingContextBuilder#append(ChainingContext)
		 */
		public ChainingContextBuilder appendIf(final boolean condition, final ChainingContext next) {
			if (condition) {
				append(next);
			}
			return this;
		}

		/**
		 * Appends all the given contexts to this chain, if the condition is true.
		 *
		 * @param condition
		 *            some boolean condition
		 * @param nexts
		 *            a list of contexts
		 * @return this builder
		 * @see ChainingContextBuilder#appendAll(List)
		 */
		public ChainingContextBuilder appendAllIf(final boolean condition, final List<ChainingContext> nexts) {
			if (condition) {
				appendAll(nexts);
			}
			return this;
		}

		/**
		 * Check whether this chain is empty, i.e. whether it has no head element.
		 *
		 * @return true if the chain is empty
		 */
		public boolean isEmpty() {
			return first == null;
		}

		/**
		 * Returns the head context, if the chain is not empty.
		 *
		 * @return the chain head context
		 * @throws NullPointerException
		 *             if the chain is empty
		 */
		public ChainingContext build() {
			return checkNotNull(first);
		}
	}
}
