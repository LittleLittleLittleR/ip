package task;

/**
 * Represents a task with a deadline.
 */
public class Deadline extends Task {

  private String due;

  public Deadline(String name, String due) {
    super(name);
    this.due = due;
  }

  @Override
  public String toString() {
    return "[D]" + super.toString() + " (due: " + due + ")";
  }
}
