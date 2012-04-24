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
 * File: Action.java
 * Created: 2008-10-07
 * Author: awos
 * $Id: Action.java 177 2012-03-31 18:32:12Z faber $
 */

package org.jage.action;

import java.util.Deque;
import java.util.Iterator;

import static com.google.common.collect.Lists.newLinkedList;

/**
 * An abstract class which represents an action. It allows to create tree structure of actions and contains the iterator
 * which returns tree leaves.
 *
 * @author AGH AgE Team
 */
public abstract class Action implements Iterable<SingleAction> {

	@Override
	public Iterator<SingleAction> iterator() {
		return new ActionIterator(this);
	}

	private class ActionIterator implements Iterator<SingleAction> {

		private final Deque<SingleAction> stack = newLinkedList();

		public ActionIterator(final Action a) {
			prepareIterator(a);
		}

		@Override
		public boolean hasNext() {
			return !stack.isEmpty();
		}

		@Override
		public SingleAction next() {
			if (!hasNext()) {
				return null;
			}
			return stack.removeFirst();
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("Can't remove subaction");
		}

		private void prepareIterator(final Action a) {
			if (a instanceof SingleAction) {
				stack.addLast((SingleAction)a);
			} else {
				final ComplexAction ca = (ComplexAction)a;

				for (Action child : ca.getChildren()) {
					prepareIterator(child);
				}
			}
		}
	}
}
