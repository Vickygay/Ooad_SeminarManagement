package models;

public class Presentation {
    private double evaluationScore;
    private String evaluationComments;

    // Setters for evaluation score and comments
    public void setEvaluationScore(double score) {
        this.evaluationScore = score;
    }

    public void setEvaluationComments(String comments) {
        this.evaluationComments = comments;
    }

    // Getters for evaluation score and comments (if needed)
    public double getEvaluationScore() {
        return evaluationScore;
    }

    public String getEvaluationComments() {
        return evaluationComments;
    }
}
