package views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class E_FeedbackPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private ArrayList<String> feedbackDetailsList; 

    public E_FeedbackPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel lblTitle = new JLabel("Evaluation History & Feedback");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(lblTitle, BorderLayout.NORTH);

        // --- UPDATED COLUMNS: Added Type and Title ---
        String[] columns = {"Date", "Student ID", "Type", "Presentation Title", "Total Score", "Action"};
        model = new DefaultTableModel(columns, 0) {
            @Override 
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };

        table = new JTable(model);
        table.setRowHeight(35);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        // Adjust column widths for better view
        table.getColumnModel().getColumn(0).setPreferredWidth(150); // Date
        table.getColumnModel().getColumn(1).setPreferredWidth(100); // ID
        table.getColumnModel().getColumn(2).setPreferredWidth(80);  // Type
        table.getColumnModel().getColumn(3).setPreferredWidth(300); // Title (Wider)
        table.getColumnModel().getColumn(4).setPreferredWidth(80);  // Score

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(153, 153, 255));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("SansSerif", Font.BOLD, 15));

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        feedbackDetailsList = new ArrayList<>();
        refreshData();

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) { 
                    int row = table.getSelectedRow();
                    if (row != -1) {
                        showFeedbackDetails(row);
                    }
                }
            }
        });
        
        JLabel hint = new JLabel("* Double-click a row to view the full detailed feedback.");
        hint.setForeground(Color.GRAY);
        add(hint, BorderLayout.SOUTH);
    }

    // --- Helper: Load Registration Data (Title & Type) into a Map ---
    private Map<String, String[]> loadStudentRegistry() {
        Map<String, String[]> registry = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader("registrations.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Format: stdID|Title|Type|Supervisor|Abstract|File
                String[] parts = line.split("\\|");
                if (parts.length >= 3) {
                    // Key: StudentID, Value: {Type, Title}
                    registry.put(parts[0].trim(), new String[]{parts[2].trim(), parts[1].trim()});
                }
            }
        } catch (IOException e) {}
        return registry;
    }

    public void refreshData() {
        model.setRowCount(0);
        feedbackDetailsList.clear();

        // 1. Load Registry Data first
        Map<String, String[]> registry = loadStudentRegistry();

        try (BufferedReader br = new BufferedReader(new FileReader("evaluations.txt"))) {
            String line;
            String currentDate = "Unknown";
            String currentID = "Unknown";
            String currentScore = "0.0";
            StringBuilder currentFeedback = new StringBuilder();
            boolean isReadingFeedback = false;

            while ((line = br.readLine()) != null) {
                if (line.contains("EVALUATION RECORD")) {
                    try {
                        String[] parts = line.split("\\|");
                        if (parts.length >= 3) {
                            currentDate = parts[1].replace("Date:", "").trim();
                            currentID = parts[2].replace("Student ID:", "").trim();
                        }
                    } catch (Exception e) {}
                    currentFeedback = new StringBuilder();
                    isReadingFeedback = false;
                } 
                else if (line.contains("TOTAL SCORE:")) {
                    currentScore = line.replace("TOTAL SCORE:", "").trim();
                } 
                else if (line.contains("DETAILED FEEDBACK:")) {
                    isReadingFeedback = true;
                } 
                else if (line.contains("==================") && isReadingFeedback) {
                    // --- FOUND END OF RECORD, ADD ROW ---
                    String[] details = registry.getOrDefault(currentID, new String[]{"Unknown", "Unknown"});
                    String type = details[0];
                    String title = details[1];

                    model.addRow(new Object[]{currentDate, currentID, type, title, currentScore, "View Details"});
                    feedbackDetailsList.add(currentFeedback.toString());
                    isReadingFeedback = false;
                }
                else if (isReadingFeedback) {
                    if (!line.trim().isEmpty()) {
                        currentFeedback.append(line).append("\n");
                    }
                }
            }
            // Add the last record if file ends
            if (!currentID.equals("Unknown")) {
                String[] details = registry.getOrDefault(currentID, new String[]{"Unknown", "Unknown"});
                model.addRow(new Object[]{currentDate, currentID, details[0], details[1], currentScore, "View Details"});
                feedbackDetailsList.add(currentFeedback.toString());
            }

        } catch (IOException e) { }
    }

    private void showFeedbackDetails(int index) {
        String details = feedbackDetailsList.get(index);
        JTextArea textArea = new JTextArea(details);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        
        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setPreferredSize(new Dimension(500, 300));
        
        JOptionPane.showMessageDialog(this, scroll, "Detailed Feedback", JOptionPane.INFORMATION_MESSAGE);
    }
}