package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import controllers.LoginController; // Import the controller

public class LoginView extends JFrame {
    private JTextField userIDField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private LoginController controller; // Add Controller variable

    public LoginView() {
        this.controller = new LoginController(); // Initialize Controller

        setTitle("Login System");
        setSize(1300, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // User ID field
        JLabel userIDLabel = new JLabel("User ID:");
        userIDField = new JTextField(20);
        userIDField.setPreferredSize(new Dimension(500, 40));
        userIDLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        userIDField.setFont(new Font("Arial", Font.PLAIN, 16));

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 5, 10);
        panel.add(userIDLabel, gbc);

        gbc.gridx = 1;
        panel.add(userIDField, gbc);

        // Password field
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);
        passwordField.setPreferredSize(new Dimension(500, 40));
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));

        gbc.gridx = 0; gbc.gridy = 1;
        gbc.insets = new Insets(5, 10, 5, 10);
        panel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        // Login button
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        panel.add(loginButton, gbc);

        getRootPane().setDefaultButton(loginButton);
        add(panel);

        // --- BUTTON ACTION LISTENER ---
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userID = userIDField.getText();
                String password = new String(passwordField.getPassword());

                // 1. ASK CONTROLLER TO VALIDATE
                String role = controller.validateLogin(userID, password);

                if (role != null) {
                    // 2. SUCCESS: Open the correct dashboard based on the returned role
                    JOptionPane.showMessageDialog(null, "Login Successful! Role: " + role);

                    if (role.equalsIgnoreCase("Student")) {
                        new StudentDashboardView(userID).setVisible(true);
                    } 
                    else if (role.equalsIgnoreCase("Evaluator")) {
                        new EvaluatorDashboardView(userID).setVisible(true); 
                    } 
                    else if (role.equalsIgnoreCase("Coordinator")) {
                        new CoordinatorDashboardView(userID).setVisible(true);
                    }
                    dispose(); // Close Login Window
                } else {
                    // 3. FAILURE
                    showErrorMessage("Invalid credentials or user does not exist!");
                    clearFields();
                }
            }
        });
    }

    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Login Error", JOptionPane.ERROR_MESSAGE);
    }

    public void clearFields() {
        userIDField.setText("");
        passwordField.setText("");
    }
}