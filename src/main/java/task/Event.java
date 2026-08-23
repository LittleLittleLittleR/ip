package task;

/**
 * Represents a task that occurs within a specific time frame.
 */
public class Event extends Task {

  private String from;
  private String to;

  public Event(String name, String from, String to) {
    super(name);
    this.from = from;
    this.to = to;
  }

  /**
   * Encodes this event as a single line string for saving to a file.
   */
  @Override
  public String toFileString() {
    return "E | " + (super.isMarked() ? "1" : "0") + " | " + super.getName() + " | " + from + " | " + to;
  }

  @Override
  public String toString() {
    return "[E]" + super.toString() + " (" + from + " to " + to + ")";
  }
  
}
