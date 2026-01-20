package models;

public class Presentation {
    // 1. New field for Student ID
    private String studentID; 
    private double evaluationScore;
    private String evaluationComments;

    // --- New Getters and Setters for Student ID ---
    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getStudentID() {
        return studentID;
    }

    // --- Existing Getters and Setters ---
    public void setEvaluationScore(double score) {
        this.evaluationScore = score;
    }

    public double getEvaluationScore() {
        return evaluationScore;
    }

    public void setEvaluationComments(String comments) {
        this.evaluationComments = comments;
    }

    public String getEvaluationComments() {
        return evaluationComments;
    }
}