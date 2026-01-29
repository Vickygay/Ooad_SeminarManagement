package views;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.*;
import java.util.*;

//-------- Manage Session ----------------//
public class ManageSession extends JPanel
{
    private JComboBox<String> sessionDropDown; // to which session to edit
    private JComboBox<String> cb; // session type
    private JTextField venueTextField; // venue
    private JFormattedTextField dateField; //date
    private JComboBox<String> evaluatorBox; //select evaluator
    private JComboBox<String> studentBox; //select student

    private ArrayList<String> allSessionLines = new ArrayList<>(); //show all available session
    private final String seminar_File = "seminars.txt";

    private String[] getEvaluators() 
    {
        java.util.ArrayList<String> list = new java.util.ArrayList<>();
        list.add("-- Select Evaluator --");

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
        list.add("-- Select student --"); 

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

        // select session to edit or delete
        gbc.gridwidth = 1;
        gbc.gridy++; //go to next row
        gbc.gridx = 0;

        JLabel selectSesion = new JLabel("Select session to manage: ");
        selectSesion.setFont(new Font("SansSerif", Font.BOLD, 15));
        add(selectSesion, gbc);

        gbc.gridx = 1;
        sessionDropDown = new JComboBox<>();
        add(sessionDropDown, gbc);

        //Session Type
        gbc.gridwidth = 1;
        gbc.gridy++; //go to next row
        gbc.gridx = 0;

        JLabel sessionTitle = new JLabel("Session Type:");
        sessionTitle.setFont(new Font("SansSerif", Font.BOLD, 15));
        add(sessionTitle, gbc);
        
        gbc.gridx = 1;
        String[] sessionType = {"Oral Session", "Poster Session"};
        cb = new JComboBox<>(sessionType);
        add(cb, gbc);

        // Venue
        gbc.gridy++; gbc.gridx = 0;
        JLabel venueTitle = new JLabel("Venue:");
        venueTitle.setFont(new Font("SansSerif", Font.BOLD, 15));
        add(venueTitle, gbc);

        gbc.gridx = 1;
        venueTextField = new JTextField(40);
        add(venueTextField, gbc);

        // Date
        gbc.gridy++; 
        gbc.gridx = 0;
        JLabel dateTitle = new JLabel("Date:");
        dateTitle.setFont(new Font("SansSerif", Font.BOLD, 15));
        add(dateTitle, gbc);

        gbc.gridx = 1;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateField = new JFormattedTextField(dateFormat);
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
        evaluatorBox = new JComboBox<>(evaluators);
        add(evaluatorBox, gbc);

        // Assign Students
        gbc.gridy++; //go to next row
        gbc.gridx = 0;
        JLabel assignStudent = new JLabel("Student involved:"); 
        assignStudent.setFont(new Font("SansSerif", Font.BOLD, 15));
        add(assignStudent, gbc);
        
        gbc.gridx = 1;
        String[] students = getStudents(); //get only the role student
        studentBox = new JComboBox<>(students);
        add(studentBox, gbc);        
        
        // save/update/delete button
        gbc.gridy++; 
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonPanel.setBackground(Color.WHITE);

        //Save
        JButton submit = new JButton("Save New Session");
        submit.setBackground(new Color(0, 128, 0));
        submit.setForeground(Color.WHITE);
        
        //Update button
        JButton edit = new JButton("Update Session");
        edit.setBackground(new Color(0, 128, 0));
        edit.setForeground(Color.WHITE);
        
        //Delete button
        JButton delete = new JButton("Delete Session");
        delete.setBackground(new Color(0, 128, 0));
        delete.setForeground(Color.WHITE);
        
        buttonPanel.add(submit);
        buttonPanel.add(edit);
        buttonPanel.add(delete);

        add(buttonPanel, gbc);

        // click save button will save new session
        submit.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                if (sessionDropDown.getSelectedIndex() > 0 ) //if the selected index is actually the existed one, update session instead of creating new one
                {
                    updateSelectedSession();
                }
                else
                    saveNewSession(); 
            }
        });

        // click edit button will save edited session
        edit.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                updateSelectedSession();
            }
        });

        // click delete button will delete the selected session
        delete.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedSession();
            }
        });

        // the dropdown that will show the sessionss
        sessionDropDown.addActionListener(e -> populateFieldsFromSelection());

        // refresh the dropdown list so it will keep updating with the newly created/deleted sessions
        refreshSessionList();
        
        // added so that the form appearing on top left: move to next row, takes up remaining space
        gbc.gridy++; 
        gbc.weightx = 1; 
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        add(new JLabel(), gbc);
    }

    // read seminar.txt file to get the updated list for the first dropdown
    private void refreshSessionList() 
    {
        allSessionLines.clear();
        sessionDropDown.removeAllItems();
        sessionDropDown.addItem("-- Create New Session --"); //here make the index of creating new 
                                                                  // session becomes 0

        try {
            File f = new File(seminar_File);
            if (!f.exists()) //if nothing inside file, do not do anything
                return;

            Scanner sc = new Scanner(f);
            while (sc.hasNextLine()) { // if there's something inside, extract out the data
                String line = sc.nextLine();
                if (!line.trim().isEmpty()) {
                    allSessionLines.add(line);
                    String[] parts = line.split(",");
                    if (parts.length >= 2) {
                        sessionDropDown.addItem(parts[0] + " with student " + parts[4]); //extract out the student id and the session type
                    } else {
                        sessionDropDown.addItem(line);
                    }
                }
            }
            sc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // to get the correct information based on first dropdown selected from "select session"
    private void populateFieldsFromSelection() {
        int index = sessionDropDown.getSelectedIndex();
        // if a particular session selected, trim the data 
        if (index > 0 && (index - 1) < allSessionLines.size()) { // index-1, to compare actual index in the dropdown with index in seminars.txt
            String line = allSessionLines.get(index - 1);
            String[] parts = line.split(",");
            
            if (parts.length >= 5) {
                cb.setSelectedItem(parts[0]);
                venueTextField.setText(parts[1]);
                dateField.setText(parts[2]);
                evaluatorBox.setSelectedItem(parts[3]);
                studentBox.setSelectedItem(parts[4]);
            }
        } else {
            // Clear fields if "Create New" is selected
            venueTextField.setText("");
            dateField.setValue(new Date()); 
            evaluatorBox.setSelectedIndex(0);
            studentBox.setSelectedIndex(0);
            cb.setSelectedIndex(0);
        }
    }
    
    // save
    private void saveNewSession() {
        String venue = venueTextField.getText();
        String date = dateField.getText();

        if (venue.isEmpty() || date.isEmpty()) {
            JOptionPane.showMessageDialog(ManageSession.this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            java.io.FileWriter writer = new java.io.FileWriter(seminar_File, true);
            java.io.BufferedWriter bufferedWriter = new java.io.BufferedWriter(writer);

            bufferedWriter.write((String) cb.getSelectedItem() + "," + venue + "," + date + "," + (String) evaluatorBox.getSelectedItem() + "," + (String) studentBox.getSelectedItem());
            bufferedWriter.newLine();
            bufferedWriter.close();

            JOptionPane.showMessageDialog(ManageSession.this, "Session Saved Successfully!");
            venueTextField.setText("");
            
            // Refresh list so the new item appears in dropdown
            refreshSessionList();

        } catch (java.io.IOException ex) {
            JOptionPane.showMessageDialog(ManageSession.this, "Error saving file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // update
    private void updateSelectedSession() {
        int index = sessionDropDown.getSelectedIndex();
        if (index <= 0) { // to make sure actually a session is selected
            JOptionPane.showMessageDialog(this, "Please select a session to update.");
            return;
        }

        // Construct the new line
        String newLine = (String) cb.getSelectedItem() + "," + 
                        venueTextField.getText() + "," + 
                        dateField.getText() + "," + 
                        (String) evaluatorBox.getSelectedItem() + "," + 
                        (String) studentBox.getSelectedItem();

        // Update the list in memory
        allSessionLines.set(index - 1, newLine);

        // Rewrite the file
        rewriteFile();
        JOptionPane.showMessageDialog(this, "Session Updated!");
        refreshSessionList();
    }

    // delete
    private void deleteSelectedSession() {
        int index = sessionDropDown.getSelectedIndex();
        if (index <= 0) {
            JOptionPane.showMessageDialog(this, "Please select a session to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this session?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            allSessionLines.remove(index - 1);
            rewriteFile();
            JOptionPane.showMessageDialog(this, "Session Deleted!");
            refreshSessionList();
            
            // Clear fields
            venueTextField.setText("");
        }
    }

    // rewrite the whole file after edit/delete
    private void rewriteFile() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(seminar_File, false)); // false = overwrite
            for (String line : allSessionLines) {
                writer.write(line);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}