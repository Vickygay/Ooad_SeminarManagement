package views;

import javax.swing.*;
import java.awt.*;

public class EvaluatorDashboardView extends JFrame {
    private JButton viewAssignedPresentationButton;
    private JButton reviewPresentationButton;
    private JButton provideFeedbackButton;
    private JButton nominateStudentButton;
    private JButton nomineeButton;
    private JButton awardeeButton;

    public EvaluatorDashboardView() {
        setTitle("Evaluator Dashboard");
        setSize(1300, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Layout and Components
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 10, 10)); // GridLayout with 3 rows, 2 columns

        // Create buttons with adjusted sizes
        viewAssignedPresentationButton = new JButton("Assigned Presentation");
        viewAssignedPresentationButton.setFont(new Font("Arial", Font.PLAIN, 18)); // Adjust font size
        viewAssignedPresentationButton.setPreferredSize(new Dimension(250, 40)); // Adjusted size

        reviewPresentationButton = new JButton("Review Presentation");
        reviewPresentationButton.setFont(new Font("Arial", Font.PLAIN, 18));
        reviewPresentationButton.setPreferredSize(new Dimension(250, 40)); // Adjusted size

        provideFeedbackButton = new JButton("Feedback");
        provideFeedbackButton.setFont(new Font("Arial", Font.PLAIN, 18));
        provideFeedbackButton.setPreferredSize(new Dimension(250, 40)); // Adjusted size

        nominateStudentButton = new JButton("Nominate");
        nominateStudentButton.setFont(new Font("Arial", Font.PLAIN, 18));
        nominateStudentButton.setPreferredSize(new Dimension(250, 40)); // Adjusted size

        nomineeButton = new JButton("Nominee");
        nomineeButton.setFont(new Font("Arial", Font.PLAIN, 18)); // Set font size for button
        nomineeButton.setPreferredSize(new Dimension(250, 40)); // Adjusted size

        awardeeButton = new JButton("Awardee");
        awardeeButton.setFont(new Font("Arial", Font.PLAIN, 18)); // Set font size for button
        awardeeButton.setPreferredSize(new Dimension(250, 40)); // Adjusted size

        // Add buttons to the panel
        panel.add(viewAssignedPresentationButton);
        panel.add(reviewPresentationButton);
        panel.add(provideFeedbackButton);
        panel.add(nominateStudentButton);
        panel.add(nomineeButton);
        panel.add(awardeeButton);

        add(panel);

        // Action listeners for each button
        viewAssignedPresentationButton.addActionListener(e -> viewAssignedPresentation());
        reviewPresentationButton.addActionListener(e -> reviewPresentation());
        provideFeedbackButton.addActionListener(e -> provideFeedback());
        nominateStudentButton.addActionListener(e -> nominateStudent());
        nomineeButton.addActionListener(e -> nominee());
        awardeeButton.addActionListener(e -> awardee());

        setVisible(true);
    }

    public static void main(String[] args) {
        // Call the loadUsersFromFile method immediately when the program starts
        new EvaluatorDashboardView(); // This will create and display the window
    }

    // Methods to handle the actions
    public void viewAssignedPresentation() {
        System.out.println("Viewing Assigned Presentation...");
        // Your logic to view the assigned presentation
    }

    public void reviewPresentation() {
        System.out.println("Reviewing Presentation...");
        // Your logic to review the presentation
    }

    public void provideFeedback() {
        System.out.println("Providing Feedback...");
        // Your logic to provide feedback after reviewing the presentation
    }

    public void nominateStudent() {
        System.out.println("Nominating Student...");
        // Your logic to nominate a student for the presentation
    }

    public void nominee() {
        System.out.println("Viewing Nominee...");
        // Your logic to view the nominee details
    }

    public void awardee() {
        System.out.println("Viewing Awardee...");
        // Your logic to view the nominee details
    }
}
