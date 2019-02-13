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

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 *
 * @author anto
 */
public class LabelAlgorithms {

    /**
     * Implementazione algoritmo Dinamico
     *
     * @param graph
     */
    public static void dynamic(Graph graph) {
        ArrayList<Node> list = graph.getList();
        list = graph.getOrdered();
        if (list == null) {
            System.out.println("Algoritmo dinamico (Cycle detected) \n");
            return;
        }
        int dist;

        long start = System.nanoTime();
        for (Node i : list) {
            for (Arc a : i.out) {
                dist = a.tail.distance + a.cost;
                if (a.head.distance > dist) {
                    a.head.distance = dist;
                    a.head.pred = a.tail;
                }
            }
        }
        long stop = System.nanoTime() - start;
        printResults(graph, "Algoritmo dinamico ", start, stop, null);
    }

    /**
     * Implementazione algoritmo Dijkstra
     *
     * @param graph
     */
    public static void dijkstra(Graph graph) {
        if (graph.isNegCost()) {
            System.out.println("Algoritmo di Dijkstra (Negative arc cost detected) \n");
            return;
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
                dist = i.tail.distance + i.cost;
                if (i.head.distance > dist) {
                    i.head.distance = dist;
                    i.head.pred = i.tail;
                }
            }
        }
        long stop = System.nanoTime() - start;
        printResults(graph, "Algoritmo di Dijkstra ", start, stop, null);
    }

    public static void dialDijkstra(Graph graph) {
        if (graph.isNegCost()) {
            System.out.println("Algoritmo di Dial - Dijkstra (Negative arc cost detected) \n");
            return;
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
                dist = i.tail.distance + i.cost;
                if (i.head.distance > dist) {
                    i.head.distance = dist;
                    i.head.pred = i.tail;
                    q.store(i.head, i.head.distance);
                }
            }
        }
        long stop = System.nanoTime() - start;
        printResults(graph, "Algoritmo di Dial - Dijkstra ", start, stop, null);
    }

    public static void RadixHeapDijkstra(Graph graph) {
        if (graph.isNegCost()) {
            System.out.println("Algoritmo RadixHeap - Dijkstra (Negative arc cost detected) \n");
            return;
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
                dist = i.tail.distance + i.cost;
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
        printResults(graph, "Algoritmo RadixHeap - Dijkstra ", start, stop, null);
    }

    /**
     * Implementazione algoritmo Dijkstra con heap
     *
     * @param graph
     */
    public static void heapDijkstra(Graph graph) {
        if (graph.isNegCost()) {
            System.out.println("Algoritmo di Dijkstra con heap (Negative arc cost detected) \n");
            return;
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
                dist = i.tail.distance + i.cost;
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
        printResults(graph, "Algoritmo di Dijkstra con heap", start, stop, null);
    }

    public static void labelCorrecting(Graph graph) {
        ArrayList<Node> list = graph.getList();
        ArrayList<Arc> arcList = graph.getArcList();
        int minDist = -list.size() * graph.getC();
        //Collections.shuffle(arcList);
        int dist = 0;
        boolean optCond = false;
        Node nCycle = null;
        Node n = null;

        long start = System.nanoTime();
        while (!optCond) {
            optCond = true;

            for (Arc i : arcList) {
                dist = i.tail.distance + i.cost;
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
        printResults(graph, "Algoritmo label correcting ", start, stop, nCycle);
    }

    public static void modifiedLabelCorrecting(Graph graph) {
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
                dist = i.tail.distance + i.cost;
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
        printResults(graph, "Algoritmo modified label correcting (FIFO) ", start, stop, nCycle);
    }

    /**
     *
     * @param graph
     */
    public static void dequeueLabelCorrecting(Graph graph) {
        ArrayList<Node> list = graph.getList();
        Node s = graph.getSource();
        Node nCycle = null;
        Node n = null;
        int minDist = -list.size() * graph.getC();
        int dist = 0;
        LinkedList<Node> LIST = new LinkedList<>();
        LIST.add(s);

        long start = System.nanoTime();
        while (!LIST.isEmpty()) {
            n = LIST.pollFirst();
            n.contained = false;

            for (Arc i : n.out) {
                dist = i.tail.distance + i.cost;
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
        printResults(graph, "Algoritmo Deque label correcting ", start, stop, nCycle);
    }
    /**
     * Stampa risultati e verifica presenza cicli negativi
     *
     * @param graph
     * @param name
     * @param start
     * @param stop
     */
    public static void printResults(Graph graph, String name, long start, long stop, Node nCycle) {
        System.out.println(name);
        System.out.println("Tempo di esecuzione = " + (double) stop / 1000000 + " millisecondi");
        Node n;
        if (nCycle == null) {
            n = graph.getSink();
        } else {
            n = nCycle;
        }

        int cost = n.distance;
        System.out.print("Nodi soluzione = ");
        ArrayList<Integer> r = new ArrayList<>();
        graph.previously();
        while (n.pred != null && !n.previously) {

            r.add(n.getValue());
            n.previously = true;
            n = n.pred;
            if (n.previously) {
                System.out.print(" (Negative cycle detected) ");
            }
        }
        r.add(n.getValue());
        Collections.reverse(r);
        System.out.print(r + "\n");
        System.out.println("Costo totale = " + cost + "\n");
    }
}
