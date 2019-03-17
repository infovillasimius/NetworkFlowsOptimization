/*
 * Copyright (C) 2019 anto
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package NetworkFlowOptimization;

/**
 * Circular Queue for Dijkstra Algorithm
 *
 * @author anto
 * @param <E> The type of the element to store
 */
public class CircularQueue<E> {

    private final Object[] t;
    private final int C;
    private int pointer;
    private int size;

    /**
     * Get a circular queue of size n
     *
     * @param n The size of the queue
     */
    public CircularQueue(int n) {
        this.size = 0;
        this.t = new Object[n];
        this.C = n;
        this.pointer = 0;
    }

    /**
     * Store a new element
     *
     * @param n The element to store
     * @param d The key of the element (distance)
     */
    public void store(E n, int d) {
        if (t[d % (C)] == null) {
            t[d % (C)] = new Element<>(n);
        } else {
            @SuppressWarnings("unchecked")
            Element<E> a = (Element<E>) t[d % (C)];

            @SuppressWarnings("unchecked")
            Element<E> b = new Element<>(n);
            b.next = a;
            t[d % (C)] = b;
        }
        size++;
    }

    /**
     * Get the next element in the queue
     *
     * @return E (the next element in the queue)
     */
    public E next() {
        if (size < 1) {
            return null;
        }
        while (t[pointer] == null) {
            pointer = (pointer + 1) % C;
        }
        @SuppressWarnings("unchecked")
        Element<E> r = (Element<E>) t[pointer];
        if (r.next == null) {
            t[pointer] = null;
        } else {
            @SuppressWarnings("unchecked")
            Element<E> next = (Element<E>) t[pointer];
            next = next.next;
            t[pointer] = next;
        }
        size--;
        return r.value;
    }

    private class Element<E> {

        E value;
        Element<E> next;

        public Element(E value) {
            this.next = null;
            this.value = value;
        }

    }

}
