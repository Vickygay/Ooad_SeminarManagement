package views;

import controllers.RegistrationController;
import java.awt.*;
import javax.swing.*;
import models.Registration;

public class StudentDashboardView extends JFrame implements Dashboard {
    // Class-level variables to capture user inputs [cite: 34, 35]
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

        // Sidebar Navigation [cite: 46]
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

        // 1. Research Title [cite: 34]
        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("Research Title:"), gbc);
        titleField = new JTextField(35);
        gbc.gridx = 1; formPanel.add(titleField, gbc);

        // 2. Abstract [cite: 34]
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("Abstract:"), gbc);
        abstractArea = new JTextArea(6, 35);
        gbc.gridx = 1; formPanel.add(new JScrollPane(abstractArea), gbc);

        // 3. Supervisor Name [cite: 34]
        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(new JLabel("Supervisor Name:"), gbc);
        supervisorField = new JTextField(25);
        gbc.gridx = 1; formPanel.add(supervisorField, gbc);

        // 4. Presentation Type (Oral/Poster) [cite: 34]
        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(new JLabel("Preferred Type:"), gbc);
        typeCombo = new JComboBox<>(new String[]{"Oral", "Poster"});
        gbc.gridx = 1; formPanel.add(typeCombo, gbc);

        // 5. File Upload (Materials) [cite: 35]
        gbc.gridx = 0; gbc.gridy = 4; formPanel.add(new JLabel("Upload Materials:"), gbc);
        JButton uploadBtn = new JButton("Attach File (Slides/Poster)");
        pathLabel = new JLabel("No file selected");
        
        uploadBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedFilePath = fileChooser.getSelectedFile().getAbsolutePath();
                pathLabel.setText(fileChooser.getSelectedFile().getName());
            }
        });
        
        JPanel uploadPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        uploadPanel.add(uploadBtn);
        uploadPanel.add(pathLabel);
        gbc.gridx = 1; formPanel.add(uploadPanel, gbc);

        // 6. Submit Button [cite: 54]
        gbc.gridy = 5;
        JButton submitBtn = new JButton("Register for Seminar");
        submitBtn.addActionListener(e -> {
            Registration reg = new Registration(
                titleField.getText(),
                abstractArea.getText(),
                supervisorField.getText(),
                (String) typeCombo.getSelectedItem(),
                selectedFilePath
            );

            RegistrationController controller = new RegistrationController();
            if (controller.saveRegistration(reg)) {
                JOptionPane.showMessageDialog(this, "Registration Saved Successfully!");
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Error saving registration.", "Error", JOptionPane.ERROR_MESSAGE);
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
        selectedFilePath = "No file attached";
    }

    @Override
    public void viewAwardee() {
        // This prepares the system for the Award & Ceremony Module [cite: 49]
        JOptionPane.showMessageDialog(this, "Viewing Seminar Award Winners...");
    }

    @Override
    public void viewNominates() {
        // This covers the Award nomination oversight requirement [cite: 44]
        JOptionPane.showMessageDialog(this, "Viewing Award Nominations...");
    }
}