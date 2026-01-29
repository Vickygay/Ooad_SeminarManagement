package views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class E_NominatePanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private String myEvaluatorID;
    private JComboBox<String> awardTypeCombo;

    public E_NominatePanel(String myEvaluatorID) {
        this.myEvaluatorID = myEvaluatorID;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Header
        JLabel lblTitle = new JLabel("Nominate Students for Award");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(lblTitle, BorderLayout.NORTH);

        // Table Setup
        String[] columns = {"Student ID", "Evaluation Status", "Total Score", "Action"};
        model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(model);
        table.setRowHeight(35);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(0, 102, 204));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("SansSerif", Font.BOLD, 15));

        add(new JScrollPane(table), BorderLayout.CENTER);

        // Load Data
        refreshData();

        // Bottom Panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(new JLabel("Select Award Category:"));

        String[] awards = {"Best Oral", "Best Poster", "People's Choice"};
        awardTypeCombo = new JComboBox<>(awards);
        awardTypeCombo.setPreferredSize(new Dimension(220, 30));
        bottomPanel.add(awardTypeCombo);

        JButton btnNominate = new JButton("Nominate Selected Student");
        btnNominate.setBackground(new Color(255, 204, 102)); 
        btnNominate.setFont(new Font("SansSerif", Font.BOLD, 14));
        
        btnNominate.addActionListener(e -> nominateSelected());
        
        bottomPanel.add(btnNominate);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void refreshData() {
        model.setRowCount(0);
        java.util.Map<String, Double> eligibleScores = getHighScoringStudents();
        
        // 1. Get list of students I ALREADY nominated
        Set<String> alreadyNominatedByMe = getMyNominatedStudents();

        try (BufferedReader br = new BufferedReader(new FileReader("seminars.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    String fEvaluator = parts[3].trim();
                    String fStudent = parts[4].trim();

                    if (fEvaluator.equalsIgnoreCase(myEvaluatorID) 
                        && eligibleScores.containsKey(fStudent)) {
                        
                        // 2. Filter: Only add if NOT already nominated
                        if (!alreadyNominatedByMe.contains(fStudent)) {
                            double score = eligibleScores.get(fStudent);
                            model.addRow(new Object[]{fStudent, "Eligible for Nomination", score, "Select to Nominate"});
                        }
                    }
                }
            }
        } catch (IOException e) { }
    }

    private java.util.Map<String, Double> getHighScoringStudents() {
        java.util.Map<String, Double> highScorers = new java.util.HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader("evaluations.txt"))) {
            String line;
            String currentID = null;
            while ((line = br.readLine()) != null) {
                if (line.contains("Student ID:")) {
                    String[] parts = line.split("Student ID:");
                    if (parts.length > 1) currentID = parts[1].trim();
                } 
                else if (line.contains("TOTAL SCORE:") && currentID != null) {
                    String scorePart = line.replace("TOTAL SCORE:", "").split("/")[0].trim();
                    try {
                        double score = Double.parseDouble(scorePart);
                        if (score > 15.0) highScorers.put(currentID, score);
                    } catch (Exception e) {}
                    currentID = null;
                }
            }
        } catch (IOException e) {}
        return highScorers;
    }

    // New Helper: Check nominations.txt for previous actions
    private Set<String> getMyNominatedStudents() {
        Set<String> nominated = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader("nominations.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Format: StudentID, EvaluatorID, AwardType, Status
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    if (parts[1].trim().equalsIgnoreCase(myEvaluatorID)) {
                        nominated.add(parts[0].trim());
                    }
                }
            }
        } catch (IOException e) {}
        return nominated;
    }

    private void nominateSelected() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student from the list.");
            return;
        }

        String studentID = (String) model.getValueAt(row, 0);
        String selectedAward = (String) awardTypeCombo.getSelectedItem();
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Nominate " + studentID + " for [" + selectedAward + "]?\n(They will be removed from this list after)", 
            "Confirm Nomination", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            saveNomination(studentID, selectedAward);
            refreshData(); // Refresh to remove the student
        }
    }

    private void saveNomination(String studentID, String awardType) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("nominations.txt", true))) {
            bw.write(studentID + "," + myEvaluatorID + "," + awardType + ",Nominated");
            bw.newLine();
            JOptionPane.showMessageDialog(this, "Nomination saved successfully!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving nomination.");
        }
    }
}