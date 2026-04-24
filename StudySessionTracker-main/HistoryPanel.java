import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class HistoryPanel extends JPanel {
    private JTable historyTable;
    private DefaultTableModel tableModel;
    private SessionManager sessionManager;
    
    public HistoryPanel(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        setLayout(new BorderLayout());
        
        String[] columns = {"Date", "Subject", "Duration", "Notes"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        historyTable = new JTable(tableModel);
        add(new JScrollPane(historyTable), BorderLayout.CENTER);
        
        refreshTable();
    }
    
    public void refreshTable() {
        tableModel.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        
        List<StudySession> sessions = sessionManager.getSessions();
        for (int i = sessions.size() - 1; i >= 0; i--) {
            StudySession s = sessions.get(i);
            tableModel.addRow(new Object[]{
                sdf.format(s.getStartTime()),
                s.getSubject(),
                s.getDurationFormatted(),
                s.getNotes()
            });
        }
    }
}