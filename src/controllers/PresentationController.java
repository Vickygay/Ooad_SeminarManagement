package controllers;

import views.PresentationView;
import models.Student;

public class PresentationController {
    private PresentationView view;
    private Student student;

    public PresentationController(PresentationView view, Student student) {
        this.view = view;
        this.student = student;
    }

    public void registerPresentation(String title, String abstractText, String supervisor, String presentationType) {
        // Set the presentation details for the student
        student.setTitle(title);
        student.setAbstractText(abstractText);
        student.setSupervisorName(supervisor);
        student.setPresentationType(presentationType);

        // Register the presentation
        student.registerPresentation();

        // Update the view (this method needs to be defined in PresentationView)
        view.updateView(); // Assuming your PresentationView has a method like this
    }
}