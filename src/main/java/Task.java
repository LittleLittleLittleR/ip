
public class Task {
  private String name;
  private boolean marked;

  public Task(String name) {
    this.name = name;
    this.marked = false;
  }

  public boolean compareName(String name) {
    return this.name.equals(name);
  }

  public void mark() {
    marked = true;
  }

  public void unmark() {
    marked = false;
  }

  @Override
  public String toString() {
    return (marked ? "[X] " : "[ ] ") + name;
  }
}