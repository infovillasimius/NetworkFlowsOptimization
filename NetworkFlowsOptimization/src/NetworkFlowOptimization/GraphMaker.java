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

import java.io.File;
import java.io.IOException;
import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;
import javax.swing.JFileChooser;

/**
 *
 * @author anto
 */
public class GraphMaker {

    private static File selectedFile = null;

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
                i = t.period;
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

    public static Graph exampleGraph(int seed, int min, int max) {
        if (max < 0) {
            max = -max;
        }
        
        if (max < min) {
            int swap=max;
            max = min;
            min=swap;
        }
        
        
        
        int[] v = {11, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -11};
        ArrayList<Node> list = new ArrayList<>();
        ArrayList<Arc> arcList = new ArrayList<>();

        for (int i = 0; i < 16; i++) {
            list.add(new Node(v[i],i));
        }
        Random rand = new Random(seed);

        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(0), list.get(1), 6));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(0), list.get(3), 5));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(1), list.get(2), 7));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(2), list.get(4), 3));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(2), list.get(5), 3));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(2), list.get(9), 2));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(3), list.get(5), 9));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(4), list.get(1), 1));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(4), list.get(3), 1));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(5), list.get(7), 9));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(5), list.get(6), 5));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(5), list.get(8), 3));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(6), list.get(12), 2));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(6), list.get(9), 4));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(6), list.get(15), 5));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(6), list.get(10), 2));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(7), list.get(0), 3));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(7), list.get(8), 2));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(7), list.get(12), 7));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(8), list.get(6), 4));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(9), list.get(1), 3));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(9), list.get(5), 5));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(10), list.get(11), 2));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(10), list.get(15), 7));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(11), list.get(12), 1));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(11), list.get(13), 2));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(12), list.get(8), 1));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(12), list.get(10), 3));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(12), list.get(13), 3));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(12), list.get(14), 2));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(13), list.get(14), 7));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(13), list.get(9), 7));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(13), list.get(15), 5));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(14), list.get(7), 8));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(15), list.get(9), 2));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min, list.get(15), list.get(11), 1));

        number(list);
        Graph graph = new Graph(list, arcList);
        return graph;
    }

    public static Graph randomGraph(int n, int perc, int seed, int minCost, int maxCost, int maxCapacity, boolean cycle, int flow) {
        ArrayList<Node> list = new ArrayList<>();
        ArrayList<Arc> arcList = new ArrayList<>();

        if (n < 2) {
            n = 2;
        }
        if (n < 4) {
            perc = 100;
        }
        if (n < 20 && perc < 10) {
            perc = 10;
        }
        if (perc < 10) {
            perc = 10;
        } else if (perc > 100) {
            perc = 100;
        }

        int[][] nad = new int[n][n];
        int arc;

        for (int i = 1; i <= n; i++) {
            list.add(new Node(0,i));
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
        int capacity;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (nad[row][col] == 1) {
                    cost = rand.nextInt((maxCost - minCost) + 1) + minCost;
                    capacity = rand.nextInt(maxCapacity) + 1;
                    arcList.add(new Arc(cost, list.get(row), list.get(col), capacity));;
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

        for (Node i : ordered) {
            ArrayList<Arc> newList = new ArrayList<>();
            for (Arc a : i.in) {
                if (a.tail.number != 0) {
                    newList.add(a);
                }
            }
            i.in = newList;
        }
        
        ordered.get(0).setValue(flow);
        ordered.get(ordered.size()-1).setValue(-flow);
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

    static void number(ArrayList<Node> list) {
        int next = 0;
        for (Node n : list) {
            n.number = ++next;
        }
    }

    public static Graph loadGraph() throws IOException {
        ArrayList<Node> list = new ArrayList<>();
        ArrayList<Arc> arcList = new ArrayList<>();

        JFileChooser fileChooser = new JFileChooser(selectedFile);
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue != JFileChooser.APPROVE_OPTION) {
            return null;
        }

        selectedFile = fileChooser.getSelectedFile();

        Scanner scanner = new Scanner(selectedFile);
        int n = 0;

        if (scanner.hasNextInt()) {
            n = scanner.nextInt();
        }

        if (n < 2) {
            return null;
        }
        int[] val = new int[n];
        int[][] nad = new int[n][n];
        int[][] cost = new int[n][n];
        int[][] cap = new int[n][n];

        for (int i = 0; i < n; ++i) {
            if (scanner.hasNextInt()) {
                val[i] = scanner.nextInt();
            }
        }

        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                if (scanner.hasNextInt()) {
                    nad[i][j] = scanner.nextInt();
                }
            }
        }

        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                if (scanner.hasNextInt()) {
                    cost[i][j] = scanner.nextInt();
                }
            }
        }

        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                if (scanner.hasNextInt()) {
                    cap[i][j] = scanner.nextInt();
                }
            }
        }

        for (int i = 0; i < n; i++) {
            list.add(new Node(val[i],i));
        }

        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (nad[row][col] == 1) {
                    arcList.add(new Arc(cost[row][col], list.get(row), list.get(col), cap[row][col]));
                }
            }
        }

        for (Arc a : arcList) {
            a.getHead().in.add(a);
            a.getTail().out.add(a);
        }

        number(list);
        return new Graph(list, arcList, list.get(0), list.get(list.size() - 1));
    }
}
