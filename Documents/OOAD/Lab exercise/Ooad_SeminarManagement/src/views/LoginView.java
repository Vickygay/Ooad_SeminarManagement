package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LoginView extends JFrame {
    private JTextField userIDField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private String validatedRole = null; // Stores role after successful file check

    public LoginView() {
        setTitle("Login System");
        setSize(1300, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centers the window

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
        gbc.gridx = 1; gbc.gridy = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        panel.add(loginButton, gbc);

        add(panel);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userID = userIDField.getText();
                String password = new String(passwordField.getPassword());

                if (validateLoginFromFile(userID, password)) {
                    String role = getRole(); 
                    
                    JOptionPane.showMessageDialog(null, "Login Successful! Role: " + role);
                    
                    // Navigate based on role found in text file
                    if (role.equalsIgnoreCase("Student")) {
                        new StudentDashboardView().setVisible(true);
                    } else if (role.equalsIgnoreCase("Evaluator")) {
                        new EvaluatorDashboardView().setVisible(true);
                    } else if (role.equalsIgnoreCase("Coordinator")) {
                        new CoordinatorDashboardView().setVisible(true);
                    }
                    dispose(); 
                } else {
                    showErrorMessage("Invalid credentials or user does not exist!");
                    clearFields();
                }
            }
        });
    }

    /**
     * Reads users.txt and checks if credentials match.
     * Expected format: userID,password,role
     */
    private boolean validateLoginFromFile(String userID, String password) {
        String line;
        // Using try-with-resources to ensure the file closes automatically
        try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
            while ((line = br.readLine()) != null) {
                String[] data = line.split(","); // Split by comma
                if (data.length == 3) {
                    String fUser = data[0].trim();
                    String fPass = data[1].trim();
                    String fRole = data[2].trim();

                    if (fUser.equals(userID) && fPass.equals(password)) {
                        this.validatedRole = fRole; // Store the role for the dashboard logic
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            showErrorMessage("Error: users.txt file not found in project root.");
        }
        return false;
    }

    private String getRole() {
        return validatedRole;
    }

    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Login Error", JOptionPane.ERROR_MESSAGE);
    }

    public void clearFields() {
        userIDField.setText("");
        passwordField.setText("");
    }

    public static void main(String[] args) {
        // Run UI on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new LoginView().setVisible(true);
        });
    }
}