package views;

import java.awt.*;
import javax.swing.*;

public class CoordinatorDashboardView extends JFrame implements Dashboard 
{  // Implementing the Dashboard interface

    private String currentCoordinatorID;
    // to call the methods later
    private ManageSession manageSession;
    private SchedulePanel seminarSchedule;
    private ReportPanel report;
    private NominationPanel nomination;

    public CoordinatorDashboardView(String coordinatorID) {
        this.currentCoordinatorID = coordinatorID;
        super("FCI Seminar Management System - Coordinator Dashboard");
        setSize(1300, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(255, 255, 255));

        // Header Panel
        JPanel header = new JPanel();
        header.setBackground(new Color(51, 102, 0));
        JLabel title = new JLabel("Coordinator Portal: Seminar Management & Award Nomination Management");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        header.add(title);
        add(header, BorderLayout.NORTH);

        // Sidebar Navigation
        // Increased rows to 12 to provide space to push Logout to the bottom
        JPanel sidebar = new JPanel(new GridLayout(12, 1, 10, 10));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        sidebar.setPreferredSize(new Dimension(220, 700));
        
        JButton btn1 = new JButton("Manage Seminar Session");
        JButton btn2 = new JButton("Seminar Schedules");
        JButton btn3 = new JButton("Final evaluation reports");
        JButton btn4 = new JButton("Oversee Nomination");
        JButton btn5 = new JButton("Update Profile");
        JButton btn6 = new JButton("Logout"); // Logout button initialized
        
        Color greenColor = new Color(229, 255, 204);
        btn1.setBackground(greenColor);
        btn2.setBackground(greenColor);
        btn3.setBackground(greenColor);
        btn4.setBackground(greenColor);
        btn5.setBackground(greenColor);
        
        // Styling Logout Button
        btn6.setBackground(new Color(231, 76, 60)); // Red color
        btn6.setForeground(Color.WHITE);
        btn6.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn6.setFocusPainted(false);

        sidebar.add(btn1);
        sidebar.add(btn2);
        sidebar.add(btn3);
        sidebar.add(btn4);
        sidebar.add(btn5);

        // Added spacers to push the logout button to the bottom row
        sidebar.add(new JLabel(""));
        sidebar.add(new JLabel(""));
        sidebar.add(new JLabel(""));
        sidebar.add(new JLabel(""));
        sidebar.add(new JLabel(""));
        sidebar.add(new JLabel(""));
        
        sidebar.add(btn6); // Added logout button at the end

        add(sidebar, BorderLayout.WEST);
        
        // Panel for content
        JPanel content = new JPanel();
        content.setBackground(new Color(255, 255, 255));
        CardLayout cardLayout = new CardLayout();
        content.setLayout(cardLayout);

        manageSession = new ManageSession();
        seminarSchedule = new SchedulePanel();
        report = new ReportPanel();
        nomination = new NominationPanel();

        content.add(manageSession, "Manage Seminar");
        content.add(seminarSchedule, "Schedule");
        content.add(report, "Report");
        content.add(nomination, "Nomination");

        add(content, BorderLayout.CENTER);
        
        // to change the content based on button selected
        btn1.addActionListener(e -> cardLayout.show(content, "Manage Seminar"));
        btn2.addActionListener(e -> {((SchedulePanel) seminarSchedule).loadScheduleData();cardLayout.show(content, "Schedule");});
        btn3.addActionListener(e -> {((ReportPanel) report).generateReportData();cardLayout.show(content, "Report");});
        btn4.addActionListener(e -> {((NominationPanel) nomination).calculateNominations();cardLayout.show(content, "Nomination");});

        btn5.addActionListener(e -> {new ProfileUpdateView(currentCoordinatorID).setVisible(true);});

        // Logout Logic
        btn6.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to logout?", 
                "Logout Confirmation", 
                JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                // 1. Open the Login Interface
                // Replace 'LoginView' with the actual name of your login class
                new LoginView().setVisible(true); 
                
                // 2. Close ONLY the current dashboard window
                this.dispose(); 
                
                JOptionPane.showMessageDialog(null, "Successfully logged out.");
            }
        });

        setVisible(true);
    }

    @Override
    public void viewAwardee() {
        System.out.println("Viewing Coordinator Awardee...");
    }

    @Override
    public void viewNominates() {
        System.out.println("Viewing Coordinator Nominations...");
    }

}