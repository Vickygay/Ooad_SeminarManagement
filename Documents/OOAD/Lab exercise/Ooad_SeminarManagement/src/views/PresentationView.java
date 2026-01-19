package views;

import javax.swing.*;
import java.awt.*;
import controllers.PresentationController;
import models.Student;

public class PresentationView extends JFrame {
    private JTextField titleField;
    private JTextArea abstractArea;
    private JTextField supervisorField;
    private JComboBox<String> presentationTypeCombo;
    private JButton registerButton;
    private PresentationController controller;

    public PresentationView() {
        setTitle("Presentation Registration");
        setSize(1300, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));

        JLabel titleLabel = new JLabel("Title:");
        titleField = new JTextField();
        JLabel abstractLabel = new JLabel("Abstract:");
        abstractArea = new JTextArea();
        JLabel supervisorLabel = new JLabel("Supervisor:");
        supervisorField = new JTextField();
        JLabel presentationTypeLabel = new JLabel("Presentation Type:");
        presentationTypeCombo = new JComboBox<>(new String[] {"Oral", "Poster"});
        registerButton = new JButton("Register");

        panel.add(titleLabel);
        panel.add(titleField);
        panel.add(abstractLabel);
        panel.add(abstractArea);
        panel.add(supervisorLabel);
        panel.add(supervisorField);
        panel.add(presentationTypeLabel);
        panel.add(presentationTypeCombo);
        panel.add(new JLabel()); // Empty space
        panel.add(registerButton);

        add(panel);

        // Create the controller
        controller = new PresentationController(this, new Student("12345", "John Doe", "", "", "", ""));

        // Register button logic
        registerButton.addActionListener(e -> {
            String title = titleField.getText();
            String abstractText = abstractArea.getText();
            String supervisor = supervisorField.getText();
            String presentationType = (String) presentationTypeCombo.getSelectedItem();

            // Delegate the registration task to the controller
            controller.registerPresentation(title, abstractText, supervisor, presentationType);
            JOptionPane.showMessageDialog(null, "Presentation Registered!");
        });
    }

    // Add method to update the view if needed
    public void updateView() {
        // For example, you can clear the fields after successful registration
        titleField.setText("");
        abstractArea.setText("");
        supervisorField.setText("");
        presentationTypeCombo.setSelectedIndex(0); // Reset to "Oral"
    }
}
