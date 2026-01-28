package controllers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JOptionPane;

public class PresentationController {

    // Empty Constructor
    public PresentationController() {
    }

    // Register method that accepts 'filePath'
    public void registerPresentation(String studentID, String title, String abstractText, String supervisor, String presentationType, String filePath) {
        
        if (studentID == null || title.isEmpty()) {
            System.out.println("Invalid data");
            return;
        }
        
        // Handle null filePath (e.g., from PresentationView)
        if (filePath == null) {
            filePath = "No file attached";
        }

        // Save to presentations.txt
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("registrations.txt", true))) {
            // Format: StudentID | Title | Type | Supervisor | Abstract | FilePath
            String line = String.format("%s|%s|%s|%s|%s|%s", 
                studentID, title, presentationType, supervisor, abstractText, filePath);
            
            writer.write(line);
            writer.newLine();
            
            System.out.println("Saved presentation for: " + studentID);

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error saving to file: " + e.getMessage());
        }
    }
}