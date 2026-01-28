package views;

import controllers.ReportController;
import javax.swing.*;

public class ReportView extends JFrame {
    private JButton generateReportButton;

    public ReportView() {
        setTitle("Generate Reports");
        setSize(1300, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        generateReportButton = new JButton("Generate Report");

        panel.add(generateReportButton);
        add(panel);

        generateReportButton.addActionListener(e -> {
            ReportController reportController = new ReportController();
            reportController.getNominationReport();
        });
    }
}
