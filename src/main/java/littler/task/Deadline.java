package littler.task;

import littler.datetime.StringDateTimeConverter;
import littler.datetime.StringDateTimeConverter.ParsedDateTime;

/**
 * Represents a task with a specific deadline date and optional time.
 */
public class Deadline extends Task implements Schedulable {

    private ParsedDateTime due;

    /**
     * Constructs a new Deadline task with the specified description and due date/time.
     *
     * @param name the description of the task
     * @param due the target date and optional time by which the task is due
     */
    public Deadline(String name, ParsedDateTime due) {
        super(name);
        assert due != null : "due date is required and should already be parsed by the time it reaches here";
        this.due = due;
    }

    /**
     * Checks whether this deadline matches or falls on the specified date and time.
     *
     * @param date the target parsed date/time to check against
     * @return true if the deadline occurs on the specified date (and time, if specified); false otherwise
     */
    @Override
    public boolean isOccurringOn(ParsedDateTime date) {
        if (date.hasTime()) {
            // If the user provided a time, we only consider it a match if both date and time match
            return date.compareDate(due) == 0 && date.compareTime(due) == 0;
        } else {
            return date.compareDate(due) == 0;
        }
    }

    /**
     * Encodes this deadline as a single-line string for saving to file storage.
     *
     * @return the formatted data string representing this deadline task
     */
    @Override
    public String toFileString() {
        return "D | " + (super.isMarked() ? "1" : "0") + " | " + super.getName() + " | "
            + StringDateTimeConverter.toStorageString(due);
    }

    /**
     * Returns a user-friendly string representation of this deadline task,
     * including its type icon and due date/time.
     *
     * @return the formatted string representation of the deadline task
     */
    @Override
    public String toString() {
        return "[D]" + super.toString()
            + " (due: " + due + ")";
    }
}
