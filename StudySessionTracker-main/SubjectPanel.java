import java.awt.*;
import javax.swing.*;

public class SubjectPanel extends JPanel {
    private JList<Subject> subjectList;
    private DefaultListModel<Subject> listModel;
    private SubjectManager subjectManager;
    private StudySessionTracker mainFrame;
    
    public SubjectPanel(SubjectManager subjectManager, StudySessionTracker mainFrame) {
        this.subjectManager = subjectManager;
        this.mainFrame = mainFrame;
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        listModel = new DefaultListModel<>();
        subjectList = new JList<>(listModel);
        add(new JScrollPane(subjectList), BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Add Subject");
        JButton removeButton = new JButton("Remove Subject");
        
        addButton.addActionListener(e -> addSubject());
        removeButton.addActionListener(e -> removeSubject());
        
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        add(buttonPanel, BorderLayout.SOUTH);
        
        refreshList();
    }
    
    private void addSubject() {
        String name = JOptionPane.showInputDialog(this, "Enter subject name:");
        if (name != null && !name.trim().isEmpty()) {
            String trimmedName = name.trim();
            
            // Check for duplicate subject names
            for (Subject s : subjectManager.getSubjects()) {
                if (s.getName().equalsIgnoreCase(trimmedName)) {
                    JOptionPane.showMessageDialog(this, "Subject '" + trimmedName + "' already exists!");
                    return;
                }
            }
            
            subjectManager.addSubject(new Subject(trimmedName, "#3498db"));
            refreshList();
            if (mainFrame != null && mainFrame.timerPanel != null) {
                mainFrame.timerPanel.updateSubjectCombo();
            }
        }
    }
    
    private void removeSubject() {
        Subject selected = subjectList.getSelectedValue();
        if (selected != null) {
            int result = JOptionPane.showConfirmDialog(
                this, 
                "Are you sure you want to remove '" + selected.getName() + "'?", 
                "Confirm Remove", 
                JOptionPane.YES_NO_OPTION
            );
            if (result == JOptionPane.YES_OPTION) {
                subjectManager.removeSubject(selected);
                refreshList();
                if (mainFrame != null && mainFrame.timerPanel != null) {
                    mainFrame.timerPanel.updateSubjectCombo();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a subject to remove.");
        }
    }
    
    private void refreshList() {
        listModel.clear();
        for (Subject s : subjectManager.getSubjects()) {
            listModel.addElement(s);
        }
    }
}