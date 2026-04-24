import java.awt.*;
import java.util.Date;
import javax.swing.*;

public class TimerPanel extends JPanel {
    private JLabel timerLabel;
    private JButton startButton, stopButton, pauseButton;
    private JComboBox<Subject> subjectCombo;
    private JTextArea notesArea;
    private Timer timer;
    private long elapsedSeconds = 0;
    private boolean isRunning = false;
    private SubjectManager subjectManager;
    private SessionManager sessionManager;
    private StudySessionTracker mainFrame;
    
    public TimerPanel(SubjectManager subjectManager, SessionManager sessionManager, StudySessionTracker mainFrame) {
        this.subjectManager = subjectManager;
        this.sessionManager = sessionManager;
        this.mainFrame = mainFrame;
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Timer Display
        timerLabel = new JLabel("00:00:00", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 48));
        add(timerLabel, BorderLayout.NORTH);
        
        // Center Panel
        JPanel centerPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        
        // Subject Selection
        JPanel subjectPanel = new JPanel(new FlowLayout());
        subjectPanel.add(new JLabel("Subject:"));
        subjectCombo = new JComboBox<>();
        updateSubjectCombo();
        subjectPanel.add(subjectCombo);
        centerPanel.add(subjectPanel);
        
        // Notes
        JPanel notesPanel = new JPanel(new BorderLayout());
        notesPanel.add(new JLabel("Notes:"), BorderLayout.NORTH);
        notesArea = new JTextArea(5, 30);
        notesArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        notesPanel.add(new JScrollPane(notesArea), BorderLayout.CENTER);
        centerPanel.add(notesPanel);
        
        add(centerPanel, BorderLayout.CENTER);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        startButton = new JButton("Start");
        pauseButton = new JButton("Pause");
        stopButton = new JButton("Stop");
        
        pauseButton.setEnabled(false);
        stopButton.setEnabled(false);
        
        startButton.addActionListener(e -> startTimer());
        pauseButton.addActionListener(e -> pauseTimer());
        stopButton.addActionListener(e -> stopTimer());
        
        buttonPanel.add(startButton);
        buttonPanel.add(pauseButton);
        buttonPanel.add(stopButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Timer
        timer = new Timer(1000, e -> {
            elapsedSeconds++;
            updateTimerDisplay();
        });
    }
    
    public void updateSubjectCombo() {
        subjectCombo.removeAllItems();
        for (Subject s : subjectManager.getSubjects()) {
            subjectCombo.addItem(s);
        }
    }
    
    private void startTimer() {
        if (subjectCombo.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "Please add a subject first!");
            return;
        }
        
        isRunning = true;
        timer.start();
        startButton.setEnabled(false);
        pauseButton.setEnabled(true);
        stopButton.setEnabled(true);
    }
    
    private void pauseTimer() {
        if (isRunning) {
            timer.stop();
            pauseButton.setText("Resume");
            isRunning = false;
        } else {
            timer.start();
            pauseButton.setText("Pause");
            isRunning = true;
        }
    }
    
    private void stopTimer() {
        timer.stop();
        
        if (elapsedSeconds > 0 && subjectCombo.getSelectedItem() != null) {
            Object selectedItem = subjectCombo.getSelectedItem();
            if (selectedItem instanceof Subject) {
                Subject subject = (Subject) selectedItem;
                StudySession session = new StudySession(
                    subject.getName(),
                    new Date(),
                    elapsedSeconds,
                    notesArea.getText()
                );
                sessionManager.addSession(session);
                mainFrame.refreshAllPanels();
                
                JOptionPane.showMessageDialog(this, 
                    "Session saved!\nDuration: " + session.getDurationFormatted());
            }
        }
        
        elapsedSeconds = 0;
        updateTimerDisplay();
        notesArea.setText("");
        startButton.setEnabled(true);
        pauseButton.setEnabled(false);
        pauseButton.setText("Pause");
        stopButton.setEnabled(false);
        isRunning = false;
    }
    
    private void updateTimerDisplay() {
        long hours = elapsedSeconds / 3600;
        long minutes = (elapsedSeconds % 3600) / 60;
        long seconds = elapsedSeconds % 60;
        timerLabel.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
    }
}