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
 * Arc class
 *
 * @author antonello.meloni
 */
public class Arc implements Comparable<Arc> {

    int capacity;
    int minFlow;
    int flow;
    int residualForwardCapacity;
    int residualReverseCapacity;
    int cost;           //costo associato all'arco
    Node tail;          //nodo coda
    Node head;          //nodo testa

    /**
     * New Arc
     * @param cost int Arc cost
     * @param tail Node Arc tail
     * @param head Node Arc head
     */
    public Arc(int cost, Node tail, Node head) {
        this.capacity = 0;
        this.minFlow = 0;
        this.flow = 0;
        this.residualForwardCapacity = 0;
        this.residualReverseCapacity = 0;
        this.cost = cost;
        this.tail = tail;
        this.head = head;
    }

    /**
     * New Arc
     * @param cost int (Arc cost)
     * @param tail Node (Arc tail)
     * @param head Node (Arc head)
     * @param capacity int (Arc capacity)
     */
    public Arc(int cost, Node tail, Node head, int capacity) {
        this.capacity = capacity;
        this.minFlow = 0;
        this.flow = 0;
        this.residualForwardCapacity = 0;
        this.residualReverseCapacity = 0;
        this.cost = cost;
        this.tail = tail;
        this.head = head;
    }

    /**
     * New Arc
     * @param tail Node (Arc tail)
     * @param head Node (Arc head)
     */
    public Arc(Node tail, Node head) {
        this.capacity = 0;
        this.minFlow = 0;
        this.flow = 0;
        this.residualForwardCapacity = 0;
        this.residualReverseCapacity = 0;
        this.cost = 0;
        this.tail = tail;
        this.head = head;
    }

    /**
     * Arc to String for MaxFlow Problem 
     * @return String
     */
    public String toFlow() {
        return "(" + tail.number + ", " + head.number + ") => flow=" + flow + ", capacity=" + capacity + "\n";
    }

    /**
     * Arc to String for MinCostFlow Problem results
     * @return String
     */
    public String toMinCostFlow() {
        return "(" + tail.number + ", " + head.number + ") => flow=" + flow + ", cost per unit=" + cost + ", capacity=" + capacity + "\n";
    }

    /**
     * Arc to String for MinCostFlow Problem initial graph
     * @return String 
     */
    public String toEmptyMinCostFlow() {
        return "(" + tail.number + ", " + head.number + ") => cost per unit=" + cost + ", capacity=" + capacity + "\n";
    }

    @Override
    public String toString() {
        return "Arc{" + "tail=" + tail + ", head=" + head + '}';
    }

    @Override
    public int compareTo(Arc o) {
        return (this.tail.number - o.tail.number) * 1000000 + (this.head.number - o.head.number);
    }

    /**
     * Get arc cost
     * @return int (Arc cost)
     */
    public int getCost() {
        return cost;
    }

    /**
     * Get arc tail
     * @return Node (Arc tail)
     */
    public Node getTail() {
        return tail;
    }

    /**
     * Get arc head
     * @return Node (Arc head)
     */
    public Node getHead() {
        return head;
    }

    /**
     *  Set arc flow and update residual capacities
     * @param flow Flow to set
     */
    public void setFlow(int flow) {
        if (flow < 0) {
            return;
        }
        int delta;
        if (flow <= this.capacity) {
            delta = flow - this.flow;
            this.flow = flow;
        } else {
            delta = this.capacity - this.flow;
            this.flow = this.capacity;
        }
        this.residualForwardCapacity = this.capacity - this.flow;
        this.residualReverseCapacity = this.flow;
        this.tail.massBalance -= delta;
        this.head.massBalance += delta;
    }
}
