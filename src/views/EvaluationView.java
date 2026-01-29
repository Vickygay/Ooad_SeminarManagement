package views;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import models.Presentation;
import controllers.EvaluationController;

public class EvaluationView extends JFrame {
    // Text fields
    private JTextField scoreField1, scoreField2, scoreField3, scoreField4;
    private JTextField feedbackField1, feedbackField2, feedbackField3, feedbackField4;
    private JButton submitButton;
    private Presentation presentation;

    // --- COLORS ---
    private Color lightPurple2 = new Color(204, 204, 255); 
    private Color whiteColor = Color.WHITE;
    private Color inputBackgroundColor = new Color(245, 245, 255);
    private Font inputFont = new Font("Arial", Font.PLAIN, 16);

    public EvaluationView(Presentation presentation) {
        this.presentation = presentation;

        setTitle("Evaluate Presentation - " + presentation.getStudentID());
        setSize(1100, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(whiteColor);

        // =========================================================================
        // 1. INFO PANEL (Student Details)
        // =========================================================================
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBackground(new Color(240, 240, 255));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // --- LOAD DATA ---
        String[] details = getStudentDetails(presentation.getStudentID());
        String title = details[0];
        String type = details[1];
        String abstractText = details[2];

        // Line 1: ID & Type
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel idLabel = new JLabel("Student ID: " + presentation.getStudentID());
        idLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        infoPanel.add(idLabel, gbc);
        
        gbc.gridx = 1;
        JLabel typeLabel = new JLabel("Presentation Type: " + type);
        typeLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        typeLabel.setForeground(new Color(0, 102, 204));
        infoPanel.add(typeLabel, gbc);

        // Line 2: Title
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        JLabel titleLbl = new JLabel("Title: " + title);
        titleLbl.setFont(new Font("SansSerif", Font.PLAIN, 16));
        infoPanel.add(titleLbl, gbc);

        // Line 3: Abstract
        gbc.gridy = 2;
        JTextArea abstractDisplay = new JTextArea(abstractText);
        abstractDisplay.setFont(new Font("SansSerif", Font.ITALIC, 14));
        abstractDisplay.setLineWrap(true);
        abstractDisplay.setWrapStyleWord(true);
        abstractDisplay.setEditable(false);
        abstractDisplay.setBackground(new Color(240, 240, 255));
        abstractDisplay.setBorder(BorderFactory.createTitledBorder("Abstract"));
        infoPanel.add(new JScrollPane(abstractDisplay) {{ setPreferredSize(new Dimension(800, 100)); }}, gbc);

        mainPanel.add(infoPanel, BorderLayout.NORTH);

        // =========================================================================
        // 2. RUBRICS PANEL
        // =========================================================================
        JPanel rubricsPanel = new JPanel();
        rubricsPanel.setLayout(new GridLayout(5, 3, 20, 20)); 
        rubricsPanel.setBackground(whiteColor);
        rubricsPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40)); 

        rubricsPanel.add(createMiniBoxLabel("Criteria", lightPurple2, Color.BLACK));
        rubricsPanel.add(createMiniBoxLabel("Scoring (0-5)", lightPurple2,Color.BLACK));
        rubricsPanel.add(createMiniBoxLabel("Feedback", lightPurple2, Color.BLACK));

        // Rows
        rubricsPanel.add(createMiniBoxLabel("Problem Clarity", whiteColor, Color.BLACK));
        scoreField1 = createSmallTextField(); rubricsPanel.add(wrapInPanel(scoreField1)); 
        feedbackField1 = createSmallTextField(); rubricsPanel.add(wrapInPanel(feedbackField1));

        rubricsPanel.add(createMiniBoxLabel("Methodology", whiteColor, Color.BLACK));
        scoreField2 = createSmallTextField(); rubricsPanel.add(wrapInPanel(scoreField2));
        feedbackField2 = createSmallTextField(); rubricsPanel.add(wrapInPanel(feedbackField2));

        rubricsPanel.add(createMiniBoxLabel("Results", whiteColor, Color.BLACK));
        scoreField3 = createSmallTextField(); rubricsPanel.add(wrapInPanel(scoreField3));
        feedbackField3 = createSmallTextField(); rubricsPanel.add(wrapInPanel(feedbackField3));

        rubricsPanel.add(createMiniBoxLabel("Presentation", whiteColor, Color.BLACK));
        scoreField4 = createSmallTextField(); rubricsPanel.add(wrapInPanel(scoreField4));
        feedbackField4 = createSmallTextField(); rubricsPanel.add(wrapInPanel(feedbackField4));

        mainPanel.add(rubricsPanel, BorderLayout.CENTER);

        // =========================================================================
        // 3. BOTTOM PANEL
        // =========================================================================
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(whiteColor);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        submitButton = new JButton("Submit Evaluation");
        submitButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        submitButton.setPreferredSize(new Dimension(250, 50));
        submitButton.setBackground(lightPurple2); 
        submitButton.setForeground(Color.BLACK);
        submitButton.setFocusPainted(false);
        submitButton.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 200), 2));
        
        bottomPanel.add(submitButton);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        submitButton.addActionListener(e -> handleSubmit());
    }

    // --- UPDATED HELPER: Read Registration Data Robustly ---
    private String[] getStudentDetails(String id) {
        String[] data = {"(Title Not Found)", "(Type Not Found)", "(Abstract Not Found)"};
        
        if (id == null || id.trim().isEmpty()) return data;

        try (BufferedReader br = new BufferedReader(new FileReader("registrations.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                // File Format: stdID|Title|Type|Supervisor|Abstract|File
                // We use split("\\|") because | is a special regex character
                String[] parts = line.split("\\|");
                
                if (parts.length >= 5) { // Ensure enough columns exist
                    String fileID = parts[0].trim();
                    if (fileID.equalsIgnoreCase(id.trim())) {
                        data[0] = parts[1]; // Title
                        data[1] = parts[2]; // Type
                        data[2] = parts[4]; // Abstract
                        break; 
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading registrations.txt: " + e.getMessage());
        }
        return data;
    }

    private JLabel createMiniBoxLabel(String text, Color bgColor, Color textColor) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setOpaque(true); 
        label.setBackground(bgColor); 
        label.setForeground(textColor); 
        label.setFont(new Font("SansSerif", Font.BOLD, 16));
        label.setBorder(new CompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 180), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        return label;
    }

    private JTextField createSmallTextField() {
        JTextField tf = new JTextField(15); 
        tf.setFont(inputFont);
        tf.setPreferredSize(new Dimension(200, 35)); 
        tf.setBackground(inputBackgroundColor); 
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 220), 1), 
            BorderFactory.createEmptyBorder(2, 5, 2, 5) 
        ));
        return tf;
    }

    private JPanel wrapInPanel(JComponent component) {
        JPanel p = new JPanel(new GridBagLayout()); 
        p.setBackground(Color.WHITE);
        p.add(component);
        return p;
    }

    private void handleSubmit() {
        try {
            double s1 = Double.parseDouble(scoreField1.getText());
            double s2 = Double.parseDouble(scoreField2.getText());
            double s3 = Double.parseDouble(scoreField3.getText());
            double s4 = Double.parseDouble(scoreField4.getText());

            if (isInvalid(s1) || isInvalid(s2) || isInvalid(s3) || isInvalid(s4)) {
                JOptionPane.showMessageDialog(this, "Error: All scores must be between 0 and 5!", "Invalid Score", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double totalScore = s1 + s2 + s3 + s4;

            String detailedReport = String.format(
                "\n\t   > Problem Clarity: %.1f  [Feedback: %s]" +
                "\n\t   > Methodology:     %.1f  [Feedback: %s]" +
                "\n\t   > Results:         %.1f  [Feedback: %s]" +
                "\n\t   > Presentation:    %.1f  [Feedback: %s]",
                s1, feedbackField1.getText(),
                s2, feedbackField2.getText(),
                s3, feedbackField3.getText(),
                s4, feedbackField4.getText()
            );

            EvaluationController controller = new EvaluationController();
            controller.submitEvaluation(presentation, String.valueOf(totalScore), detailedReport);

            JOptionPane.showMessageDialog(this, "Evaluation Submitted Successfully!\nTotal Score: " + totalScore + "/20");
            dispose();

        } catch (NumberFormatException ex) {
           JOptionPane.showMessageDialog(this, "Please enter valid numbers for scores.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } 
    }

    private boolean isInvalid(double score) {
        return score < 0 || score > 5;
    }
}