package task;

/**
 * Represents a simple task without any additional attributes.
 */
public class Todo extends Task {

  public Todo(String name) {
    super(name);
  }

  @Override
  public String toString() {
    return "[T]" + super.toString();
  }
}
