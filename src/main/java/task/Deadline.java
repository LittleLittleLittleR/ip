package task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import datetime.DateFormats;

/**
 * Represents a task with a deadline.
 */
public class Deadline extends Task {

  private LocalDateTime due;

  public Deadline(String name, LocalDateTime due) {
    super(name);
    this.due = due;
  }

  /**
   * Encodes this deadline as a single line string for saving to a file.
   */
  @Override
  public String toFileString() {
    return "D | " + (super.isMarked() ? "1" : "0") + " | " + super.getName() + " | " 
      + due.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
  }

  @Override
  public String toString() {
    return "[D]" + super.toString() 
      + " (due: " + due.format(DateFormats.DISPLAY_FORMAT) + ")";
  }
}
