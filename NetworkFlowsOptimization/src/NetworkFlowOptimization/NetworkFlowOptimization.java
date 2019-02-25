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
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
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
    private static ImageIcon icon;
    private static final JLabel IMAGE = new JLabel();
    private static final JButton SPP_BUTTON = new JButton("Shortest Path Problems");
    private static final JButton MFP_BUTTON = new JButton("Max Flow Problems");
    private static final JButton BACK_SPP_BUTTON = new JButton("Back");
    private static final JButton BACK_MFP_BUTTON = new JButton("Back");

    private static final JPanel MENU = new JPanel(new FlowLayout());
    private static final JPanel SPP = new JPanel(new BorderLayout());
    private static final JPanel MFP = new JPanel(new BorderLayout());

    private static final JLabel SPP_LABEL = new JLabel("Select the Shortest Path problem: ");
    private static final JLabel MFP_LABEL = new JLabel("Select a Graph: ");
    private static final JTextPane SPP_TEXT = new JTextPane();
    private static final JTextPane MFP_TEXT = new JTextPane();
    private static final JScrollPane SPP_SCROLL = new JScrollPane(SPP_TEXT);
    private static final JScrollPane MFP_SCROLL = new JScrollPane(MFP_TEXT);
    private static final JButton SPP_CALCULATE = new JButton("Calculate");
    private static final JButton MFP_CALCULATE = new JButton("Calculate");
    private static final String[] SPP_GRAPHTYPES = {"Personnel planning problem (Clark and Hastings [1977])",
        "Fixed graph with random arc costs", "Random graph", "Load graph"};
    @SuppressWarnings("unchecked")
    private static final JComboBox SPP_GRAPHLIST = new JComboBox(SPP_GRAPHTYPES);

    private static final String[] MFP_GRAPHTYPES = {"Example graph", "Random graph", "Load graph"};
    @SuppressWarnings("unchecked")
    private static final JComboBox MFP_GRAPHLIST = new JComboBox(MFP_GRAPHTYPES);

    private static final JPanel SPP_GRID_PAGE_START = new JPanel(new GridLayout(0, 1));
    private static final JPanel MFP_GRID_PAGE_START = new JPanel(new GridLayout(0, 1));
    private static final JPanel SPP_FLOW_PAGE_END = new JPanel(new FlowLayout());
    private static final JPanel MFP_FLOW_PAGE_END = new JPanel(new FlowLayout());
    private static final JPanel SPP_UP_FLOW0 = new JPanel(new FlowLayout());
    private static final JPanel MFP_UP_FLOW0 = new JPanel(new FlowLayout());
    private static final JPanel SPP_UP_FLOW1 = new JPanel(new FlowLayout());
    private static final JPanel MFP_UP_FLOW1 = new JPanel(new FlowLayout());
    private static final JPanel SPP_GRID_PAGE_END = new JPanel(new GridLayout(1, 2));
    private static final JPanel MFP_GRID_PAGE_END = new JPanel(new GridLayout(1, 2));
    private static final JPanel SPP_LEFT_GRID = new JPanel(new GridLayout(0, 1));
    private static final JPanel MFP_LEFT_GRID = new JPanel(new GridLayout(0, 1));
    private static final JPanel SPP_LEFT_FLOW0 = new JPanel(new FlowLayout());
    private static final JPanel SPP_RIGHT_FLOW0 = new JPanel(new FlowLayout());
    private static final JPanel SPP_RIGHT_FLOW2 = new JPanel(new FlowLayout());
    private static final JPanel MFP_LEFT_FLOW0 = new JPanel(new FlowLayout());
    private static final JPanel MFP_LEFT_FLOW1 = new JPanel(new FlowLayout());
    private static final JPanel MFP_LEFT_FLOW2 = new JPanel(new FlowLayout());
    private static final JPanel SPP_RIGHT_GRID = new JPanel(new GridLayout(0, 1));
    private static final JPanel MFP_RIGHT_GRID = new JPanel(new GridLayout(0, 1));
    private static final JPanel SPP_LEFT_FLOW1 = new JPanel(new FlowLayout());
    private static final JPanel SPP_RIGHT_FLOW1 = new JPanel(new FlowLayout());
    private static final JPanel SPP_LEFT_FLOW2 = new JPanel(new FlowLayout());

    private static final JPanel MFP_RIGHT_FLOW0 = new JPanel(new FlowLayout());
    private static final JPanel MFP_RIGHT_FLOW1 = new JPanel(new FlowLayout());
    private static final JPanel MFP_RIGHT_FLOW2 = new JPanel(new FlowLayout());
    private static JFormattedTextField sppNodes;
    private static JFormattedTextField sppArcPercent;
    private static JFormattedTextField sppMinArcCost;
    private static JFormattedTextField sppMaxArcCost;
    private static JFormattedTextField sppSeed;
    private static JCheckBox sppCycleCheck;
    private static JCheckBox sppAdjMatrix;
    private static JCheckBox sppArcCosts;

    private static JFormattedTextField mfpNodes;
    private static JFormattedTextField mfpArcPercent;
    private static JFormattedTextField mfpMaxArcCapacity;
    private static JFormattedTextField mfpSeed;
    private static JCheckBox mfpCycleCheck;
    private static JCheckBox mfpAllArcs;
    private static JCheckBox mfpAdjMatrix;
    private static JCheckBox mfpArcFlows;

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

        FRAME.setPreferredSize(new Dimension(350, 520));
        FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FRAME.getContentPane().add(MENU, BorderLayout.CENTER);
        FRAME.setResizable(false);

        SPP_BUTTON.setPreferredSize(new Dimension(250, 100));
        MFP_BUTTON.setPreferredSize(new Dimension(250, 100));

        URL percorso = NetworkFlowOptimization.class.getResource("Logo_UniCa2.png");
        icon = new ImageIcon(percorso);

        IMAGE.setIcon(icon);
        MENU.add(IMAGE);
        MENU.add(SPP_BUTTON);
        MENU.add(MFP_BUTTON);

        BACK_SPP_BUTTON.setPreferredSize(new Dimension(75, 25));
        BACK_MFP_BUTTON.setPreferredSize(new Dimension(75, 25));

        // SPP part definition
        SPP.add(SPP_GRID_PAGE_START, BorderLayout.PAGE_START);
        SPP.add(SPP_SCROLL, BorderLayout.CENTER);
        SPP.add(SPP_FLOW_PAGE_END, BorderLayout.PAGE_END);

        SPP_GRID_PAGE_START.add(SPP_UP_FLOW0);
        SPP_GRID_PAGE_START.add(SPP_UP_FLOW1);
        SPP_FLOW_PAGE_END.add(SPP_GRID_PAGE_END);
        SPP_GRID_PAGE_END.add(SPP_LEFT_GRID);
        SPP_GRID_PAGE_END.add(SPP_RIGHT_GRID);

        SPP_UP_FLOW0.add(SPP_LABEL);
        SPP_LABEL.setPreferredSize(new Dimension(200, 25));
        SPP_UP_FLOW0.add(SPP_GRAPHLIST);
        SPP_GRAPHLIST.setPreferredSize(new Dimension(350, 25));

        sppAdjMatrix = new JCheckBox("Adjacency Matrix");
        sppAdjMatrix.setPreferredSize(new Dimension(150, 25));
        SPP_UP_FLOW1.add(sppAdjMatrix);

        sppArcCosts = new JCheckBox("Arc costs list");
        sppArcCosts.setPreferredSize(new Dimension(150, 25));
        SPP_UP_FLOW1.add(sppArcCosts);

        SPP_LEFT_GRID.add(SPP_LEFT_FLOW0);
        SPP_LEFT_FLOW0.add(new JLabel("Number of nodes"));
        SPP_LEFT_FLOW0.getComponent(0).setPreferredSize(new Dimension(140, 15));
        sppNodes = new JFormattedTextField(20);
        sppNodes.setColumns(10);
        sppNodes.setHorizontalAlignment(JTextField.RIGHT);
        SPP_LEFT_FLOW0.add(sppNodes);

        SPP_LEFT_GRID.add(SPP_LEFT_FLOW1);
        SPP_LEFT_FLOW1.add(new JLabel("% arcs (1 .. 100)"));
        SPP_LEFT_FLOW1.getComponent(0).setPreferredSize(new Dimension(140, 15));
        sppArcPercent = new JFormattedTextField(10);
        sppArcPercent.setColumns(10);
        sppArcPercent.setHorizontalAlignment(JTextField.RIGHT);
        SPP_LEFT_FLOW1.add(sppArcPercent);

        SPP_RIGHT_GRID.add(SPP_RIGHT_FLOW0);
        SPP_RIGHT_FLOW0.add(new JLabel("Minimum arc cost"));
        SPP_RIGHT_FLOW0.getComponent(0).setPreferredSize(new Dimension(140, 15));
        sppMinArcCost = new JFormattedTextField(0);
        sppMinArcCost.setColumns(10);
        sppMinArcCost.setHorizontalAlignment(JTextField.RIGHT);
        SPP_RIGHT_FLOW0.add(sppMinArcCost);

        SPP_RIGHT_GRID.add(SPP_RIGHT_FLOW1);
        SPP_RIGHT_FLOW1.add(new JLabel("Max arc cost "));
        SPP_RIGHT_FLOW1.getComponent(0).setPreferredSize(new Dimension(140, 15));
        sppMaxArcCost = new JFormattedTextField(100);
        sppMaxArcCost.setColumns(10);
        sppMaxArcCost.setHorizontalAlignment(JTextField.RIGHT);
        SPP_RIGHT_FLOW1.add(sppMaxArcCost);

        SPP_RIGHT_GRID.add(SPP_RIGHT_FLOW2);
        SPP_RIGHT_FLOW2.add(new JLabel("Random seed "));
        SPP_RIGHT_FLOW2.getComponent(0).setPreferredSize(new Dimension(140, 15));
        sppSeed = new JFormattedTextField(0);
        sppSeed.setColumns(10);
        sppSeed.setHorizontalAlignment(JTextField.RIGHT);
        SPP_RIGHT_FLOW2.add(sppSeed);

        SPP_LEFT_GRID.add(SPP_LEFT_FLOW2);
        sppCycleCheck = new JCheckBox("Graph with cycles");
        sppCycleCheck.setPreferredSize(new Dimension(260, 15));
        SPP_LEFT_FLOW2.add(sppCycleCheck);

        SPP_LEFT_GRID.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        SPP_RIGHT_GRID.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        SPP_UP_FLOW1.add(SPP_CALCULATE);
        SPP_CALCULATE.setPreferredSize(new Dimension(165, 25));
        SPP_UP_FLOW1.add(BACK_SPP_BUTTON);                                                  // end SPP part definition

        MFP.add(MFP_GRID_PAGE_START, BorderLayout.PAGE_START);
        MFP.add(MFP_SCROLL, BorderLayout.CENTER);
        MFP.add(MFP_FLOW_PAGE_END, BorderLayout.PAGE_END);
        MFP_FLOW_PAGE_END.add(MFP_GRID_PAGE_END);

        MFP_GRID_PAGE_START.add(MFP_UP_FLOW0);
        MFP_GRID_PAGE_START.add(MFP_UP_FLOW1);

        mfpAdjMatrix = new JCheckBox("Adjacency Matrix");
        mfpAdjMatrix.setPreferredSize(new Dimension(150, 25));
        MFP_UP_FLOW1.add(mfpAdjMatrix);

        mfpAllArcs = new JCheckBox("Arc List");
        mfpAllArcs.setPreferredSize(new Dimension(150, 25));
        MFP_UP_FLOW1.add(mfpAllArcs);

        MFP_UP_FLOW1.add(MFP_CALCULATE);
        MFP_UP_FLOW1.add(BACK_MFP_BUTTON);
        MFP_CALCULATE.setPreferredSize(new Dimension(165, 25));
        MFP_UP_FLOW0.add(MFP_LABEL);
        MFP_LABEL.setPreferredSize(new Dimension(200, 25));
        MFP_UP_FLOW0.add(MFP_GRAPHLIST);
        MFP_GRAPHLIST.setPreferredSize(new Dimension(350, 25));

        MFP_GRID_PAGE_END.add(MFP_LEFT_GRID);
        MFP_GRID_PAGE_END.add(MFP_RIGHT_GRID);

        MFP_LEFT_GRID.add(MFP_LEFT_FLOW0);
        MFP_LEFT_FLOW0.add(new JLabel("Number of nodes"));
        MFP_LEFT_FLOW0.getComponent(0).setPreferredSize(new Dimension(140, 15));
        mfpNodes = new JFormattedTextField(20);
        mfpNodes.setColumns(10);
        mfpNodes.setHorizontalAlignment(JTextField.RIGHT);
        MFP_LEFT_FLOW0.add(mfpNodes);

        MFP_LEFT_GRID.add(MFP_LEFT_FLOW1);
        MFP_LEFT_FLOW1.add(new JLabel("% arcs (1 .. 100)"));
        MFP_LEFT_FLOW1.getComponent(0).setPreferredSize(new Dimension(140, 15));
        mfpArcPercent = new JFormattedTextField(10);
        mfpArcPercent.setColumns(10);
        mfpArcPercent.setHorizontalAlignment(JTextField.RIGHT);
        MFP_LEFT_FLOW1.add(mfpArcPercent);

        MFP_LEFT_GRID.add(MFP_LEFT_FLOW2);
        mfpCycleCheck = new JCheckBox("Graph with cycles");
        mfpCycleCheck.setPreferredSize(new Dimension(260, 15));
        MFP_LEFT_FLOW2.add(mfpCycleCheck);

        MFP_RIGHT_GRID.add(MFP_RIGHT_FLOW0);
        MFP_RIGHT_FLOW0.add(new JLabel("Max arc capacity"));
        MFP_RIGHT_FLOW0.getComponent(0).setPreferredSize(new Dimension(140, 15));
        mfpMaxArcCapacity = new JFormattedTextField(10);
        mfpMaxArcCapacity.setColumns(10);
        mfpMaxArcCapacity.setHorizontalAlignment(JTextField.RIGHT);
        MFP_RIGHT_FLOW0.add(mfpMaxArcCapacity);

        MFP_RIGHT_GRID.add(MFP_RIGHT_FLOW1);
        MFP_RIGHT_FLOW1.add(new JLabel("Random seed "));
        MFP_RIGHT_FLOW1.getComponent(0).setPreferredSize(new Dimension(140, 15));
        mfpSeed = new JFormattedTextField(0);
        mfpSeed.setColumns(10);
        mfpSeed.setHorizontalAlignment(JTextField.RIGHT);
        MFP_RIGHT_FLOW1.add(mfpSeed);

        MFP_RIGHT_GRID.add(MFP_RIGHT_FLOW2);
        mfpArcFlows = new JCheckBox("List of flow arcs");
        mfpArcFlows.setPreferredSize(new Dimension(260, 15));
        MFP_RIGHT_FLOW2.add(mfpArcFlows);

        MFP_LEFT_GRID.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        MFP_RIGHT_GRID.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        SPP_CALCULATE.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SPP_TEXT.setText("Calculating graph...\n\n");
                calculate();

            }
        });

        MFP_CALCULATE.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SPP_TEXT.setText("Calculating graph...\n\n");
                mfp();
            }
        });

        SPP_BUTTON.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MENU.setVisible(false);
                SPP.setVisible(true);
                SPP_TEXT.setText("");
                FRAME.setTitle("Network Flows Optimization - Shortest Path Problems");
                FRAME.getContentPane().remove(MENU);
                FRAME.getContentPane().add(SPP, BorderLayout.CENTER);
                FRAME.setPreferredSize(new Dimension(800, 600));
                FRAME.setResizable(true);
                FRAME.pack();
            }
        });

        MFP_BUTTON.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MENU.setVisible(false);
                MFP.setVisible(true);
                MFP_TEXT.setText("");
                FRAME.setTitle("Network Flows Optimization - Max Flow Problems");
                FRAME.getContentPane().remove(MENU);
                FRAME.getContentPane().add(MFP, BorderLayout.CENTER);
                FRAME.setPreferredSize(new Dimension(800, 600));
                FRAME.setResizable(true);
                FRAME.pack();
            }
        });

        BACK_SPP_BUTTON.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SPP.setVisible(false);
                MENU.setVisible(true);
                FRAME.setTitle("Network Flows Optimization");
                FRAME.getContentPane().remove(SPP);
                FRAME.getContentPane().add(MENU, BorderLayout.CENTER);
                FRAME.setPreferredSize(new Dimension(350, 520));
                FRAME.setResizable(false);
                FRAME.pack();
            }
        });

        BACK_MFP_BUTTON.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MFP.setVisible(false);
                MENU.setVisible(true);
                FRAME.setTitle("Network Flows Optimization");
                FRAME.getContentPane().remove(MFP);
                FRAME.getContentPane().add(MENU, BorderLayout.CENTER);
                FRAME.setPreferredSize(new Dimension(350, 520));
                FRAME.setResizable(false);
                FRAME.pack();
            }
        });

        SPP_GRAPHLIST.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int n = SPP_GRAPHLIST.getSelectedIndex();
                switch (n) {
                    case 0:
                        sppMinArcCost.setEnabled(false);
                        sppMaxArcCost.setEnabled(false);
                        sppSeed.setEnabled(false);
                        sppCycleCheck.setEnabled(false);
                        sppNodes.setEnabled(false);
                        sppArcPercent.setEnabled(false);
                        break;
                    case 1:
                        sppMinArcCost.setEnabled(true);
                        sppMaxArcCost.setEnabled(true);
                        sppSeed.setEnabled(true);
                        sppCycleCheck.setEnabled(false);
                        sppNodes.setEnabled(false);
                        sppArcPercent.setEnabled(false);
                        break;
                    case 2:
                        sppMinArcCost.setEnabled(true);
                        sppMaxArcCost.setEnabled(true);
                        sppSeed.setEnabled(true);
                        sppCycleCheck.setEnabled(true);
                        sppNodes.setEnabled(true);
                        sppArcPercent.setEnabled(true);
                        break;
                }
            }
        });

        MFP_GRAPHLIST.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int n = MFP_GRAPHLIST.getSelectedIndex();
                switch (n) {
                    case 0:
                        mfpMaxArcCapacity.setEnabled(false);
                        mfpSeed.setEnabled(false);
                        mfpCycleCheck.setEnabled(false);
                        mfpNodes.setEnabled(false);
                        mfpArcPercent.setEnabled(false);
                        break;
                    case 1:
                        mfpMaxArcCapacity.setEnabled(true);
                        mfpSeed.setEnabled(true);
                        mfpCycleCheck.setEnabled(true);
                        mfpNodes.setEnabled(true);
                        mfpArcPercent.setEnabled(true);
                        break;
                    case 2:
                        mfpMaxArcCapacity.setEnabled(true);
                        mfpSeed.setEnabled(true);
                        mfpCycleCheck.setEnabled(true);
                        mfpNodes.setEnabled(true);
                        mfpArcPercent.setEnabled(true);
                        break;
                }
            }
        });

        SPP_TEXT.setText("");
        SPP_TEXT.setEditable(false);
        sppMinArcCost.setEnabled(false);
        sppMaxArcCost.setEnabled(false);
        sppSeed.setEnabled(false);
        sppCycleCheck.setEnabled(false);
        sppNodes.setEnabled(false);
        sppArcPercent.setEnabled(false);

        MFP_TEXT.setText("");
        MFP_TEXT.setEditable(false);

        mfpSeed.setEnabled(false);
        mfpCycleCheck.setEnabled(false);
        mfpNodes.setEnabled(false);
        mfpArcPercent.setEnabled(false);
        mfpMaxArcCapacity.setEnabled(false);

        FRAME.pack();
        FRAME.setLocation(200, 100);
        FRAME.setVisible(true);

    }

    private static void calculate() {

        String result = "";
        Graph graph = null;

        int n = SPP_GRAPHLIST.getSelectedIndex();
        @SuppressWarnings("unchecked")
        int minLocal = ((Number) sppMinArcCost.getValue()).intValue();
        @SuppressWarnings("unchecked")
        int maxLocal = ((Number) sppMaxArcCost.getValue()).intValue();
        @SuppressWarnings("unchecked")
        int seedLocal = ((Number) sppSeed.getValue()).intValue();
        @SuppressWarnings("unchecked")
        int nodesLocal = ((Number) sppNodes.getValue()).intValue();
        @SuppressWarnings("unchecked")
        int arcPercentLocal = ((Number) NetworkFlowOptimization.sppArcPercent.getValue()).intValue();
        boolean cycle = sppCycleCheck.isSelected();

        switch (n) {
            case 3: {
                try {
                    graph = GraphMaker.loadGraph();
                    if (graph == null) {
                        return;
                    }
                } catch (IOException ex) {
                    Logger.getLogger(NetworkFlowOptimization.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
            break;

            case 2:
                graph = GraphMaker.randomGraph(nodesLocal, arcPercentLocal, seedLocal, minLocal, maxLocal, 5, cycle, result);
                break;
            case 1:
                graph = GraphMaker.cycleGraphMaker(seedLocal, minLocal, maxLocal);
                break;
            default:
                graph = GraphMaker.sppGraphMaker();
                break;
        }

        if (sppAdjMatrix.isSelected()) {
            SPP_TEXT.setText(SPP_TEXT.getText().concat(graph.adjMatrix()));
        }
        if (sppArcCosts.isSelected()) {
            SPP_TEXT.setText(SPP_TEXT.getText().concat(graph.arcCosts()));
        }

        result = "Number of nodes = " + graph.nodesNumber() + " - " + "Number of arcs = " + graph.arcsNumber() + "\n\n";
        SPP_TEXT.setText(SPP_TEXT.getText().concat(result));

        result = ShortestPathProblem.dijkstra(graph);
        SPP_TEXT.setText(SPP_TEXT.getText().concat(result));
        result = ShortestPathProblem.heapDijkstra(graph);
        SPP_TEXT.setText(SPP_TEXT.getText().concat(result));
        result = ShortestPathProblem.dialDijkstra(graph);
        SPP_TEXT.setText(SPP_TEXT.getText().concat(result));
        result = ShortestPathProblem.RadixHeapDijkstra(graph);
        SPP_TEXT.setText(SPP_TEXT.getText().concat(result));
        result = ShortestPathProblem.dynamic(graph);
        SPP_TEXT.setText(SPP_TEXT.getText().concat(result));
        result = ShortestPathProblem.labelCorrecting(graph);
        SPP_TEXT.setText(SPP_TEXT.getText().concat(result));
        result = ShortestPathProblem.modifiedLabelCorrecting(graph);
        SPP_TEXT.setText(SPP_TEXT.getText().concat(result));
        result = ShortestPathProblem.dequeueLabelCorrecting(graph);
        SPP_TEXT.setText(SPP_TEXT.getText().concat(result));

    }

    private static void mfp() {
        MFP_TEXT.setText("");
        int n = MFP_GRAPHLIST.getSelectedIndex();
        @SuppressWarnings("unchecked")
        int maxCap = ((Number) mfpMaxArcCapacity.getValue()).intValue();
        if (maxCap <= 0) {
            maxCap = 1;
        }

        @SuppressWarnings("unchecked")
        int seedLocal = ((Number) mfpSeed.getValue()).intValue();
        @SuppressWarnings("unchecked")
        int nodesLocal = ((Number) mfpNodes.getValue()).intValue();
        @SuppressWarnings("unchecked")
        int arcPercentLocal = ((Number) NetworkFlowOptimization.mfpArcPercent.getValue()).intValue();
        boolean cycle = mfpCycleCheck.isSelected();
        Graph graph = null;
        String result = "";
        switch (n) {
            case 2: {
                try {
                    graph = GraphMaker.loadGraph();
                    if (graph == null) {
                        return;
                    }
                } catch (IOException ex) {
                    Logger.getLogger(NetworkFlowOptimization.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
            break;
            case 1:
                graph = GraphMaker.randomGraph(nodesLocal, arcPercentLocal, seedLocal, 0, 1, maxCap, cycle, result);
                break;
            default:
                graph = GraphMaker.cycleGraphMaker(seedLocal, 0, 10);//GraphMaker.cycleGraphMaker(seedLocal, minLocal, maxLocal);
                break;
        }

        if (mfpAdjMatrix.isSelected()) {
            MFP_TEXT.setText(MFP_TEXT.getText().concat(graph.adjMatrix()));
        }
        if (mfpAllArcs.isSelected()) {
            MFP_TEXT.setText(MFP_TEXT.getText().concat(graph.maxFlowAllArcs()));
        }

        result = "Number of nodes = " + graph.nodesNumber() + " - " + "Number of arcs = " + graph.arcsNumber() + "\n\n";
        long stop = MaxFlowProblem.labeling(graph);
        result = result.concat("LABELING ALGORITHM\nExecution time = " + (double) stop / 1000000 + " milliseconds\n");

        if (mfpArcFlows.isSelected()) {
            result = result.concat(graph.maxFlowArcs());
        } else {
            result = result.concat(graph.maxFlow());
        }

        MFP_TEXT.setText(MFP_TEXT.getText().concat(result));

        stop = MaxFlowProblem.preflowPush(graph);

        result = "\nPREFLOW-PUSH ALGORITHM\nExecution time = " + (double) stop / 1000000 + " milliseconds\n";

        if (mfpArcFlows.isSelected()) {
            result = result.concat(graph.maxFlowArcs());
        } else {
            result = result.concat(graph.maxFlow());
        }

        MFP_TEXT.setText(MFP_TEXT.getText().concat(result));

    }

}
