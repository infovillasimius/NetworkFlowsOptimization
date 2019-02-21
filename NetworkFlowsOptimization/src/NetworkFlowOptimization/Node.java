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

import java.util.ArrayList;

public class Node implements Comparable<Node> {

    public static final int INFINITY = (int) 10E6;      //valore arbitrario associato a infinito

    private static int counter = 0;                     //contatore dei nodi generati

    int distance;                                       //distanza iniziale settata a +infinito

    private final int id;                               //identificativo univoco del nodo
    private final int value;                            //valore associato al nodo
    int number;
    int order;
    int period;
    boolean necessary;
    boolean previously;
    boolean contained;
    int indegree;

    Node pred;                                          //nodo predecessore
    Arc predArc;

    ArrayList<Arc> in;                                  //Lista degli archi entranti
    ArrayList<Arc> out;                                 //Lista degli archi uscenti

    /**
     * Constructor
     *
     * @param value
     */
    public Node(int value) {
        this.distance = INFINITY;
        this.contained = false;
        this.necessary = false;
        id = counter++;
        this.value = value;
        in = new ArrayList<>();
        out = new ArrayList<>();
        pred = null;
        this.previously = false;
    }

    public Node(int value, int month) {
        this.distance = INFINITY;
        this.contained = false;
        this.previously = false;
        this.necessary = false;
        id = counter++;
        this.value = value;
        this.period = month;
        in = new ArrayList<>();
        out = new ArrayList<>();
        pred = null;
    }

    public int getId() {
        return id;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Node{" + "i=" + period + ", k=" + value + ", number=" + number + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + this.id;
        hash = 43 * hash + this.number;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Node other = (Node) obj;

        if (this.number == other.number && this.number>0) {
            return true;
        }
        if (this.value != other.value) {
            return false;
        }
        if (this.period != other.period) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Node o) {
        return this.distance - o.distance;
    }

}
