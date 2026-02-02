package views;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class P_ProfileUpdateView extends JFrame {
    private JTextField nameField, emailField, passwordField;
    private JButton saveButton;
    private String currentUserID;

    public P_ProfileUpdateView(String userID) {
        this.currentUserID = userID; // Store ID for saving later
        setTitle("Update Profile");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // UI Components
        panel.add(new JLabel("User ID:"));
        JTextField idField = new JTextField(userID);
        idField.setEditable(false);
        panel.add(idField);

        panel.add(new JLabel("Name:"));
        nameField = new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("Email:"));
        emailField = new JTextField();
        panel.add(emailField);

        panel.add(new JLabel("Password:"));
        passwordField = new JTextField();
        panel.add(passwordField);

        saveButton = new JButton("Save Changes");
        panel.add(new JLabel()); // Spacer
        panel.add(saveButton);

        add(panel);

        // 1. Load Data safely
        loadDataForUser(userID);

        // 2. Save Data safely
        saveButton.addActionListener(e -> {
            updateUserInFile(currentUserID, nameField.getText(), emailField.getText(), passwordField.getText());
            JOptionPane.showMessageDialog(this, "Profile Updated Successfully!");
            dispose();
        });

        setVisible(true);
    }

    // --- Helper: Find the user and fill the text boxes ---
    private void loadDataForUser(String id) {
        try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Format: ID,Password,Role,Name,Email
                String[] parts = line.split(",");
                if (parts[0].trim().equalsIgnoreCase(id)) {
                    // Assuming parts[1] is Password, parts[3] is Name, parts[4] is Email
                    // Adjust indices based on your ACTUAL file format
                    if (parts.length > 3) nameField.setText(parts[3]);
                    if (parts.length > 4) emailField.setText(parts[4]);
                    if (parts.length > 1) passwordField.setText(parts[1]);
                    return;
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    // --- Helper: Read ALL lines, Update ONE line, Write ALL back ---
    private void updateUserInFile(String id, String newName, String newEmail, String newPass) {
        ArrayList<String> allLines = new ArrayList<>();
        
        // 1. Read all lines into memory
        try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].trim().equalsIgnoreCase(id)) {
                    // This is the user we are updating!
                    // Reconstruct the line with NEW values
                    // Keep ID (0) and Role (2) the same. Update Pass (1), Name (3), Email (4)
                    String role = parts[2]; 
                    String newLine = id + "," + newPass + "," + role + "," + newName + "," + newEmail;
                    allLines.add(newLine);
                } else {
                    // Keep other users exactly as they are
                    allLines.add(line);
                }
            }
        } catch (IOException e) { e.printStackTrace(); }

        // 2. Write everything back
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("users.txt"))) {
            for (String line : allLines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) { e.printStackTrace(); }
    }
}