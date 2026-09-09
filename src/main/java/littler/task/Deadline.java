package littler.task;

import java.util.Objects;

import littler.datetime.StringDateTimeConverter;
import littler.datetime.StringDateTimeConverter.ParsedDateTime;

/**
 * Represents a task with a specific deadline date and optional time.
 */
public class Deadline extends Task implements Schedulable {
    public static final String TYPE_CODE = "D";
    public static final String INPUT_DELIMITER = "/by";
    private final ParsedDateTime due;

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
        return TYPE_CODE + " | " + (super.isMarked() ? "1" : "0") + " | " + super.getName() + " | "
            + StringDateTimeConverter.toStorageString(due);
    }

    /**
     * Returns the date/time value used to order this task chronologically relative to other schedulable tasks.
     *
     * @return the parsed date/time representing this task's position in time
     */
    @Override
    public ParsedDateTime getSortDate() {
        return due;
    }

    /**
     * Checks if this deadline task is equal to another object.
     * Two deadlines are considered equal if they have the same name and due date/time.
     *
     * @param obj the object to compare with
     * @return true if the other object is a Deadline with the same name and due date/time, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Deadline other)) {
            return false;
        }
        return super.getName().equalsIgnoreCase(other.getName()) && due.equals(other.due);
    }

    /**
     * Computes the hash code for this deadline task based on its class, name, and due date/time.
     *
     * @return the hash code of this task
     */
    @Override
    public int hashCode() {
        return Objects.hash(Deadline.class, super.getName().toLowerCase(), due);
    }

    /**
     * Returns a user-friendly string representation of this deadline task,
     * including its type icon and due date/time.
     *
     * @return the formatted string representation of the deadline task
     */
    @Override
    public String toString() {
        return "[" + TYPE_CODE + "]" + super.toString()
            + " (due: " + due + ")";
    }
}
