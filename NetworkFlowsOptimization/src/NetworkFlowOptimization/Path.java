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
import java.util.HashSet;
import java.util.LinkedList;

/**
 *
 * @author antonello.meloni
 */
public class Path implements Cloneable {

    LinkedList<Node> nodes;
    ArrayList<Arc> arcList;
    ArrayList<Boolean> reverse;
    HashSet<Node> map;
    int minFlow;
    int sum;
    boolean cycle;

    public Path(Node s) {
        this.cycle = false;
        this.minFlow = Integer.MAX_VALUE;
        this.sum = 0;
        this.nodes = new LinkedList<>();
        nodes.add(s);
        this.arcList = new ArrayList<>();
        this.reverse = new ArrayList<>();
        this.map = new HashSet<>();
    }

    public Path() {

    }

    public int add(Arc arc, Node node) {
        if (!map.contains(node) && !cycle) {
            nodes.add(node);
            map.add(node);
            arcList.add(arc);
            if (arc.head.equals(node)) {
                reverse.add(Boolean.FALSE);
                sum += arc.cost;
                if (minFlow > arc.residualForwardCapacity) {
                    minFlow = arc.residualForwardCapacity;
                }
            } else {
                reverse.add(Boolean.TRUE);
                sum -= arc.cost;
                if (minFlow > arc.residualReverseCapacity) {
                    minFlow = arc.residualReverseCapacity;
                }
            }
        } else if (sum < 0) {
            cycle = true;
            return -1;
        } else {
            cycle = true;
            return 1;
        }
        return 0;
    }

    public void flow() {
        if (sum >= 0 || !cycle) {
            return;
        }
        int i = 0;
        for (Arc a : arcList) {
            if (!reverse.get(i)) {
                a.setFlow(a.flow + minFlow);
            } else {
                a.setFlow(a.flow - minFlow);
            }
            i++;
        }
        minFlow=0;
    }

    @Override
    public Path clone() {
        Path path = new Path();

        @SuppressWarnings("unchecked")
        LinkedList<Node> n = (LinkedList<Node>) this.nodes.clone();
        path.nodes = n;

        @SuppressWarnings("unchecked")
        ArrayList<Arc> a = (ArrayList<Arc>) this.arcList.clone();
        path.arcList = a;

        @SuppressWarnings("unchecked")
        ArrayList<Boolean> b = (ArrayList<Boolean>) this.reverse.clone();
        path.reverse = b;

        @SuppressWarnings("unchecked")
        HashSet<Node> m = (HashSet<Node>) this.map.clone();
        path.map = m;

        path.minFlow = this.minFlow;
        path.sum = this.sum;
        path.cycle = this.cycle;

        return path;
    }

}
