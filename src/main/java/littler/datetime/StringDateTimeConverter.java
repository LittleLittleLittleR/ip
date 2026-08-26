package littler.datetime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import littler.exception.LittleRException;

/**
 * Converts between date/time strings and ParsedDateTime values.
 * Handles both directions: parsing raw input into ParsedDateTime, and
 * formatting a ParsedDateTime back into a display string.
 */
public final class StringDateTimeConverter {

  private static final DateTimeFormatter DISPLAY_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  private static final DateTimeFormatter DISPLAY_TIME = DateTimeFormatter.ofPattern("HH:mm");

  // Formats that include a time component (HHmm, 24-hour)
  private static final List<DateTimeFormatter> DATE_TIME_FORMATS = List.of(
    DateTimeFormatter.ofPattern("d-M-yyyy HHmm"),
    DateTimeFormatter.ofPattern("yyyy-M-d HHmm")
  );

  // Date-only formats; result will have no time component
  private static final List<DateTimeFormatter> DATE_ONLY_FORMATS = List.of(
    DateTimeFormatter.ofPattern("d-M-yyyy"),
    DateTimeFormatter.ofPattern("yyyy-M-d")
  );

  private StringDateTimeConverter() {}

  /**
   * Encodes a ParsedDateTime for storage, 
   * e.g. "2019-12-02|18:00" or "2019-12-02|NONE" if no time was specified. 
   * Independent of the user-facing parse()/format() formats, 
   * so a change to accepted input formats can never silently break saved data.
   */
  public static String toStorageString(ParsedDateTime dateTime) {
    String dateStr = dateTime.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
    String timeStr = dateTime.hasTime()
        ? dateTime.getTime().format(DateTimeFormatter.ISO_LOCAL_TIME)
        : "NONE";
    return dateStr + "|" + timeStr;
  }

  /**
   * Decodes a ParsedDateTime previously written by toStorageString().
   * @throws LittleRException if the stored string is malformed
   */
  public static ParsedDateTime fromStorageString(String stored) throws LittleRException {
    String[] parts = stored.split("\\|");
    if (parts.length != 2) {
      throw new LittleRException("Corrupted stored date: " + stored);
    }
    try {
      LocalDate date = LocalDate.parse(parts[0]);
      LocalTime time = parts[1].equals("NONE") ? null : LocalTime.parse(parts[1]);
      return new ParsedDateTime(date, time);
    } catch (DateTimeParseException e) {
      throw new LittleRException("Corrupted stored date: " + stored);
    }
  }

  /**
   * Parses a date/time string against each accepted format
   * in turn, returning the first successful match.
   * @throws LittleRException if the string matches none of the accepted formats
   */
  public static ParsedDateTime parse(String input) throws LittleRException {
    String trimmed = input.trim();

    for (DateTimeFormatter format : DATE_TIME_FORMATS) {
      try {
        LocalDateTime dateTime = LocalDateTime.parse(trimmed, format);
        return new ParsedDateTime(dateTime.toLocalDate(), dateTime.toLocalTime());
      } catch (DateTimeParseException ignored) {
        // try next format
      }
    }

    for (DateTimeFormatter format : DATE_ONLY_FORMATS) {
      try {
        LocalDate date = LocalDate.parse(trimmed, format);
        return new ParsedDateTime(date, null);
      } catch (DateTimeParseException ignored) {
        // try next format
      }
    }

    throw new LittleRException(
      "Invalid date format: '" + input + "'. Accepted formats: "
      + "d-M-yyyy, or yyyy-M-d, each optionally followed by a time as HHmm "
      + "(e.g. 2-12-2019 1800). Time is omitted from output if not provided.");
  }

  /**
   * Formats a ParsedDateTime for display, 
   * e.g. "2019-12-02" or "2019-12-02 18:00" depending on whether a time was specified.
   */
  public static String format(ParsedDateTime dateTime) {
    String display = dateTime.getDate().format(DISPLAY_DATE);
    if (dateTime.hasTime()) {
      display += " " + dateTime.getTime().format(DISPLAY_TIME);
    }
    return display;
  }

  /**
   * Formats a bare LocalDate for display (used where no ParsedDateTime
   * exists, e.g. the "on <date>" command's query date).
   */
  public static String formatDate(LocalDate date) {
    return date.format(DISPLAY_DATE);
  }

  /**
   * Represents a date with an optional time component. 
   */
  public static final class ParsedDateTime {
    private final LocalDate date;
    private final LocalTime time; // null means no time was specified

    public ParsedDateTime(LocalDate date, LocalTime time) {
      this.date = date;
      this.time = time;
    }

    /**
     * Compares date of ParsedDateTime to another,
     * returning -1 if this is earlier, 0 if equal, and 1 if later.
     */
    public int compareDate(ParsedDateTime other) {
      if (this.date.isBefore(other.date)) {
        return -1;
      } else if (this.date.isAfter(other.date)) {
        return 1;
      } else {
        return 0;
      }
    }

    /**
     * Compares time of ParsedDateTime to another,
     * returning -1 if this is earlier, 0 if equal, and 1 if later.
     * If either ParsedDateTime has no time, they are considered equal.
     */
    public int compareTime(ParsedDateTime other) {
      if (this.time == null || other.time == null) {
        return 0; // Assume equal if either has no time
      }
      if (this.time.isBefore(other.time)) {
        return -1;
      } else if (this.time.isAfter(other.time)) {
        return 1;
      } else {
        return 0;
      }
    }



    /**
     * Returns the date component.
     */
    private LocalDate getDate() {
      return date;
    }

    /**
     * Returns the time component, or null if none was specified.
     */
    private LocalTime getTime() {
      return time;
    }

    public boolean hasTime() {
      return time != null;
    }

    @Override
    public String toString() {
      return format(this);
    }
  }
}