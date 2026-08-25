package task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import datetime.DateFormats;

/**
 * Represents a task that occurs within a specific time frame.
 */
public class Event extends Task {

  private LocalDateTime from;
  private LocalDateTime to;

  public Event(String name, LocalDateTime from, LocalDateTime to) {
    super(name);
    this.from = from;
    this.to = to;
  }

  /**
   * Encodes this event as a single line string for saving to a file.
   */
  @Override
  public String toFileString() {
    return "E | " + (super.isMarked() ? "1" : "0") + " | " + super.getName() + " | " 
      + from.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + " | " 
      + to.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
  }

  @Override
  public String toString() {
    return "[E]" + super.toString() 
      + " (" + from.format(DateFormats.DISPLAY_FORMAT) + " to " + to.format(DateFormats.DISPLAY_FORMAT) + ")";
  }
  
}
