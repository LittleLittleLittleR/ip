package littler.task;

/**
 * Represents a simple task without any additional attributes.
 */
public class Todo extends Task {
    
    public Todo(String name) {
        super(name);
    }
    
    /**
     * Encodes this todo as a single line string for saving to a file.
     */
    @Override
    public String toFileString() {
        return "T | " + (super.isMarked() ? "1" : "0") + " | " + super.getName();
    }
    
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
