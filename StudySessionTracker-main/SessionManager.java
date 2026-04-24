import java.io.*;
import java.util.*;
import javax.swing.*;

public class SessionManager {
    private List<StudySession> sessions;
    private static final String SESSIONS_FILE = "sessions.dat";
    
    public SessionManager() {
        sessions = new ArrayList<>();
    }
    
    public void addSession(StudySession session) {
        sessions.add(session);
        saveSessions();
    }
    
    public List<StudySession> getSessions() {
        return sessions;
    }
    
    public void saveSessions() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SESSIONS_FILE))) {
            List<Object[]> sessionData = new ArrayList<>();
            for (StudySession s : sessions) {
                sessionData.add(new Object[]{
                    s.getSubject(), 
                    s.getStartTime(), 
                    s.getDurationSeconds(), 
                    s.getNotes()
                });
            }
            oos.writeObject(sessionData);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving sessions: " + e.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    @SuppressWarnings("unchecked")
    public void loadSessions() {
        File file = new File(SESSIONS_FILE);
        if (!file.exists()) return;
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SESSIONS_FILE))) {
            List<Object[]> sessionData = (List<Object[]>) ois.readObject();
            sessions.clear();
            for (Object[] data : sessionData) {
                sessions.add(new StudySession(
                    (String) data[0],
                    (Date) data[1],
                    (Long) data[2],
                    (String) data[3]
                ));
            }
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Error loading sessions: " + e.getMessage(), "Load Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}