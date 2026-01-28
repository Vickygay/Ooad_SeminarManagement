package views;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
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
        JPanel sidebar = new JPanel(new GridLayout(12, 1, 10, 10));
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
        JButton btnProfile = createSidebarButton("Update Profile");
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
        sidebar.add(btnProfile);
        
        sidebar.add(new JLabel(""));
        sidebar.add(new JLabel(""));
        sidebar.add(new JLabel(""));
        sidebar.add(new JLabel(""));
        
        add(sidebar, BorderLayout.WEST);
        sidebar.add(btnLogout); 


        // --- 3. Content ---
        JPanel content = new JPanel();
        content.setBackground(whiteColor);
        CardLayout cardLayout = new CardLayout();
        content.setLayout(cardLayout);

        // Main Panels
        JPanel assignedPanel = new AssignedPanel(currentEvaluatorID);
        ReviewPanel reviewPanel = new ReviewPanel(currentEvaluatorID);
        JPanel feedbackPanel = new FeedbackPanel();        

        // Placeholders for other features
        JPanel nominatePanel = new NominatePanel(currentEvaluatorID);        
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
            reviewPanel.refreshStudentList(); 
        });

        btnFeedback.addActionListener(e -> cardLayout.show(content, "Feedback"));
        btnNominee.addActionListener(e -> cardLayout.show(content, "Nominee"));
        btnAwardee.addActionListener(e -> cardLayout.show(content, "Awardee"));


        btnNominate.addActionListener(e -> cardLayout.show(content, "Nominate"));

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

        btnProfile.addActionListener(e -> {
            new ProfileUpdateView(currentEvaluatorID).setVisible(true);
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
    //  INNER CLASS 1: AssignedPanel
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

            JLabel lblTitle = new JLabel("My Assigned Schedule");
            lblTitle.setFont(new Font("SansSerif", Font.BOLD, 28));
            lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
            add(lblTitle, BorderLayout.NORTH);

            String[] columns = {"Date", "Student ID", "Session Type", "Venue"};
            model = new DefaultTableModel(columns, 0);
            
            table = new JTable(model);
            table.setRowHeight(30);
            table.setFont(new Font("SansSerif", Font.PLAIN, 14));
            
            JTableHeader header = table.getTableHeader();
            header.setBackground(new Color(153, 153, 255));
            header.setForeground(Color.WHITE);
            header.setFont(new Font("SansSerif", Font.BOLD, 16));

            JScrollPane scrollPane = new JScrollPane(table);
            add(scrollPane, BorderLayout.CENTER);

            loadData();
        }

        public void loadData() {
            model.setRowCount(0);
            try (BufferedReader br = new BufferedReader(new FileReader("seminars.txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 5) {
                        String fType = parts[0].trim();
                        String fVenue = parts[1].trim();
                        String fDate = parts[2].trim();
                        String fEvaluator = parts[3].trim();
                        String fStudent = parts[4].trim();

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

    // =========================================================
    //  INNER CLASS 2: ReviewPanel
    // =========================================================
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

            JLabel titleLabel = new JLabel("Review Presentations");
            titleLabel.setFont(new Font("SansSerif", Font.BOLD, 30));
            gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
            add(titleLabel, gbc);

            gbc.gridy++; gbc.gridwidth = 1;
            JLabel lblStudent = new JLabel("Select Student:");
            lblStudent.setFont(new Font("SansSerif", Font.BOLD, 15));
            add(lblStudent, gbc);

            gbc.gridx = 1;
            cbStudent = new JComboBox<>();
            cbStudent.setPreferredSize(new Dimension(250, 30));
            add(cbStudent, gbc);

            refreshStudentList();

            gbc.gridy++; gbc.gridx = 1;
            JButton btnStart = new JButton("Start Evaluation");
            btnStart.setBackground(new Color(204, 204, 255)); 
            btnStart.setForeground(Color.BLACK);
            btnStart.setFont(new Font("SansSerif", Font.BOLD, 14));
            add(btnStart, gbc);

            btnStart.addActionListener(e -> {
                String selected = (String) cbStudent.getSelectedItem();
                
                if (selected == null || selected.contains("--") || selected.equals("No Pending Evaluations")) {
                    JOptionPane.showMessageDialog(this, "Please select a valid student to evaluate.");
                } else {
                    Presentation p = new Presentation();
                    p.setStudentID(selected); 
                    
                    EvaluationView evaluationWindow = new EvaluationView(p);
                    evaluationWindow.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent e) {
                            refreshStudentList(); 
                        }
                    });
                    evaluationWindow.setVisible(true);
                }
            });

            gbc.gridy++; gbc.gridx = 0; gbc.gridwidth = 2;
            gbc.weightx = 1; gbc.weighty = 1;
            gbc.fill = GridBagConstraints.BOTH;
            add(new JLabel(), gbc);
        }

        public void refreshStudentList() {
            cbStudent.removeAllItems();
            String[] students = loadMyPendingStudents(myEvaluatorID);
            for (String s : students) {
                cbStudent.addItem(s);
            }
        }

        private String[] loadMyPendingStudents(String evalID) {
            ArrayList<String> list = new ArrayList<>();
            list.add("-- Select Student --");

            Set<String> evaluatedStudents = getEvaluatedStudentIDs();

            try (BufferedReader br = new BufferedReader(new FileReader("seminars.txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 5) {
                        String fEvaluator = parts[3].trim();
                        String fStudent = parts[4].trim();

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
            } catch (IOException e) { }
            return finishedIDs;
        }
    }

    // =========================================================
    //  INNER CLASS 3: FeedbackPanel
    // =========================================================
    private class FeedbackPanel extends JPanel {
        private JTable table;
        private DefaultTableModel model;
        private ArrayList<String> feedbackDetailsList; 

        public FeedbackPanel() {
            setLayout(new BorderLayout());
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

            JLabel lblTitle = new JLabel("Evaluation History & Feedback");
            lblTitle.setFont(new Font("SansSerif", Font.BOLD, 28));
            lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
            add(lblTitle, BorderLayout.NORTH);

            String[] columns = {"Date", "Student ID", "Total Score", "Action"};
            model = new DefaultTableModel(columns, 0) {
                @Override 
                public boolean isCellEditable(int row, int column) {
                    return false; 
                }
            };

            table = new JTable(model);
            table.setRowHeight(35);
            table.setFont(new Font("SansSerif", Font.PLAIN, 14));
            
            JTableHeader header = table.getTableHeader();
            header.setBackground(new Color(153, 153, 255));
            header.setForeground(Color.WHITE);
            header.setFont(new Font("SansSerif", Font.BOLD, 15));

            JScrollPane scrollPane = new JScrollPane(table);
            add(scrollPane, BorderLayout.CENTER);

            feedbackDetailsList = new ArrayList<>();
            loadFeedbackData();

            table.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    if (e.getClickCount() == 2) { 
                        int row = table.getSelectedRow();
                        if (row != -1) {
                            showFeedbackDetails(row);
                        }
                    }
                }
            });
            
            JLabel hint = new JLabel("* Double-click a row to view the full detailed feedback.");
            hint.setForeground(Color.GRAY);
            add(hint, BorderLayout.SOUTH);
        }

        private void loadFeedbackData() {
            model.setRowCount(0);
            feedbackDetailsList.clear();

            try (BufferedReader br = new BufferedReader(new FileReader("evaluations.txt"))) {
                String line;
                String currentDate = "Unknown";
                String currentID = "Unknown";
                String currentScore = "0.0";
                StringBuilder currentFeedback = new StringBuilder();
                boolean isReadingFeedback = false;

                while ((line = br.readLine()) != null) {
                    if (line.contains("EVALUATION RECORD")) {
                        try {
                            String[] parts = line.split("\\|");
                            if (parts.length >= 3) {
                                currentDate = parts[1].replace("Date:", "").trim();
                                currentID = parts[2].replace("Student ID:", "").trim();
                            }
                        } catch (Exception e) {}
                        currentFeedback = new StringBuilder();
                        isReadingFeedback = false;
                    } 
                    else if (line.contains("TOTAL SCORE:")) {
                        currentScore = line.replace("TOTAL SCORE:", "").trim();
                    } 
                    else if (line.contains("DETAILED FEEDBACK:")) {
                        isReadingFeedback = true;
                    } 
                    else if (line.contains("==================") && isReadingFeedback) {
                        model.addRow(new Object[]{currentDate, currentID, currentScore, "View Details"});
                        feedbackDetailsList.add(currentFeedback.toString());
                        isReadingFeedback = false;
                    }
                    else if (isReadingFeedback) {
                        if (!line.trim().isEmpty()) {
                            currentFeedback.append(line).append("\n");
                        }
                    }
                }
                if (!currentID.equals("Unknown")) {
                    model.addRow(new Object[]{currentDate, currentID, currentScore, "View Details"});
                    feedbackDetailsList.add(currentFeedback.toString());
                }

            } catch (IOException e) { }
        }

        private void showFeedbackDetails(int index) {
            String details = feedbackDetailsList.get(index);
            JTextArea textArea = new JTextArea(details);
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            
            JScrollPane scroll = new JScrollPane(textArea);
            scroll.setPreferredSize(new Dimension(500, 300));
            
            JOptionPane.showMessageDialog(this, scroll, "Detailed Feedback", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // =========================================================
    //  INNER CLASS 4: NominatePanel (UPDATED WITH AWARD TYPES)
    // =========================================================
    private class NominatePanel extends JPanel {
        private JTable table;
        private DefaultTableModel model;
        private String myEvaluatorID;
        private JComboBox<String> awardTypeCombo; // Dropdown for Award

        public NominatePanel(String myEvaluatorID) {
            this.myEvaluatorID = myEvaluatorID;
            setLayout(new BorderLayout());
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

            // Header
            JLabel lblTitle = new JLabel("Nominate Students for Award");
            lblTitle.setFont(new Font("SansSerif", Font.BOLD, 28));
            lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
            add(lblTitle, BorderLayout.NORTH);

            // Table
            String[] columns = {"Student ID", "Evaluation Status", "Action"};
            model = new DefaultTableModel(columns, 0) {
                @Override public boolean isCellEditable(int row, int column) { return false; }
            };

            table = new JTable(model);
            table.setRowHeight(35);
            table.setFont(new Font("SansSerif", Font.PLAIN, 14));
            
            JTableHeader header = table.getTableHeader();
            header.setBackground(new Color(153, 153, 255));
            header.setForeground(Color.WHITE);
            header.setFont(new Font("SansSerif", Font.BOLD, 15));

            JScrollPane scrollPane = new JScrollPane(table);
            add(scrollPane, BorderLayout.CENTER);

            // Load Data
            loadEligibleStudents();

            // --- BOTTOM PANEL: Selection & Button ---
            JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
            bottomPanel.setBackground(Color.WHITE);

            // 1. Label
            bottomPanel.add(new JLabel("Select Award Category:"));

            // 2. Dropdown for Awards
            String[] awards = {"Best Presenter Award", "Best Research Content", "People's Choice Award"};
            awardTypeCombo = new JComboBox<>(awards);
            awardTypeCombo.setPreferredSize(new Dimension(200, 30));
            bottomPanel.add(awardTypeCombo);

            // 3. Button
            JButton btnNominate = new JButton("Nominate Selected Student");
            btnNominate.setBackground(new Color(255, 204, 102)); // Gold color
            btnNominate.setFont(new Font("SansSerif", Font.BOLD, 14));
            
            btnNominate.addActionListener(e -> nominateSelected());
            
            bottomPanel.add(btnNominate);
            add(bottomPanel, BorderLayout.SOUTH);
        }

        private void loadEligibleStudents() {
            model.setRowCount(0);
            Set<String> evaluatedStudents = getEvaluatedStudentIDs();

            try (BufferedReader br = new BufferedReader(new FileReader("seminars.txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 5) {
                        String fEvaluator = parts[3].trim();
                        String fStudent = parts[4].trim();

                        if (fEvaluator.equalsIgnoreCase(myEvaluatorID) && evaluatedStudents.contains(fStudent)) {
                            // Only show if I haven't nominated them yet (simplification)
                            model.addRow(new Object[]{fStudent, "Evaluated (Eligible)", "Select to Nominate"});
                        }
                    }
                }
            } catch (IOException e) { e.printStackTrace(); }
        }

        private void nominateSelected() {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a student from the list.");
                return;
            }

            String studentID = (String) model.getValueAt(row, 0);
            String selectedAward = (String) awardTypeCombo.getSelectedItem();
            
            // Check double nomination for specific award
            if (isAlreadyNominatedForAward(studentID, selectedAward)) {
                JOptionPane.showMessageDialog(this, "You have already nominated this student for " + selectedAward + "!");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, 
                "Nominate " + studentID + " for [" + selectedAward + "]?", 
                "Confirm Nomination", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                saveNomination(studentID, selectedAward);
            }
        }

        private void saveNomination(String studentID, String awardType) {
            try (java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.FileWriter("nominations.txt", true))) {
                // NEW FORMAT: StudentID, EvaluatorID, AwardType, Status
                bw.write(studentID + "," + myEvaluatorID + "," + awardType + ",Nominated");
                bw.newLine();
                JOptionPane.showMessageDialog(this, "Nomination saved successfully!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error saving nomination.");
            }
        }

        private Set<String> getEvaluatedStudentIDs() {
            Set<String> finishedIDs = new HashSet<>();
            try (BufferedReader br = new BufferedReader(new FileReader("evaluations.txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.contains("Student ID:")) {
                        String[] parts = line.split("Student ID:");
                        if (parts.length > 1) finishedIDs.add(parts[1].trim());
                    }
                }
            } catch (IOException e) {}
            return finishedIDs;
        }

        // Updated check to include Award Type
        private boolean isAlreadyNominatedForAward(String studentID, String awardType) {
            try (BufferedReader br = new BufferedReader(new FileReader("nominations.txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    // Check if *I* nominated *Student* for *Award*
                    // Format: StudentID, EvaluatorID, AwardType, Status
                    String[] parts = line.split(",");
                    if (parts.length >= 3) {
                        if (parts[0].equals(studentID) && parts[1].equals(myEvaluatorID) && parts[2].equals(awardType)) {
                            return true;
                        }
                    }
                }
            } catch (IOException e) {}
            return false;
        }
    }
}