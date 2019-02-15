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

    private static final JFrame FRAME = new JFrame("Network Flows Optimization");
    private static final JLabel LABEL = new JLabel("Select the Shortest Path Problem: ");
    private static final JTextPane TEXT = new JTextPane();
    private static final JScrollPane SCROLL = new JScrollPane(TEXT);
    private static final JButton BUTTON1 = new JButton("Calculate");
    private static final String[] GRAPHTYPES = {"Personnel planning problem (Clark and Hastings [1977])", "Fixed graph with random arc costs", "Random graph"};
    @SuppressWarnings("unchecked")
    private static final JComboBox GRAPHLIST = new JComboBox(GRAPHTYPES);
    private static final JPanel FLOWPANE = new JPanel(new FlowLayout());
    private static JFormattedTextField nodes;
    private static JFormattedTextField arcPercent;
    private static JFormattedTextField min;
    private static JFormattedTextField max;
    private static JFormattedTextField seed;
    private static JCheckBox cycleCheck;
    private static JCheckBox adjMatrix;
    private static JCheckBox arcCosts;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });

    }

    private static void createAndShowGUI() {
        JFrame.setDefaultLookAndFeelDecorated(true);

        FRAME.setPreferredSize(new Dimension(820, 600));
        FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FRAME.setLocationRelativeTo(null);
        FRAME.getContentPane().add(FLOWPANE, BorderLayout.NORTH);

        FRAME.getContentPane().add(SCROLL);
        SCROLL.setPreferredSize(new Dimension(640, 400));
        FLOWPANE.setPreferredSize(new Dimension(0, 100));
        FLOWPANE.add(LABEL);
        FLOWPANE.add(GRAPHLIST);
        FLOWPANE.add(new JLabel(" Number of nodes "));
        nodes = new JFormattedTextField(20);
        nodes.setColumns(10);
        nodes.setHorizontalAlignment(JTextField.RIGHT);
        FLOWPANE.add(nodes);
        FLOWPANE.add(new JLabel(" % arcs (1 .. 100) "));
        arcPercent = new JFormattedTextField(10);
        arcPercent.setColumns(10);
        arcPercent.setHorizontalAlignment(JTextField.RIGHT);
        FLOWPANE.add(arcPercent);

        FLOWPANE.add(new JLabel(" min arc cost "));
        min = new JFormattedTextField(0);
        min.setColumns(10);
        min.setHorizontalAlignment(JTextField.RIGHT);
        FLOWPANE.add(min);

        FLOWPANE.add(new JLabel(" max arc cost "));
        max = new JFormattedTextField(100);
        max.setColumns(10);
        max.setHorizontalAlignment(JTextField.RIGHT);
        FLOWPANE.add(max);

        FLOWPANE.add(new JLabel(" Random seed "));
        seed = new JFormattedTextField(0);
        seed.setColumns(10);
        seed.setHorizontalAlignment(JTextField.RIGHT);
        FLOWPANE.add(seed);

        cycleCheck = new JCheckBox("Cycles");
        FLOWPANE.add(cycleCheck);

        adjMatrix = new JCheckBox("Adjacency Matrix");
        FLOWPANE.add(adjMatrix);
        arcCosts = new JCheckBox("List arc costs");
        FLOWPANE.add(arcCosts);

        FLOWPANE.add(BUTTON1);

        //frame.getContentPane().add(GRAPHLIST, BorderLayout.SOUTH);
        BUTTON1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TEXT.setText("Calculating graph...\n\n");
                calculate();
                adjMatrix.setSelected(false);
                arcCosts.setSelected(false);
            }
        });

        GRAPHLIST.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int n = GRAPHLIST.getSelectedIndex();
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

        TEXT.setText("");
        TEXT.setEditable(false);
        min.setEnabled(false);
        max.setEnabled(false);
        seed.setEnabled(false);
        cycleCheck.setEnabled(false);
        nodes.setEnabled(false);
        arcPercent.setEnabled(false);

        FRAME.pack();
        FRAME.setLocation(100, 100);
        FRAME.setVisible(true);
    }

    private static void calculate() {

        String result = "";
        Graph graph;

        int n = GRAPHLIST.getSelectedIndex();
        @SuppressWarnings("unchecked")
        int minLocal = ((Number) min.getValue()).intValue();
        @SuppressWarnings("unchecked")
        int maxLocal = ((Number) max.getValue()).intValue();
        @SuppressWarnings("unchecked")
        int seedLocal = ((Number) seed.getValue()).intValue();
        @SuppressWarnings("unchecked")
        int nodesLocal = ((Number) nodes.getValue()).intValue();
        @SuppressWarnings("unchecked")
        int arcPercentLocal = ((Number) ShortestPathProblem.arcPercent.getValue()).intValue();
        boolean cycle = cycleCheck.isSelected();

        switch (n) {
            case 2:
                graph = GraphMaker.randomGraph(nodesLocal, arcPercentLocal, seedLocal, minLocal, maxLocal, cycle, result);
                break;
            case 1:
                graph = GraphMaker.cycleGraphMaker(seedLocal, minLocal, maxLocal);
                break;
            default:
                graph = GraphMaker.sppGraphMaker();
                break;
        }

        if (adjMatrix.isSelected()) {
            TEXT.setText(TEXT.getText().concat(graph.adjMatrix()));
        }
        if (arcCosts.isSelected()) {
            TEXT.setText(TEXT.getText().concat(graph.arcCosts()));
        }

        result = "Number of nodes = " + graph.nodesNumber() + " - " + "Number of arcs = " + graph.arcsNumber() + "\n\n";
        TEXT.setText(TEXT.getText().concat(result));

        result = LabelAlgorithms.dijkstra(graph);
        TEXT.setText(TEXT.getText().concat(result));
        result = LabelAlgorithms.heapDijkstra(graph);
        TEXT.setText(TEXT.getText().concat(result));
        result = LabelAlgorithms.dialDijkstra(graph);
        TEXT.setText(TEXT.getText().concat(result));
        result = LabelAlgorithms.RadixHeapDijkstra(graph);
        TEXT.setText(TEXT.getText().concat(result));
        result = LabelAlgorithms.dynamic(graph);
        TEXT.setText(TEXT.getText().concat(result));
        result = LabelAlgorithms.labelCorrecting(graph);
        TEXT.setText(TEXT.getText().concat(result));
        result = LabelAlgorithms.modifiedLabelCorrecting(graph);
        TEXT.setText(TEXT.getText().concat(result));
        result = LabelAlgorithms.dequeueLabelCorrecting(graph);
        TEXT.setText(TEXT.getText().concat(result));

    }

}
