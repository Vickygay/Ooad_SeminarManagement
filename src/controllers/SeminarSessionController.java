package controllers;

import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import models.SeminarSession;

public class SeminarSessionController {
    public void createSession(SeminarSession session) {
        session.createSession();
    }

    public DefaultTableModel getScheduleModel() {
        // Define Column Names
        String[] cols = {"Date", "Type", "Venue", "Student", "Evaluator"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);

        // 1. Ask Model for raw data
        ArrayList<String[]> data = SeminarSession.getAllSeminars();

        // 2. Format data for the View
        for (String[] row : data) {
            // Text file order: [Type, Venue, Date, Evaluator, Student]
            // Table order:     [Date, Type, Venue, Student, Evaluator]
            if (row.length >= 5) {
                model.addRow(new Object[]{
                    row[2], // Date
                    row[0], // Type
                    row[1], // Venue
                    row[4], // Student
                    row[3]  // Evaluator
                });
            }
        }
        return model;
    }
}
