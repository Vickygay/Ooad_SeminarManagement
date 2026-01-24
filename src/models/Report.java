package models;

import java.io.*;
import java.util.*;

public class Report {
    public static Map<String, Double> getStudentScores() {
        Map<String, Double> scores = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader("evaluations.txt"))) {
            String line;
            String currentStudent = null;
            
            while ((line = br.readLine()) != null) {
                if (line.contains("Student ID:")) {
                    // Parse "EVALUATION RECORD | ... | Student ID: std123"
                    String[] parts = line.split("Student ID:");
                    if (parts.length > 1) {
                        currentStudent = parts[1].trim();
                    }
                } else if (line.contains("TOTAL SCORE:") && currentStudent != null) {
                    // Parse "TOTAL SCORE: 17.00 / 20.00"
                    String s = line.split("/")[0].replace("TOTAL SCORE:", "").trim();
                    try {
                        double score = Double.parseDouble(s);
                        // If student has multiple evaluations, keep the highest score
                        if (score > scores.getOrDefault(currentStudent, 0.0)) {
                            scores.put(currentStudent, score);
                        }
                    } catch (NumberFormatException e) {
                        // ignore invalid numbers
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error reading evaluations: " + e.getMessage());
        }
        return scores;
    }
}
