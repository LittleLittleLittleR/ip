package littler.task;

/**
 * Represents a basic task without any specific dates or deadlines.
 */
public class Todo extends Task {

  /**
   * Constructs a new Todo task with the specified name or description.
   * @param name the description of the task
   */
  public Todo(String name) {
    super(name);
  }

  /**
   * Encodes this todo as a formatted single-line string for file storage.
   * @return the formatted data string representing this task
   */
  @Override
  public String toFileString() {
    return "T | " + (super.isMarked() ? "1" : "0") + " | " + super.getName();
  }

  /**
   * Returns a user-friendly string representation of this todo task, including its type icon.
   * @return the string representation of the task
   */
  @Override
  public String toString() {
    return "[T]" + super.toString();
  }
}
