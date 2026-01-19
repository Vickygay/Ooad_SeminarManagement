package controllers;

import models.Presentation;

public class EvaluationController {
    public void submitEvaluation(Presentation presentation, String score, String comments) {
        // Submit the evaluation and store the score and feedback
        presentation.setEvaluationScore(Double.parseDouble(score));
        presentation.setEvaluationComments(comments);
        System.out.println("Evaluation submitted: " + comments);
    }
}
