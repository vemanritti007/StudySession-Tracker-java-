import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class GoalsPanel extends JPanel {
    private JTextField dailyGoalField, weeklyGoalField;
    private JProgressBar dailyProgress, weeklyProgress;
    private JLabel dailyLabel, weeklyLabel;
    private static final String GOALS_FILE = "goals.dat";
    private int dailyGoalMinutes = 120;
    private int weeklyGoalMinutes = 600;
    private SessionManager sessionManager;
    
    public GoalsPanel(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        setLayout(new GridLayout(6, 1, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Daily Goal
        JPanel dailyPanel = new JPanel(new FlowLayout());
        dailyPanel.add(new JLabel("Daily Goal (minutes):"));
        dailyGoalField = new JTextField(String.valueOf(dailyGoalMinutes), 10);
        dailyPanel.add(dailyGoalField);
        add(dailyPanel);
        
        dailyProgress = new JProgressBar(0, 100);
        dailyProgress.setStringPainted(true);
        add(dailyProgress);
        
        dailyLabel = new JLabel("0 / 0 minutes");
        add(dailyLabel);
        
        // Weekly Goal
        JPanel weeklyPanel = new JPanel(new FlowLayout());
        weeklyPanel.add(new JLabel("Weekly Goal (minutes):"));
        weeklyGoalField = new JTextField(String.valueOf(weeklyGoalMinutes), 10);
        weeklyPanel.add(weeklyGoalField);
        add(weeklyPanel);
        
        weeklyProgress = new JProgressBar(0, 100);
        weeklyProgress.setStringPainted(true);
        add(weeklyProgress);
        
        weeklyLabel = new JLabel("0 / 0 minutes");
        add(weeklyLabel);
        
        // Save Button
        JButton saveButton = new JButton("Save Goals");
        saveButton.addActionListener(e -> saveGoals());
        add(saveButton);
        
        loadGoals();
        refreshProgress();
    }
    
    private void saveGoals() {
        try {
            int daily = Integer.parseInt(dailyGoalField.getText());
            int weekly = Integer.parseInt(weeklyGoalField.getText());
            
            if (daily < 0 || weekly < 0) {
                JOptionPane.showMessageDialog(this, "Goal values must be positive numbers!");
                return;
            }
            
            if (weekly < daily) {
                JOptionPane.showMessageDialog(this, "Weekly goal should be at least as large as daily goal!");
                return;
            }
            
            dailyGoalMinutes = daily;
            weeklyGoalMinutes = weekly;
            
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(GOALS_FILE))) {
                oos.writeObject(new int[]{dailyGoalMinutes, weeklyGoalMinutes});
            }
            
            JOptionPane.showMessageDialog(this, "Goals saved successfully!");
            refreshProgress();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers!");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving goals: " + ex.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void loadGoals() {
        File file = new File(GOALS_FILE);
        if (!file.exists()) return;
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(GOALS_FILE))) {
            int[] goals = (int[]) ois.readObject();
            dailyGoalMinutes = goals[0];
            weeklyGoalMinutes = goals[1];
            dailyGoalField.setText(String.valueOf(dailyGoalMinutes));
            weeklyGoalField.setText(String.valueOf(weeklyGoalMinutes));
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Error loading goals: " + e.getMessage(), "Load Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void refreshProgress() {
        if (sessionManager == null) {
            dailyLabel.setText("0 / " + dailyGoalMinutes + " minutes");
            weeklyLabel.setText("0 / " + weeklyGoalMinutes + " minutes");
            dailyProgress.setValue(0);
            weeklyProgress.setValue(0);
            return;
        }
        
        Calendar today = Calendar.getInstance();
        Calendar weekStart = Calendar.getInstance();
        weekStart.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        weekStart.set(Calendar.HOUR_OF_DAY, 0);
        weekStart.set(Calendar.MINUTE, 0);
        weekStart.set(Calendar.SECOND, 0);
        
        long dailyMinutes = 0;
        long weeklyMinutes = 0;
        
        try {
            for (StudySession session : sessionManager.getSessions()) {
                if (session == null || session.getStartTime() == null) continue;
                
                Calendar sessionTime = Calendar.getInstance();
                sessionTime.setTime(session.getStartTime());
                
                if (sessionTime.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                    sessionTime.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
                    dailyMinutes += session.getDurationSeconds() / 60;
                }
                
                if (sessionTime.after(weekStart) || sessionTime.equals(weekStart)) {
                    weeklyMinutes += session.getDurationSeconds() / 60;
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error calculating progress: " + e.getMessage());
        }
        
        dailyLabel.setText(dailyMinutes + " / " + dailyGoalMinutes + " minutes");
        weeklyLabel.setText(weeklyMinutes + " / " + weeklyGoalMinutes + " minutes");
        
        int dailyPercent = dailyGoalMinutes > 0 ? (int) (dailyMinutes * 100 / dailyGoalMinutes) : 0;
        int weeklyPercent = weeklyGoalMinutes > 0 ? (int) (weeklyMinutes * 100 / weeklyGoalMinutes) : 0;
        
        dailyProgress.setValue(Math.min(dailyPercent, 100));
        weeklyProgress.setValue(Math.min(weeklyPercent, 100));
    }
}