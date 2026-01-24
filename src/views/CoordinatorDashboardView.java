package views;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;

public class CoordinatorDashboardView extends JFrame implements Dashboard {  // Implementing the Dashboard interface

    private String currentCoordinatorID;
    private Color headerColor = new Color(51, 102, 0);
    private Color sideBarColor = new Color(229, 255, 204);
    private Color whiteColor = Color.WHITE;

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

        // Manage Session
        JPanel manageSession = new ManageSession();
        
        // Seminar
        JPanel seminarSchedule = new SchedulePanel();
        
        JPanel report = new ReportPanel();

        JPanel nomination = new NominationPanel();

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

    private class ManageSession extends JPanel {
        private String[] getUsers(String role) {
            java.util.ArrayList<String> list = new java.util.ArrayList<>();
            list.add("-- Select " + role + " --");

            try {
                java.util.Scanner sc = new java.util.Scanner(new java.io.File("users.txt"));
                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    String[] parts = line.split(",");

                    // parts[2] means the 3rd element in the users.txt, which is the role
                    if (parts.length >= 3 && parts[2].trim().equalsIgnoreCase(role)) {
                        list.add(parts[0].trim());
                    }
                }
                sc.close();
            } catch (Exception e) {
                // If error, just ignore or print to console
                System.out.println("File error"); 
            }
            // convert into array, to be put into the dropdown list later
            return list.toArray(new String[0]);
        }

        public ManageSession() {
            setLayout(new GridBagLayout());
            setBackground(new Color(255, 255, 255));
            setOpaque(true);
        
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.anchor = GridBagConstraints.NORTHWEST;

            //Title
            JLabel title1 = new JLabel("Manage Sessions");
            title1.setFont(new Font("SansSerif", Font.BOLD, 30));
            gbc.gridx = 0; //left column
            gbc.gridy = 0; //first row
            gbc.gridwidth = 2; //span two column
            add(title1, gbc);
           
           //Session Type
            gbc.gridwidth = 1;
            gbc.gridy++; //go to next row
            gbc.gridx = 0;

            JLabel sessionTitle = new JLabel("Session Type:");
            sessionTitle.setFont(new Font("SansSerif", Font.BOLD, 15));
            add(sessionTitle, gbc);
            
            gbc.gridx = 1;
            String[] sessionType = {"Paper Presentation Session", "Expert Lecture", "Poster Session"};
            JComboBox<String> cb = new JComboBox<>(sessionType);
            add(cb, gbc);

            // Venue
            gbc.gridy++; gbc.gridx = 0;
            JLabel venueTitle = new JLabel("Venue:");
            venueTitle.setFont(new Font("SansSerif", Font.BOLD, 15));
            add(venueTitle, gbc);

            gbc.gridx = 1;
            JTextField venueTextField = new JTextField(40);
            add(venueTextField, gbc);

            // Date
            gbc.gridy++; 
            gbc.gridx = 0;
            JLabel dateTitle = new JLabel("Date:");
            dateTitle.setFont(new Font("SansSerif", Font.BOLD, 15));
            add(dateTitle, gbc);

            gbc.gridx = 1;
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            JFormattedTextField dateField = new JFormattedTextField(dateFormat);
            dateField.setValue(new Date());
            dateField.setColumns(10);
            add(dateField, gbc);

            // Assign Evaluator
            gbc.gridy++; //go to next row
            gbc.gridx = 0;

            JLabel assignEvaluator = new JLabel("Evaluator:");
            assignEvaluator.setFont(new Font("SansSerif", Font.BOLD, 15));
            add(assignEvaluator, gbc);
            
            gbc.gridx = 1;
            String[] evaluators = getUsers("Evaluator"); //get only the role evaluator
            JComboBox<String> evaluatorBox = new JComboBox<>(evaluators);
            add(evaluatorBox, gbc);

            // Assign Students
            gbc.gridy++; //go to next row
            gbc.gridx = 0;
            JLabel assignStudent = new JLabel("Student involved:"); 
            assignStudent.setFont(new Font("SansSerif", Font.BOLD, 15));
            add(assignStudent, gbc);
            
            gbc.gridx = 1;
            String[] students = getUsers("Student");
            JComboBox<String> studentBox = new JComboBox<>(students);
            add(studentBox, gbc);        
           
           // Save button that will fetch all data and then save to text file
            gbc.gridy++; 
            gbc.gridx = 1;
            gbc.anchor = GridBagConstraints.WEST;
            JButton submit = new JButton("Save");
            submit.setBackground(new Color(0, 128, 0));
            submit.setForeground(Color.WHITE);
            add(submit, gbc);

            submit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String venue = venueTextField.getText();
                    String date = dateField.getText();

                    if(venue.isEmpty() || date.isEmpty()) {
                        JOptionPane.showMessageDialog(ManageSession.this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    try {
                        java.io.FileWriter writer = new java.io.FileWriter("seminars.txt", true);
                        java.io.BufferedWriter bufferedWriter = new java.io.BufferedWriter(writer);

                        // separate info by ","
                        bufferedWriter.write((String)cb.getSelectedItem() + "," + venue + "," + date + "," + (String)evaluatorBox.getSelectedItem() + "," + (String)studentBox.getSelectedItem());
                        bufferedWriter.newLine(); // move to next line
                        bufferedWriter.close();

                        JOptionPane.showMessageDialog(ManageSession.this, "Session Saved Successfully!");

                        // clear fields after reading
                        venueTextField.setText("");

                    } catch (java.io.IOException ex) { //display error message
                        JOptionPane.showMessageDialog(ManageSession.this, "Error saving file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            
            // added so that the form appearing on top left: move to next row, takes up remaining space
            gbc.gridy++; 
            gbc.weightx = 1; 
            gbc.weighty = 1;
            gbc.fill = GridBagConstraints.BOTH;
            add(new JLabel(), gbc);
        }
    }
//-------- Seminar Schedule Panel ----------------//
    private class SchedulePanel extends JPanel {
        private DefaultTableModel model;

        public SchedulePanel() {
            setLayout(new BorderLayout());
            setBackground(whiteColor);
            setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JLabel lblTitle = new JLabel("Seminar Schedules");
            lblTitle.setFont(new Font("SansSerif", Font.BOLD, 28));
            lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
            add(lblTitle, BorderLayout.NORTH);

            String[] cols = {"Date", "Type", "Venue", "Student", "Evaluator"};
            model = new DefaultTableModel(cols, 0);
            JTable table = new JTable(model);
            styleTable(table);
            
            add(new JScrollPane(table), BorderLayout.CENTER);
        }

        public void loadScheduleData() {
            model.setRowCount(0);
            try (BufferedReader br = new BufferedReader(new FileReader("seminars.txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    // Format: SessionType,Venue,Date,Evaluator,Student
                    String[] p = line.split(",");
                    if (p.length >= 5) {
                        model.addRow(new Object[]{p[2], p[0], p[1], p[4], p[3]});
                    }
                }
            } catch (IOException e) {
                System.out.println("No seminars found.");
            }
        }
    }

//-------- Report Panel ----------------//
    private class ReportPanel extends JPanel {
        private DefaultTableModel model;

        public ReportPanel() {
            setLayout(new BorderLayout());
            setBackground(whiteColor);
            setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JLabel lblTitle = new JLabel("Final Evaluation Reports");
            lblTitle.setFont(new Font("SansSerif", Font.BOLD, 28));
            lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
            add(lblTitle, BorderLayout.NORTH);

            String[] cols = {"Student ID", "Total Score (Max 20)", "Status"};
            model = new DefaultTableModel(cols, 0);
            JTable table = new JTable(model);
            styleTable(table);

            add(new JScrollPane(table), BorderLayout.CENTER);
        }

        public void generateReportData() {
            model.setRowCount(0);
            // Reads evaluations.txt and extracts scores
            try (BufferedReader br = new BufferedReader(new FileReader("evaluations.txt"))) {
                String line;
                String currentStudent = null;
                String currentScore = null;

                while ((line = br.readLine()) != null) {
                    if (line.contains("Student ID:")) {
                        // Parse "EVALUATION RECORD | ... | Student ID: std123"
                        String[] parts = line.split("Student ID:");
                        if (parts.length > 1) currentStudent = parts[1].trim();
                    } 
                    else if (line.contains("TOTAL SCORE:")) {
                        // Parse "TOTAL SCORE: 17.00 / 20.00"
                        String[] parts = line.split("/");
                        String scorePart = parts[0].replace("TOTAL SCORE:", "").trim();
                        currentScore = scorePart;
                    }

                    // Once we have both ID and Score, add to table
                    if (currentStudent != null && currentScore != null) {
                        double scoreVal = Double.parseDouble(currentScore);
                        String status = (scoreVal >= 10) ? "Pass" : "Fail"; // Pass if >= 50%
                        model.addRow(new Object[]{currentStudent, currentScore, status});
                        
                        currentStudent = null;
                        currentScore = null;
                    }
                }
            } catch (Exception e) {
                // Ignore errors
            }
        }
    }

//-------- Nomination Panel ----------------//
    private class NominationPanel extends JPanel {
        private JTextArea resultsArea;

        public NominationPanel() {
            setLayout(new BorderLayout());
            setBackground(whiteColor);
            setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JLabel lblTitle = new JLabel("Award Nominations Overseer");
            lblTitle.setFont(new Font("SansSerif", Font.BOLD, 28));
            add(lblTitle, BorderLayout.NORTH);

            resultsArea = new JTextArea();
            resultsArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
            resultsArea.setEditable(false);
            resultsArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            add(new JScrollPane(resultsArea), BorderLayout.CENTER);
        }

        public void calculateNominations() {
            StringBuilder sb = new StringBuilder();
            sb.append("=== AWARD NOMINATION REPORT ===\n\n");

            // 1. Map StudentID -> Session Type
            Map<String, String> studentTypes = new HashMap<>();
            try (BufferedReader br = new BufferedReader(new FileReader("seminars.txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] p = line.split(",");
                    if (p.length >= 5) {
                        studentTypes.put(p[4].trim(), p[0].trim());
                    }
                }
            } catch (Exception e) {}

            // 2. Map StudentID -> Highest Score
            Map<String, Double> scores = new HashMap<>();
            try (BufferedReader br = new BufferedReader(new FileReader("evaluations.txt"))) {
                String line;
                String currentStudent = null;
                while ((line = br.readLine()) != null) {
                    if (line.contains("Student ID:")) {
                        currentStudent = line.split("Student ID:")[1].trim();
                    } else if (line.contains("TOTAL SCORE:") && currentStudent != null) {
                        String s = line.split("/")[0].replace("TOTAL SCORE:", "").trim();
                        double score = Double.parseDouble(s);
                        if (score > scores.getOrDefault(currentStudent, 0.0)) {
                            scores.put(currentStudent, score);
                        }
                    }
                }
            } catch (Exception e) {}

            // 3. Determine Winners
            String bestOral = "None";
            double maxOral = -1.0;
            String bestPoster = "None";
            double maxPoster = -1.0;
            String peoplesChoice = "None"; 
            double maxOverall = -1.0;

            for (Map.Entry<String, Double> entry : scores.entrySet()) {
                String std = entry.getKey();
                Double score = entry.getValue();
                String type = studentTypes.getOrDefault(std, "Unknown");

                if (score > maxOverall) {
                    maxOverall = score;
                    peoplesChoice = std + " (" + score + ")";
                }

                if (type.contains("Paper") || type.contains("Oral")) { 
                    if (score > maxOral) {
                        maxOral = score;
                        bestOral = std + " (" + score + ")";
                    }
                } else if (type.contains("Poster")) { 
                    if (score > maxPoster) {
                        maxPoster = score;
                        bestPoster = std + " (" + score + ")";
                    }
                }
            }

            sb.append("BEST ORAL PRESENTATION:\n");
            sb.append("   Winner: ").append(bestOral).append("\n\n");

            sb.append("BEST POSTER PRESENTATION:\n");
            sb.append("   Winner: ").append(bestPoster).append("\n\n");

            sb.append("PEOPLE'S CHOICE AWARD:\n");
            sb.append("   Winner: ").append(peoplesChoice).append("\n\n");

            resultsArea.setText(sb.toString());
        }
    }

    private void styleTable(JTable table) {
            JTableHeader header = table.getTableHeader();
            header.setBackground(headerColor);
            header.setForeground(Color.WHITE);
            header.setFont(new Font("SansSerif", Font.BOLD, 14));
            table.setRowHeight(25);
            table.setFont(new Font("SansSerif", Font.PLAIN, 12));
    }
}