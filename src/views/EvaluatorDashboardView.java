package views;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import models.Presentation;

public class EvaluatorDashboardView extends JFrame {

    // Define Colors
    private Color headerColor = new Color(153, 153, 255);
    private Color lightPurple = new Color(204, 204, 255);
    private Color whiteColor = Color.WHITE;

    private String currentEvaluatorID;

    public EvaluatorDashboardView(String evaluatorID) {
        this.currentEvaluatorID = evaluatorID;

        setTitle("Evaluator Dashboard - Logged in as: " + evaluatorID);
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
        JPanel sidebar = new JPanel(new GridLayout(8, 1, 10, 10));
        sidebar.setPreferredSize(new Dimension(280, 700));
        sidebar.setBackground(whiteColor);
        
        sidebar.setBorder(new CompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 2, Color.LIGHT_GRAY), 
            BorderFactory.createEmptyBorder(20, 10, 20, 10) 
        ));

        // Sidebar Buttons
        JButton btnAssigned = createSidebarButton("Assigned Presentation");
        JButton btnReview = createSidebarButton("Review Presentation");
        JButton btnFeedback = createSidebarButton("Feedback");
        JButton btnNominate = createSidebarButton("Nominate Student");
        JButton btnNominee = createSidebarButton("Nominee");
        JButton btnAwardee = createSidebarButton("Awardee");

        JButton btnLogout = new JButton("Logout");
        btnLogout.setFont(new Font("SansSerif", Font.BOLD, 15));
        btnLogout.setBackground(new Color(255, 102, 102)); // Light Red for Logout
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);

        sidebar.add(btnAssigned);
        sidebar.add(btnReview);
        sidebar.add(btnFeedback);
        sidebar.add(btnNominate);
        sidebar.add(btnNominee);
        sidebar.add(btnAwardee);
        add(sidebar, BorderLayout.WEST);
        sidebar.add(new JLabel()); 
        sidebar.add(btnLogout); 

        // --- 3. Content ---
        JPanel content = new JPanel();
        content.setBackground(whiteColor);
        CardLayout cardLayout = new CardLayout();
        content.setLayout(cardLayout);

        // Main Panels
        JPanel assignedPanel = new AssignedPanel(currentEvaluatorID);
        ReviewPanel reviewPanel = new ReviewPanel(currentEvaluatorID);

        // Placeholders for other features
        JPanel feedbackPanel = createPlaceholderPanel("General Feedback Page");
        JPanel nominatePanel = createPlaceholderPanel("Nominate Students for Awards");
        JPanel nomineePanel = createPlaceholderPanel("View Current Nominees");
        JPanel awardeePanel = createPlaceholderPanel("View Award Winners");

        content.add(assignedPanel, "Assigned");
        content.add(reviewPanel, "Review");
        content.add(feedbackPanel, "Feedback");
        content.add(nominatePanel, "Nominate");
        content.add(nomineePanel, "Nominee");
        content.add(awardeePanel, "Awardee");

        add(content, BorderLayout.CENTER);

        // --- 4. Button Logic ---
        btnAssigned.addActionListener(e -> cardLayout.show(content, "Assigned"));
        
        btnReview.addActionListener(e -> {
            cardLayout.show(content, "Review");
            // Refresh list immediately when clicking the tab
            reviewPanel.refreshStudentList(); 
        });

        btnFeedback.addActionListener(e -> cardLayout.show(content, "Feedback"));
        btnNominate.addActionListener(e -> cardLayout.show(content, "Nominate"));
        btnNominee.addActionListener(e -> cardLayout.show(content, "Nominee"));
        btnAwardee.addActionListener(e -> cardLayout.show(content, "Awardee"));

        btnLogout.addActionListener(e -> {
            // Confirm logout?
            int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if(choice == JOptionPane.YES_OPTION){
                new LoginView().setVisible(true); // Open Login Screen
                dispose(); // Close Dashboard
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

    private JPanel createPlaceholderPanel(String title) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        JLabel l = new JLabel(title, SwingConstants.CENTER);
        l.setFont(new Font("SansSerif", Font.BOLD, 24));
        p.add(l, BorderLayout.CENTER);
        return p;
    }
// =========================================================
    //  INNER CLASS 1: AssignedPanel (The Schedule Table)
    // =========================================================
    private class AssignedPanel extends JPanel {
        private JTable table;
        private DefaultTableModel model;
        private String myEvaluatorID;

        public AssignedPanel(String myEvaluatorID) {
            this.myEvaluatorID = myEvaluatorID;
            setLayout(new BorderLayout());
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

            // Title
            JLabel lblTitle = new JLabel("My Assigned Schedule");
            lblTitle.setFont(new Font("SansSerif", Font.BOLD, 28));
            lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
            add(lblTitle, BorderLayout.NORTH);

            // Table Columns
            String[] columns = {"Date", "Student ID", "Session Type", "Venue"};
            model = new DefaultTableModel(columns, 0);
            
            table = new JTable(model);
            table.setRowHeight(30);
            table.setFont(new Font("SansSerif", Font.PLAIN, 14));
            
            // Style the Header
            JTableHeader header = table.getTableHeader();
            header.setBackground(new Color(153, 153, 255)); // Header Purple
            header.setForeground(Color.WHITE);
            header.setFont(new Font("SansSerif", Font.BOLD, 16));

            JScrollPane scrollPane = new JScrollPane(table);
            add(scrollPane, BorderLayout.CENTER);

            // Load data initially
            loadData();
        }

        public void loadData() {
            model.setRowCount(0); // Clear existing data
            
            try (BufferedReader br = new BufferedReader(new FileReader("seminars.txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    // Format: SessionType,Venue,Date,EvaluatorID,StudentID
                    String[] parts = line.split(",");
                    if (parts.length >= 5) {
                        String fType = parts[0].trim();
                        String fVenue = parts[1].trim();
                        String fDate = parts[2].trim();
                        String fEvaluator = parts[3].trim();
                        String fStudent = parts[4].trim();

                        // Only show rows assigned to ME
                        if (fEvaluator.equalsIgnoreCase(myEvaluatorID)) {
                            model.addRow(new Object[]{fDate, fStudent, fType, fVenue});
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Error reading seminars.txt");
            }
        }
    }

    // --- INNER CLASS2: Review Panel (With Filtering Logic) ---
    private class ReviewPanel extends JPanel {
        private JComboBox<String> cbStudent;
        private String myEvaluatorID;

        public ReviewPanel(String myEvaluatorID) {
            this.myEvaluatorID = myEvaluatorID;
            setLayout(new GridBagLayout());
            setBackground(Color.WHITE);
            
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10); 
            gbc.anchor = GridBagConstraints.NORTHWEST; 

            // Title
            JLabel titleLabel = new JLabel("Review Presentations");
            titleLabel.setFont(new Font("SansSerif", Font.BOLD, 30));
            gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
            add(titleLabel, gbc);

            // Label
            gbc.gridy++; gbc.gridwidth = 1;
            JLabel lblStudent = new JLabel("Select Student:");
            lblStudent.setFont(new Font("SansSerif", Font.BOLD, 15));
            add(lblStudent, gbc);

            // Dropdown
            gbc.gridx = 1;
            cbStudent = new JComboBox<>();
            cbStudent.setPreferredSize(new Dimension(250, 30));
            add(cbStudent, gbc);

            // Load data initially
            refreshStudentList();

            // Start Button
            gbc.gridy++; gbc.gridx = 1;
            JButton btnStart = new JButton("Start Evaluation");
            btnStart.setBackground(new Color(204, 204, 255)); 
            btnStart.setForeground(Color.BLACK);
            btnStart.setFont(new Font("SansSerif", Font.BOLD, 14));
            add(btnStart, gbc);

            // --- Button Logic ---
            btnStart.addActionListener(e -> {
                String selected = (String) cbStudent.getSelectedItem();
                
                if (selected == null || selected.contains("--") || selected.equals("No Pending Evaluations")) {
                    JOptionPane.showMessageDialog(this, "Please select a valid student to evaluate.");
                } else {
                    System.out.println("Opening Evaluation Form for: " + selected);
                    
                    Presentation p = new Presentation();
                    p.setStudentID(selected); 
                    
                    EvaluationView evaluationWindow = new EvaluationView(p);
                    
                    // --- NEW: Refresh list when Evaluation window closes ---
                    evaluationWindow.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent e) {
                            refreshStudentList(); // Auto-remove graded student
                        }
                    });

                    evaluationWindow.setVisible(true);
                }
            });

            // Layout Pusher
            gbc.gridy++; gbc.gridx = 0; gbc.gridwidth = 2;
            gbc.weightx = 1; gbc.weighty = 1;
            gbc.fill = GridBagConstraints.BOTH;
            add(new JLabel(), gbc);
        }

        // --- Refresh Method ---
        public void refreshStudentList() {
            cbStudent.removeAllItems();
            String[] students = loadMyPendingStudents(myEvaluatorID);
            for (String s : students) {
                cbStudent.addItem(s);
            }
        }

        // --- Logic: Read Seminars & Filter out Evaluations ---
        private String[] loadMyPendingStudents(String evalID) {
            ArrayList<String> list = new ArrayList<>();
            list.add("-- Select Student --");

            // 1. Find who is already graded
            Set<String> evaluatedStudents = getEvaluatedStudentIDs();

            // 2. Read assigned seminars
            try (BufferedReader br = new BufferedReader(new FileReader("seminars.txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    // Format: SessionType,Venue,Date,EvaluatorID,StudentID
                    String[] parts = line.split(",");
                    if (parts.length >= 5) {
                        String fEvaluator = parts[3].trim();
                        String fStudent = parts[4].trim();

                        // Only add if assigned to ME and NOT yet graded
                        if (fEvaluator.equalsIgnoreCase(evalID)) {
                            if (!evaluatedStudents.contains(fStudent)) {
                                list.add(fStudent);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Error reading seminars.txt: " + e.getMessage());
            }

            if (list.size() == 1) {
                list.clear();
                list.add("No Pending Evaluations");
            }

            return list.toArray(new String[0]);
        }

        // --- Helper: Get list of ID's from evaluations.txt ---
        private Set<String> getEvaluatedStudentIDs() {
            Set<String> finishedIDs = new HashSet<>();
            try (BufferedReader br = new BufferedReader(new FileReader("evaluations.txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.contains("Student ID:")) {
                        String[] parts = line.split("Student ID:");
                        if (parts.length > 1) {
                            finishedIDs.add(parts[1].trim());
                        }
                    }
                }
            } catch (IOException e) {
                // File might not exist yet
            }
            return finishedIDs;
        }
    }
}