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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author anto
 */
public class NetworkFlowOptimization {

    private static JFrame mainFrame;
    private static JFrame childFrame;
    private static ImageIcon icon;
    private static JLabel logo;
    private static JButton sppButton;
    private static JButton mfpButton;
    private static JButton mcfButton;
    private static JPanel mainPane;
    static Graph graph = null;

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

        mainFrame = new JFrame("Network Flows Optimization");
        mainPane = new JPanel(new FlowLayout());

        mainFrame.setPreferredSize(new Dimension(350, 570));
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.getContentPane().add(mainPane, BorderLayout.CENTER);
        mainFrame.setResizable(false);

        URL percorso = NetworkFlowOptimization.class.getResource("Logo_UniCa_256.png");
        icon = new ImageIcon(percorso);
        logo = new JLabel();
        logo.setIcon(icon);

        sppButton = new JButton("Shortest Path Problems");
        sppButton.setPreferredSize(new Dimension(250, 75));

        mfpButton = new JButton("Max Flow Problems");
        mfpButton.setPreferredSize(new Dimension(250, 75));
        mcfButton = new JButton("Min Cost Flow Problems");
        mcfButton.setPreferredSize(new Dimension(250, 75));

        mainPane.add(logo);
        mainPane.add(sppButton);
        mainPane.add(mfpButton);
        mainPane.add(mcfButton);

        mainFrame.pack();
        mainFrame.setLocation(200, 100);
        mainFrame.setVisible(true);

        sppButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                childFrame = new JFrame("Network Flows Optimization");

                JPanel childPane = new ShortestPathProblems();
                childFrame.add(childPane);
                childFrame.setLocation(200, 100);
                childFrame.setVisible(true);
                childFrame.pack();

                childFrame.addWindowListener(new WindowListenerImpl());
            }
        });

        mfpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                childFrame = new JFrame("Network Flows Optimization");
                JPanel childPane = new MaxFlowProblems();
                childFrame.add(childPane);
                childFrame.setLocation(200, 100);
                childFrame.setVisible(true);
                childFrame.pack();
                childFrame.addWindowListener(new WindowListenerImpl());
            }
        });

        mcfButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                childFrame = new JFrame("Network Flows Optimization");
                JPanel childPane = new MinCostFlowProblems();
                childFrame.add(childPane);
                childFrame.setLocation(200, 100);
                childFrame.setVisible(true);
                childFrame.pack();
                childFrame.addWindowListener(new WindowListenerImpl());
            }
        });
    }

    static void disposeChildFrame() {
        childFrame.dispose();
        mainFrame.setVisible(true);
    }

    static class WindowListenerImpl implements WindowListener {

        public WindowListenerImpl() {
        }

        @Override
        public void windowOpened(WindowEvent e) {
            mainFrame.setVisible(false);
        }

        @Override
        public void windowClosing(WindowEvent e) {
            mainFrame.setVisible(true);
        }

        @Override
        public void windowClosed(WindowEvent e) {
            //FRAME.disposeChildFrame(true);
        }

        @Override
        public void windowIconified(WindowEvent e) {

        }

        @Override
        public void windowDeiconified(WindowEvent e) {

        }

        @Override
        public void windowActivated(WindowEvent e) {

        }

        @Override
        public void windowDeactivated(WindowEvent e) {

        }
    }

}
