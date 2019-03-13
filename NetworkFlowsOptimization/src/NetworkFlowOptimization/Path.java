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
import java.util.Collections;
import java.util.HashSet;

/**
 * Path / Cycle class for Min Cost Flow Problems
 * @author antonello.meloni
 */
public class Path implements Cloneable {

    ArrayList<Node> nodes;
    ArrayList<Arc> arcList;
    ArrayList<Boolean> reverse;
    HashSet<Node> map;
    int minFlow;
    int sum;
    boolean cycle;
    int flow;

    /**
     * New path
     */
    public Path() {
        this.nodes = new ArrayList<>();
        this.arcList = new ArrayList<>();

    }

    /**
     * Get Nodes
     * @return ArrayList reverse ordered List of nodes
     */
    public ArrayList<Node> getNodes() {
        ArrayList<Node> getNodes = new ArrayList<>();
        getNodes.addAll(nodes);
        Collections.reverse(getNodes);
        return getNodes;
    }

    /**
     * Get arcs
     * @return ArrayList reverse ordered List of arcs
     */
    public ArrayList<Arc> getArcList() {
        ArrayList<Arc> getArcs = new ArrayList<>();
        getArcs.addAll(arcList);
        Collections.reverse(getArcs);
        return getArcs;
    }

    @Override
    @SuppressWarnings("unchecked")
    public String toString() {
        String result;

        if (cycle) {
            result = "Cycle: ( ";
        } else {
            result = "Path: ( ";
        }
        ArrayList<Node> ordered = (ArrayList<Node>) nodes.clone();
        Collections.reverse(ordered);
        for (Node n : ordered) {
            result = result.concat(n.number + " ");
        }
        result = result.concat(") - Flow: " + flow + "\n");
        return result;
    }

}
