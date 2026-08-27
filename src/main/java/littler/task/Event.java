package littler.task;

import littler.datetime.StringDateTimeConverter;
import littler.datetime.StringDateTimeConverter.ParsedDateTime;

/**
 * Represents a task that occurs within a specific time frame, 
 * defined by a start date/time and an end date/time.
 */
public class Event extends Task implements Schedulable {
    
    private ParsedDateTime from;
    private ParsedDateTime to;
    
    /**
     * Constructs a new Event task with the specified description, start date/time, and end date/time.
     * @param name the description of the event
     * @param from the starting date and optional time of the event
     * @param to the ending date and optional time of the event
     */
    public Event(String name, ParsedDateTime from, ParsedDateTime to) {
        super(name);
        this.from = from;
        this.to = to;
    }
    
    /**
     * Checks whether this event occurs on or within the specified target date and time range.
     * @param date the target parsed date/time to check against
     * @return true if the event overlaps with or occurs on the given date/time; false otherwise
     */
    @Override
    public boolean occursOn(ParsedDateTime date) {
        if (date.hasTime()) {
            // If the user provided a time, we only consider it a match,
            // if both the date and time are within the range
            return date.compareDate(from) >= 0 && date.compareDate(to) <= 0
            && date.compareTime(from) >= 0 && date.compareTime(to) <= 0;
        } else {
            return date.compareDate(from) >= 0 && date.compareDate(to) < 0;
        }
    }
    
    /**
     * Encodes this event as a single-line string for saving to file storage.
     * @return the formatted data string representing this event
     */
    @Override
    public String toFileString() {
        return "E | " + (super.isMarked() ? "1" : "0") + " | " + super.getName() + " | " 
        + StringDateTimeConverter.toStorageString(from) + " | " 
        + StringDateTimeConverter.toStorageString(to);
    }
    
    /**
     * Returns a user-friendly string representation of this event task, including its type icon and duration.
     * @return the formatted string representation of the event
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() 
        + " (" + from + " to " + to + ")";
    }
    
}
