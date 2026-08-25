package task;

import datetime.StringDateTimeConverter.ParsedDateTime;

/**
 * An interface for tasks that can be scheduled on a specific date.
 */
public interface Schedulable {
  /**
   * Returns true if this task is due, or occurring, on the given date.
   */
  boolean occursOn(ParsedDateTime date);
}