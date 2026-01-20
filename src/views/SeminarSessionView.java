package views;

import javax.swing.*;
import java.awt.*;
import models.SeminarSession;

public class SeminarSessionView extends JFrame {
    private JTextField sessionIDField;
    private JTextField sessionDateField;
    private JTextField sessionVenueField;
    private JComboBox<String> sessionTypeCombo;
    private JButton createSessionButton;

    public SeminarSessionView() {
        setTitle("Create Seminar Session");
        setSize(1300, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));

        JLabel sessionIDLabel = new JLabel("Session ID:");
        sessionIDField = new JTextField();
        JLabel sessionDateLabel = new JLabel("Session Date:");
        sessionDateField = new JTextField();
        JLabel sessionVenueLabel = new JLabel("Session Venue:");
        sessionVenueField = new JTextField();
        JLabel sessionTypeLabel = new JLabel("Session Type:");
        sessionTypeCombo = new JComboBox<>(new String[] {"Oral", "Poster"});
        createSessionButton = new JButton("Create Session");

        panel.add(sessionIDLabel);
        panel.add(sessionIDField);
        panel.add(sessionDateLabel);
        panel.add(sessionDateField);
        panel.add(sessionVenueLabel);
        panel.add(sessionVenueField);
        panel.add(sessionTypeLabel);
        panel.add(sessionTypeCombo);
        panel.add(new JLabel()); // Empty space
        panel.add(createSessionButton);

        add(panel);

        createSessionButton.addActionListener(e -> {
            String sessionID = sessionIDField.getText();
            String sessionDate = sessionDateField.getText();
            String sessionVenue = sessionVenueField.getText();
            String sessionType = (String) sessionTypeCombo.getSelectedItem();

            SeminarSession session = new SeminarSession(sessionID, sessionDate, sessionVenue, sessionType);
            session.createSession();
            JOptionPane.showMessageDialog(null, "Session Created!");
        });
    }
}
