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
 * File: RebuildablePriorityQueue.java
 * Created: 2012-03-29
 * Author: faber
 * $Id: RebuildablePriorityQueue.java 166 2012-03-30 08:26:53Z faber $
 */

package org.jage.util;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.SortedSet;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * A wrapper for {@link PriorityBlockingQueue} that allows changing comparators in runtime.
 * 
 * @param <E>
 *            the type of elements held in this collection.
 * 
 * @since 2.6
 * @author AGH AgE Team
 */
public class RebuildablePriorityQueue<E> implements Queue<E> {

	private Queue<E> queue;

	/**
	 * Creates a new {@code RebuildablePriorityQueue} with a given comparator and default initial capacity.
	 * 
	 * @param comparator
	 *            the comparator to use.
	 * @return a new queue.
	 */
	public static <T> RebuildablePriorityQueue<T> createWithComparator(Comparator<? super T> comparator) {
		return new RebuildablePriorityQueue<T>(11, comparator);
	}

	/**
	 * Creates a <tt>RebuildablePriorityQueue</tt> with the default initial capacity (11) that orders its elements
	 * according to their {@linkplain Comparable natural ordering}.
	 */
	public RebuildablePriorityQueue() {
		queue = new PriorityBlockingQueue<E>();
	}

	/**
	 * Creates a <tt>RebuildablePriorityQueue</tt> with the specified initial capacity that orders its elements
	 * according to their {@linkplain Comparable natural ordering}.
	 * 
	 * @param initialCapacity
	 *            the initial capacity for this priority queue
	 * @throws IllegalArgumentException
	 *             if <tt>initialCapacity</tt> is less than 1
	 */
	public RebuildablePriorityQueue(int initialCapacity) {
		queue = new PriorityBlockingQueue<E>(initialCapacity, null);
	}

	/**
	 * Creates a <tt>RebuildablePriorityQueue</tt> with the specified initial capacity that orders its elements
	 * according to the specified comparator.
	 * 
	 * @param initialCapacity
	 *            the initial capacity for this priority queue
	 * @param comparator
	 *            the comparator that will be used to order this priority queue. If {@code null}, the
	 *            {@linkplain Comparable natural ordering} of the elements will be used.
	 * @throws IllegalArgumentException
	 *             if <tt>initialCapacity</tt> is less than 1
	 */
	public RebuildablePriorityQueue(int initialCapacity, Comparator<? super E> comparator) {
		queue = new PriorityBlockingQueue<E>(initialCapacity, comparator);
	}

	/**
	 * Creates a <tt>RebuildablePriorityQueue</tt> containing the elements in the specified collection. If the specified
	 * collection is a {@link SortedSet} or a {@link PriorityQueue}, this priority queue will be ordered according to
	 * the same ordering. Otherwise, this priority queue will be ordered according to the {@linkplain Comparable natural
	 * ordering} of its elements.
	 * 
	 * @param c
	 *            the collection whose elements are to be placed into this priority queue
	 * @throws ClassCastException
	 *             if elements of the specified collection cannot be compared to one another according to the priority
	 *             queue's ordering
	 * @throws NullPointerException
	 *             if the specified collection or any of its elements are null
	 */
	public RebuildablePriorityQueue(Collection<? extends E> c) {
		queue = new PriorityBlockingQueue<E>(c);
	}

	@Override
	public boolean add(E e) {
		return queue.add(e);
	}

	@Override
	public int size() {
		return queue.size();
	}

	@Override
	public boolean isEmpty() {
		return queue.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return queue.contains(o);
	}

	@Override
	public boolean offer(E e) {
		return queue.offer(e);
	}

	@Override
	public Iterator<E> iterator() {
		return queue.iterator();
	}

	@Override
	public Object[] toArray() {
		return queue.toArray();
	}

	@Override
	public E remove() {
		return queue.remove();
	}

	@Override
	public E poll() {
		return queue.poll();
	}

	@Override
	public E element() {
		return queue.element();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return queue.toArray(a);
	}

	@Override
	public E peek() {
		return queue.peek();
	}

	@Override
	public boolean remove(Object o) {
		return queue.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return queue.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		return queue.addAll(c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return queue.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return queue.retainAll(c);
	}

	@Override
	public void clear() {
		queue.clear();
	}

	@Override
	public boolean equals(Object o) {
		return queue.equals(o);
	}

	@Override
	public int hashCode() {
		return queue.hashCode();
	}

	/**
	 * Sets a new comparator for this queue.
	 * <p>
	 * 
	 * Note: this operation requires a rebuild of the whole queue. It may be costly.
	 * 
	 * @param comparator
	 *            the new comparator to use.
	 */
	public void setComparator(final Comparator<E> comparator) {
		final Queue<E> newQueue = new PriorityBlockingQueue<E>(queue.size(), comparator);
		newQueue.addAll(queue);
		queue = newQueue;
	}

}
