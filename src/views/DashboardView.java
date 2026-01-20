package views;

// In views/DashboardView.java
import java.awt.*;
import javax.swing.*;

public class DashboardView extends JFrame implements Dashboard {
    public DashboardView() {
        setTitle("Dashboard");
        setSize(1300, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Example of common dashboard layout
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel label = new JLabel("Welcome to the Dashboard!", JLabel.CENTER);
        panel.add(label, BorderLayout.CENTER);

        add(panel);
    }

    @Override
    public void viewAwardee() {
        // Default view awardee logic (if any)
        System.out.println("Viewing Awardee...");
    }

    @Override
    public void viewNominates() {
        // Default view nominations logic (if any)
        System.out.println("Viewing Nominations...");
    }
}