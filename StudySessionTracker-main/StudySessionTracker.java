import javax.swing.*;

public class StudySessionTracker extends JFrame {
    private JTabbedPane tabbedPane;
     TimerPanel timerPanel;
    private HistoryPanel historyPanel;
    private StatisticsPanel statisticsPanel;
    private GoalsPanel goalsPanel;
    private SubjectManager subjectManager;
    private SessionManager sessionManager;
    
    public StudySessionTracker() {
        setTitle("Study Session Tracker");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        subjectManager = new SubjectManager();
        sessionManager = new SessionManager();
        
        tabbedPane = new JTabbedPane();
        
        timerPanel = new TimerPanel(subjectManager, sessionManager, this);
        historyPanel = new HistoryPanel(sessionManager);
        statisticsPanel = new StatisticsPanel(sessionManager, subjectManager);
        goalsPanel = new GoalsPanel(sessionManager);
        
        tabbedPane.addTab("Timer", timerPanel);
        tabbedPane.addTab("History", historyPanel);
        tabbedPane.addTab("Statistics", statisticsPanel);
        tabbedPane.addTab("Goals", goalsPanel);
        tabbedPane.addTab("Subjects", new SubjectPanel(subjectManager, this));
        
        add(tabbedPane);
        
        subjectManager.loadSubjects();
        sessionManager.loadSessions();
        goalsPanel.loadGoals();
    }
    
    public void refreshAllPanels() {
        historyPanel.refreshTable();
        statisticsPanel.refreshStatistics();
        goalsPanel.refreshProgress();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StudySessionTracker tracker = new StudySessionTracker();
            tracker.setVisible(true);
        });
    }
}

