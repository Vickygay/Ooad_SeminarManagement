package views;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.CompoundBorder;

public class StudentDashboardView extends JFrame {
    
    private String currentStudentID;

    // --- COLORS (Requested) ---
    private Color headerColor = new Color(0, 102, 204);     // Darker Blue
    private Color buttonColor = new Color(153, 204, 255);   // Lighter Blue
    private Color whiteColor = Color.WHITE;

    public StudentDashboardView(String studentID) {
        this.currentStudentID = studentID;
        setTitle("FCI Seminar Management System - Student Dashboard - Logged in as: " + studentID);
        setSize(1300, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(whiteColor);

        // =========================================================
        // 1. HEADER PANEL
        // =========================================================
        JPanel header = new JPanel();
        header.setBackground(headerColor);
        JLabel title = new JLabel("Student Portal: Seminar Registration & Uploads");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        header.add(title);
        add(header, BorderLayout.NORTH);

        // =========================================================
        // 2. SIDEBAR NAVIGATION
        // =========================================================
        JPanel sidebar = new JPanel(new GridLayout(12, 1, 10, 10)); 
        sidebar.setPreferredSize(new Dimension(280, 700));
        sidebar.setBackground(whiteColor);
        
        sidebar.setBorder(new CompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 2, Color.LIGHT_GRAY), 
            BorderFactory.createEmptyBorder(20, 10, 20, 10) 
        ));

        // Create Buttons
        JButton btnReg = createSidebarButton("Registration Form");
        JButton btnViewReg = createSidebarButton("My Registration"); // NEW BUTTON
        JButton btnNominee = createSidebarButton("View Nominee"); 
        JButton btnAwardee = createSidebarButton("View Awardee"); 
        JButton btnProfile = createSidebarButton("Update Profile"); 
        JButton btnLogout = new JButton("Logout"); 

        // Styling Logout Button
        btnLogout.setBackground(new Color(255, 102, 102)); 
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFont(new Font("SansSerif", Font.BOLD, 15));
        btnLogout.setFocusPainted(false);
        
        sidebar.add(btnReg);
        sidebar.add(btnViewReg); // Add New Button
        sidebar.add(btnNominee);
        sidebar.add(btnAwardee);
        sidebar.add(btnProfile);
        
        // Spacers
        sidebar.add(new JLabel(""));
        sidebar.add(new JLabel(""));
        sidebar.add(new JLabel(""));
        sidebar.add(new JLabel(""));
        sidebar.add(new JLabel(""));
        sidebar.add(new JLabel("")); // 6th spacer

        sidebar.add(btnLogout); 

        add(sidebar, BorderLayout.WEST);

        // =========================================================
        // 3. CONTENT PANEL (CardLayout)
        // =========================================================
        JPanel content = new JPanel();
        content.setBackground(whiteColor);
        CardLayout cardLayout = new CardLayout();
        content.setLayout(cardLayout);

        // --- Panels ---
        S_RegistrationPanel regPanel = new S_RegistrationPanel(currentStudentID);
        // We initialize this, but we will "refresh" it when the button is clicked to get latest data
        S_ViewRegistrationPanel viewRegPanel = new S_ViewRegistrationPanel(currentStudentID); 
        P_NominationPanel nomineePanel = new P_NominationPanel();
        P_AwardeePanel awardeePanel = new P_AwardeePanel();

        // Add to CardLayout
        content.add(regPanel, "Registration");
        content.add(viewRegPanel, "ViewRegistration"); // Add to cards
        content.add(nomineePanel, "Nominee");
        content.add(awardeePanel, "Awardee");

        add(content, BorderLayout.CENTER);

        // =========================================================
        // 4. BUTTON LOGIC
        // =========================================================
        btnReg.addActionListener(e -> cardLayout.show(content, "Registration"));
        
        // NEW: Logic for View Registration Button
        btnViewReg.addActionListener(e -> {
            // Re-create the panel to ensure it fetches the latest data from the file
            // Otherwise, if they just registered, the old panel won't show the new data.
            S_ViewRegistrationPanel newPanel = new S_ViewRegistrationPanel(currentStudentID);
            content.add(newPanel, "ViewRegistration"); 
            cardLayout.show(content, "ViewRegistration");
        });

        btnNominee.addActionListener(e -> {
            nomineePanel.calculateNominations(); 
            cardLayout.show(content, "Nominee");
        });

        btnAwardee.addActionListener(e -> {
            awardeePanel.calculateWinners(); // Calculate fresh results
            cardLayout.show(content, "Awardee");
        });

        btnProfile.addActionListener(e -> {
            new P_ProfileUpdateView(currentStudentID).setVisible(true);
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
        btn.setBackground(buttonColor); 
        btn.setFont(new Font("SansSerif", Font.BOLD, 15));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 200), 1)); 
        return btn;
    }

}