package task;

import datetime.StringDateTimeConverter.ParsedDateTime;

/**
 * Represents a task that occurs within a specific time frame.
 */
public class Event extends Task implements Schedulable {

  private ParsedDateTime from;
  private ParsedDateTime to;

  public Event(String name, ParsedDateTime from, ParsedDateTime to) {
    super(name);
    this.from = from;
    this.to = to;
  }

  /**
   * Returns true if this event occurs on the given date.
   */
  @Override
  public boolean occursOn(ParsedDateTime date) {
    if (date.hasTime()) {
      // If the user provided a time, we only consider it a match if both the date and time are within the range
      return date.compareDate(from) >= 0 && date.compareDate(to) <= 0
          && date.compareTime(from) >= 0 && date.compareTime(to) <= 0;
    } else {
      return date.compareDate(from) >= 0 && date.compareDate(to) < 0;
    }
  }

  /**
   * Encodes this event as a single line string for saving to a file.
   */
  @Override
  public String toFileString() {
    return "E | " + (super.isMarked() ? "1" : "0") + " | " + super.getName() + " | " 
      + from + " | " + to;
  }

  @Override
  public String toString() {
    return "[E]" + super.toString() 
      + " (" + from + " to " + to + ")";
  }
  
}
