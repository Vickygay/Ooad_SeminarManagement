package views;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.*;

public class S_ViewRegistrationPanel extends JPanel {
    
    private String currentStudentID;
    
    // Fonts & Colors (Matching Student Theme)
    private Font labelFont = new Font("SansSerif", Font.BOLD, 16);
    private Font valueFont = new Font("SansSerif", Font.PLAIN, 16);
    private Color whiteColor = Color.WHITE;

    public S_ViewRegistrationPanel(String studentID) {
        this.currentStudentID = studentID;
        
        setLayout(new GridBagLayout());
        setBackground(whiteColor);
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Title ---
        JLabel lblTitle = new JLabel("My Registered Presentation");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 28));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(lblTitle, gbc);

        // --- Load Data ---
        String[] data = getRegistrationDetails(studentID);

        if (data == null) {
            // Case: No Registration Found
            gbc.gridy++;
            JLabel lblNoData = new JLabel("No registration record found. Please submit the Registration Form first.");
            lblNoData.setFont(new Font("SansSerif", Font.ITALIC, 16));
            lblNoData.setForeground(Color.RED);
            add(lblNoData, gbc);
        } else {
            // Case: Data Found - Display It
            // data structure: [ID, Title, Type, Supervisor, Abstract, FilePath]
            
            gbc.gridwidth = 1;

            // Row 1: Title
            gbc.gridy++; gbc.gridx = 0;
            add(createLabel("Research Title:"), gbc);
            gbc.gridx = 1;
            add(createValueField(data[1]), gbc);

            // Row 2: Abstract
            gbc.gridy++; gbc.gridx = 0;
            add(createLabel("Abstract:"), gbc);
            gbc.gridx = 1;
            JTextArea area = new JTextArea(data[4]);
            area.setFont(valueFont);
            area.setLineWrap(true);
            area.setWrapStyleWord(true);
            area.setEditable(false);
            area.setBackground(new Color(245, 245, 250)); // Light gray to indicate read-only
            area.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            add(new JScrollPane(area) {{ setPreferredSize(new Dimension(400, 100)); }}, gbc);

            // Row 3: Supervisor
            gbc.gridy++; gbc.gridx = 0;
            add(createLabel("Supervisor:"), gbc);
            gbc.gridx = 1;
            add(createValueField(data[3]), gbc);

            // Row 4: Type
            gbc.gridy++; gbc.gridx = 0;
            add(createLabel("Presentation Type:"), gbc);
            gbc.gridx = 1;
            add(createValueField(data[2]), gbc);

            // Row 5: File
            gbc.gridy++; gbc.gridx = 0;
            add(createLabel("Submitted File:"), gbc);
            gbc.gridx = 1;
            JLabel fileLabel = new JLabel(data[5]);
            fileLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
            fileLabel.setForeground(new Color(0, 102, 204));
            add(fileLabel, gbc);
        }

        // Pusher to align everything top-left
        gbc.gridy++; gbc.weightx = 1; gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        add(new JLabel(), gbc);
    }

    // --- Helper: Read from File ---
    private String[] getRegistrationDetails(String id) {
        try (BufferedReader br = new BufferedReader(new FileReader("registrations.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Format: stdID|Title|Type|Supervisor|Abstract|File
                String[] parts = line.split("\\|");
                if (parts.length >= 6 && parts[0].trim().equalsIgnoreCase(id)) {
                    return parts; // Return the matching line as array
                }
            }
        } catch (IOException e) {
            // File might not exist yet
        }
        return null; // Not found
    }

    // --- Helper: Components ---
    private JLabel createLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(labelFont);
        return l;
    }

    private JTextField createValueField(String text) {
        JTextField tf = new JTextField(text);
        tf.setFont(valueFont);
        tf.setEditable(false); // Read-only
        tf.setBackground(new Color(245, 245, 250)); 
        tf.setPreferredSize(new Dimension(400, 35));
        tf.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        return tf;
    }
    
    // Method to refresh data when the user clicks the button again
    public void refreshData() {
        removeAll();
        // Re-run the constructor logic to reload UI with fresh file data
        new S_ViewRegistrationPanel(currentStudentID); 
        // (Simpler approach: Just creating a new instance in Dashboard is often easier, 
        // but if you keep the same instance, you'd move the layout code to a init() method)
    }
}