package views;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.CompoundBorder;

public class CoordinatorDashboardView extends JFrame {  

    private String currentCoordinatorID;
    // to call the methods later
    private C_ManageSession manageSession;
    private C_SchedulePanel seminarSchedule;
    private C_ReportPanel report;
    private P_NominationPanel nomination;
    private P_AwardeePanel awardeePanel;

    private Color headerColor = new Color(51, 102, 0);
    private Color buttonColor = new Color(229, 255, 204);
    private Color whiteColor = Color.WHITE;

    public CoordinatorDashboardView(String coordinatorID) {
        this.currentCoordinatorID = coordinatorID;
        super("FCI Seminar Management System - Coordinator Dashboard - Logged in as: " + coordinatorID);
        setSize(1300, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(whiteColor);

        // Header Panel
        JPanel header = new JPanel();
        header.setBackground(headerColor);
        JLabel title = new JLabel("Coordinator Portal: Seminar Management & Award Nomination Management");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        header.add(title);
        add(header, BorderLayout.NORTH);

        // Sidebar Navigation
        // Increased rows to 12 to provide space to push Logout to the bottom
        JPanel sidebar = new JPanel(new GridLayout(12, 1, 10, 10));
        sidebar.setPreferredSize(new Dimension(280, 700));
        sidebar.setBackground(whiteColor);
        
        sidebar.setBorder(new CompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 2, Color.LIGHT_GRAY), 
            BorderFactory.createEmptyBorder(20, 10, 20, 10) 
        ));
        
        JButton btn1 = createSidebarButton("Manage Seminar Session");
        JButton btn2 = createSidebarButton("Seminar Schedules");
        JButton btn3 = createSidebarButton("Final evaluation reports");
        JButton btn4 = createSidebarButton("Nominee");
        JButton btnAwardee = createSidebarButton("Awardee");
        JButton btn5 = createSidebarButton("Update Profile");
        JButton btn6 = new JButton("Logout"); 
        
        // Styling Logout Button
        btn6.setBackground(new Color(231, 76, 60)); 
        btn6.setForeground(Color.WHITE);
        btn6.setFont(new Font("SansSerif", Font.BOLD, 15));
        btn6.setFocusPainted(false);

        sidebar.add(btn1);
        sidebar.add(btn2);
        sidebar.add(btn3);
        sidebar.add(btn4);
        sidebar.add(btnAwardee);
        sidebar.add(btn5);

        // Added spacers to push the logout button to the bottom row
        sidebar.add(new JLabel(""));
        sidebar.add(new JLabel(""));
        sidebar.add(new JLabel(""));
        sidebar.add(new JLabel(""));
        sidebar.add(new JLabel(""));
        
        sidebar.add(btn6); 

        add(sidebar, BorderLayout.WEST);
        
        // Panel for content
        JPanel content = new JPanel();
        content.setBackground(whiteColor);
        CardLayout cardLayout = new CardLayout();
        content.setLayout(cardLayout);

        manageSession = new C_ManageSession();
        seminarSchedule = new C_SchedulePanel();
        report = new C_ReportPanel();
        nomination = new P_NominationPanel();
        awardeePanel = new P_AwardeePanel();

        content.add(manageSession, "Manage Seminar");
        content.add(seminarSchedule, "Schedule");
        content.add(report, "Report");
        content.add(nomination, "Nomination");
        content.add(awardeePanel, "Awardee");

        add(content, BorderLayout.CENTER);
        
        // to change the content based on button selected
        btn1.addActionListener(e -> cardLayout.show(content, "Manage Seminar"));
        
        btn2.addActionListener(e -> {
            ((C_SchedulePanel) seminarSchedule).loadScheduleData();
            cardLayout.show(content, "Schedule");
        });
        
        btn3.addActionListener(e -> {
            ((C_ReportPanel) report).generateReportData();
            cardLayout.show(content, "Report");
        });
        
        btn4.addActionListener(e -> {
            ((P_NominationPanel) nomination).calculateNominations();
            cardLayout.show(content, "Nomination");
        });

        btnAwardee.addActionListener(e -> {
            awardeePanel.calculateWinners(); // Refresh data when clicked
            cardLayout.show(content, "Awardee");
        });

        btn5.addActionListener(e -> {
            new P_ProfileUpdateView(currentCoordinatorID).setVisible(true);
        });

        // Logout Logic
        btn6.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to logout?", 
                "Logout Confirmation", 
                JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                new LoginView().setVisible(true); 
                this.dispose(); 
                JOptionPane.showMessageDialog(null, "Successfully logged out.");
            }
        });

        setVisible(true);
    }

    private JButton createSidebarButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(buttonColor); 
        btn.setFont(new Font("SansSerif", Font.BOLD, 15));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 200), 1)); 
        return btn;
    }
}