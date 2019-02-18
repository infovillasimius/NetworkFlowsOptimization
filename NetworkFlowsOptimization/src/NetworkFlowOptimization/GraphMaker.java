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

import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

/**
 *
 * @author anto
 */
public class GraphMaker {

    final static private int MULTIPLIER = 1;
    final static private boolean MAXIMIZER = false;

    private GraphMaker() {
    }

    /**
     * Costruzione grafo a partire dai vincoli
     *
     * @return
     */
    public static Graph sppGraphMaker() {
        ArrayList<Node> list = new ArrayList<>();
        ArrayList<Arc> arcList = new ArrayList<>();

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
        int i;
        int cost;

        while (!q.isEmpty()) {
            c = q.poll();
            k = c.getValue();
            i = c.period + 1;
            if (i < I + 1) {
                for (int k1 = 1; k1 <= K; k1++) {
                    if ((k1 - k <= 3 && 3 * k1 >= 2 * k && 4 * k1 >= 3 * n[i]) || MAXIMIZER) {
                        Node newNode = new Node(k1, i);
                        cost = MULTIPLIER * (abs(k1 - n[i]) * 200 + abs(k1 - k) * 160 - (abs(k1 - k) + (k1 - k)) * 30);
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
                i = t.period;
                int k1 = t.getValue();
                k = c.getValue();
                if ((k1 - k <= 3 && 3 * k1 >= 2 * k && 4 * k1 >= 3 * n[i]) || MAXIMIZER) {
                    cost = MULTIPLIER * (abs(k1 - n[i]) * 200 + abs(k1 - k) * 160 - (abs(k1 - k) + (k1 - k)) * 30);
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
                list.remove(a.getHead());
                toRemove.add(a.getHead());
            } else if (!a.tail.necessary) {
                list.remove(a.getTail());
                toRemove.add(a.getTail());
            }
        }
        for (Node x : toRemove) {
            arcList.removeAll(x.in);
            arcList.removeAll(x.out);
        }
        
        number(list);
        return new Graph(list, arcList, true);

    }

    /**
     * Etichetta i nodi del grafo in ordine topologico e con una reverse visit a
     * partire da t stabilisce quali nodi non sono in grado di raggiungere il
     * nodo pozzo e li marca per l'eliminazione
     *
     * @param list
     */
    private static void order(ArrayList<Node> list) {
        Node s = list.get(0);
        Node t = list.get(list.size() - 1);
        LinkedList<Node> q = new LinkedList<>();
        Node c;
        q.add(t);
        while (!q.isEmpty()) {
            c = q.poll();
            c.necessary = true;
            for (Arc a : c.in) {
                q.add(a.getTail());
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
                q.add(a.getHead());
            }
        }
    }

    public static Graph cycleGraphMaker(int seed, int min, int max) {

        ArrayList<Node> list = new ArrayList<>();
        ArrayList<Arc> arcList = new ArrayList<>();

        for (int i = 1; i < 17; i++) {
            list.add(new Node(i));
        }
        Random rand = new Random(seed);

        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(0), list.get(1)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(0), list.get(3)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(7), list.get(0)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(7), list.get(8)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(7), list.get(12)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(14), list.get(7)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(13), list.get(14)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(13), list.get(9)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(12), list.get(8)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(5), list.get(7)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(3), list.get(5)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(2), list.get(4)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(4), list.get(3)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(4), list.get(1)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(1), list.get(2)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(9), list.get(1)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(2), list.get(9)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(9), list.get(5)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(2), list.get(5)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(8), list.get(6)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(6), list.get(12)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(6), list.get(9)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(6), list.get(15)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(6), list.get(10)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(10), list.get(11)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(10), list.get(15)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(15), list.get(9)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(15), list.get(11)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(12), list.get(10)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(5), list.get(6)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(5), list.get(8)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(11), list.get(12)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(11), list.get(13)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(12), list.get(13)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(12), list.get(14)));

        number(list);
        Graph graph = new Graph(list, arcList);
        return graph;
    }

    public static Graph randomGraph(int n, int perc, int seed, int minCost, int maxCost, boolean cycle, String result) {
        ArrayList<Node> list = new ArrayList<>();
        ArrayList<Arc> arcList = new ArrayList<>();
        int[][] nad = new int[n][n];
        int arc;

        if (perc <= 0) {
            perc = 5;
        } else if (perc > 100) {
            perc = 100;
        }

        for (int i = 1; i <= n; i++) {
            list.add(new Node(i));
        }
        Random rand = new Random(seed);

        for (int row = 0; row < n; row++) {
            for (int col = row + 1; col < n; col++) {
                arc = rand.nextInt(101);
                if (arc <= perc) {
                    if (rand.nextInt(101) <= 50 && cycle) {
                        nad[col][row] = 1;
                    } else {
                        nad[row][col] = 1;
                    }
                }
            }
        }

        int cost;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (nad[row][col] == 1) {
                    cost = rand.nextInt((maxCost - minCost) + 1) + minCost;
                    arcList.add(new Arc(cost, list.get(row), list.get(col)));;
                }
            }
        }

        for (Arc a : arcList) {
            a.getHead().in.add(a);
            a.getTail().out.add(a);
        }

        ArrayList<Node> ordered = sourceSearch(list);
        number(ordered);

        ArrayList<Arc> arcList2 = new ArrayList<>();
        for (Arc a : arcList) {
            if (ordered.contains(a.getHead()) && ordered.contains(a.getTail())) {
                arcList2.add(a);
            }
        }
        arcList = arcList2;

        return new Graph(ordered, arcList, ordered.get(0), ordered.get(ordered.size() - 1));
    }

    private static ArrayList<Node> sourceSearch(ArrayList<Node> list) {
        ArrayList<Node> ordered = null;
        ArrayList<Node> lista;
        int max = 0;
        for (Node n : list) {
            lista = bfsearch(n, list);

            if (lista.size() > max) {
                ordered = lista;
                max = lista.size();
            }
        }
        return ordered;
    }

    private static ArrayList<Node> bfsearch(Node source, ArrayList<Node> list) {
        Graph.previously(list);
        ArrayList<Node> ordered = new ArrayList<>();
        LinkedList<Node> LIST = new LinkedList<>();
        int next = 0;
        Node n;
        LIST.add(source);
        source.previously = true;

        while (!LIST.isEmpty()) {
            n = LIST.poll();

            n.order = ++next;
            ordered.add(n);
            for (Arc a : n.out) {
                if (!a.head.previously) {
                    LIST.add(a.getHead());
                    a.head.previously = true;
                }
            }
        }
        return ordered;
    }

    private static void number(ArrayList<Node> list) {
        int next = 0;
        for (Node n : list) {
            n.number = ++next;
        }
    }
}
