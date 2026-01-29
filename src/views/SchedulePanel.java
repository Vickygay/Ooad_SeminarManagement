package views;
import javax.swing.*;
import controllers.SeminarSessionController;
import java.awt.*;

//-------- Seminar Schedule Panel ----------------//
public class SchedulePanel extends JPanel {
    private JTable table;
    private SeminarSessionController controller = new SeminarSessionController(); // Use Controller

    public SchedulePanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("Seminar Schedules");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        add(lblTitle, BorderLayout.NORTH);

        table = new JTable();
        StyleHelper.styleTable(table); // Your existing style helper
        add(new JScrollPane(table), BorderLayout.CENTER);
        
        loadScheduleData(); // Initial load
    }

    public void loadScheduleData() {
        // VIEW ASKS CONTROLLER for the model
        table.setModel(controller.getScheduleModel());
        
        // Re-apply style because setting model can reset column widths
        StyleHelper.styleTable(table); 
    }
}


