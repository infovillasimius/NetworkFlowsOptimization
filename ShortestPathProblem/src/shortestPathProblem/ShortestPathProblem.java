/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shortestPathProblem;

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
        ArrayList<Node> list = new ArrayList<>();       //lista nodi
        ArrayList<Arc> arcList = new ArrayList<>();     //lista archi

        Graph.sppGraphMaker(list, arcList);                 //creazione grafo
        ArrayList<Node> list2 = new ArrayList<>();      //seconda lista nodi - può essere modificata

        //esecuzione algoritmo Dijkstra
        list2.clear();
        list2.addAll(list);
        dijkstra(list2);

        //esecuzione algoritmo Dijkstra con heap
        list2.clear();
        list2.addAll(list);
        heapDijkstra(list2);

        //esecuzione algoritmo dinamico
        list2.clear();
        list2.addAll(list);
        dynamic(list2);

        list2.clear();
        list2.addAll(list);
        labelCorrecting(list2, arcList);

        list2.clear();
        list2.addAll(list);
        modifiedLabelCorrecting(list2, arcList);

        list2.clear();
        list2.addAll(list);
        FifoLabelCorrecting(list2, arcList); //Arc list has to be ordered by tail node

        list2.clear();
        list2.addAll(list);
        dequeueLabelCorrecting(list2, arcList);
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
        System.out.println("Costo totale = " + cost);
    }

    /**
     * Implementazione algoritmo Dijkstra
     *
     * @param list
     */
    public static void dijkstra(ArrayList<Node> list) {
        Graph.initialize(list);
        Node sink = list.get(list.size() - 1);
        Node n = list.get(0);

        int dist;
        long start = System.nanoTime();
        while (!list.isEmpty()) {
            int min = Node.INFINITY;
            for (Node i : list) {
                if (i.distance <= min) {
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
        printResults(sink, "Algoritmo di Dijkstra ", start, stop);
    }

    /**
     * Implementazione algoritmo Dinamico
     *
     * @param list
     */
    public static void dynamic(ArrayList<Node> list) {
        Graph.initialize(list);
        Node sink = list.get(list.size() - 1);
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
        printResults(sink, "\nAlgoritmo dinamico ", start, stop);
    }

    /**
     * Implementazione algoritmo Dijkstra con heap
     *
     * @param list
     */
    public static void heapDijkstra(ArrayList<Node> list) {
        PriorityQueue<Node> q = new PriorityQueue<>();
        Graph.initialize(list);
        Node sink = list.get(list.size() - 1);
        Node n = list.get(0);
        q.add(n);
        int dist;
        long start = System.nanoTime();
        while (!list.isEmpty() && !q.isEmpty()) {
            n = q.poll();
            list.remove(n);
            for (Arc i : n.out) {
                dist = i.tail.distance + i.cost;
                if (i.head.distance > dist) {
                    if (i.head.distance < Node.INFINITY) {
                        q.remove(i.head);
                    }
                    i.head.distance = dist;
                    i.head.pred = i.tail;
                    q.add(i.head);
                }
            }
        }
        long stop = System.nanoTime() - start;
        printResults(sink, "\nAlgoritmo di Dijkstra con heap", start, stop);
    }

    public static void labelCorrecting(ArrayList<Node> list, ArrayList<Arc> arcList) {
        Graph.initialize(list);
        Node sink = list.get(list.size() - 1);

        ArrayList<Arc> arcList2 = new ArrayList<>();
        arcList2.addAll(arcList);
        Collections.shuffle(arcList2);
        int dist;
        boolean optCond = false;

        long start = System.nanoTime();
        while (!optCond) {
            optCond = true;

            for (Arc i : arcList2) {
                dist = i.tail.distance + i.cost;
                if (i.head.distance > dist) {
                    i.head.distance = dist;
                    i.head.pred = i.tail;
                    optCond = false;
                }
            }
        }
        long stop = System.nanoTime() - start;
        printResults(sink, "\nAlgoritmo label correcting ", start, stop);
    }

    public static void modifiedLabelCorrecting(ArrayList<Node> list, ArrayList<Arc> arcList) {
        Graph.initialize(list);

        Node sink = list.get(list.size() - 1);
        Node s = list.get(0);
        Node n;

        Collections.shuffle(list);

        LinkedList<Node> LIST = new LinkedList<>();
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
                        LIST.add(i.head);
                        i.head.contained = true;
                    }
                }
            }
        }
        long stop = System.nanoTime() - start;
        printResults(sink, "\nAlgoritmo modified label correcting ", start, stop);
    }

    /**
     * Need arcs ordered by tail node topological order
     *
     * @param list
     * @param arcList
     */
    public static void FifoLabelCorrecting(ArrayList<Node> list, ArrayList<Arc> arcList) {
        Graph.initialize(list);
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
        printResults(sink, "\nAlgoritmo FIFO label correcting ", start, stop);
    }

    public static void dequeueLabelCorrecting(ArrayList<Node> list, ArrayList<Arc> arcList) {
        Graph.initialize(list);
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
        printResults(sink, "\nAlgoritmo Deque label correcting ", start, stop);
    }

}
