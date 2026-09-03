package littler.task;

import littler.datetime.StringDateTimeConverter;
import littler.datetime.StringDateTimeConverter.ParsedDateTime;

/**
 * Represents a task that occurs within a specific time frame,
 * defined by a start date/time and an end date/time.
 */
public class Event extends Task implements Schedulable {
    public static final String TYPE_CODE = "E";
    public static final String FROM_DELIMITER = "/from";
    public static final String TO_DELIMITER = "/to";
    private final ParsedDateTime startDateTime;
    private final ParsedDateTime endDateTime;

    /**
     * Constructs a new Event task with the specified description, start date/time, and end date/time.
     *
     * @param name the description of the event
     * @param from the starting date and optional time of the event
     * @param to the ending date and optional time of the event
     */
    public Event(String name, ParsedDateTime startDateTime, ParsedDateTime endDateTime) {
        super(name);
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    /**
     * Checks whether this event occurs on or within the specified target date and time range.
     *
     * @param date the target parsed date/time to check against
     * @return true if the event overlaps with or occurs on the given date/time; false otherwise
     */
    @Override
    public boolean isOccurringOn(ParsedDateTime date) {
        boolean isOnOrAfterStartDate = date.compareDate(startDateTime) >= 0;

        if (!date.hasTime()) {
            boolean isBeforeEndDate = date.compareDate(endDateTime) < 0;
            return isOnOrAfterStartDate && isBeforeEndDate;
        }

        boolean isOnOrBeforeEndDate = date.compareDate(endDateTime) <= 0;
        boolean isOnOrAfterStartTime = date.compareTime(startDateTime) >= 0;
        boolean isOnOrBeforeEndTime = date.compareTime(endDateTime) <= 0;
        return isOnOrAfterStartDate && isOnOrBeforeEndDate && isOnOrAfterStartTime && isOnOrBeforeEndTime;
    }

    /**
     * Encodes this event as a single-line string for saving to file storage.
     *
     * @return the formatted data string representing this event
     */
    @Override
    public String toFileString() {
        return TYPE_CODE + " | " + (super.isMarked() ? "1" : "0") + " | " + super.getName() + " | "
            + StringDateTimeConverter.toStorageString(startDateTime) + " | "
            + StringDateTimeConverter.toStorageString(endDateTime);
    }

    /**
     * Returns a user-friendly string representation of this event task, including its type icon and duration.
     *
     * @return the formatted string representation of the event
     */
    @Override
    public String toString() {
        return "[" + TYPE_CODE + "]" + super.toString()
            + " (" + startDateTime + " to " + endDateTime + ")";
    }

}
