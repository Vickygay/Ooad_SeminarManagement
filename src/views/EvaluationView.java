package views;

import javax.swing.*;
import java.awt.*;
import models.Presentation;
import controllers.EvaluationController;

public class EvaluationView extends JFrame {
    // Text fields for Scores
    private JTextField scoreField1, scoreField2, scoreField3, scoreField4;
    // Text fields for Feedback
    private JTextField feedbackField1, feedbackField2, feedbackField3, feedbackField4;
    private JButton submitButton;
    private Presentation presentation;

    public EvaluationView(Presentation presentation) {
        this.presentation = presentation;

        setTitle("Evaluate Presentation");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main Panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- NEW: Header Panel to show Student ID ---
        JPanel headerPanel = new JPanel(new GridLayout(2, 1));

        JLabel titleLabel = new JLabel("Evaluation Rubrics", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        // Show the Student ID here!
        String studentID = presentation.getStudentID();
        JLabel studentLabel = new JLabel("Evaluating Student ID: " + (studentID != null ? studentID : "Unknown"),
                JLabel.CENTER);
        studentLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        studentLabel.setForeground(Color.BLUE); // Make it stand out

        headerPanel.add(titleLabel);
        headerPanel.add(studentLabel);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        // ---------------------------------------------

        // Center Panel for Rubrics (Grid Layout)
        JPanel rubricsPanel = new JPanel();
        rubricsPanel.setLayout(new GridLayout(5, 3, 15, 15));

        // --- Row 1: Headers ---
        rubricsPanel.add(createHeaderLabel("Criteria"));
        rubricsPanel.add(createHeaderLabel("Scoring (0-5)"));
        rubricsPanel.add(createHeaderLabel("Feedback"));

        // --- Row 2: Problem Clarity ---
        rubricsPanel.add(createCriteriaLabel("Problem Clarity"));
        scoreField1 = new JTextField();
        rubricsPanel.add(scoreField1);
        feedbackField1 = new JTextField();
        rubricsPanel.add(feedbackField1);

        // --- Row 3: Methodology ---
        rubricsPanel.add(createCriteriaLabel("Methodology"));
        scoreField2 = new JTextField();
        rubricsPanel.add(scoreField2);
        feedbackField2 = new JTextField();
        rubricsPanel.add(feedbackField2);

        // --- Row 4: Results ---
        rubricsPanel.add(createCriteriaLabel("Results"));
        scoreField3 = new JTextField();
        rubricsPanel.add(scoreField3);
        feedbackField3 = new JTextField();
        rubricsPanel.add(feedbackField3);

        // --- Row 5: Presentation ---
        rubricsPanel.add(createCriteriaLabel("Presentation"));
        scoreField4 = new JTextField();
        rubricsPanel.add(scoreField4);
        feedbackField4 = new JTextField();
        rubricsPanel.add(feedbackField4);

        mainPanel.add(rubricsPanel, BorderLayout.CENTER);

        // Bottom Panel
        JPanel bottomPanel = new JPanel();
        submitButton = new JButton("Submit Evaluation");
        submitButton.setFont(new Font("Arial", Font.BOLD, 16));
        submitButton.setPreferredSize(new Dimension(200, 50));
        bottomPanel.add(submitButton);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Button Logic
        submitButton.addActionListener(e -> handleSubmit());
    }

    private void handleSubmit() {
        try {
            // 1. Get scores
            double s1 = Double.parseDouble(scoreField1.getText());
            double s2 = Double.parseDouble(scoreField2.getText());
            double s3 = Double.parseDouble(scoreField3.getText());
            double s4 = Double.parseDouble(scoreField4.getText());

            // 2. Validate
            if (isInvalid(s1) || isInvalid(s2) || isInvalid(s3) || isInvalid(s4)) {
                JOptionPane.showMessageDialog(this, "Error: All scores must be between 0 and 5!", "Invalid Score", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 3. Calculate Total
            double totalScore = s1 + s2 + s3 + s4;

            // 4. Create a DETAILED Report String (using \n for new lines)
            // This format will look like:
            // Problem Clarity: 5.0 (Comments...)
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

            // 5. Send to Controller
            EvaluationController controller = new EvaluationController();
            controller.submitEvaluation(presentation, String.valueOf(totalScore), detailedReport);

            // 6. Success & Close
            JOptionPane.showMessageDialog(this, "Evaluation Submitted Successfully!\nTotal Score: " + totalScore + "/20");
            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for scores.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isInvalid(double score) {
        return score < 0 || score > 5;
    }

    private JLabel createHeaderLabel(String text) {
        JLabel label = new JLabel(text, JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        return label;
    }

    private JLabel createCriteriaLabel(String text) {
        JLabel label = new JLabel(text, JLabel.LEFT);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        return label;
    }
}