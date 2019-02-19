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
 *
 * @author anto
 */
public class MaxFlowProblem {

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

}
