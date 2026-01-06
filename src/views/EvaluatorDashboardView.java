package views;

import javax.swing.*;
import java.awt.*;

public class EvaluatorDashboardView extends JFrame {
    private JButton viewAssignedPresentationButton;
    private JButton reviewPresentationButton;
    private JButton provideFeedbackButton;
    private JButton nominateStudentButton;

    public EvaluatorDashboardView() {
        setTitle("Evaluator Dashboard");
        setSize(1300, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Layout and Components
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 3, 10, 10)); // Use GridLayout for better alignment with 3 columns

        // Create buttons with adjusted sizes
        viewAssignedPresentationButton = new JButton("View Assigned Presentation");
        viewAssignedPresentationButton.setFont(new Font("Arial", Font.PLAIN, 14)); // Adjust font size
        viewAssignedPresentationButton.setPreferredSize(new Dimension(250, 40)); // Set smaller size

        reviewPresentationButton = new JButton("Review Presentation");
        reviewPresentationButton.setFont(new Font("Arial", Font.PLAIN, 14)); // Adjust font size
        reviewPresentationButton.setPreferredSize(new Dimension(250, 40)); // Set smaller size

        provideFeedbackButton = new JButton("Provide Feedback");
        provideFeedbackButton.setFont(new Font("Arial", Font.PLAIN, 30)); // Adjust font size
        provideFeedbackButton.setPreferredSize(new Dimension(250, 40)); // Set smaller size

        nominateStudentButton = new JButton("Nominate Student");
        nominateStudentButton.setFont(new Font("Arial", Font.PLAIN, 14)); // Adjust font size
        nominateStudentButton.setPreferredSize(new Dimension(250, 40)); // Set smaller size

        // Add buttons to the panel
        panel.add(viewAssignedPresentationButton);
        panel.add(reviewPresentationButton);
        panel.add(provideFeedbackButton);
        panel.add(nominateStudentButton);

        add(panel);

        // Action listeners for each button
        viewAssignedPresentationButton.addActionListener(e -> viewAssignedPresentation());
        reviewPresentationButton.addActionListener(e -> reviewPresentation());
        provideFeedbackButton.addActionListener(e -> provideFeedback());
        nominateStudentButton.addActionListener(e -> nominateStudent());

        setVisible(true);
    }

    // Methods to handle the actions
    public void viewAssignedPresentation() {
        System.out.println("Viewing Assigned Presentation...");
        // Your logic to view the assigned presentation
    }

    public void reviewPresentation() {
        System.out.println("Reviewing Presentation...");
        // Your logic to review the presentation, such as viewing more details
    }

    public void provideFeedback() {
        System.out.println("Providing Feedback...");
        // Your logic to provide feedback after reviewing the presentation
    }

    public void nominateStudent() {
        System.out.println("Nominating Student...");
        // Your logic to nominate a student for the presentation
    }

    public static void main(String[] args) {
        new EvaluatorDashboardView(); // This will create and display the window
    }
}
