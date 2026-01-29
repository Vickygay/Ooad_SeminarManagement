package views;

import javax.swing.*;
import controllers.ReportController;
import java.awt.*;

//-------- Report Panel ----------------//
public class ReportPanel extends JPanel {
    private JTable table;
    private ReportController controller = new ReportController(); // Use Controller

    public ReportPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("Final Evaluation Reports");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        add(lblTitle, BorderLayout.NORTH);

        table = new JTable();
        StyleHelper.styleTable(table); 
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public void generateReportData() {
            // VIEW ASKS CONTROLLER
        table.setModel(controller.getEvaluationModel());
        StyleHelper.styleTable(table); 
    }
}

