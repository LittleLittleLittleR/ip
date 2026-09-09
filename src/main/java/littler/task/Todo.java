package littler.task;

import java.util.Objects;

/**
 * Represents a basic task without any specific dates or deadlines.
 */
public class Todo extends Task {
    public static final String TYPE_CODE = "T";

    /**
     * Constructs a new Todo task with the specified name or description.
     *
     * @param name the description of the task
     */
    public Todo(String name) {
        super(name);
    }

    /**
     * Encodes this todo as a formatted single-line string for file storage.
     *
     * @return the formatted data string representing this task
     */
    @Override
    public String toFileString() {
        return TYPE_CODE + " | " + (super.isMarked() ? "1" : "0") + " | " + super.getName();
    }

    /**
     * Checks if this todo task is equal to another object.
     * Two todos are considered equal if they have the same name, ignoring case.
     *
     * @param obj the object to compare with
     * @return true if the other object is a Todo with the same name, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Todo other)) {
            return false;
        }
        return super.getName().equalsIgnoreCase(other.getName());
    }

    /**
     * Computes the hash code for this todo task based on its class and name.
     *
     * @return the hash code of this task
     */
    @Override
    public int hashCode() {
        return Objects.hash(Todo.class, super.getName().toLowerCase());
    }

    /**
     * Returns a user-friendly string representation of this todo task, including its type icon.
     *
     * @return the string representation of the task
     */
    @Override
    public String toString() {
        return "[" + TYPE_CODE + "]" + super.toString();
    }
}
