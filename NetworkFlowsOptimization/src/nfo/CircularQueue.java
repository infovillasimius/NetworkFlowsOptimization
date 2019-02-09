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
 * @param <T>
 */
public class CircularQueue<T> {

    private Object[] t;
    private int C;
    private int pointer;

    public CircularQueue(int n) {
        this.t = new Object[n];
        this.C = n;
        this.pointer = 0;
    }

    public void store(T n, int d) {
        if (t[d % (C)] == null) {
            t[d % (C)] = new Nodo<T>(n);
        } else {
            Nodo<T> a = (Nodo<T>) t[d % (C)];
            Nodo<T> b = new Nodo<T>(n);
            b.next=a;
            t[d % (C)] = b;
        }
    }

    public T next() {
        
        while (t[pointer] == null) {
            pointer = (pointer + 1) % C;
        }
        Nodo<T> r =(Nodo<T>) t[pointer];
        if (r.next == null) {
            t[pointer] = null;
        } else {
            Nodo<T> next =(Nodo<T>)t[pointer];
            next = next.next;
            t[pointer]=next;
        }
        return r.value;
    }

}

class Nodo<T> {

    T value;
    Nodo<T> next;

    public Nodo(T value) {
        this.next = null;
        this.value = value;
    }

}
