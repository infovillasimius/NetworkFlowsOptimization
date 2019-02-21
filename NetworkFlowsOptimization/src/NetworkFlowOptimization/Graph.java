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

/**
 *
 * @author anto
 */
public class Graph {

    private final ArrayList<Node> list;
    private final ArrayList<Arc> arcList;
    private ArrayList<Node> ordered;
    private final Node s;
    private final Node t;
    private final boolean isOrdered;
    private final boolean negCost;

    public Graph(ArrayList<Node> list, ArrayList<Arc> arcList) {
        this.list = list;
        this.arcList = arcList;
        this.s = list.get(0);
        this.t = list.get(list.size() - 1);
        for (Arc a : arcList) {
            a.getHead().in.add(a);
            a.getTail().out.add(a);
        }
        this.isOrdered = this.order();
        this.negCost = this.negCost();
    }

    public Graph(ArrayList<Node> list, ArrayList<Arc> arcList, boolean spp) {
        this.list = list;
        this.arcList = arcList;
        this.s = list.get(0);
        this.t = list.get(list.size() - 1);
        this.isOrdered = true;
        this.ordered = new ArrayList<>();
        this.ordered.addAll(list);
        this.negCost = false;
    }

    public Graph(ArrayList<Node> list, ArrayList<Arc> arcList, Node source, Node sink) {

        if (list.get(0) != source || list.get(list.size() - 1) != sink) {
            list.remove(source);
            list.remove(sink);
            this.list = new ArrayList<>();
            this.list.add(source);
            this.list.addAll(list);
            this.list.add(sink);
        } else {
            this.list = list;
        }
        this.arcList = arcList;
        this.s = source;
        this.t = sink;
        this.ordered = new ArrayList<>();
        this.isOrdered = this.order();
        this.negCost = this.negCost();
    }

    public ArrayList<Node> getList() {
        ArrayList<Node> list2 = new ArrayList<>();
        list2.addAll(list);
        initialize(list2);
        return list2;
    }

    public ArrayList<Arc> getArcList() {
        ArrayList<Arc> arcList2 = new ArrayList<>();
        arcList2.addAll(arcList);
        return arcList2;
    }

    public Node getSource() {
        return s;
    }

    public Node getSink() {
        return t;
    }

    public int getC() {
        int c = 0;
        for (Arc a : arcList) {
            if (c < Math.abs(a.getCost())) {
                c = Math.abs(a.getCost());
            }
        }
        return c;
    }

    public int nodesNumber() {
        return list.size();
    }

    public int arcsNumber() {
        return arcList.size();
    }

    public ArrayList<Node> getOrdered() {
        if (isOrdered) {
            ArrayList<Node> newOrdered = new ArrayList<>();
            newOrdered.addAll(ordered);
            initialize(newOrdered);
            return newOrdered;
        }
        return null;
    }

    public boolean isIsOrdered() {
        return isOrdered;
    }

    public boolean isNegCost() {
        return negCost;
    }

    public void previously() {

        for (Node i : list) {
            i.previously = false;
        }
    }

    private boolean order() {
        for (Node i : list) {
            i.indegree = 0;
        }
        for (Arc a : arcList) {
            a.head.indegree++;
        }

        LinkedList<Node> LIST = new LinkedList<>();
        int next = 0;
        Node n;

        for (Node i : list) {
            if (i.indegree == 0) {
                LIST.add(i);
            }
        }

        while (!LIST.isEmpty()) {
            n = LIST.poll();
            n.order = ++next;
            ordered.add(n);

            for (Arc a : n.out) {
                a.head.indegree--;
                if (a.getHead().indegree == 0) {
                    LIST.add(a.getHead());
                }
            }
        }

        return next >= list.size();
    }

    private boolean negCost() {
        for (Arc a : arcList) {
            if (a.getCost() < 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * reinizializzazione nodi
     *
     * @param list
     */
    private void initialize(ArrayList<Node> list) {
        for (Node i : list) {
            i.previously = false;
            i.contained = false;
            i.distance = Node.INFINITY;
            i.pred = null;
        }
        list.get(0).distance = 0;
    }

    public static void previously(ArrayList<Node> list) {
        for (Node i : list) {
            i.previously = false;
        }
    }

    public String adjMatrix() {
        int n = list.size();
        String result = "Adjacency matrix \n";
        int[][] nad = new int[n][n];

        if (n <= 100) {
            for (Arc a : arcList) {
                nad[list.indexOf(a.getTail())][list.indexOf(a.getHead())] = 1;
            }

            for (int row = 0; row < n; row++) {
                for (int col = 0; col < n; col++) {
                    result = result.concat(nad[row][col] + " ");
                }
                result = result.concat("\n");
            }
            result = result.concat("\n");
        } else {
            result = result.concat("Matrix too large!! (n>100)\n\n");
        }

        return result;
    }

    public String arcCosts() {
        int n = arcList.size();
        ArrayList<Arc> ordArc = new ArrayList<>();
        ordArc.addAll(arcList);

        Collections.sort(ordArc);
        String result = "Arc costs\n";

        if (n <= 2000) {
            for (Arc a : ordArc) {
                result = result.concat("( " + a.getTail().number + " , " + a.getHead().number + " ) =>" + a.getCost() + "\n");
            }
        } else {
            result = result.concat("To many arcs!! (>2000)\n");
        }
        result = result.concat("\n");
        return result;
    }

    public void setResidual() {
        ArrayList<Arc> ordArc = new ArrayList<>();
        ordArc.addAll(arcList);
        Collections.sort(ordArc);
        for (Arc a : ordArc) {
            a.residualForwardCapacity = a.capacity - a.flow;
            a.residualReverseCapacity = a.flow;
        }
    }

    public String maxFlowArcs() {
        String result = "Flow out of source node \n";
        int maxFlowS = 0;
        int maxFlowT = 0;

        for (Arc a : s.out) {
            maxFlowS += a.flow;
            if (a.flow > 0) {
                result = result.concat(a.toFlow());
            }
        }
        result = result.concat("Tot=" + maxFlowS + "\n");
        result = result.concat("\nFlow entering sink \n");
        for (Arc a : t.in) {
            maxFlowT += a.flow;
            if (a.flow > 0) {
                result = result.concat(a.toFlow());
            }
        }
        result = result.concat("Tot=" + maxFlowT + "\n");

        ArrayList<Arc> ordArc = new ArrayList<>();
        ordArc.addAll(arcList);
        Collections.sort(ordArc);

        result = result.concat("\nFlow arcs\n");
        for (Arc a : ordArc) {
            if (a.flow > 0) {
                result = result.concat(a.toFlow());
            }
        }

        return result;
    }

    public String maxFlow() {
        String result = "Flow exiting the source = \t";
        int maxFlowS = 0;
        int maxFlowT = 0;

        for (Arc a : s.out) {
            maxFlowS += a.flow;
        }
        result = result.concat(maxFlowS + "\nFlow entering the sink = \t");
        for (Arc a : t.in) {
            maxFlowT += a.flow;
        }
        result = result.concat(maxFlowT + "\n");
        return result;
    }

    public String maxFlowAllArcs() {
        String result = "List of arcs\n";
        ArrayList<Arc> ordArc = new ArrayList<>();
        ordArc.addAll(arcList);
        Collections.sort(ordArc);

        for (Arc a : ordArc) {
            result = result.concat(a.toFlow());
        }
        result = result.concat("\n");
        return result;
    }

}
