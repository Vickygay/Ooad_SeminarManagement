package views;

import controllers.RegistrationController;
import java.awt.*;
import javax.swing.*;
import models.Registration;

/**
 * StudentDashboardView provides the UI for seminar registration.
 * Includes validation to ensure no fields or file uploads are ignored.
 */
public class StudentDashboardView extends JFrame implements Dashboard {
    // Class-level variables to capture user inputs
    private JTextField titleField;
    private JTextArea abstractArea;
    private JTextField supervisorField;
    private JComboBox<String> typeCombo;
    private String selectedFilePath = "No file attached";
    private JLabel pathLabel;

    public StudentDashboardView() {
        super("FCI Seminar Management System - Student Dashboard");
        setSize(1300, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel header = new JPanel();
        header.setBackground(new Color(41, 128, 185));
        JLabel title = new JLabel("Student Portal: Seminar Registration & Uploads");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        header.add(title);
        add(header, BorderLayout.NORTH);

        // Sidebar Navigation
        JPanel sidebar = new JPanel(new GridLayout(8, 1, 10, 10));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        sidebar.setPreferredSize(new Dimension(220, 700));
        sidebar.add(new JButton("Registration Form"));
        sidebar.add(new JButton("View Award Nominations"));
        add(sidebar, BorderLayout.WEST);

        // Main Content Area: Registration Form
        JPanel mainContent = createRegistrationForm();
        add(new JScrollPane(mainContent), BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createRegistrationForm() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Seminar Registration Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 1. Research Title
        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("Research Title:"), gbc);
        titleField = new JTextField(35);
        gbc.gridx = 1; formPanel.add(titleField, gbc);

        // 2. Abstract
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("Abstract:"), gbc);
        abstractArea = new JTextArea(6, 35);
        abstractArea.setLineWrap(true);
        gbc.gridx = 1; formPanel.add(new JScrollPane(abstractArea), gbc);

        // 3. Supervisor Name
        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(new JLabel("Supervisor Name:"), gbc);
        supervisorField = new JTextField(25);
        gbc.gridx = 1; formPanel.add(supervisorField, gbc);

        // 4. Presentation Type (Oral/Poster)
        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(new JLabel("Preferred Type:"), gbc);
        typeCombo = new JComboBox<>(new String[]{"Oral", "Poster"});
        gbc.gridx = 1; formPanel.add(typeCombo, gbc);

        // 5. File Upload (Materials)
        gbc.gridx = 0; gbc.gridy = 4; formPanel.add(new JLabel("Upload Materials:"), gbc);
        JButton uploadBtn = new JButton("Attach File (Slides/Poster)");
        pathLabel = new JLabel("No file selected");
        
        uploadBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedFilePath = fileChooser.getSelectedFile().getAbsolutePath();
                pathLabel.setText(fileChooser.getSelectedFile().getName());
                pathLabel.setForeground(new Color(39, 174, 96)); // Green color for success
            }
        });
        
        JPanel uploadPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        uploadPanel.add(uploadBtn);
        uploadPanel.add(pathLabel);
        gbc.gridx = 1; formPanel.add(uploadPanel, gbc);

        // 6. Submit Button with VALIDATION
        gbc.gridy = 5;
        JButton submitBtn = new JButton("Register for Seminar");
        submitBtn.setPreferredSize(new Dimension(150, 40));
        submitBtn.addActionListener(e -> {
            // Get data and trim whitespace
            String titleStr = titleField.getText().trim();
            String abstractStr = abstractArea.getText().trim();
            String supervisorStr = supervisorField.getText().trim();
            String presentationType = (String) typeCombo.getSelectedItem();

            // Perform Validation Check
            if (titleStr.isEmpty() || abstractStr.isEmpty() || supervisorStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Error: All text fields must be filled out.", 
                    "Incomplete Form", 
                    JOptionPane.ERROR_MESSAGE);
                return; // Stop execution
            }

            if (selectedFilePath.equals("No file attached")) {
                JOptionPane.showMessageDialog(this, 
                    "Error: Please upload your presentation slide or poster.", 
                    "Missing File", 
                    JOptionPane.ERROR_MESSAGE);
                return; // Stop execution
            }

            // If valid, create model and save
            Registration reg = new Registration(
                titleStr,
                abstractStr,
                supervisorStr,
                presentationType,
                selectedFilePath
            );

            RegistrationController controller = new RegistrationController();
            if (controller.saveRegistration(reg)) {
                JOptionPane.showMessageDialog(this, "Registration Saved Successfully!");
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Error saving registration to database.", "System Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        formPanel.add(submitBtn, gbc);

        return formPanel;
    }

    private void clearForm() {
        titleField.setText("");
        abstractArea.setText("");
        supervisorField.setText("");
        typeCombo.setSelectedIndex(0);
        pathLabel.setText("No file selected");
        pathLabel.setForeground(Color.BLACK);
        selectedFilePath = "No file attached";
    }

    @Override
    public void viewAwardee() {
        JOptionPane.showMessageDialog(this, "Viewing Seminar Award Winners...");
    }

    @Override
    public void viewNominates() {
        JOptionPane.showMessageDialog(this, "Viewing Award Nominations...");
    }
}