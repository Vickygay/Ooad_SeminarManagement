package models;
import java.io.*;
import java.util.ArrayList;

public class SeminarSession {
    private String sessionID;
    private String sessionDate;
    private String sessionVenue;
    private String sessionType;

    // Constructor
    public SeminarSession(String sessionID, String sessionDate, String sessionVenue, String sessionType) {
        this.sessionID = sessionID;
        this.sessionDate = sessionDate;
        this.sessionVenue = sessionVenue;
        this.sessionType = sessionType;
    }

    public void createSession() {
        // Example session creation logic
        System.out.println("Creating seminar session with the following details:");
        System.out.println("Session ID: " + this.sessionID);
        System.out.println("Session Date: " + this.sessionDate);
        System.out.println("Session Venue: " + this.sessionVenue);
        System.out.println("Session Type: " + this.sessionType);

        // You could also save this information to a database or a file if needed
    }

    public static ArrayList<String[]> getAllSeminars() {
        ArrayList<String[]> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("seminars.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                // File format: Type,Venue,Date,Evaluator,Student
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    list.add(parts);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading seminars: " + e.getMessage());
        }
        return list;
    }
}
