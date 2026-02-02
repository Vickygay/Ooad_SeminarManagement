package views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;

//-------- Nomination Panel (View Only) ----------------//
public class P_NominationPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;

    public P_NominationPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // 1. Header
        JLabel lblTitle = new JLabel("Current Nominees");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(lblTitle, BorderLayout.NORTH);

        // 2. Table Setup (3 Columns Only)
        String[] columns = {"Award Category", "Student ID", "Nomination Status"};
        model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(model);
        table.setRowHeight(35);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        // Header Style
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(0, 102, 204)); 
        header.setForeground(Color.WHITE);
        header.setFont(new Font("SansSerif", Font.BOLD, 15));

        add(new JScrollPane(table), BorderLayout.CENTER);

        // 3. Refresh Button
        JButton btnRefresh = new JButton("Refresh List");
        btnRefresh.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnRefresh.addActionListener(e -> loadNominations());
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(btnRefresh);
        add(bottomPanel, BorderLayout.SOUTH);
        
        // 4. Initial Load
        loadNominations();
    }

    public void calculateNominations() {
        // Alias for compatibility if called from other files
        loadNominations();
    }

    private void loadNominations() {
        model.setRowCount(0);
        try (BufferedReader br = new BufferedReader(new FileReader("nominations.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                // File Format: StudentID, EvaluatorID, AwardType, Status
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String stdID = parts[0].trim();
                    String awardType = parts[2].trim();
                    String status = parts[3].trim();

                    // Add Row: Award | Student | Status
                    model.addRow(new Object[]{awardType, stdID, status});
                }
            }
        } catch (Exception e) {
            // File might not exist yet
        }
        
        if (model.getRowCount() == 0) {
            model.addRow(new Object[]{"-", "No nominations yet", "-"});
        }
    }
}