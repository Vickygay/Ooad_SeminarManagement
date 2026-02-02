package views;

import controllers.PresentationController;
import java.awt.*;
import javax.swing.*;

public class S_RegistrationPanel extends JPanel {
    // Variables
    private JTextField titleField;
    private JTextArea abstractArea;
    private JTextField supervisorField;
    private JComboBox<String> typeCombo;
    private String selectedFilePath = "No file attached";
    private JLabel pathLabel;
    private String currentStudentID;

    // Colors (Blue Theme)
    private Color buttonColor = new Color(102, 178, 255); 
    private Color whiteColor = Color.WHITE;

    // --- STANDARD FONTS ---
    private Font labelFont = new Font("SansSerif", Font.BOLD, 15);  // For Labels
    private Font inputFont = new Font("SansSerif", Font.PLAIN, 15); // For TextFields
    private Font buttonFont = new Font("SansSerif", Font.BOLD, 15); // For Buttons

    public S_RegistrationPanel(String studentID) {
        this.currentStudentID = studentID;
        
        setLayout(new GridBagLayout());
        setBackground(whiteColor);
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); 
        gbc.anchor = GridBagConstraints.NORTHWEST; 
        gbc.fill = GridBagConstraints.HORIZONTAL;  

        // =========================================================
        // 1. TITLE
        // =========================================================
        JLabel lblTitle = new JLabel("Seminar Registration Details");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 28)); // Keep Title Big
        
        gbc.gridx = 0; 
        gbc.gridy = 0; 
        gbc.gridwidth = 2; 
        add(lblTitle, gbc);

        // =========================================================
        // 2. FORM FIELDS
        // =========================================================
        
        gbc.gridwidth = 1; 

        // --- Row 1: Research Title ---
        gbc.gridx = 0; gbc.gridy = 1; 
        add(createLabel("Research Title:"), gbc);

        gbc.gridx = 1; 
        titleField = new JTextField(35);
        titleField.setFont(inputFont); // Set Font
        add(titleField, gbc);

        // --- Row 2: Abstract ---
        gbc.gridx = 0; gbc.gridy = 2; 
        add(createLabel("Abstract:"), gbc);
        
        gbc.gridx = 1; 
        abstractArea = new JTextArea(6, 35);
        abstractArea.setFont(inputFont); // Set Font
        abstractArea.setLineWrap(true);
        abstractArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(new JScrollPane(abstractArea), gbc);

        // --- Row 3: Supervisor Name ---
        gbc.gridx = 0; gbc.gridy = 3; 
        add(createLabel("Supervisor Name:"), gbc);

        gbc.gridx = 1; 
        supervisorField = new JTextField(25);
        supervisorField.setFont(inputFont); // Set Font
        add(supervisorField, gbc);

        // --- Row 4: Presentation Type ---
        gbc.gridx = 0; gbc.gridy = 4; 
        add(createLabel("Preferred Type:"), gbc);

        gbc.gridx = 1; 
        typeCombo = new JComboBox<>(new String[]{"Oral", "Poster"});
        typeCombo.setFont(inputFont); // Set Font
        typeCombo.setBackground(Color.WHITE);
        add(typeCombo, gbc);

        // --- Row 5: File Upload ---
        gbc.gridx = 0; gbc.gridy = 5; 
        add(createLabel("Upload Materials:"), gbc);

        gbc.gridx = 1; 
        JButton uploadBtn = new JButton("Attach File (Slides/Poster)");
        uploadBtn.setFont(buttonFont); // Set Font
        uploadBtn.setBackground(new Color(230, 230, 250)); 
        
        pathLabel = new JLabel("No file selected");
        pathLabel.setFont(new Font("SansSerif", Font.PLAIN, 14)); // Slightly smaller for path
        
        uploadBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedFilePath = fileChooser.getSelectedFile().getAbsolutePath();
                pathLabel.setText(fileChooser.getSelectedFile().getName());
                pathLabel.setForeground(new Color(39, 174, 96)); 
            }
        });
        
        JPanel uploadPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        uploadPanel.setBackground(whiteColor);
        uploadPanel.add(uploadBtn);
        uploadPanel.add(Box.createHorizontalStrut(10)); 
        uploadPanel.add(pathLabel);
        add(uploadPanel, gbc);

        // =========================================================
        // 3. SUBMIT BUTTON
        // =========================================================
        gbc.gridx = 0; 
        gbc.gridy = 6;
        gbc.gridwidth = 2; 
        gbc.insets = new Insets(30, 10, 10, 10); 
        gbc.fill = GridBagConstraints.NONE; 
        gbc.anchor = GridBagConstraints.CENTER; 
        
        JButton submitBtn = new JButton("Register for Seminar");
        submitBtn.setPreferredSize(new Dimension(250, 50)); 
        submitBtn.setBackground(buttonColor); 
        submitBtn.setFont(new Font("SansSerif", Font.BOLD, 18)); // Keep main button slightly bigger
        submitBtn.setFocusPainted(false);
        
        submitBtn.addActionListener(e -> submitRegistration());
        add(submitBtn, gbc);

        // =========================================================
        // 4. PUSHER
        // =========================================================
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0; 
        gbc.weighty = 1.0; 
        gbc.fill = GridBagConstraints.BOTH;
        add(new JLabel(), gbc);
    }

    // --- Helper to keep font consistent ---
    private JLabel createLabel(String text) {
        JLabel l = new JLabel(text);
        // UPDATED: Using the specific requested font size
        l.setFont(labelFont); 
        return l;
    }

    private void submitRegistration() {
        String titleStr = titleField.getText().trim();
        String abstractStr = abstractArea.getText().trim();
        String supervisorStr = supervisorField.getText().trim();
        String presentationType = (String) typeCombo.getSelectedItem();

        if (titleStr.isEmpty() || abstractStr.isEmpty() || supervisorStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Error: All text fields must be filled out.", "Incomplete Form", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (selectedFilePath.equals("No file attached")) {
            JOptionPane.showMessageDialog(this, "Error: Please upload your presentation slide or poster.", "Missing File", JOptionPane.ERROR_MESSAGE);
            return;
        }

        PresentationController controller = new PresentationController();
        controller.registerPresentation(currentStudentID, titleStr, abstractStr, supervisorStr, presentationType, selectedFilePath);

        JOptionPane.showMessageDialog(this, "Registration Saved Successfully to System!");
        clearForm();
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
}