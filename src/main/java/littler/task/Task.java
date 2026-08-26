package littler.task;

/**
 * Represents a single task with a name and a completion state.
 */
public abstract class Task {
  private String name;
  private boolean marked;

  public Task(String name) {
    this.name = name;
    this.marked = false;
  }

  /**
   * Encodes this task as a single line string for saving to a file.
   */
  public abstract String toFileString();

  /**
  * Mark the task as completed
  */
  public void mark() {
    marked = true;
  }

  /**
  * Unmark the task as not completed
  */
  public void unmark() {
    marked = false;
  }

  /**
   * Returns the name of the task.
   */
  protected String getName() {
    return name;
  }

  /**
   * Returns the completion state of the task.
   */
  protected boolean isMarked() {
    return marked;
  }

  @Override
  public String toString() {
    return (marked ? "[X] " : "[ ] ") + name;
  }
}