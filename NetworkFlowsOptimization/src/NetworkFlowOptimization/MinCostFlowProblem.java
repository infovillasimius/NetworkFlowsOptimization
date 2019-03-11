/*
 * Copyright (C) 2019 antonello.meloni
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
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author antonello.meloni
 */
public class MinCostFlowProblem {

    /**
     * Cycle Canceling algorithm (Min cost flow)
     *
     * @param graph
     * @return
     */
    public static long cycleCanceling(Graph graph) {
        graph.resetFlows();
        graph.paths.clear();
        int times = 0;
        Graph fGraph = getGraphForFeasibleSolution(graph);
        MaxFlowProblem.labeling(fGraph);

        if (!isFeasible(fGraph)) {
            return -1;
        }

        graph.setNegCycle(true);
        Node n;
        graph.renumber();
        long start = System.nanoTime();
        while (graph.isNegCycle()) {
            n = cycleCancelingFifoLabelCorrecting(graph);

            if (graph.isNegCycle()) {
                flowNegCycle(n, graph);
                times++;
            }
        }
        long stop = System.nanoTime() - start;
        graph.times = times;
        return stop;
    }

    /**
     * Successive Shortest Path Algorithm
     *
     * @param graph
     * @return
     */
    public static long successiveShortestPath(Graph graph) {
        graph.resetFlows();
        graph.paths.clear();
        int times = 0;

        Graph fGraph = getGraphForFeasibleSolution(graph);
        MaxFlowProblem.labeling(fGraph);

        if (!isFeasible(fGraph)) {
            return -1;
        }
        fGraph.resetFlows();
        fGraph.setSourceResidualFlow();
        int flow = fGraph.sourceFlow;
        boolean negCyclePresent = false;

        long start = System.nanoTime();

        while (flow > 0 && !negCyclePresent) {
            times++;
            negCyclePresent = modifiedDequeueLabelCorrecting(fGraph);
            flow = fGraph.sourceFlow;
        }
        long stop = System.nanoTime() - start;
        graph.times = times;
        graph.paths.clear();

        graph.paths.addAll(fGraph.paths);
        return stop;
    }

    private static boolean isFeasible(Graph graph) {

        if (!graph.getSource().out.stream().noneMatch((a) -> (a.capacity - a.flow != 0))) {
            return false;
        }
        return graph.getSink().in.stream().noneMatch((a) -> (a.capacity - a.flow != 0));
    }

    private static Graph getGraphForFeasibleSolution(Graph graph) {
        Graph fGraph;
        ArrayList<Node> list = graph.getList();
        ArrayList<Node> ordered = new ArrayList<>();
        ArrayList<Arc> arcList = graph.getArcList();
        int b = 0;
        int neg = 0;

        for (Node n : list) {
            b += n.getValue();
            if (n.getValue() < 0) {
                neg += n.getValue();
            }
        }

        if (b != 0) {
            return null;
        }

        Node s = new Node(-neg);
        Node t = new Node(neg);
        Arc a = null;

        for (Node n : list) {
            if (n.getValue() > 0) {
                a = new Arc(0, s, n, n.getValue());
                s.out.add(a);
                n.in.add(a);
                arcList.add(a);

            } else if (n.getValue() < 0) {
                a = new Arc(0, n, t, -n.getValue());
                n.out.add(a);
                t.in.add(a);
                arcList.add(a);
            }
        }

        ordered.add(s);
        ordered.addAll(list);
        ordered.add(t);

        GraphMaker.number(ordered);

        fGraph = new Graph(ordered, arcList, s, t);

        return fGraph;
    }

    private static boolean modifiedDequeueLabelCorrecting(Graph graph) {
        graph.initialize();
        Node s = graph.getSource();
        int minDist = -graph.nodesNumber() * graph.getC();
        Node n;
        Node nCycle = graph.getSink();
        int dist = 0;
        LinkedList<Node> LIST = new LinkedList<>();
        LIST.add(s);

        while (!LIST.isEmpty()) {
            n = LIST.pollFirst();
            n.contained = false;

            for (Arc i : n.out) {
                dist = i.tail.distance + i.getCost();
                if (i.head.distance > dist && i.residualForwardCapacity > 0) {
                    i.head.distance = dist;
                    i.head.pred = i.tail;
                    i.head.predArc = i;
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

            for (Arc i : n.in) {
                dist = i.head.distance - i.getCost();
                if (i.tail.distance > dist && i.residualReverseCapacity > 0) {
                    i.tail.distance = dist;
                    i.tail.pred = i.head;
                    i.tail.predArc = i;
                    if (!i.tail.contained) {
                        if (i.tail.previously) {
                            LIST.addFirst(i.tail);
                        } else {
                            LIST.add(i.tail);
                        }
                        i.tail.previously = true;
                        i.tail.contained = true;
                    }
                }
            }
            if (dist < minDist) {
                LIST.clear();
                nCycle = n;
            }
        }

        n = nCycle;

        ArrayList<Arc> arcs = new ArrayList<>();
        int minResCap = Integer.MAX_VALUE;
        graph.previously();

        Path path = new Path();
        graph.paths.add(path);

        while (n.pred != null && !n.previously) {
            arcs.add(n.predArc);
            path.nodes.add(n);
            if (n.predArc.head.equals2(n)) {
                if (minResCap > n.predArc.residualForwardCapacity) {
                    minResCap = n.predArc.residualForwardCapacity;
                }
            } else {
                if (minResCap > n.predArc.residualReverseCapacity) {
                    minResCap = n.predArc.residualReverseCapacity;
                }
            }

            n.previously = true;
            n = n.pred;
            if (n.previously) {
                graph.setNegCycle(true);
                path.nodes.add(n);
                return true;
            }
        }

        n = nCycle;

        for (Arc a : arcs) {
            if (n.predArc.head.equals2(n)) {
                a.setFlow(a.flow + minResCap);
            } else {
                a.setFlow(a.flow - minResCap);
            }
            n=n.pred;
        }
        graph.sourceFlow -= minResCap;
        path.flow = minResCap;
        path.nodes.remove(path.nodes.get(0));
        return false;
    }

    private static Node cycleCancelingFifoLabelCorrecting(Graph graph) {
        graph.initialize();
        Node s = graph.getSource();
        int minDist = -graph.nodesNumber() * graph.getC();
        Node n;
        Node nCycle = graph.getSink();
        int dist = 0;
        LinkedList<Node> LIST = new LinkedList<>();
        LIST.add(s);

        while (!LIST.isEmpty()) {
            n = LIST.pollFirst();
            n.contained = false;

            for (Arc i : n.out) {
                dist = i.tail.distance + i.getCost();
                //System.out.println("Mindist="+minDist+" dist="+dist);
                if (i.head.distance > dist && i.residualForwardCapacity > 0) {
                    i.head.distance = dist;
                    i.head.pred = i.tail;
                    i.head.predArc = i;
                    if (!i.head.contained) {
                        LIST.add(i.head);
                        i.head.contained = true;
                    }
                }
            }

            if (n.distance < minDist) {
                LIST.clear();
                nCycle = n;
                graph.setNegCycle(true);
                return nCycle;
            }

            for (Arc i : n.in) {
                dist = i.head.distance - i.getCost();
                //System.out.println("Mindist="+minDist+" dist="+dist);
                if (i.tail.distance > dist && i.residualReverseCapacity > 0) {
                    i.tail.distance = dist;
                    i.tail.pred = i.head;
                    i.tail.predArc = i;
                    if (!i.tail.contained) {
                        LIST.add(i.tail);
                        i.tail.contained = true;
                    }
                }
            }
            if (n.distance < minDist) {
                LIST.clear();
                nCycle = n;
                graph.setNegCycle(true);
                return nCycle;
            }
        }
        
        graph.setNegCycle(false);
        return nCycle;
    }

    private static void flowNegCycle(Node nCycle, Graph graph) {
        Node n;
        n = nCycle;

        ArrayList<Arc> arcs = new ArrayList<>();
        int minResCap = Integer.MAX_VALUE;
        graph.previously();

        Path path = new Path();
        path.cycle = true;
        graph.paths.add(path);

        while (!n.previously) {
            //System.out.println(n.toString());
            n.previously = true;
            n = n.pred;
        }      
        graph.previously();
        
        while (!n.previously) {
            arcs.add(n.predArc);
            path.arcList.add(n.predArc);
            path.nodes.add(n);
            n.previously = true;
            n = n.pred;
        }  
        path.nodes.add(n);
        
        
        ArrayList<Node> nodes=path.getNodes();
        Iterator<Node> iterator = nodes.iterator();
        
        for(Arc a:path.getArcList()){
            //System.out.println(n.toString());
            //System.out.print(a.toMinCostFlow());
            n=iterator.next();
            if (a.tail.equals2(n)) {
                if (minResCap > a.residualForwardCapacity) {
                    minResCap = a.residualForwardCapacity;
                }
            } else {
                if (minResCap > a.residualReverseCapacity) {
                    minResCap = a.residualReverseCapacity;
                }
            }
        }

        //int cycleCost = 0;
        iterator=nodes.iterator();
        for (Arc a : path.getArcList()) {
            n=iterator.next();
            //System.out.println(n.toString());
            //System.out.print(a.toMinCostFlow());
            if (a.tail.equals2(n)) {
                a.setFlow(a.flow + minResCap);
                //cycleCost += a.cost;
            } else {
                a.setFlow(a.flow - minResCap);
                //cycleCost -= a.cost;
            }
            //System.out.print(a.toMinCostFlow());

        }
        //System.out.println("Cycle cost " + cycleCost);
        path.flow = minResCap;
    }
}
