package views;

import javax.swing.*;
import controllers.ReportController;
import java.awt.*;

//-------- Nomination Panel ----------------//
public class NominationPanel extends JPanel {
    private JTextArea resultsArea;
    private ReportController controller = new ReportController(); // Use Controller

    public NominationPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
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
