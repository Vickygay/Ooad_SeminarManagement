package views;

import javax.swing.*;
import java.awt.*;
import models.Presentation;

public class EvaluatorDashboardView extends JFrame {
    // 1. Declare all 6 buttons shown in the image
    private JButton assignedPresentationButton;
    private JButton reviewPresentationButton;
    private JButton feedbackButton;
    private JButton nominateStudentButton;
    private JButton nomineeButton;
    private JButton awardeeButton;

    public EvaluatorDashboardView() {
        setTitle("Evaluator Dashboard");
        setSize(1300, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window on screen

        // 2. Main Container with Padding
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add margins around the edges

        // 3. Title Label
        JLabel titleLabel = new JLabel("Evaluation Dashboard", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0)); // Space below title
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // 4. Grid Panel for Buttons (3 Rows, 2 Columns, Gaps of 50px horizontal, 30px
        // vertical)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 2, 50, 30));

        // Initialize Buttons with text matching your image
        assignedPresentationButton = createStyledButton("Assigned Presentation");
        reviewPresentationButton = createStyledButton("Review Presentation");
        feedbackButton = createStyledButton("Feedback");
        nominateStudentButton = createStyledButton("Nominate Student");
        nomineeButton = createStyledButton("Nominee");
        awardeeButton = createStyledButton("Awardee");

        // Add buttons to the panel in the specific order:
        // Row 1
        buttonPanel.add(assignedPresentationButton);
        buttonPanel.add(reviewPresentationButton);
        // Row 2
        buttonPanel.add(feedbackButton);
        buttonPanel.add(nominateStudentButton);
        // Row 3
        buttonPanel.add(nomineeButton);
        buttonPanel.add(awardeeButton);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        add(mainPanel);

        // 5. Add Action Listeners
        assignedPresentationButton.addActionListener(e -> viewAssignedPresentation());
        reviewPresentationButton.addActionListener(e -> reviewPresentation());
        feedbackButton.addActionListener(e -> provideFeedback());
        nominateStudentButton.addActionListener(e -> nominateStudent());
        nomineeButton.addActionListener(e -> viewNominees());
        awardeeButton.addActionListener(e -> viewAwardees());

        setVisible(true);
    }

    // Helper method to style buttons consistently
    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.PLAIN, 20));
        btn.setFocusPainted(false);
        btn.setBackground(new Color(200, 210, 230)); // Light blue-ish color like the image
        return btn;
    }

    // --- Action Methods ---

    public void viewAssignedPresentation() {
        System.out.println("Viewing Assigned Presentation...");
    }

    public void reviewPresentation() {
        System.out.println("Opening Evaluation Form...");
        
        // 1. Create a dummy presentation WITH A STUDENT ID
        Presentation dummyPresentation = new Presentation();
        dummyPresentation.setStudentID("S101"); // <--- Hardcoded for testing now
        
        // 2. Open the Evaluation Window
        EvaluationView evaluationWindow = new EvaluationView(dummyPresentation);
        evaluationWindow.setVisible(true);
    }

    public void provideFeedback() {
        System.out.println("Providing Feedback...");
    }

    public void nominateStudent() {
        System.out.println("Nominating Student...");
    }

    // New methods for the new buttons
    public void viewNominees() {
        System.out.println("Viewing Nominees...");
    }

    public void viewAwardees() {
        System.out.println("Viewing Awardees...");
    }

    public static void main(String[] args) {
        // Run this file directly to test the UI
        new EvaluatorDashboardView();
    }
}