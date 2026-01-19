package views;

import javax.swing.*;

public class CoordinatorDashboardView extends JFrame implements Dashboard {  // Implementing the Dashboard interface

    public CoordinatorDashboardView() {
        setTitle("Coordinator Dashboard");
        setSize(1300, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Sample content for the coordinator dashboard
        JLabel label = new JLabel("Welcome to the Coordinator Dashboard!", JLabel.CENTER);
        add(label);

        // More components can be added here for the coordinator role

        setVisible(true);
    }

    @Override
    public void updateProfile() {
        // Logic for updating coordinator profile
        System.out.println("Updating Coordinator Profile...");
    }

    @Override
    public void viewAwardee() {
        // Logic for viewing awardee in the coordinator dashboard
        System.out.println("Viewing Coordinator Awardee...");
    }

    @Override
    public void viewNominates() {
        // Logic for viewing nominations in the coordinator dashboard
        System.out.println("Viewing Coordinator Nominations...");
    }
}
