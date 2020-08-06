/*
* File: P4GUI.java
* Author: John Kucera
* Date: March 3, 2020
* Purpose: This Java program creates a GUI for behaving like a Java command line
* compiler. The user inputs a the name of a text file that contains class 
* dependency information to be read in. A directed graph of those classes is 
* created. Then, the user inputs a class name from the text file to recompile, 
* and the GUI displays the recompilation order of classes in topological order.
*/

// import of necessary java classes
import java.io.IOException;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// P4GUI Class
public class P4GUI extends JFrame {
    // Instance Variables + Directed Graph initialization
    private String file;
    private String classToRe;
    private DGraph<String> dGraph;
    
    // Window Components
    private static JLabel fileLbl = new JLabel("Input file name: ");
    private static JLabel classToReLbl = new JLabel("Class to recompile:");
    private static JTextField fileTxt = new JTextField(null, 20);
    private static JTextField classToReTxt = new JTextField(null, 20);
    private static JButton buildBtn = new JButton("Build Directed Graph");
    private static JButton orderBtn = new JButton("Topological Order");
    private static JTextArea recompTxt = new JTextArea();
    
    // Build Button Listener
    class BuildBtnListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Text Field -> Listener
            file = fileTxt.getText();
            try {
                if (file.isEmpty()) {
                    throw new NullPointerException();
                }
                else {
                    // Directed Graph creation
                    dGraph = new DGraph<>();
                    dGraph.buildDGraphFromFile(dGraph.tokenizeFile(file));
                    JOptionPane.showMessageDialog(null, "Graph Built Successfully");
                } // end of else
            } // end of try
            
            // Catching exceptions
            catch (NullPointerException ex) { // Empty Input
                JOptionPane.showMessageDialog(null, "Please enter a File Name.");
            } // end of catch
            catch (IOException ex) { // Failure to open file
                JOptionPane.showMessageDialog(null, "File Did Not Open");
            } // end of catch
        } // end of method
    } // end of listener

    // Topological Order Button Listener
    class OrderBtnListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Text Field -> Listener
            classToRe = classToReTxt.getText();
            try {
                if (classToRe.isEmpty()) {
                    throw new NullPointerException();
                }
                else {
                    // Generate recompilation order, display
                    recompTxt.setText(dGraph.topOrdGeneration(classToRe));
                } // end of else
            } // end of try
            
            // Catching exceptions
            catch (NullPointerException ex) { // Empty Input
                JOptionPane.showMessageDialog(null, 
                    "Please enter a Class Name.");
            } // end of catch
            catch (InvalidClassNameException ex) { // Input class name is invalid
                JOptionPane.showMessageDialog(null, 
                    "Invalid Class Name: " + classToRe + ". Please try again.");
            } // end of catch
            catch (GraphCycleException ex) { // Graph contains a cycle
                JOptionPane.showMessageDialog(null, 
                    "This Directed Graph contains a Cycle due to circular class dependency.");
            } // end of catch
        } // end of method
    } // end of listener

    // GUI Creation
    public P4GUI() {
        // Frame Creation
        super("John's Class Dependency Graph"); // Titling frame
        setSize(600, 300);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Creating GridBagConstraints object
        GridBagConstraints constrain = new GridBagConstraints();
        constrain.insets = new Insets(4,4,4,4);
        
        // Labels Panel
        JPanel labelsPanel = new JPanel();
        labelsPanel.setLayout(new GridBagLayout());
        labelsPanel.add(fileLbl);
        constrain.gridy = 1;
        labelsPanel.add(classToReLbl, constrain);
        
        // Text Panel
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new GridBagLayout());
        textPanel.add(fileTxt);
        textPanel.add(classToReTxt, constrain);
        
        // Buttons Panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridBagLayout());
        buttonsPanel.add(buildBtn);
        buttonsPanel.add(orderBtn, constrain);
        buildBtn.addActionListener(new BuildBtnListener());
        orderBtn.addActionListener(new OrderBtnListener());
        
        // Bringing together Upper Border
        constrain.fill = GridBagConstraints.BOTH;
        JPanel upperPanel = new JPanel();
        upperPanel.setLayout(new GridBagLayout());
        upperPanel.setBorder(BorderFactory.createTitledBorder(""));
        constrain.gridy = 0;
        upperPanel.add(labelsPanel, constrain);
        constrain.gridx = 1;
        upperPanel.add(textPanel, constrain);
        constrain.gridx = 2;
        upperPanel.add(buttonsPanel, constrain);
        
        // Recompilation Order
        JPanel recompPanel = new JPanel();
        recompPanel.setLayout(new BorderLayout());
        recompPanel.setBorder(BorderFactory.createTitledBorder("Recompilation Order"));
        recompTxt.setLineWrap(true);
        recompTxt.setWrapStyleWord(true);
        recompTxt.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(recompTxt);
        recompPanel.add(scrollPane);
        
        // Main panel
        JPanel main = new JPanel();
        main.setLayout(new GridBagLayout());
        constrain.gridx = 0;
        main.add(upperPanel, constrain);
        constrain.gridy = 1;
        constrain.weightx = 1;
        constrain.weighty = 1;
        main.add(recompPanel, constrain);
        add(main);
    } // end of GUI Creation

    // Main method
    public static void main(String[] args) {
        P4GUI gui = new P4GUI();
    } // end of main method
} // end of class
