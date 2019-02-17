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

public class Arc implements Comparable<Arc>{

    int capacity;
    int minFlow;
    int flow;
    int residualFlow;
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
        return "Arc{" + "capacity=" + capacity + ", minFlow=" + minFlow + ", cost=" + cost + ", tail=" + tail + ", head=" + head + '}';
    }

    

    @Override
    public int compareTo(Arc o) {
        return (this.tail.number - o.tail.number)*1000000+(this.head.number - o.head.number);
    }

}
