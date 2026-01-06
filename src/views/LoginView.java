package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginView extends JFrame {
    private JTextField userIDField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginView() {
        setTitle("Login");
        setSize(1300, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set GridBagLayout for more control
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // User ID field
        JLabel userIDLabel = new JLabel("User ID:");
        userIDField = new JTextField(20); // Adjust width of the field
        userIDField.setPreferredSize(new Dimension(500, 40)); // Increased width and height
        userIDLabel.setFont(new Font("Arial", Font.PLAIN, 18)); // Set font size for the label
        userIDField.setFont(new Font("Arial", Font.PLAIN, 16)); // Set font size for User ID field
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 5, 10); // Add some padding
        panel.add(userIDLabel, gbc);

        gbc.gridx = 1;
        panel.add(userIDField, gbc);

        // Password field
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20); // Adjust width of the field
        passwordField.setPreferredSize(new Dimension(500, 40)); // Increased width and height
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 18)); // Set font size for the label
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16)); // Set font size for Password field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 10, 5, 10); // Add some padding
        panel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        // Login button
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.PLAIN, 18)); // Set font size for button
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding at the bottom
        panel.add(loginButton, gbc);

        add(panel);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userID = userIDField.getText();
                String password = new String(passwordField.getPassword());

                // Example login validation
                if (validateLogin(userID, password)) {
                    String role = getRole(userID); // Get role based on user ID prefix
                    if (role.equals("Student")) {
                        new StudentDashboardView().setVisible(true); // Show student dashboard
                    } else if (role.equals("Evaluator")) {
                        new EvaluatorDashboardView().setVisible(true); // Show evaluator dashboard
                    } else if (role.equals("Coordinator")) {
                        new CoordinatorDashboardView().setVisible(true); // Show coordinator dashboard
                    }
                    dispose(); // Close login screen
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid credentials!");
                    clearFields(); 
                }
            }
        });
    }

    // Dynamically return the role based on user ID prefix
    private String getRole(String userID) {
        if (userID.startsWith("std")) {
            return "Student";
        } else if (userID.startsWith("coordinator")) {
            return "Coordinator";
        } else if (userID.startsWith("E")) {
            return "Evaluator";
        } else {
            return "Unknown"; // Default case if no prefix matches
        }
    }

    private boolean validateLogin(String userID, String password) {
        // Example validation for multiple users
        if (userID.equals("admin") && password.equals("a3")) {
            return true; // Admin login is successful
        } else if (userID.equals("std111221") && password.equals("s3")) {
            return true; // Student login is successful
        } else if (userID.equals("E120101") && password.equals("e3")) {
            return true; // Evaluator login is successful
        } else if (userID.equals("coordinator1") && password.equals("c3")) {
            return true; // Coordinator login is successful
        } else {
            return false; // If none of the conditions match, login is unsuccessful
        }
    }

    public void showWelcomeScreen() {
        // Example: Open a new window or show a welcome message
        JOptionPane.showMessageDialog(this, "Login successful! Welcome!", "Success", JOptionPane.INFORMATION_MESSAGE);
        // Here you could navigate to the next screen or update the UI accordingly
    }

    // Method to show an error message if the login fails
    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        new LoginView().setVisible(true);
    }

    public void clearFields() {
    userIDField.setText(""); // Clear the User ID field
    passwordField.setText(""); // Clear the Password field
    }
}

