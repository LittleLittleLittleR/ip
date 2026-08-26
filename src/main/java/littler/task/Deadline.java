package littler.task;

import littler.datetime.StringDateTimeConverter.ParsedDateTime;


/**
 * Represents a task with a deadline.
 */
public class Deadline extends Task implements Schedulable {

  private ParsedDateTime due;

  public Deadline(String name, ParsedDateTime due) {
    super(name);
    this.due = due;
  }

  /**
   * Returns true if this deadline is due on the given date.
   */
  @Override
  public boolean occursOn(ParsedDateTime date) {
    if (date.hasTime()) {
      // If the user provided a time, we only consider it a match if both date and time match
      return date.compareDate(due) == 0 && date.compareTime(due) == 0;
    } else {
      return date.compareDate(due) == 0;
    }
  }

  /**
   * Encodes this deadline as a single line string for saving to a file.
   */
  @Override
  public String toFileString() {
    return "D | " + (super.isMarked() ? "1" : "0") + " | " + super.getName() + " | " 
      + due;
  }

  @Override
  public String toString() {
    return "[D]" + super.toString() 
      + " (due: " + due + ")";
  }
}
