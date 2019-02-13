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
package ShortestPathProblem;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JFrame;

/**
 *
 * @author anto
 */
public class ShortestPathProblem {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /*
        JFrame frame = new JFrame("FrameDemo");                                 //1. Create the frame.
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);                   //2. Optional: What happens when the frame closes?
        //Component emptyLabel = null;                                          //...create emptyLabel...
        // frame.getContentPane().add(emptyLabel, BorderLayout.CENTER);         //3. Create components and put them in the frame.
        frame.pack();                                                           //4. Size the frame.
        frame.setVisible(true);                                                 //5. Show it.
         */
 

        Graph graph = GraphMaker.randomGraph(1000, 1, 100, 5, 100000, true);
        
        //  Graph graph = GraphMaker.sppGraphMaker();
        
        LabelAlgorithms.dijkstra(graph);
        LabelAlgorithms.heapDijkstra(graph);
        LabelAlgorithms.dialDijkstra(graph);
        LabelAlgorithms.RadixHeapDijkstra(graph);
        LabelAlgorithms.dynamic(graph);
        LabelAlgorithms.labelCorrecting(graph);
        LabelAlgorithms.modifiedLabelCorrecting(graph);
        LabelAlgorithms.dequeueLabelCorrecting(graph);

    }




}
