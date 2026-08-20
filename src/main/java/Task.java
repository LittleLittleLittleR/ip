/**
 * Represents a single task with a name and a completion state.
 */
public class Task {
  private String name;
  private boolean marked;

  public Task(String name) {
    this.name = name;
    this.marked = false;
  }

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

  @Override
  public String toString() {
    return (marked ? "[X] " : "[ ] ") + name;
  }
}