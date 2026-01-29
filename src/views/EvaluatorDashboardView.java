package views;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.CompoundBorder;

public class EvaluatorDashboardView extends JFrame {

    private Color headerColor = new Color(153, 153, 255);
    private Color lightPurple = new Color(204, 204, 255);
    private Color whiteColor = Color.WHITE;

    private String currentEvaluatorID;

    public EvaluatorDashboardView(String evaluatorID) {
        this.currentEvaluatorID = evaluatorID;

        setTitle("FCI Seminar Management System - Evaluator Dashboard - Logged in as: " + evaluatorID);
        setSize(1300, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(whiteColor);

        // --- 1. Header ---
        JPanel header = new JPanel();
        header.setBackground(headerColor);
        JLabel title = new JLabel("Evaluator Portal: Presentation Review & Awards");
        title.setForeground(Color.WHITE); 
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        header.add(title);
        add(header, BorderLayout.NORTH);

        // --- 2. Sidebar ---
        JPanel sidebar = new JPanel(new GridLayout(12, 1, 10, 10));
        sidebar.setPreferredSize(new Dimension(280, 700));
        sidebar.setBackground(whiteColor);
        sidebar.setBorder(new CompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 2, Color.LIGHT_GRAY), 
            BorderFactory.createEmptyBorder(20, 10, 20, 10) 
        ));

        JButton btnAssigned = createSidebarButton("Assigned Presentation");
        JButton btnReview = createSidebarButton("Review Presentation");
        JButton btnFeedback = createSidebarButton("Feedback");
        JButton btnNominate = createSidebarButton("Nominate Student");
        JButton btnNominee = createSidebarButton("Nominee");
        JButton btnAwardee = createSidebarButton("Awardee");
        JButton btnProfile = createSidebarButton("Update Profile");
        JButton btnLogout = new JButton("Logout");

        btnLogout.setFont(new Font("SansSerif", Font.BOLD, 15));
        btnLogout.setBackground(new Color(255, 102, 102));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);

        sidebar.add(btnAssigned);
        sidebar.add(btnReview);
        sidebar.add(btnFeedback);
        sidebar.add(btnNominate);
        sidebar.add(btnNominee);
        sidebar.add(btnAwardee);
        sidebar.add(btnProfile);
        
        sidebar.add(new JLabel(""));
        sidebar.add(new JLabel(""));
        sidebar.add(new JLabel(""));
        sidebar.add(new JLabel(""));
        
        add(sidebar, BorderLayout.WEST);
        sidebar.add(btnLogout); 

        // --- 3. Content Panel ---
        JPanel content = new JPanel();
        content.setBackground(whiteColor);
        CardLayout cardLayout = new CardLayout();
        content.setLayout(cardLayout);

        // --- NEW: Using Separate File Classes ---
        // Note: Assuming you have created E_AssignedPanel and E_ReviewPanel classes as well. 
        // If not, change these back to AssignedPanel / ReviewPanel.
        E_AssignedPanel assignedPanel = new E_AssignedPanel(currentEvaluatorID);
        E_ReviewPanel reviewPanel = new E_ReviewPanel(currentEvaluatorID);
        E_FeedbackPanel feedbackPanel = new E_FeedbackPanel();        
        E_NominatePanel nominatePanel = new E_NominatePanel(currentEvaluatorID);  
        P_NominationPanel nomineePanel = new P_NominationPanel(); 
        P_AwardeePanel awardeePanel = new P_AwardeePanel();

        content.add(assignedPanel, "Assigned");
        content.add(reviewPanel, "Review");
        content.add(feedbackPanel, "Feedback");
        content.add(nominatePanel, "Nominate");
        content.add(nomineePanel, "Nominee");
        content.add(awardeePanel, "Awardee");

        add(content, BorderLayout.CENTER);

        // --- 4. Button Logic ---
        btnAssigned.addActionListener(e -> {
            assignedPanel.loadData(); // This refreshes and removes evaluated students
            cardLayout.show(content, "Assigned");
        });
        
        btnReview.addActionListener(e -> {
            // Check if E_ReviewPanel has refreshStudentList, if not, remove this line
            reviewPanel.refreshStudentList(); 
            cardLayout.show(content, "Review");
        });

        // FIX 3: Refresh Data when buttons are clicked
        btnFeedback.addActionListener(e -> {
            feedbackPanel.refreshData(); // Now works!
            cardLayout.show(content, "Feedback");
        });

        btnNominate.addActionListener(e -> {
            nominatePanel.refreshData(); // Now works!
            cardLayout.show(content, "Nominate");
        });

        btnNominee.addActionListener(e -> {
            nomineePanel.calculateNominations(); // Refresh the viewer list
            cardLayout.show(content, "Nominee");
        });
        
        btnAwardee.addActionListener(e -> {
            awardeePanel.calculateWinners(); // Calculate fresh results
            cardLayout.show(content, "Awardee");
        });

        btnProfile.addActionListener(e -> {
            new P_ProfileUpdateView(currentEvaluatorID).setVisible(true);
        });

        btnLogout.addActionListener(e -> {
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
        btn.setBackground(lightPurple); 
        btn.setFont(new Font("SansSerif", Font.BOLD, 15));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 200), 1)); 
        return btn;
    }

}