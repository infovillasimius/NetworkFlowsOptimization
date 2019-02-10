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
package nfo;

/**
 *
 * @author anto
 * @param <E>
 */
public class RadixHeap<E> {

    private final int b;
    private int base;
    private int[] w;
    private int[] range;
    private final Object[] h;
    private int pointer;

    public RadixHeap(int C, int n) {
        this.base = 0;
        this.b = (int) Math.ceil(Math.log10(n * C) / Math.log10(2));
        this.w = new int[b];
        this.range = new int[b];
        this.h = new Object[b];
        this.pointer = 0;
        this.w[0] = 1;
        this.w[1] = 1;
        this.range[0] = 0;
        this.range[1] = 1;
        for (int i = 2; i < this.b; i++) {
            this.w[i] = this.w[i - 1] * 2;
            this.range[i] = this.w[i];
        }
    }

    public void store(E n, int d) {
        int bucket = index(d);
        if (h[bucket] == null) {
            h[bucket] = new Nodo<>(n, d);
        } else {
            @SuppressWarnings("unchecked")
            Nodo<E> a = (Nodo<E>) h[bucket];
            Nodo<E> n1 = new Nodo<>(n, d);
            n1.next = a;
            h[bucket] = n1;
        }
    }

    public E next() {

        while (h[pointer] == null) {
            pointer = (pointer + 1) % b;
        }
        @SuppressWarnings("unchecked")
        Nodo<E> r = (Nodo<E>) h[pointer];
        if (r.next == null) {
            h[pointer] = null;
        } else if (w[pointer] == 1) {
            @SuppressWarnings("unchecked")
            Nodo<E> next =(Nodo<E>)h[pointer];
            next = next.next;
            h[pointer]=next;
        } else {
            redist();
        }
        //pointer=0;
        return r.value;
    }

    class Nodo<T> {

        T value;
        Nodo<T> next;
        int d;

        public Nodo(T value, int d) {
            this.next = null;
            this.value = value;
            this.d = d;
        }

    }

    private int index(int d) {
        d = d - this.base;
        if (d == 0) {
            return 0;
        }
        return (int) Math.ceil(Math.log10(d) / Math.log10(2));
    }

    private void redist() {
        base += range[pointer];
        @SuppressWarnings("unchecked")
        Nodo<E> chain = (Nodo<E>) h[pointer];
        while (chain.next != null) {
            @SuppressWarnings("unchecked")
            Nodo<E> next = (Nodo<E>) chain.next;
            chain.next = null;           
            store(chain.value,chain.d);
            chain=next;
        }
        store(chain.value,chain.d);
        h[pointer]=null;
        pointer=0;
    }

}
