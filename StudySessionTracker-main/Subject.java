import java.io.Serializable;

public class Subject implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String color;
    
    public Subject(String name, String color) {
        this.name = name;
        this.color = color;
    }
    
    public String getName() { return name; }
    public String getColor() { return color; }
    
    @Override
    public String toString() { return name; }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Subject subject = (Subject) obj;
        return name.equals(subject.name);
    }
    
    @Override
    public int hashCode() {
        return name.hashCode();
    }
}