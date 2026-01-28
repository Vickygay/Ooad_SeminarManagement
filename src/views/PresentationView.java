package views;

import javax.swing.*;
import java.awt.*;
import controllers.PresentationController;

public class PresentationView extends JFrame {
    
    // UI Components
    private JTextField titleField;
    private JTextArea abstractArea;
    private JTextField supervisorField;
    private JComboBox<String> presentationTypeCombo;
    private JButton registerButton;
    
    // Logic
    private PresentationController controller;
    private String currentStudentID;

    // Colors (Matching your project theme)
    private Color headerColor = new Color(153, 153, 255);
    private Color lightPurple = new Color(204, 204, 255);
    private Color whiteColor = Color.WHITE;

    // --- CONSTRUCTOR NOW ACCEPTS STUDENT ID ---
    public PresentationView(String studentID) {
        this.currentStudentID = studentID;
        this.controller = new PresentationController(); // Initialize Controller!

        setTitle("Presentation Registration - Student ID: " + studentID);
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this window
        setLocationRelativeTo(null);
        
        // Main Panel (BorderLayout)
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(whiteColor);

        // =========================================================
        // 1. HEADER PANEL
        // =========================================================
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(headerColor);
        headerPanel.setPreferredSize(new Dimension(1100, 80));
        headerPanel.setLayout(new GridBagLayout()); // Center text

        JLabel titleLabel = new JLabel("Register New Presentation");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // =========================================================
        // 2. FORM PANEL
        // =========================================================
        JPanel formPanel = new JPanel();
        formPanel.setBackground(whiteColor);
        formPanel.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Title ---
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(createLabel("Presentation Title:"), gbc);
        
        gbc.gridx = 1;
        titleField = createStyledTextField();
        formPanel.add(titleField, gbc);

        // --- Abstract ---
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.NORTHWEST; // Top align for text area
        formPanel.add(createLabel("Abstract:"), gbc);
        
        gbc.gridx = 1;
        abstractArea = new JTextArea(5, 20);
        abstractArea.setFont(new Font("Arial", Font.PLAIN, 16));
        abstractArea.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 220), 1));
        // Wrap abstract area in scroll pane
        JScrollPane scrollPane = new JScrollPane(abstractArea);
        scrollPane.setPreferredSize(new Dimension(300, 100));
        formPanel.add(scrollPane, gbc);

        // --- Supervisor ---
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST; // Reset anchor
        formPanel.add(createLabel("Supervisor Name:"), gbc);
        
        gbc.gridx = 1;
        supervisorField = createStyledTextField();
        formPanel.add(supervisorField, gbc);

        // --- Type ---
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(createLabel("Presentation Type:"), gbc);
        
        gbc.gridx = 1;
        presentationTypeCombo = new JComboBox<>(new String[] {"Oral", "Poster"});
        presentationTypeCombo.setFont(new Font("Arial", Font.PLAIN, 16));
        presentationTypeCombo.setBackground(Color.WHITE);
        formPanel.add(presentationTypeCombo, gbc);

        // --- Register Button ---
        gbc.gridx = 1; gbc.gridy = 4;
        gbc.insets = new Insets(30, 10, 10, 10); // More space on top
        registerButton = new JButton("Register Presentation");
        registerButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        registerButton.setBackground(lightPurple);
        registerButton.setForeground(Color.BLACK);
        registerButton.setFocusPainted(false);
        registerButton.setPreferredSize(new Dimension(250, 45));
        formPanel.add(registerButton, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        add(mainPanel);

        // =========================================================
        // 3. LOGIC
        // =========================================================
        registerButton.addActionListener(e -> {
            String title = titleField.getText();
            String abstractText = abstractArea.getText();
            String supervisor = supervisorField.getText();
            String presentationType = (String) presentationTypeCombo.getSelectedItem();

            if (title.isEmpty() || supervisor.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.");
                return;
            }

            // Call Controller (Pass the StudentID!)
            controller.registerPresentation(currentStudentID, title, abstractText, supervisor, presentationType, null);            
            JOptionPane.showMessageDialog(this, "Presentation Registered Successfully!");
            dispose(); // Close window
        });
    }

    // --- Helper for Labels ---
    private JLabel createLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.BOLD, 16));
        l.setForeground(new Color(51, 51, 51));
        return l;
    }

    // --- Helper for Text Fields ---
    private JTextField createStyledTextField() {
        JTextField tf = new JTextField(20);
        tf.setFont(new Font("Arial", Font.PLAIN, 16));
        tf.setPreferredSize(new Dimension(300, 35));
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 220), 1),
            BorderFactory.createEmptyBorder(2, 5, 2, 5)
        ));
        return tf;
    }
}