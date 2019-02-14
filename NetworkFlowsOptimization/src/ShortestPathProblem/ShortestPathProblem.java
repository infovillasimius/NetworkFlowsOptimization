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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

/**
 *
 * @author anto
 */
public class ShortestPathProblem {

    private static JFrame frame = new JFrame("Network Flows Optimization");
    private static JLabel label = new JLabel("Select the Shortest Path Problem: ");
    private static JTextPane text = new JTextPane();
    private static JScrollPane scroll = new JScrollPane(text);
    private static JButton b1 = new JButton("Calculate");
    private static String[] graphType = {"Personnel planning problem (Clark and Hastings [1977])", "Fixed graph with random arc costs", "Random graph"};
    @SuppressWarnings("unchecked")
    private static JComboBox graphList = new JComboBox(graphType);
    private static JPanel labelPane = new JPanel(new FlowLayout());
    private static JFormattedTextField nodes;
    private static JFormattedTextField arcPercent;
    private static JFormattedTextField min;
    private static JFormattedTextField max;
    private static JFormattedTextField seed;
    private static JCheckBox cycleCheck;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });

    }

    private static void createAndShowGUI() {
        //JFrame.setDefaultLookAndFeelDecorated(true);

        frame.setPreferredSize(new Dimension(640, 480));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().add(labelPane, BorderLayout.NORTH);
        
        frame.getContentPane().add(scroll);
        scroll.setPreferredSize(new Dimension(640, 400));
        labelPane.setPreferredSize(new Dimension(0, 100));
        labelPane.add(label);
        labelPane.add(graphList);
        labelPane.add(new JLabel(" Number of nodes "));
        nodes = new JFormattedTextField(100);
        nodes.setColumns(10);
        nodes.setHorizontalAlignment(JTextField.RIGHT);
        labelPane.add(nodes);
        labelPane.add(new JLabel(" % arcs (1 .. 100) "));
        arcPercent = new JFormattedTextField(25);
        arcPercent.setColumns(10);
        arcPercent.setHorizontalAlignment(JTextField.RIGHT);
        labelPane.add(arcPercent);

        labelPane.add(new JLabel(" min arc cost "));
        min = new JFormattedTextField(0);
        min.setColumns(10);
        min.setHorizontalAlignment(JTextField.RIGHT);
        labelPane.add(min);

        labelPane.add(new JLabel(" max arc cost "));
        max = new JFormattedTextField(100);
        max.setColumns(10);
        max.setHorizontalAlignment(JTextField.RIGHT);
        labelPane.add(max);

        labelPane.add(new JLabel(" Random seed "));
        seed = new JFormattedTextField(0);
        seed.setColumns(10);
        seed.setHorizontalAlignment(JTextField.RIGHT);
        labelPane.add(seed);

        cycleCheck = new JCheckBox("Cycles");
        labelPane.add(cycleCheck);
        

        labelPane.add(b1);

        //frame.getContentPane().add(graphList, BorderLayout.SOUTH);
        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                text.setText("Calculating graph...\n\n");
                calculate();
            }
        });

        graphList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int n = graphList.getSelectedIndex();
                switch (n) {
                    case 0:
                        min.setEnabled(false);
                        max.setEnabled(false);
                        seed.setEnabled(false);
                        cycleCheck.setEnabled(false);
                        nodes.setEnabled(false);
                        arcPercent.setEnabled(false);
                        break;
                    case 1:
                        min.setEnabled(true);
                        max.setEnabled(true);
                        seed.setEnabled(true);
                        cycleCheck.setEnabled(false);
                        nodes.setEnabled(false);
                        arcPercent.setEnabled(false);
                        break;
                    case 2:
                        min.setEnabled(true);
                        max.setEnabled(true);
                        seed.setEnabled(true);
                        cycleCheck.setEnabled(true);
                        nodes.setEnabled(true);
                        arcPercent.setEnabled(true);
                        break;
                }
            }
        });

        text.setText("");
        text.setEditable(false);
        min.setEnabled(false);
        max.setEnabled(false);
        seed.setEnabled(false);
        cycleCheck.setEnabled(false);
        nodes.setEnabled(false);
        arcPercent.setEnabled(false);

        frame.pack();
        frame.setVisible(true);
    }

    private static void calculate() {

        String result = "";
        Graph graph = null;

        int n = graphList.getSelectedIndex();
        @SuppressWarnings("unchecked")
        int min = ((Number) ShortestPathProblem.min.getValue()).intValue();
        @SuppressWarnings("unchecked")
        int max = ((Number) ShortestPathProblem.max.getValue()).intValue();
        @SuppressWarnings("unchecked")
        int seed = ((Number) ShortestPathProblem.seed.getValue()).intValue();
        @SuppressWarnings("unchecked")
        int nodes = ((Number) ShortestPathProblem.nodes.getValue()).intValue();
        @SuppressWarnings("unchecked")
        int arcPercent = ((Number) ShortestPathProblem.arcPercent.getValue()).intValue();
        boolean cycle = cycleCheck.isSelected();
        
        

        switch (n) {
            case 2:
                graph = GraphMaker.randomGraph(nodes, arcPercent, seed, min, max, cycle, result);
                break;
            case 1:
                graph = GraphMaker.cycleGraphMaker(seed, min, max);
                break;
            default:
                graph = GraphMaker.sppGraphMaker();
                break;
        }
        
        text.setText(text.getText().concat(graph.adjMatrix()));

        result = "Numero nodi = " + graph.nodesNumber() + " - " + "Numero archi = " + graph.arcsNumber() + "\n\n";
        text.setText(text.getText().concat(result));

        result = LabelAlgorithms.dijkstra(graph);
        
        result = LabelAlgorithms.heapDijkstra(graph);
        text.setText(text.getText().concat(result));
        result = LabelAlgorithms.dialDijkstra(graph);
        text.setText(text.getText().concat(result));
        result = LabelAlgorithms.RadixHeapDijkstra(graph);
        text.setText(text.getText().concat(result));
        result = LabelAlgorithms.dynamic(graph);
        text.setText(text.getText().concat(result));
        result = LabelAlgorithms.labelCorrecting(graph);
        text.setText(text.getText().concat(result));
        result = LabelAlgorithms.modifiedLabelCorrecting(graph);
        text.setText(text.getText().concat(result));
        result = LabelAlgorithms.dequeueLabelCorrecting(graph);
        text.setText(text.getText().concat(result));

    }

}
