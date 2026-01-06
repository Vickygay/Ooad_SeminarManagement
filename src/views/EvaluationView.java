package views;

import javax.swing.*;
import java.awt.*;
import models.Presentation;
import controllers.EvaluationController;

public class EvaluationView extends JFrame {
    private JTextArea commentsArea;
    private JTextField scoreField;
    private JButton submitButton;

    public EvaluationView(Presentation presentation) {
        setTitle("Evaluate Presentation");
        setSize(1300, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        JLabel scoreLabel = new JLabel("Score:");
        scoreField = new JTextField();
        JLabel commentsLabel = new JLabel("Comments:");
        commentsArea = new JTextArea();
        submitButton = new JButton("Submit Evaluation");

        panel.add(scoreLabel);
        panel.add(scoreField);
        panel.add(commentsLabel);
        panel.add(commentsArea);
        panel.add(new JLabel()); // Empty space
        panel.add(submitButton);

        add(panel);

        submitButton.addActionListener(e -> {
            String score = scoreField.getText();
            String comments = commentsArea.getText();

            // Call the EvaluationController to handle submission logic
            EvaluationController evaluationController = new EvaluationController();
            evaluationController.submitEvaluation(presentation, score, comments);
        });
    }
}
