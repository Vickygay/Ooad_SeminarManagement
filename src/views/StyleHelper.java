package views;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class StyleHelper {
    public static void styleTable(JTable table) {
        table.setRowHeight(30);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JTableHeader h = table.getTableHeader();
        h.setOpaque(true); 
        h.setBackground(new Color(51, 102, 0));
        h.setForeground(Color.WHITE);
        h.setFont(new Font("SansSerif", Font.BOLD, 15));
    }
}




