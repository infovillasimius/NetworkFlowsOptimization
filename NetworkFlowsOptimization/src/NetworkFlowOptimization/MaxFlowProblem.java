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
import java.util.LinkedList;

/**
 * Maximum Flow Problem Algorithms
 *
 * @author anto
 */
public class MaxFlowProblem {

    private MaxFlowProblem() {
    }

    /**
     * Labeling Algorithm
     *
     * @param graph a graph with n nodes and m arcs
     * @return long - execution time in nanonseconds
     */
    public static long labeling(Graph graph) {
        graph.setResidual();
        ArrayList<Node> list = graph.getList();
        LinkedList<Node> LIST = new LinkedList<>();
        Node s = graph.getSource();
        Node t = graph.getSink();
        Node i;
        t.previously = true;
        long start = System.nanoTime();
        while (t.previously) {
            for (Node n : list) {
                n.previously = false;
                n.pred = null;
                n.predArc = null;
            }
            s.previously = true;
            LIST.clear();
            LIST.add(s);
            while (!LIST.isEmpty() && !t.previously) {
                i = LIST.poll();
                for (Arc a : i.out) {
                    if (a.residualForwardCapacity > 0 && !a.head.previously) {
                        a.head.pred = i;
                        a.head.predArc = a;
                        a.head.previously = true;
                        LIST.add(a.head);
                    }
                }
                for (Arc a : i.in) {

                    if (a.residualReverseCapacity > 0 && !a.tail.previously) {
                        a.tail.pred = i;
                        a.tail.predArc = a;
                        a.tail.previously = true;
                        LIST.add(a.tail);
                    }
                }
                if (t.previously) {
                    augment(t);
                }
            }
        }
        long stop = System.nanoTime() - start;
        return stop;
    }

    private static void augment(Node t) {
        Node n = t;
        Arc a;
        int minR = t.predArc.residualForwardCapacity;
        int r;
        while (n.pred != null) {
            a = n.predArc;
            if (n.pred.equals(a.tail)) {
                r = a.residualForwardCapacity;
            } else {
                r = a.residualReverseCapacity;
            }
            if (minR > r) {
                minR = r;
            }
            n = n.pred;
        }
        n = t;
        while (n.pred != null) {
            a = n.predArc;
            if (n.pred.equals(a.tail)) {
                a.flow += minR;
                a.residualForwardCapacity -= minR;
                a.residualReverseCapacity += minR;
            } else {
                a.flow -= minR;
                a.residualForwardCapacity += minR;
                a.residualReverseCapacity -= minR;
            }
            n = n.pred;
        }
    }

    /**
     * Preflow push Algorithm
     *
     * @param graph a graph with n nodes and m arcs
     * @return long - execution time in nanoseconds
     */
    public static long preflowPush(Graph graph) {
        //ArrayList<Node> list = graph.getList();
        Node t = graph.getSink();
        Node n;
        graph.activeNodesList.clear();
        preprocess(graph);
        long start = System.nanoTime();
        while (!graph.activeNodesList.isEmpty()) {
            n = graph.activeNodesList.poll();
            if (n.massBalance > 0 && !n.equals2(t)) {
                pushRelabel(n, graph);
            }
        }
        long stop = System.nanoTime() - start;
        return stop;
    }

    private static void preprocess(Graph graph) {
        graph.resetFlows();
        graph.reverseBreadthFirstSearch();
        Node s = graph.getSource();
        for (Arc a : s.out) {
            a.setFlow(a.capacity);
            graph.activeNodesList.add(a.head);
        }
        s.distance = graph.nodesNumber();
    }

    private static void pushRelabel(Node i, Graph graph) {
        Arc a;
        Node s = graph.getSource();
        Node t = graph.getSink();
        int d = Node.INFINITY;
        int counter;
        int inSize = i.in.size();
        int outSize = i.out.size();
        while (i.massBalance > 0) {
            while (i.massBalance > 0 && i.activeForwardArc < outSize) {
                a = i.out.get(i.activeForwardArc);
                if (a.head.distance < a.tail.distance && a.residualForwardCapacity > 0 && !a.head.equals2(s)) {
                    a.setFlow(a.flow + Math.min(a.residualForwardCapacity, i.massBalance));
                    if (a.head.massBalance > 0 && !a.head.equals2(t)) {
                        graph.activeNodesList.add(a.head);
                    }
                } else {
                    i.activeForwardArc++;
                }
            }
            if (i.activeForwardArc >= outSize) {
                i.activeForwardArc = 0;
            }
            if (i.massBalance <= 0) {
                return;
            }
            while (i.massBalance > 0 && i.activeReverseArc < inSize) {
                a = i.in.get(i.activeReverseArc);
                if (a.residualReverseCapacity > 0 && a.tail.distance < a.head.distance) {
                    a.setFlow(a.flow - Math.min(a.residualReverseCapacity, i.massBalance));
                    if (a.tail.massBalance > 0) {
                        graph.activeNodesList.add(a.tail);
                    }
                } else {
                    i.activeReverseArc++;
                }
            }
            if (i.activeReverseArc >= inSize) {
                i.activeReverseArc = 0;
            }
            if (i.massBalance <= 0) {
                return;
            }
            counter = 0;
            for (Arc aa : i.out) {
                if (aa.residualForwardCapacity > 0 && d > aa.head.distance && !aa.head.equals2(s)) {
                    d = aa.head.distance;
                    i.activeForwardArc = counter;
                }
                counter++;
            }
            counter = 0;
            for (Arc aa : i.in) {
                if (aa.residualReverseCapacity > 0 && d > aa.tail.distance) {
                    d = aa.tail.distance;
                    i.activeReverseArc = counter;
                    i.activeForwardArc = outSize;
                }
            }
            i.distance = d + 1;
            d = Node.INFINITY;
        }
    }
}
