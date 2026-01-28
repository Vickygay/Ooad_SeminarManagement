package views;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import controllers.SeminarSessionController;
import controllers.ReportController;

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

    private class ManageSession extends JPanel
    {
        private String[] getEvaluators() 
        {
            java.util.ArrayList<String> list = new java.util.ArrayList<>();
            list.add("-- Select Evaluator --"); // default option

            try {
                java.util.Scanner sc = new java.util.Scanner(new java.io.File("users.txt"));
                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    String[] parts = line.split(",");

                    // parts[2] means the 3rd element in the users.txt, which is the role
                    if (parts.length >= 3 && parts[2].trim().equalsIgnoreCase("Evaluator")) {
                        list.add(parts[0].trim()); //extract out the roles
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

        private String[] getStudents() 
        {
            java.util.ArrayList<String> list = new java.util.ArrayList<>();
            list.add("-- Select student --"); // default option

            try {
                java.util.Scanner sc = new java.util.Scanner(new java.io.File("registrations.txt"));
                
                while (sc.hasNextLine()) { //keep reading, until the end of the line
                    String line = sc.nextLine();
                    String[] parts = line.split("\\|"); //split parts by the "|"
                    
                    // parts[0] means the 1st element in the registrations.txt, which is the student id
                    if (parts.length >= 6) {
                        list.add(parts[0].trim()); //extract out the roles
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

        public ManageSession()
        {
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
            String[] sessionType = {"Oral Session", "Poster Session"};
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
            String[] evaluators = getEvaluators(); //get only the role evaluator
            JComboBox<String> evaluatorBox = new JComboBox<>(evaluators);
            add(evaluatorBox, gbc);

            // Assign Students
            gbc.gridy++; //go to next row
            gbc.gridx = 0;
            JLabel assignStudent = new JLabel("Student involved:"); 
            assignStudent.setFont(new Font("SansSerif", Font.BOLD, 15));
            add(assignStudent, gbc);
            
            gbc.gridx = 1;
            String[] students = getStudents(); //get only the role student
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
        private JTable table;
        private SeminarSessionController controller = new SeminarSessionController(); // Use Controller

        public SchedulePanel() {
            setLayout(new BorderLayout());
            setBackground(whiteColor);
            setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JLabel lblTitle = new JLabel("Seminar Schedules");
            lblTitle.setFont(new Font("SansSerif", Font.BOLD, 28));
            lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
            add(lblTitle, BorderLayout.NORTH);

            table = new JTable();
            styleTable(table); // Your existing style helper
            add(new JScrollPane(table), BorderLayout.CENTER);
            
            loadScheduleData(); // Initial load
        }

        public void loadScheduleData() {
            // VIEW ASKS CONTROLLER for the model
            table.setModel(controller.getScheduleModel());
            
            // Re-apply style because setting model can reset column widths
            styleTable(table); 
        }
    }

//-------- Report Panel ----------------//
    private class ReportPanel extends JPanel {
        private JTable table;
        private ReportController controller = new ReportController(); // Use Controller

        public ReportPanel() {
            setLayout(new BorderLayout());
            setBackground(whiteColor);
            setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JLabel lblTitle = new JLabel("Final Evaluation Reports");
            lblTitle.setFont(new Font("SansSerif", Font.BOLD, 28));
            lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
            add(lblTitle, BorderLayout.NORTH);

            table = new JTable();
            styleTable(table);
            add(new JScrollPane(table), BorderLayout.CENTER);
        }

        public void generateReportData() {
             // VIEW ASKS CONTROLLER
            table.setModel(controller.getEvaluationModel());
            styleTable(table);
        }
    }

//-------- Nomination Panel ----------------//
    private class NominationPanel extends JPanel {
        private JTextArea resultsArea;
        private ReportController controller = new ReportController(); // Use Controller

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
            // VIEW ASKS CONTROLLER
            String report = controller.getNominationReport();
            resultsArea.setText(report);
        }
    }

    private void styleTable(JTable table) {
        table.setRowHeight(30);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JTableHeader h = table.getTableHeader();
        h.setOpaque(true); 
        h.setBackground(headerColor);
        h.setForeground(Color.WHITE);
        h.setFont(new Font("SansSerif", Font.BOLD, 15));
    }
}