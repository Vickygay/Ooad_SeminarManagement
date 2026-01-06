package views;

import javax.swing.*;

public class StudentDashboardView extends JFrame implements Dashboard {  // Implementing the Dashboard interface

    public StudentDashboardView() {
        super();
        setTitle("Student Dashboard");
        setSize(1300, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Sample content for the student dashboard
        JLabel label = new JLabel("Welcome to the Student Dashboard!", JLabel.CENTER);
        add(label);

        // More components can be added here for the student role

        setVisible(true);
    }

    @Override
    public void updateProfile() {
        // Logic for updating student profile
        System.out.println("Updating Student Profile...");
    }

    @Override
    public void viewAwardee() {
        // Logic for viewing awardee in the student dashboard
        System.out.println("Viewing Student Awardee...");
    }

    @Override
    public void viewNominates() {
        // Logic for viewing nominations in the student dashboard
        System.out.println("Viewing Student Nominations...");
    }
}
