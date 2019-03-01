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
import java.util.Collections;
import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 *
 * @author anto
 */
public class ShortestPathProblem {

    private ShortestPathProblem() {
    }
    
    /**
     * Implementazione algoritmo Dinamico
     *
     * @param graph
     * @return 
     */
    public static String dynamic(Graph graph) {
        ArrayList<Node> list = graph.getOrdered();
        if (list == null) {
            return "Dynamic Algorithm (Cycle detected) \n\n";
        }
        int dist;
        long start = System.nanoTime();
        for (Node i : list) {
            for (Arc a : i.out) {
                dist = a.getTail().distance + a.getCost();
                if (a.getHead().distance > dist) {
                    a.head.distance = dist;
                    a.head.pred = a.tail;
                }
            }
        }
        long stop = System.nanoTime() - start;
        return printResults(graph, "Dynamic Algorithm ", start, stop, null);
    }

    /**
     * Implementazione algoritmo Dijkstra
     *
     * @param graph
     * @return 
     */
    public static String dijkstra(Graph graph) {
        if (graph.isNegCost()) {
            return "Dijkstra Algorithm (Negative arc cost detected) \n\n";
        }
        ArrayList<Node> list = graph.getList();
        Node n = graph.getSource();
        int dist, min;

        long start = System.nanoTime();

        while (!list.isEmpty()) {
            min = Node.INFINITY;
            for (Node i : list) {
                if (i.distance < min) {
                    min = i.distance;
                    n = i;
                }
            }
            list.remove(n);
            for (Arc i : n.out) {
                dist = i.tail.distance + i.getCost();
                if (i.head.distance > dist) {
                    i.head.distance = dist;
                    i.head.pred = i.tail;
                }
            }
        }
        long stop = System.nanoTime() - start;
        return printResults(graph, "Dijkstra Algorithm ", start, stop, null);
    }

    public static String dialDijkstra(Graph graph) {
        if (graph.isNegCost()) {
            return "Dial-Dijkstra Algorithm (Negative arc cost detected) \n\n";
        }
        ArrayList<Node> list = graph.getList();
        Node n = graph.getSource();
        int C = graph.getC();
        int dist;

        CircularQueue<Node> q = new CircularQueue<>(C + 1);

        q.store(n, n.distance);

        long start = System.nanoTime();

        while (!list.isEmpty()) {
            n = q.next();
            list.remove(n);
            for (Arc i : n.out) {
                dist = i.tail.distance + i.getCost();
                if (i.head.distance > dist) {
                    i.head.distance = dist;
                    i.head.pred = i.tail;
                    q.store(i.head, i.head.distance);
                }
            }
        }
        long stop = System.nanoTime() - start;
        return printResults(graph, "Dial-Dijkstra Algorithm ", start, stop, null);
    }

    public static String RadixHeapDijkstra(Graph graph) {
        if (graph.isNegCost()) {
            return "RadixHeap-Dijkstra Algorithm (Negative arc cost detected) \n\n";
        }
        ArrayList<Node> list = graph.getList();
        Node n = graph.getSource();
        int dist, oldD;

        RadixHeap<Node> q = new RadixHeap<>(graph.getC() * graph.nodesNumber());
        q.store(n, n.distance);
        n.contained = true;

        long start = System.nanoTime();

        while (!list.isEmpty()) {
            n = q.next();
            n.contained = false;
            list.remove(n);

            for (Arc i : n.out) {
                dist = i.tail.distance + i.getCost();
                oldD = i.head.distance;
                if (oldD > dist) {
                    i.head.distance = dist;
                    i.head.pred = i.tail;
                    if (!i.head.contained) {
                        q.store(i.head, i.head.distance);
                        i.head.contained = true;
                    } else {
                        q.update(i.head, dist, oldD);
                    }
                }
            }
        }
        long stop = System.nanoTime() - start;
        return printResults(graph, "RadixHeap-Dijkstra Algorithm ", start, stop, null);
    }

    /**
     * Implementazione algoritmo Dijkstra con heap
     *
     * @param graph
     */
    public static String heapDijkstra(Graph graph) {
        if (graph.isNegCost()) {
            return "Heap-Dijkstra Algorithm (Negative arc cost detected) \n\n";
        }
        ArrayList<Node> list = graph.getList();
        PriorityQueue<Node> q = new PriorityQueue<>();

        Node n = graph.getSource();
        q.add(n);
        n.contained = true;
        int dist;
        long start = System.nanoTime();
        while (!list.isEmpty() && !q.isEmpty()) {
            n = q.poll();
            n.contained = false;
            list.remove(n);
            for (Arc i : n.out) {
                dist = i.tail.distance + i.getCost();
                if (i.head.distance > dist) {
                    if (i.head.contained) {
                        q.remove(i.head);
                    }
                    i.head.distance = dist;
                    i.head.pred = i.tail;
                    q.add(i.head);
                }
            }
        }
        long stop = System.nanoTime() - start;
        return printResults(graph, "Heap-Dijkstra Algorithm ", start, stop, null);
    }

    public static String labelCorrecting(Graph graph) {
        ArrayList<Node> list = graph.getList();
        ArrayList<Arc> arcList = graph.getArcList();
        int minDist = -list.size() * graph.getC();
        int dist = 0;
        boolean optCond = false;
        Node nCycle = null;
        Node n = null;

        long start = System.nanoTime();
        while (!optCond) {
            optCond = true;

            for (Arc i : arcList) {
                dist = i.tail.distance + i.getCost();
                if (i.head.distance > dist) {
                    i.head.distance = dist;
                    i.head.pred = i.tail;
                    optCond = false;
                    n = i.head;
                }
            }
            if (dist < minDist) {
                optCond = true;
                nCycle = n;
            }
        }
        long stop = System.nanoTime() - start;
        return printResults(graph, "Label Correcting Algorithm", start, stop, nCycle);
    }

    public static String modifiedLabelCorrecting(Graph graph) {
        ArrayList<Node> list = graph.getList();
        Node s = graph.getSource();
        int minDist = -list.size() * graph.getC();
        Node n;
        Node nCycle = null;
        int dist = 0;
        LinkedList<Node> LIST = new LinkedList<>();
        LIST.add(s);

        long start = System.nanoTime();

        while (!LIST.isEmpty()) {
            n = LIST.pollFirst();
            n.contained = false;

            for (Arc i : n.out) {
                dist = i.tail.distance + i.getCost();
                if (i.head.distance > dist) {
                    i.head.distance = dist;
                    i.head.pred = i.tail;
                    if (!i.head.contained) {
                        LIST.add(i.head);
                        i.head.contained = true;
                    }
                }
            }
            if (dist < minDist) {
                LIST.clear();
                nCycle = n;
            }
        }
        long stop = System.nanoTime() - start;
        return printResults(graph, "Modified Label Correcting (FIFO) Algorithm ", start, stop, nCycle);
    }

    /**
     *
     * @param graph
     * @return 
     */
    public static String dequeueLabelCorrecting(Graph graph) {
        ArrayList<Node> list = graph.getList();
        Node s = graph.getSource();
        Node nCycle = null;
        Node n;
        int minDist = -list.size() * graph.getC();
        int dist = 0;
        LinkedList<Node> LIST = new LinkedList<>();
        LIST.add(s);

        long start = System.nanoTime();
        while (!LIST.isEmpty()) {
            n = LIST.pollFirst();
            n.contained = false;

            for (Arc i : n.out) {
                dist = i.tail.distance + i.getCost();
                if (i.head.distance > dist) {
                    i.head.distance = dist;
                    i.head.pred = i.tail;
                    if (!i.head.contained) {
                        if (i.head.previously) {
                            LIST.addFirst(i.head);
                        } else {
                            LIST.add(i.head);
                        }
                        i.head.previously = true;
                        i.head.contained = true;
                    }
                }
            }
            if (dist < minDist) {
                LIST.clear();
                nCycle = n;
            }
        }
        long stop = System.nanoTime() - start;
        return printResults(graph, "Deque Label Correcting Algorithm ", start, stop, nCycle);
    }

    /**
     * Stampa risultati e verifica presenza cicli negativi
     *
     * @param graph
     * @param name
     * @param start
     * @param stop
     */
    public static String printResults(Graph graph, String name, long start, long stop, Node nCycle) {
        String result = name + "\n";
        result = result.concat("Execution time = " + (double) stop / 1000000 + " milliseconds\n");
        Node n;
        if (nCycle == null) {
            n = graph.getSink();
        } else {
            n = nCycle;
        }
        int cost = n.distance;
        result = result.concat("Values ​​of the solution nodes = ");
        ArrayList<Integer> values = new ArrayList<>();
        ArrayList<Integer> order = new ArrayList<>();
        graph.previously();
        while (n.pred != null && !n.previously) {
            values.add(n.getValue());
            order.add(n.number);
            n.previously = true;
            n = n.pred;
            if (n.previously) {
                result = result.concat(" (Negative cycle detected) ");
            }
        }
        values.add(n.getValue());
        order.add(n.number);
        Collections.reverse(values);
        Collections.reverse(order);
        result = result.concat(values + "\n");
        result = result.concat("Solution nodes = ");
        result = result.concat(order + "\n");
        result = result.concat("Total cost = " + cost + "\n" + "\n");

        return result;

    }
}
