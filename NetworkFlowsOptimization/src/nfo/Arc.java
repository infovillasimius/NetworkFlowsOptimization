/*
 * Copyright (C) 2019
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

public class Arc {

    int cost;           //costo associato all'arco
    Node tail;          //nodo coda
    Node head;          //nodo testa

    public Arc(int cost, Node tail, Node head) {
        this.cost = cost;                           
        this.tail = tail;                           
        this.head = head;       
    }

    @Override
    public String toString() {
        return "Arc{" + "cost=" + cost + ", tail=" + tail + ", head=" + head + '}';
    }

}
