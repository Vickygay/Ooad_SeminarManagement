package views;

import java.awt.*;
import java.io.File;
import java.util.Scanner;
import javax.swing.*;

public class S_GetSessionPanel extends JPanel {

    private String currentStudentID;
    private final String seminar_File = "seminars.txt";

    private JLabel lblStatus;
    private JTextField typeField, venueField, dateField, evaluatorField;

    public S_GetSessionPanel(String studentID) {
        this.currentStudentID = studentID;

        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.WEST;

        // --- TITLE ---
        JLabel title = new JLabel("My Assigned Seminar Session");
        title.setFont(new Font("SansSerif", Font.BOLD, 32));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(title, gbc);

        // --- STATUS MESSAGE ---
        lblStatus = new JLabel("Checking status...");
        lblStatus.setFont(new Font("SansSerif", Font.BOLD, 18));
        gbc.gridy = 1;
        add(lblStatus, gbc);

        // --- FORM FIELDS ---
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;

        // Session Type
        gbc.gridy = 2;
        gbc.gridx = 0;
        add(new JLabel("Session Type:"), gbc);
        typeField = createLargeReadOnlyField();
        gbc.gridx = 1;
        add(typeField, gbc);

        // Venue
        gbc.gridy = 3;
        gbc.gridx = 0;
        add(new JLabel("Venue:"), gbc);
        venueField = createLargeReadOnlyField();
        gbc.gridx = 1;
        add(venueField, gbc);

        // Date
        gbc.gridy = 4;
        gbc.gridx = 0;
        add(new JLabel("Date:"), gbc);
        dateField = createLargeReadOnlyField();
        gbc.gridx = 1;
        add(dateField, gbc);

        // Evaluator
        gbc.gridy = 5;
        gbc.gridx = 0;
        add(new JLabel("Assigned Evaluator:"), gbc);
        evaluatorField = createLargeReadOnlyField();
        gbc.gridx = 1;
        add(evaluatorField, gbc);

        // --- REFRESH BUTTON ---
        JButton btnRefresh = new JButton("Refresh Status");
        btnRefresh.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnRefresh.setPreferredSize(new Dimension(180, 40));
        btnRefresh.addActionListener(e -> loadSessionData());
        gbc.gridy = 6;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(btnRefresh, gbc);

        loadSessionData();
    }

    private JTextField createLargeReadOnlyField() {
        JTextField tf = new JTextField(30);
        tf.setFont(new Font("SansSerif", Font.PLAIN, 18));
        tf.setEditable(false);
        tf.setBackground(new Color(240, 248, 255));
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        return tf;
    }

    private void loadSessionData() {
        boolean found = false;
        try {
            File f = new File(seminar_File);
            if (f.exists()) {
                Scanner sc = new Scanner(f);
                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    String[] parts = line.split(",");
                    // Match Student ID at index 4 based on seminars.txt structure
                    if (parts.length >= 5 && parts[4].trim().equalsIgnoreCase(currentStudentID)) {
                        typeField.setText(parts[0].trim());
                        venueField.setText(parts[1].trim());
                        dateField.setText(parts[2].trim());
                        evaluatorField.setText(parts[3].trim());
                        lblStatus.setText("Status: Session Assigned.");
                        lblStatus.setForeground(new Color(0, 128, 0));
                        found = true;
                        break;
                    }
                }
                sc.close();
            }
        } catch (Exception e) {
            lblStatus.setText("Error reading file.");
        }

        if (!found) {
            lblStatus.setText("Status: Pending assignment by Coordinator.");
            lblStatus.setForeground(Color.RED);
            typeField.setText("-");
            venueField.setText("-");
            dateField.setText("-");
            evaluatorField.setText("-");
        }
    }
}