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
package ShortestPathProblem;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author antonello.meloni
 */
public class CycleGraph {

    private CycleGraph() {
    }

    public static Graph cycleGraphMaker(int seed, int min, int max) {

        ArrayList<Node> list = new ArrayList<>();
        ArrayList<Arc> arcList = new ArrayList<>();
        
        for(int i=1;i<17;i++){
        list.add(new Node(i));
        }
        Random rand =new Random(seed);
                
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min,list.get(0),list.get(1)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min,list.get(0),list.get(3)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min,list.get(7),list.get(0)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min,list.get(7),list.get(8)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min,list.get(7),list.get(12)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min,list.get(14),list.get(7)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min,list.get(13),list.get(14)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min,list.get(13),list.get(9)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min,list.get(12),list.get(8)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min,list.get(5),list.get(7)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min,list.get(3),list.get(5)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min,list.get(2),list.get(4)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min,list.get(4),list.get(3)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min,list.get(4),list.get(1)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min,list.get(1),list.get(2)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min,list.get(9),list.get(1)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min,list.get(2),list.get(9)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min,list.get(9),list.get(5)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min,list.get(2),list.get(5)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min,list.get(8),list.get(6)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min,list.get(6),list.get(12)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min,list.get(6),list.get(9)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min,list.get(6),list.get(15)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min,list.get(6),list.get(10)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min,list.get(10),list.get(11)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min,list.get(10),list.get(15)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min,list.get(15),list.get(9)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min,list.get(15),list.get(11)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min,list.get(12),list.get(10)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min,list.get(5),list.get(6)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min,list.get(5),list.get(8)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min,list.get(11),list.get(12)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min,list.get(11),list.get(13)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min,list.get(12),list.get(13)));
        arcList.add(new Arc(rand.nextInt((max - min) + 1) + min,list.get(12),list.get(14)));
        
        Graph graph=new Graph(list,arcList);
        
        System.out.println("Creazione Grafo con costo archi random con valori tra "+min+" e "+max);
        System.out.println(list);
        System.out.println(arcList);
        System.out.println("Numero di nodi generati = " + list.size());
        System.out.println("Numero di archi generati = " + arcList.size() + "\n");
        return graph;
    }
}
