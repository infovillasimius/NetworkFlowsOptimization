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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
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
public class NetworkFlowOptimization {

    private static final JFrame FRAME = new JFrame("Network Flows Optimization");
    private static final JLabel LABEL = new JLabel("Select the problem: ");
    private static final JTextPane TEXT = new JTextPane();
    private static final JScrollPane SCROLL = new JScrollPane(TEXT);
    private static final JButton BUTTON1 = new JButton("Calculate");
    private static final String[] GRAPHTYPES = {"Personnel planning problem (Clark and Hastings [1977])",
        "Fixed graph with random arc costs", "Random graph"};
    @SuppressWarnings("unchecked")
    private static final JComboBox GRAPHLIST = new JComboBox(GRAPHTYPES);

    private static final JPanel GRID0 = new JPanel(new GridLayout(0, 1));
    private static final JPanel GRID1 = new JPanel(new GridLayout(0, 1));
    private static final JPanel FLOW0 = new JPanel(new FlowLayout());
    private static final JPanel FLOW01 = new JPanel(new FlowLayout());
    private static final JPanel GRID2 = new JPanel(new GridLayout(1, 2));
    private static final JPanel LEFT_GRID = new JPanel(new GridLayout(0, 1));
    private static final JPanel FLOW3 = new JPanel(new FlowLayout());
    private static final JPanel FLOW31 = new JPanel(new FlowLayout());
    private static final JPanel FLOW32 = new JPanel(new FlowLayout());
    private static final JPanel RIGHT_GRID = new JPanel(new GridLayout(0, 1));
    private static final JPanel FLOW4 = new JPanel(new FlowLayout());
    private static final JPanel FLOW41 = new JPanel(new FlowLayout());
    private static final JPanel FLOW42 = new JPanel(new FlowLayout());
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

        FRAME.setPreferredSize(new Dimension(800, 600));
        FRAME.setResizable(false);
        FLOW0.setPreferredSize(new Dimension(800, 30));
        SCROLL.setPreferredSize(new Dimension(800, 440));
        LEFT_GRID.setPreferredSize(new Dimension(800, 200));
        FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FRAME.getContentPane().add(GRID0, BorderLayout.NORTH);
        FRAME.getContentPane().add(GRID1);

        GRID0.add(FLOW0);
        GRID0.add(FLOW01);
        GRID1.add(GRID2);
        GRID1.add(SCROLL);
        GRID2.add(LEFT_GRID);
        GRID2.add(RIGHT_GRID);

        FLOW0.add(LABEL);
        LABEL.setPreferredSize(new Dimension(200, 25));

        FLOW0.add(GRAPHLIST);
        GRAPHLIST.setPreferredSize(new Dimension(550, 25));

        adjMatrix = new JCheckBox("Adjacency Matrix");
        adjMatrix.setPreferredSize(new Dimension(250, 25));

        FLOW01.add(adjMatrix);
        arcCosts = new JCheckBox("List arc costs");
        arcCosts.setPreferredSize(new Dimension(250, 25));
        FLOW01.add(arcCosts);

        LEFT_GRID.add(FLOW3);
        FLOW3.add(new JLabel("Number of nodes"));
        FLOW3.getComponent(0).setPreferredSize(new Dimension(140, 15));
        nodes = new JFormattedTextField(20);
        nodes.setColumns(10);
        nodes.setHorizontalAlignment(JTextField.RIGHT);
        FLOW3.add(nodes);

        LEFT_GRID.add(FLOW4);
        FLOW4.add(new JLabel("% arcs (1 .. 100)"));
        FLOW4.getComponent(0).setPreferredSize(new Dimension(140, 15));
        arcPercent = new JFormattedTextField(10);
        arcPercent.setColumns(10);
        arcPercent.setHorizontalAlignment(JTextField.RIGHT);
        FLOW4.add(arcPercent);

        RIGHT_GRID.add(FLOW31);
        FLOW31.add(new JLabel("Minimum arc cost"));
        FLOW31.getComponent(0).setPreferredSize(new Dimension(140, 15));
        min = new JFormattedTextField(0);
        min.setColumns(10);
        min.setHorizontalAlignment(JTextField.RIGHT);
        FLOW31.add(min);

        RIGHT_GRID.add(FLOW41);
        FLOW41.add(new JLabel("Max arc cost "));
        FLOW41.getComponent(0).setPreferredSize(new Dimension(140, 15));
        max = new JFormattedTextField(100);
        max.setColumns(10);
        max.setHorizontalAlignment(JTextField.RIGHT);
        FLOW41.add(max);

        RIGHT_GRID.add(FLOW32);
        FLOW32.add(new JLabel("Random seed "));
        FLOW32.getComponent(0).setPreferredSize(new Dimension(140, 15));
        seed = new JFormattedTextField(0);
        seed.setColumns(10);
        seed.setHorizontalAlignment(JTextField.RIGHT);
        FLOW32.add(seed);

        LEFT_GRID.add(FLOW42);
        FLOW42.add(new JLabel("Graph with cycles"));
        FLOW42.getComponent(0).setPreferredSize(new Dimension(140, 15));
        cycleCheck = new JCheckBox();
        cycleCheck.setPreferredSize(new Dimension(120, 15));
        FLOW42.add(cycleCheck);

        LEFT_GRID.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        RIGHT_GRID.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        FLOW01.add(BUTTON1);
        BUTTON1.setPreferredSize(new Dimension(250, 25));
        FRAME.getContentPane().add(SCROLL, BorderLayout.SOUTH);
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
        int arcPercentLocal = ((Number) NetworkFlowOptimization.arcPercent.getValue()).intValue();
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