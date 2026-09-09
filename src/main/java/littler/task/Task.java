package littler.task;

import java.util.Map;

import littler.exception.LittleRException;

/**
 * Represents an abstract task containing a description name and a completion status.
 * Serves as the base class for specific task types such as Todo, Deadline, and Event.
 */
public abstract class Task {
    public static final String NAME_DELIMITER = "/name";

    private String name;
    private boolean marked;

    /**
     * Constructs a new Task with the specified name and initializes its completion status to false.
     *
     * @param name the description or name of the task
     */
    public Task(String name) {
        this.name = name;
        this.marked = false;
    }

    /**
     * Returns a new task of the same concrete type as this one, with any fields present in
     * the given updates map replaced, and all other fields (including completion status)
     * carried over unchanged.
     *
     * @param updates a map from field delimiter (e.g. "/name") to the new raw value for that field
     * @return the updated task instance
     * @throws LittleRException if updates contains a field not applicable to this task type,
     *     or a date field's value cannot be parsed
     */
    public abstract Task withUpdates(Map<String, String> updates) throws LittleRException;

    /**
     * Copies this task's completion status onto another task instance, so building an
     * updated or duplicated task never silently resets whether it was marked done.
     *
     * @param other the task to copy this task's completion status onto
     */
    protected void copyMarkedStatusTo(Task other) {
        if (this.marked) {
            other.mark();
        }
    }

    /**
     * Encodes this task into a formatted single-line string representation for persistent storage.
     *
     * @return the formatted data string representing this task
     */
    public abstract String toFileString();

    /**
     * Marks the task as completed.
     */
    public void mark() {
        marked = true;
    }

    /**
     * Unmarks the task, setting its state to incomplete.
     */
    public void unmark() {
        marked = false;
    }

    /**
     * Returns the description or name of the task.
     *
     * @return the task name
     */
    protected String getName() {
        return name;
    }

    /**
     * Returns the completion state of the task.
     *
     * @return true if the task is marked as completed; false otherwise
     */
    protected boolean isMarked() {
        return marked;
    }

    /**
     * Returns a string representation of the task including its completion status indicator.
     *
     * @return the formatted status string of the task
     */
    @Override
    public String toString() {
        return (marked ? "[X] " : "[ ] ") + name;
    }
}
