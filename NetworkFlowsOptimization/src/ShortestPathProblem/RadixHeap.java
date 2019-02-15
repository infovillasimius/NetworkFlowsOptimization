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
package ShortestPathProblem;

/**
 * Radix-Heap for Dijkstra Algorithm
 *
 * @author anto
 * @param <E>
 */
public class RadixHeap<E> {

    private final int b;
    private int base;
    private final int[] min;
    private final int[] w;
    private final int[] width;
    private final int[] range;
    private final Object[] h;
    private int pointer;

    public RadixHeap(int n) {
        this.base = 0;
        if (n<=0) n=5;
        this.b = (int) Math.ceil(Math.log10(n) / Math.log10(2));
        this.w = new int[b];
        this.min = new int[b];
        this.width = new int[b];
        this.range = new int[b];
        this.h = new Object[b];
        this.pointer = 0;
        this.w[0] = 1;
        this.w[1] = 1;
        this.width[0] = 1;
        this.width[1] = 1;
        this.range[0] = 0;
        this.range[1] = 1;
        for (int i = 2; i < this.b; i++) {
            this.w[i] = this.w[i - 1] * 2;
            this.width[i] = this.w[i];
            this.range[i] = this.w[i];
        }
    }

    public void store(E n, int d) {

        int bucket = index(d);
        if (h[bucket] == null) {
            h[bucket] = new Element<>(n, d);
            min[bucket] = d;
        } else {
            @SuppressWarnings("unchecked")
            Element<E> a = (Element<E>) h[bucket];
            Element<E> n1 = new Element<>(n, d);
            n1.next = a;
            h[bucket] = n1;
            if (min[bucket] > d) {
                min[bucket] = d;
            }
        }
    }

    public E next() {
        while (h[pointer] == null) {
            pointer = (pointer + 1) % b;
        }
        @SuppressWarnings("unchecked")
        Element<E> r = (Element<E>) h[pointer];
        if (r.next == null) {
            h[pointer] = null;
        } else if (w[pointer] < 2) {
            @SuppressWarnings("unchecked")
            Element<E> next = (Element<E>) h[pointer];
            next = next.next;
            h[pointer] = next;
        } else {
            redist();
        }
        return r.value;

    }

    private int index(int d) {
        int distance = d - this.base;
        if (distance == 0) {
            return 0;
        }
        int bucket = (int) Math.ceil(Math.log10(distance) / Math.log10(2));
        if ((d >= range[bucket]) && (d < range[bucket] + w[bucket]) && w[bucket] > 0) {
            return bucket;
        }

        for (int i = bucket; i < b; i++) {
            if ((d >= range[i]) && (d < range[i] + w[i]) && w[i] > 0) {
                return i;
            }
        }
        return 0;
    }

    /**
     * Redistribute nodes
     */
    private void redist() {
        @SuppressWarnings("unchecked")
        Element<E> chain = (Element<E>) h[pointer];
        this.range[0] = min[pointer];
        base = range[0];
        this.range[1] = this.range[0] + 1;
        for (int i = 2; i < pointer; i++) {
            w[i] = width[i];
            this.range[i] = this.range[i - 1] + this.w[i - 1];
        }
        this.range[pointer] += w[pointer];
        w[pointer] = 0;
        w[pointer - 1] = range[pointer] - range[pointer - 1];
        w[0]=1;
        w[1]=1;
        while (chain.next != null) {
            @SuppressWarnings("unchecked")
            Element<E> next = (Element<E>) chain.next;
            chain.next = null;
            store(chain.value, chain.d);
            chain = next;
        }
        store(chain.value, chain.d);
        h[pointer] = null;
        pointer = 0;
    }

    /**
     * Update Element from distance oldD to distance d
     *
     * @param n
     * @param d
     * @param oldD
     */
    public void update(E n, int d, int oldD) {
        int oldBucket = index(oldD);
        int newBucket = index(d);
        if (oldBucket != newBucket) {
            @SuppressWarnings("unchecked")
            Element<E> prev = (Element<E>) h[oldBucket];
            @SuppressWarnings("unchecked")
            Element<E> chain = (Element<E>) h[oldBucket];
            if (chain != null && chain.value.equals(n) && chain.next == null) {
                store(n, d);
                h[oldBucket] = null;
            } else if (chain != null) {
                while (!chain.value.equals(n) && chain.next != null) {
                    prev = chain;
                    chain = chain.next;
                }
                if (chain.value.equals(n)) {
                    store(n, d);
                    prev.next = chain.next;
                }
            }
        }
    }

    private class Element<T> {

        T value;
        Element<T> next;
        int d;

        public Element(T value, int d) {
            this.next = null;
            this.value = value;
            this.d = d;
        }
    }

}
