import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;

public class StatisticsPanel extends JPanel {
    private JTextArea statsArea;
    private SessionManager sessionManager;
    private SubjectManager subjectManager;
    
    public StatisticsPanel(SessionManager sessionManager, SubjectManager subjectManager) {
        this.sessionManager = sessionManager;
        this.subjectManager = subjectManager;
        
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        statsArea = new JTextArea();
        statsArea.setEditable(false);
        statsArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        add(new JScrollPane(statsArea), BorderLayout.CENTER);
        
        JButton refreshButton = new JButton("Refresh Statistics");
        refreshButton.addActionListener(e -> refreshStatistics());
        add(refreshButton, BorderLayout.SOUTH);
        
        refreshStatistics();
    }
    
    public void refreshStatistics() {
        StringBuilder sb = new StringBuilder();
        List<StudySession> sessions = sessionManager.getSessions();
        
        if (sessions.isEmpty()) {
            statsArea.setText("No study sessions recorded yet.");
            return;
        }
        
        // Total hours
        long totalSeconds = 0;
        for (StudySession s : sessions) {
            totalSeconds += s.getDurationSeconds();
        }
        
        double totalHours = totalSeconds / 3600.0;
        sb.append("=== OVERALL STATISTICS ===\n\n");
        sb.append(String.format("Total Study Time: %.2f hours\n", totalHours));
        sb.append(String.format("Total Sessions: %d\n", sessions.size()));
        sb.append(String.format("Average Session: %.2f minutes\n\n", (totalSeconds / 60.0) / sessions.size()));
        
        // Subject-wise breakdown
        sb.append("=== SUBJECT-WISE BREAKDOWN ===\n\n");
        Map<String, Long> subjectTime = new HashMap<>();
        Map<String, Integer> subjectCount = new HashMap<>();
        
        for (StudySession s : sessions) {
            subjectTime.put(s.getSubject(), 
                subjectTime.getOrDefault(s.getSubject(), 0L) + s.getDurationSeconds());
            subjectCount.put(s.getSubject(), 
                subjectCount.getOrDefault(s.getSubject(), 0) + 1);
        }
        
        for (String subject : subjectTime.keySet()) {
            double hours = subjectTime.get(subject) / 3600.0;
            int count = subjectCount.get(subject);
            sb.append(String.format("%s:\n", subject));
            sb.append(String.format("  Total Time: %.2f hours\n", hours));
            sb.append(String.format("  Sessions: %d\n", count));
            sb.append(String.format("  Avg Session: %.2f minutes\n\n", 
                (subjectTime.get(subject) / 60.0) / count));
        }
        
        statsArea.setText(sb.toString());
    }
}