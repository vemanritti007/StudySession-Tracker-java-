import java.io.*;
import java.util.*;
import javax.swing.*;

public class SubjectManager {
    private List<Subject> subjects;
    private static final String SUBJECTS_FILE = "subjects.dat";

    public SubjectManager() {
        subjects = new ArrayList<>();
        loadSubjects(); // load automatically
    }

    public void addSubject(Subject subject) {
        subjects.add(subject);
        saveSubjects();
    }

    public void removeSubject(Subject subject) {
        subjects.remove(subject);
        saveSubjects();
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void saveSubjects() {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(SUBJECTS_FILE))) {
            oos.writeObject(subjects);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error saving subjects: " + e.getMessage(),
                    "Save Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    @SuppressWarnings("unchecked")
    public void loadSubjects() {
        File file = new File(SUBJECTS_FILE);
        if (!file.exists()) return;

        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(SUBJECTS_FILE))) {

            subjects = (List<Subject>) ois.readObject();

        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error loading subjects: " + e.getMessage(),
                    "Load Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
