package controllers;

import models.Presentation;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EvaluationController {

    private static final String FILE_PATH = "evaluations.txt";

    public void submitEvaluation(Presentation presentation, String score, String comments) {
        presentation.setEvaluationScore(Double.parseDouble(score));
        presentation.setEvaluationComments(comments);
        saveToFile(presentation);
        System.out.println("Evaluation submitted and saved.");
    }

    private void saveToFile(Presentation p) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String studentID = (p.getStudentID() != null) ? p.getStudentID() : "Unknown";

            // --- WRITE HEADER ---
            writer.write("==================================================================================");
            writer.newLine();
            writer.write(String.format("EVALUATION RECORD | Date: %s | Student ID: %s", timestamp, studentID));
            writer.newLine();
            writer.write("==================================================================================");
            writer.newLine();
            
            // --- WRITE TOTAL SCORE ---
            writer.write(String.format("TOTAL SCORE: %.2f / 20.00", p.getEvaluationScore()));
            writer.newLine();
            
            // --- WRITE DETAILED BREAKDOWN ---
            writer.write("DETAILED FEEDBACK:");
            writer.write(p.getEvaluationComments()); // This now contains the new lines from View
            writer.newLine();
            writer.newLine(); // Extra space between records

        } catch (IOException e) {
            System.err.println("Error saving evaluation: " + e.getMessage());
        }
    }
}