package views;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import models.Presentation;
import controllers.EvaluationController;

public class EvaluationView extends JFrame {
    // Text fields
    private JTextField scoreField1, scoreField2, scoreField3, scoreField4;
    private JTextField feedbackField1, feedbackField2, feedbackField3, feedbackField4;
    private JButton submitButton;
    private Presentation presentation;

    // --- COLORS ---
    // 2. Grid Headers (Criteria, Scoring, Feedback)
    private Color lightPurple = new Color(153, 153, 255); 
    
    // 3. Row Labels (Problem Clarity, etc.) & Button
    private Color lightPurple2 = new Color(204, 204, 255); 
    
    private Color whiteColor = Color.WHITE;
    private Color inputBackgroundColor = new Color(245, 245, 255);
    // Font
    private Font inputFont = new Font("Arial", Font.PLAIN, 16);

    public EvaluationView(Presentation presentation) {
        this.presentation = presentation;

        setTitle("Evaluate Presentation");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main Container
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(whiteColor);

        // =========================================================================
        // 1. HEADER PANEL (Solid Colored Strip)
        // =========================================================================
        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        headerPanel.setBackground(lightPurple); 
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0)); 

        JLabel titleLabel = new JLabel("Evaluation Rubrics", JLabel.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE); 

        String studentID = presentation.getStudentID();
        JLabel studentLabel = new JLabel("Evaluating Student ID: " + (studentID != null ? studentID : "Unknown"), JLabel.CENTER);
        studentLabel.setFont(new Font("SansSerif", Font.ITALIC, 18));
        studentLabel.setForeground(Color.WHITE);

        headerPanel.add(titleLabel);
        headerPanel.add(studentLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // =========================================================================
        // 2. RUBRICS PANEL (The Grid)
        // =========================================================================
        JPanel rubricsPanel = new JPanel();
        rubricsPanel.setLayout(new GridLayout(5, 3, 20, 20)); 
        rubricsPanel.setBackground(whiteColor);
        rubricsPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40)); 

        // --- Row 1: Headers (Use lightPurple - 153) ---
        rubricsPanel.add(createMiniBoxLabel("Criteria", lightPurple2, Color.BLACK));
        rubricsPanel.add(createMiniBoxLabel("Scoring (0-5)", lightPurple2,Color.BLACK));
        rubricsPanel.add(createMiniBoxLabel("Feedback", lightPurple2, Color.BLACK));

        // --- Row 2: Problem Clarity (Use lightPurple2 - 204) ---
        rubricsPanel.add(createMiniBoxLabel("Problem Clarity", whiteColor, Color.BLACK));
        scoreField1 = createSmallTextField();
        rubricsPanel.add(wrapInPanel(scoreField1)); 
        feedbackField1 = createSmallTextField();
        rubricsPanel.add(wrapInPanel(feedbackField1));

        // --- Row 3: Methodology (Use lightPurple2 - 204) ---
        rubricsPanel.add(createMiniBoxLabel("Methodology", whiteColor, Color.BLACK));
        scoreField2 = createSmallTextField();
        rubricsPanel.add(wrapInPanel(scoreField2));
        feedbackField2 = createSmallTextField();
        rubricsPanel.add(wrapInPanel(feedbackField2));

        // --- Row 4: Results (Use lightPurple2 - 204) ---
        rubricsPanel.add(createMiniBoxLabel("Results", whiteColor, Color.BLACK));
        scoreField3 = createSmallTextField();
        rubricsPanel.add(wrapInPanel(scoreField3));
        feedbackField3 = createSmallTextField();
        rubricsPanel.add(wrapInPanel(feedbackField3));

        // --- Row 5: Presentation (Use lightPurple2 - 204) ---
        rubricsPanel.add(createMiniBoxLabel("Presentation", whiteColor, Color.BLACK));
        scoreField4 = createSmallTextField();
        rubricsPanel.add(wrapInPanel(scoreField4));
        feedbackField4 = createSmallTextField();
        rubricsPanel.add(wrapInPanel(feedbackField4));

        mainPanel.add(rubricsPanel, BorderLayout.CENTER);

        // =========================================================================
        // 3. BOTTOM PANEL (Submit Button)
        // =========================================================================
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(whiteColor);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        submitButton = new JButton("Submit Evaluation");
        submitButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        submitButton.setPreferredSize(new Dimension(250, 50));
        
        // Button uses the lighter purple (204)
        submitButton.setBackground(lightPurple2); 
        submitButton.setForeground(Color.BLACK);
        submitButton.setFocusPainted(false);
        submitButton.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 200), 2));
        
        bottomPanel.add(submitButton);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Button Logic
        submitButton.addActionListener(e -> handleSubmit());
    }

    // --- HELPER: Create "Mini Box" Labels with Custom Color ---
    private JLabel createMiniBoxLabel(String text, Color bgColor, Color textColor) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setOpaque(true); 
        
        label.setBackground(bgColor); // Set Background
        label.setForeground(textColor); // Set Text Color (White or Black)
        
        label.setFont(new Font("SansSerif", Font.BOLD, 16));
        
        label.setBorder(new CompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 180), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        return label;
    }

    // --- HELPER: Create Smaller Input Fields ---
    private JTextField createSmallTextField() {
        JTextField tf = new JTextField(15); 
        tf.setFont(inputFont);
        tf.setPreferredSize(new Dimension(200, 35)); 
        
        // --- THIS PART ADDS THE COLOR & BORDER ---
        tf.setBackground(inputBackgroundColor); // Light Lavender Background
        
        // Add a thin border so it looks like a box
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 220), 1), // Blue-ish border
            BorderFactory.createEmptyBorder(2, 5, 2, 5) // Padding inside text field
        ));
        
        return tf;
    }

    // --- HELPER: Wrap TextField in a Panel ---
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