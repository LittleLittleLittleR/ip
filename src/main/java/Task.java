
public class Task {
  private String name;
  private boolean marked;

  public Task(String name) {
    this.name = name;
    this.marked = false;
  }

  /**
  * Compare the name of the task with a given name
  * @param name the name to compare with
  * @return true if the names are equal, false otherwise
  */
  public boolean compareName(String name) {
    return this.name.equals(name);
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