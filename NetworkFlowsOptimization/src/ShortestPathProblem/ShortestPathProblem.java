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
package ShortestPathProblem;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 *
 * @author anto
 */
public class ShortestPathProblem {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Graph graph = new Graph();

        dijkstra(graph);
        heapDijkstra(graph);
        dialDijkstra(graph);
        RadixHeapDijkstra(graph);
        dynamic(graph);
        labelCorrecting(graph);
        modifiedLabelCorrecting(graph);
        FifoLabelCorrecting(graph);
        dequeueLabelCorrecting(graph);
    }

    /**
     * Stampa risultati
     *
     * @param sink
     * @param name
     * @param start
     * @param stop
     */
    public static void printResults(Node sink, String name, long start, long stop) {
        System.out.println(name);
        System.out.println("Tempo di esecuzione = " + (double) stop / 1000000 + " millisecondi");
        Node n = sink;
        int cost = n.distance;
        System.out.print("Operai nei mesi da febbraio a settembre = ");
        ArrayList<Integer> r = new ArrayList<>();
        do {
            r.add(n.getValue());
            n = n.pred;
        } while (n.order != 1);
        r.add(n.getValue());
        Collections.reverse(r);
        System.out.print(r + "\n");
        System.out.println("Costo totale = " + cost + "\n");
    }

    /**
     * Implementazione algoritmo Dinamico
     *
     * @param graph
     */
    public static void dynamic(Graph graph) {
        ArrayList<Node> list = graph.getList();
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
        printResults(graph.getSink(), "Algoritmo dinamico ", start, stop);
    }

    /**
     * Implementazione algoritmo Dijkstra
     *
     * @param graph
     */
    public static void dijkstra(Graph graph) {
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
        printResults(graph.getSink(), "Algoritmo di Dijkstra ", start, stop);
    }

    public static void dialDijkstra(Graph graph) {
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
        printResults(graph.getSink(), "Algoritmo di Dial - Dijkstra ", start, stop);
    }

    public static void RadixHeapDijkstra(Graph graph) {
        ArrayList<Node> list = graph.getList();
        Node n = graph.getSource();
        int dist,oldD;

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
                oldD=i.head.distance;
                if (oldD > dist) {
                    i.head.distance = dist;
                    i.head.pred = i.tail;
                    if (!i.head.contained) {
                        q.store(i.head, i.head.distance);
                        i.head.contained=true;
                    } else {
                        q.update(i.head, dist, oldD);
                    }
                }
            }
        }
        long stop = System.nanoTime() - start;
        printResults(graph.getSink(), "Algoritmo RadixHeap - Dijkstra ", start, stop);
    }

    /**
     * Implementazione algoritmo Dijkstra con heap
     *
     * @param graph
     */
    public static void heapDijkstra(Graph graph) {
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
        printResults(graph.getSink(), "Algoritmo di Dijkstra con heap", start, stop);
    }

    public static void labelCorrecting(Graph graph) {
        ArrayList<Node> list = graph.getList();
        ArrayList<Arc> arcList = graph.getArcList();

        //Collections.shuffle(arcList);
        int dist;
        boolean optCond = false;

        long start = System.nanoTime();
        while (!optCond) {
            optCond = true;

            for (Arc i : arcList) {
                dist = i.tail.distance + i.cost;
                if (i.head.distance > dist) {
                    i.head.distance = dist;
                    i.head.pred = i.tail;
                    optCond = false;
                }
            }
        }
        long stop = System.nanoTime() - start;
        printResults(graph.getSink(), "Algoritmo label correcting ", start, stop);
    }

    public static void modifiedLabelCorrecting(Graph graph) {
        ArrayList<Node> list = graph.getList();
        Node s = graph.getSource();
        Node n;
        int dist;
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
        }
        long stop = System.nanoTime() - start;
        printResults(graph.getSink(), "Algoritmo modified label correcting ", start, stop);
    }

    /**
     * Need arcs ordered by tail node topological order
     *
     * @param graph
     */
    public static void FifoLabelCorrecting(Graph graph) {
        ArrayList<Node> list = graph.getList();
        ArrayList<Arc> arcList = graph.getArcList();
        Node sink = list.get(list.size() - 1);
        int dist;

        LinkedList<Arc> LIST = new LinkedList<>();

        long start = System.nanoTime();

        for (Arc a : arcList) {
            dist = a.tail.distance + a.cost;
            if (a.head.distance > dist) {
                a.head.distance = dist;
                a.head.pred = a.tail;
                LIST.add(a);
            }
        }

        while (!LIST.isEmpty()) {
            Arc i = LIST.poll();
            dist = i.tail.distance + i.cost;
            if (i.head.distance > dist) {
                i.head.distance = dist;
                i.head.pred = i.tail;
                LIST.add(i);
            }
        }

        long stop = System.nanoTime() - start;
        printResults(sink, "Algoritmo FIFO label correcting ", start, stop);
    }

    /**
     *
     * @param graph
     */
    public static void dequeueLabelCorrecting(Graph graph) {
        ArrayList<Node> list = graph.getList();
        Node sink = list.get(list.size() - 1);
        Node s = list.get(0);
        Node n;

        ArrayDeque<Node> LIST = new ArrayDeque<>();
        LIST.add(s);

        int dist;

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
                            i.head.previously = true;
                        }
                        i.head.contained = true;
                    }
                }
            }
        }
        long stop = System.nanoTime() - start;
        printResults(sink, "Algoritmo Deque label correcting ", start, stop);
    }

}
