package views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class E_AssignedPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private String myEvaluatorID;

    public E_AssignedPanel(String myEvaluatorID) {
        this.myEvaluatorID = myEvaluatorID;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel lblTitle = new JLabel("My Pending Schedule");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(lblTitle, BorderLayout.NORTH);

        String[] columns = {"Date", "Student ID", "Session Type", "Venue"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(153, 153, 255));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("SansSerif", Font.BOLD, 16));

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        loadData();
    }

    public void loadData() {
        model.setRowCount(0);
        
        // 1. Get list of students I have already evaluated
        Set<String> evaluatedStudents = getEvaluatedStudentIDs();

        try (BufferedReader br = new BufferedReader(new FileReader("seminars.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    String fType = parts[0].trim();
                    String fVenue = parts[1].trim();
                    String fDate = parts[2].trim();
                    String fEvaluator = parts[3].trim();
                    String fStudent = parts[4].trim();

                    // 2. Only add if Assigned to ME AND NOT yet evaluated
                    if (fEvaluator.equalsIgnoreCase(myEvaluatorID) && !evaluatedStudents.contains(fStudent)) {
                        model.addRow(new Object[]{fDate, fStudent, fType, fVenue});
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading seminars.txt");
        }
        
        if (model.getRowCount() == 0) {
            model.addRow(new Object[]{"-", "No pending tasks", "-", "-"});
        }
    }

    // Helper: Read evaluations.txt to find finished students
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