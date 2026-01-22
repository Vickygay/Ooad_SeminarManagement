package views;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.DateFormat;

public class CoordinatorDashboardView extends JFrame implements Dashboard {  // Implementing the Dashboard interface

    public CoordinatorDashboardView() {
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
        JPanel sidebar = new JPanel(new GridLayout(8, 1, 10, 10));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        sidebar.setPreferredSize(new Dimension(220, 700));
        JButton btn1 = new JButton("Manage Seminar Session");
        JButton btn2 = new JButton("Seminar Schedules");
        JButton btn3 = new JButton("Final evaluation reports");
        JButton btn4 = new JButton("Oversee Nomination");

        Color greenColor = new Color(229, 255, 204);
        btn1.setBackground(greenColor);
        btn2.setBackground(greenColor);
        btn3.setBackground(greenColor);
        btn4.setBackground(greenColor);

        sidebar.add(btn1);
        sidebar.add(btn2);
        sidebar.add(btn3);
        sidebar.add(btn4);
        add(sidebar, BorderLayout.WEST);
        // Panel for content
        JPanel content = new JPanel();
        content.setBackground(new Color(255, 255, 255));
        CardLayout cardLayout = new CardLayout();
        content.setLayout(cardLayout);

        // Manage Session
        JPanel manageSession = new ManageSession();
        
        // Seminar
        JPanel seminarSchedule = new JPanel();
        seminarSchedule.add(new JLabel("This is the generate schedule page."));
        seminarSchedule.setBackground(new Color(255, 255, 255));
        seminarSchedule.setOpaque(true);
        
        JPanel report = new JPanel();
        report.add(new JLabel("This is the generate report page."));
        report.setBackground(new Color(255, 255, 255));
        report.setOpaque(true);

        JPanel nomination = new JPanel();
        nomination.add(new JLabel("This is the oversee nomination page."));
        nomination.setBackground(new Color(255, 255, 255));
        nomination.setOpaque(true); 

        content.add(manageSession, "Manage Seminar");
        content.add(seminarSchedule, "Schedule");
        content.add(report, "Report");
        content.add(nomination, "Nomination");

        add(content, BorderLayout.CENTER);
        // to change the content based on button selected
        btn1.addActionListener(e -> cardLayout.show(content, "Manage Seminar"));
        btn2.addActionListener(e -> cardLayout.show(content, "Schedule"));
        btn3.addActionListener(e -> cardLayout.show(content, "Report"));
        btn4.addActionListener(e -> cardLayout.show(content, "Nomination"));

        setVisible(true);
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

    private class ManageSession extends JPanel
    {
        private String[] getUsers(String role) 
        {
            java.util.ArrayList<String> list = new java.util.ArrayList<>();
            list.add("-- Select " + role + " --"); // default option

            try {
                java.util.Scanner sc = new java.util.Scanner(new java.io.File("users.txt"));
                
                while (sc.hasNextLine()) { //keep reading, until the end of the line
                    String line = sc.nextLine();
                    String[] parts = line.split(","); //split parts by the ","
                    
                    // parts[2] means the 3rd element in the users.txt, which is the role
                    if (parts.length >= 3 && parts[2].trim().equalsIgnoreCase(role)) {
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
            String[] sessionType = {"Paper Presentation Session", "Expert Lecture", "Poster Session"};
            JComboBox<String> cb = new JComboBox<>(sessionType);
            cb.setVisible(true);
            add(cb, gbc);

            // Venue
            gbc.gridy++;
            gbc.gridx = 0;
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
            evaluatorBox.setVisible(true);
            add(evaluatorBox, gbc);

            // Assign Students
            gbc.gridy++; //go to next row
            gbc.gridx = 0;
                
            JLabel assignStudent = new JLabel("Student involved:"); 
            assignStudent.setFont(new Font("SansSerif", Font.BOLD, 15));
            add(assignStudent, gbc);
            
            gbc.gridx = 1;
            String[] students = getUsers("Student"); //get only the role student
            JComboBox<String> studentBox = new JComboBox<>(students);
            studentBox.setVisible(true);
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
                public void actionPerformed(ActionEvent e)
                {
                    String selectedSessionType = (String) cb.getSelectedItem();
                    String venue = venueTextField.getText();
                    String date = dateField.getText();
                    String selectedEvaluator = (String) evaluatorBox.getSelectedItem();
                    String selectedStudent = (String) studentBox.getSelectedItem();

                    if(venue.isEmpty() || date.isEmpty())
                    {
                        JOptionPane.showMessageDialog(ManageSession.this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    try
                    {
                        java.io.FileWriter writer = new java.io.FileWriter("seminars.txt", true); //write info into seminars.txt
                        java.io.BufferedWriter bufferedWriter = new java.io.BufferedWriter(writer);

                        // separate info by ","
                        bufferedWriter.write(selectedSessionType + "," + venue + "," + date + "," + selectedEvaluator + "," + selectedStudent);
                        bufferedWriter.newLine(); // move to next line
                        bufferedWriter.close();

                        JOptionPane.showMessageDialog(ManageSession.this, "Session Saved Successfully!");
                        
                        // clear fields after reading
                        venueTextField.setText("");
                        
                        }
                        catch (java.io.IOException ex) //display error message
                        {
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
}