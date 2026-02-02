package views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

//-------- Awardee Panel (Calculates Winners) ----------------//
public class P_AwardeePanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;

    public P_AwardeePanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // 1. Header
        JLabel lblTitle = new JLabel("Official Award Winners");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(lblTitle, BorderLayout.NORTH);

        // 2. Table Setup
        String[] columns = {"Award Category", "Winner (Student ID)", "Winning Score", "Status"};
        model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(model);
        table.setRowHeight(40); // Taller rows for winners
        table.setFont(new Font("SansSerif", Font.BOLD, 15));
        
        // Header Style
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(0, 102, 204)); 
        header.setForeground(Color.WHITE);
        header.setFont(new Font("SansSerif", Font.BOLD, 15));

        add(new JScrollPane(table), BorderLayout.CENTER);

        // 3. Refresh Button
        JButton btnRefresh = new JButton("Check Winners");
        btnRefresh.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnRefresh.addActionListener(e -> calculateWinners());
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(btnRefresh);
        add(bottomPanel, BorderLayout.SOUTH);
        
        calculateWinners();
    }

    public void calculateWinners() {
        model.setRowCount(0);
        
        // 1. Load All Scores <StudentID, Score>
        Map<String, Double> allScores = loadScores();

        // 2. Load Nominations <AwardType, List of StudentIDs>
        Map<String, ArrayList<String>> categories = new HashMap<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader("nominations.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Format: StudentID, EvaluatorID, AwardType, Status
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String stdID = parts[0].trim();
                    String award = parts[2].trim();
                    
                    categories.putIfAbsent(award, new ArrayList<>());
                    categories.get(award).add(stdID);
                }
            }
        } catch (Exception e) {}

        // 3. Determine Highest Score per Category
        for (String award : categories.keySet()) {
            ArrayList<String> nominees = categories.get(award);
            
            String winnerID = "None";
            double highestScore = -1.0;

            for (String std : nominees) {
                double score = allScores.getOrDefault(std, 0.0);
                if (score > highestScore) {
                    highestScore = score;
                    winnerID = std;
                }
            }

            if (!winnerID.equals("None")) {
                model.addRow(new Object[]{award, winnerID, highestScore, "OFFICIAL WINNER"});
            }
        }
        
        if (model.getRowCount() == 0) {
            model.addRow(new Object[]{"-", "No winners announced yet", "-", "-"});
        }
    }

    private Map<String, Double> loadScores() {
        Map<String, Double> scores = new HashMap<>();
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
                        scores.put(currentID, Double.parseDouble(scorePart));
                    } catch (Exception e) {}
                    currentID = null; // Reset
                }
            }
        } catch (Exception e) {}
        return scores;
    }
}