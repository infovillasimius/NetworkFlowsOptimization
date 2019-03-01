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
import java.util.HashMap;

/**
 *
 * @author antonello.meloni
 */
public class MinCostFlowProblem {

    public static void cycleFinder(Graph graph) {
        HashMap<Node, Path> map = new HashMap<>();
        Node s = graph.getSource();
        Path path = new Path(s);
        map.put(s, path);

    }

    public static Graph feasible(Graph graph) {
        Graph fGraph;
        ArrayList<Node> list = graph.getList();
        ArrayList<Arc> arcList = graph.getArcList();
        int b = 0;
        int neg = 0;
        int pos = 0;

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

        return null;
    }

}
