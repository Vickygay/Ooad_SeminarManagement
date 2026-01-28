package controllers;

import java.util.*;
import javax.swing.table.DefaultTableModel;
import models.Report;
import models.SeminarSession;
public class ReportController {
    // 1. Get Evaluation Report Table Model
    public DefaultTableModel getEvaluationModel() {
        String[] cols = {"Student ID", "Total Score (Max 20)", "Status"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);

        // Ask Model for data
        Map<String, Double> scores = Report.getStudentScores();

        // Process data
        for (Map.Entry<String, Double> entry : scores.entrySet()) {
            double score = entry.getValue();
            String status = (score >= 10) ? "Pass" : "Fail";
            model.addRow(new Object[]{
                entry.getKey(), 
                String.format("%.2f", score), 
                status
            });
        }
        return model;
    }

    // 2. Generate Nomination Report Text
    public String getNominationReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== AWARD NOMINATION REPORT ===\n\n");

        // Ask Models for data
        ArrayList<String[]> sessions = SeminarSession.getAllSeminars();
        Map<String, Double> scores = Report.getStudentScores();

        // Logic: Map Student -> Session Type (to distinguish Oral vs Poster)
        Map<String, String> studentTypes = new HashMap<>();
        for (String[] s : sessions) {
            if (s.length >= 5) {
                studentTypes.put(s[4].trim(), s[0].trim());
            }
        }

        // Logic: Find Winners
        String bestOral = "None"; double maxOral = -1.0;
        String bestPoster = "None"; double maxPoster = -1.0;
        String peoplesChoice = "None"; double maxOverall = -1.0;

        for (Map.Entry<String, Double> entry : scores.entrySet()) {
            String std = entry.getKey();
            double score = entry.getValue();
            String type = studentTypes.getOrDefault(std, "Unknown");

            // Overall
            if (score > maxOverall) { 
                maxOverall = score; 
                peoplesChoice = std + " (" + score + ")"; 
            }

            // Categories
            if (type.contains("Paper") || type.contains("Oral")) {
                if (score > maxOral) { 
                    maxOral = score; 
                    bestOral = std + " (" + score + ")"; 
                }
            } else if (type.contains("Poster")) {
                if (score > maxPoster) { 
                    maxPoster = score; 
                    bestPoster = std + " (" + score + ")"; 
                }
            }
        }

        sb.append("BEST ORAL PRESENTATION:\n   Winner: " + bestOral + "\n\n");
        sb.append("BEST POSTER PRESENTATION:\n   Winner: " + bestPoster + "\n\n");
        sb.append("PEOPLE'S CHOICE AWARD:\n   Winner: " + peoplesChoice + "\n\n");

        return sb.toString();
    }
}
