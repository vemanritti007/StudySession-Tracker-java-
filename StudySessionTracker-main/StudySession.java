import java.io.Serializable;
import java.util.Date;

public class StudySession implements Serializable {
    private static final long serialVersionUID = 1L;
    private String subject;
    private Date startTime;
    private long durationSeconds;
    private String notes;
    
    public StudySession(String subject, Date startTime, long durationSeconds, String notes) {
        this.subject = subject;
        this.startTime = startTime;
        this.durationSeconds = durationSeconds;
        this.notes = notes;
    }
    
    public String getSubject() { return subject; }
    public Date getStartTime() { return startTime; }
    public long getDurationSeconds() { return durationSeconds; }
    public String getNotes() { return notes; }
    
    public String getDurationFormatted() {
        long hours = durationSeconds / 3600;
        long minutes = (durationSeconds % 3600) / 60;
        long seconds = durationSeconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
} 