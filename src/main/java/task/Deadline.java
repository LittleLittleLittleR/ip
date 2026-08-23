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

  /**
   * Encodes this deadline as a single line string for saving to a file.
   */
  @Override
  public String toFileString() {
    return "D | " + (super.isMarked() ? "1" : "0") + " | " + super.getName() + " | " + due;
  }

  @Override
  public String toString() {
    return "[D]" + super.toString() + " (due: " + due + ")";
  }
}
