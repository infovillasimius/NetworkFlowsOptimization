/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shortestPathProblem;

import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author anto
 */
public class Graph {

    ArrayList<Node> list;
    ArrayList<Arc> arcList;

    /**
     * Costruzione grafo a partire dai vincoli
     *
     * @param list
     * @param arcList
     */
    public static void sppGraphMaker(ArrayList<Node> list, ArrayList<Arc> arcList) {

        long start = System.nanoTime();
        int n[] = {3, 4, 6, 7, 4, 6, 2, 3}; //fabbisogni del personale

        int K = 0;          //stabilisce il max di n[i] = numero righe
        for (int i : n) {
            if (K < i) {
                K = i;
            }
        }

        int I = n.length - 2;  // numero colonne

        Node s = new Node(n[0]);
        Node t = new Node(n[n.length - 1], n.length - 1);
        s.distance = 0;
        list.add(s);

        LinkedList<Node> q = new LinkedList<>();
        q.add(s);
        Node c;
        int k;
        int i = 1;
        int cost;

        while (!q.isEmpty()) {
            c = q.poll();
            k = c.getValue();
            i = c.month + 1;
            if (i < I + 1) {
                for (int k1 = 1; k1 <= K; k1++) {
                    if (k1 - k <= 3 && 3 * k1 >= 2 * k && 4 * k1 >= 3 * n[i]) {
                        Node newNode = new Node(k1, i);
                        cost = abs(k1 - n[i]) * 200 + abs(k1 - k) * 160 - (abs(k1 - k) + (k1 - k)) * 30;
                        if (!q.contains(newNode)) {
                            q.add(newNode);
                            list.add(newNode);
                        } else {
                            newNode = q.get(q.indexOf(newNode));
                        }
                        Arc newArc = new Arc(cost, c, newNode);
                        arcList.add(newArc);
                        c.out.add(newArc);
                        newNode.in.add(newArc);
                    }
                }
            } else {
                i = t.month;
                int k1 = t.getValue();
                k = c.getValue();
                if (k1 - k <= 3 && 3 * k1 >= 2 * k && 4 * k1 >= 3 * n[i]) {
                    cost = abs(k1 - n[i]) * 200 + abs(k1 - k) * 160 - (abs(k1 - k) + (k1 - k)) * 30;
                    Arc newArc = new Arc(cost, c, t);
                    arcList.add(newArc);
                    c.out.add(newArc);
                    t.in.add(newArc);
                }
            }
        }
        list.add(t);    //aggiunge il nodo pozzo in ultima posizione
        order(list);

        //Eliminazione nodi non necessari
        ArrayList<Node> toRemove = new ArrayList<>();
        for (Arc a : arcList) {
            if (!a.head.necessary) {
                list.remove(a.head);
                toRemove.add(a.head);
            } else if (!a.tail.necessary) {
                list.remove(a.tail);
                toRemove.add(a.tail);
            }
        }
        for (Node x : toRemove) {
            arcList.removeAll(x.in);
            arcList.removeAll(x.out);
        }

        long stop = System.nanoTime() - start;
        System.out.println("Creazione Grafo\nTempo di esecuzione = " + (double) stop / 1000000 + " millisecondi");
        System.out.println("Numero massimo di nodi generabili a partire dalle \ndimensioni del problema I x K + 2 = " + ((I * K) + 2));
        System.out.println("Massimo numero di archi possibile 2 x K + (K x K x (I-1)) = " + (2 * K + (K * K * (I - 1))));
        System.out.println("Numero di nodi generati = " + list.size());
        System.out.println("Numero di archi generati = " + arcList.size() + "\n");
    }

    /**
     * Etichetta i nodi del grafo in ordine topologico e con una reverse visit a
     * partire da t stabilisce quali nodi non sono in grado di raggiungere il
     * nodo pozzo e li marca per l'eliminazione
     *
     * @param list
     */
    public static void order(ArrayList<Node> list) {
        Node s = list.get(0);
        Node t = list.get(list.size() - 1);
        LinkedList<Node> q = new LinkedList<>();
        Node c;
        q.add(t);
        while (!q.isEmpty()) {
            c = q.poll();
            c.necessary = true;
            for (Arc a : c.in) {
                q.add(a.tail);
            }
        }
        q.add(s);
        int order = 1;
        while (!q.isEmpty()) {
            c = q.poll();
            if (c.order == 0 && c.necessary) {
                c.order = order++;
            }
            for (Arc a : c.out) {
                q.add(a.head);
            }
        }
    }
    
    /**
     * reinizializzazione nodi
     *
     * @param list
     */
    public static void initialize(ArrayList<Node> list) {
        for (Node i : list) {
            i.previously = false;
            i.contained = false;
            if (i.getId() == 0) {
                i.distance = 0;
            } else {
                i.distance = Node.INFINITY;
            }
            i.pred = null;
        }
    }

}
