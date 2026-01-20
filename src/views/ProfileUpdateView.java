package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import models.UserProfile;
import models.UserDatabase;

public class ProfileUpdateView extends JFrame {
    private JTextField nameField;
    private JTextField emailField;
    private JTextField passwordField;
    private JButton saveButton;

    public ProfileUpdateView(String userID) {
        setTitle("Update Profile");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close this window, not the whole application

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));

        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField();
        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JTextField();

        saveButton = new JButton("Save");

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(new JLabel()); // Empty label for spacing
        panel.add(saveButton);

        add(panel);

        // Read user data from the file based on userID and populate the fields
        readUserDataFromFile(userID);

        // Action Listener for Save button
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create UserProfile object with values entered in the fields
                UserProfile userProfile = new UserProfile(userID, nameField.getText(), emailField.getText(),
                        passwordField.getText());

                // Update the user profile in UserDatabase (if you want to keep the data in memory)
                UserDatabase.getUsers().put(userID, userProfile); // Access users map through the getter method

                // Show success message
                JOptionPane.showMessageDialog(ProfileUpdateView.this, "Profile Updated Successfully!");

                // Optionally, you can save the updated data back to the file
                saveUserDataToFile();

                // Close the profile update window after saving
                dispose();
            }
        });

        setVisible(true);
    }

    // Function to read user data from the file and populate the fields based on userID
    private void readUserDataFromFile(String userID) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("user.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                // Assuming user information is stored as "userID,name,email,password"
                String[] userData = line.split(","); // Split by comma

                // If the first value matches the userID, load the corresponding data
                if (userData[0].equals(userID)) {
                    String name = userData[1]; // Name is the second part
                    String email = userData[2]; // Email is the third part
                    String password = userData[3]; // Password is the fourth part

                    // Set values to text fields
                    nameField.setText(name);
                    emailField.setText(email);
                    passwordField.setText(password);
                    break; // No need to continue reading once the user is found
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Function to save the updated user data back to the file
    private void saveUserDataToFile() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("user.txt"));

            // Iterate through all users in UserDatabase and write them back to the file
            for (UserProfile user : UserDatabase.getUsers().values()) {
                String userLine = user.getUserID() + "," + user.getName() + "," + user.getEmail() + ","
                        + user.getPassword();
                writer.write(userLine);
                writer.newLine();
            }

            writer.close();
            System.out.println("User data has been updated in the file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
