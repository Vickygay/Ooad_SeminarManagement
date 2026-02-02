package views;

import javax.swing.*;
import models.Presentation;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class E_ReviewPanel extends JPanel {
    private JComboBox<String> cbStudent;
    private String myEvaluatorID;

    // --- STANDARD FONTS ---
    private Font labelFont = new Font("SansSerif", Font.BOLD, 18);  // For Labels
    private Font inputFont = new Font("SansSerif", Font.PLAIN, 15); // For ComboBox
    private Font buttonFont = new Font("SansSerif", Font.BOLD, 15); // For Buttons

    public E_ReviewPanel(String myEvaluatorID) {
        this.myEvaluatorID = myEvaluatorID;
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); 
        gbc.anchor = GridBagConstraints.NORTHWEST; 

        // 1. Title
        JLabel titleLabel = new JLabel("Review Presentations");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 30));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(titleLabel, gbc);

        // 2. Label: Select Student
        gbc.gridy++; gbc.gridwidth = 1;
        JLabel lblStudent = new JLabel("Select Student:");
        lblStudent.setFont(labelFont); // Set Font
        add(lblStudent, gbc);

        // 3. Dropdown
        gbc.gridx = 1;
        cbStudent = new JComboBox<>();
        cbStudent.setPreferredSize(new Dimension(250, 35)); // Slightly taller to match font
        cbStudent.setFont(inputFont); // Set Font
        cbStudent.setBackground(Color.WHITE);
        add(cbStudent, gbc);

        refreshStudentList();

        // 4. Button
        gbc.gridy++; gbc.gridx = 1;
        gbc.insets = new Insets(30, 20, 10, 20);
        JButton btnStart = new JButton("Start Evaluation");
        btnStart.setBackground(new Color(204, 204, 255)); 
        btnStart.setForeground(Color.BLACK);
        btnStart.setFont(buttonFont); // Set Font
        btnStart.setFocusPainted(false);
        add(btnStart, gbc);

        btnStart.addActionListener(e -> {
            String selected = (String) cbStudent.getSelectedItem();
            
            if (selected == null || selected.contains("--") || selected.equals("No Pending Evaluations")) {
                JOptionPane.showMessageDialog(this, "Please select a valid student to evaluate.");
            } else {
                Presentation p = new Presentation();
                p.setStudentID(selected); 
                
                EvaluationView evaluationWindow = new EvaluationView(p);
                evaluationWindow.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        refreshStudentList(); 
                    }
                });
                evaluationWindow.setVisible(true);
            }
        });

        // 5. Pusher (Fills empty space)
        gbc.gridy++; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.weightx = 1; gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        add(new JLabel(), gbc);
    }

    public void refreshStudentList() {
        cbStudent.removeAllItems();
        String[] students = loadMyPendingStudents(myEvaluatorID);
        for (String s : students) {
            cbStudent.addItem(s);
        }
    }

    private String[] loadMyPendingStudents(String evalID) {
        ArrayList<String> list = new ArrayList<>();
        list.add("-- Select Student --");

        Set<String> evaluatedStudents = getEvaluatedStudentIDs();

        try (BufferedReader br = new BufferedReader(new FileReader("seminars.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    String fEvaluator = parts[3].trim();
                    String fStudent = parts[4].trim();

                    if (fEvaluator.equalsIgnoreCase(evalID)) {
                        if (!evaluatedStudents.contains(fStudent)) {
                            list.add(fStudent);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading seminars.txt: " + e.getMessage());
        }

        if (list.size() == 1) {
            list.clear();
            list.add("No Pending Evaluations");
        }
        return list.toArray(new String[0]);
    }

    private Set<String> getEvaluatedStudentIDs() {
        Set<String> finishedIDs = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader("evaluations.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("Student ID:")) {
                    String[] parts = line.split("Student ID:");
                    if (parts.length > 1) {
                        finishedIDs.add(parts[1].trim());
                    }
                }
            }
        } catch (IOException e) { }
        return finishedIDs;
    }
}