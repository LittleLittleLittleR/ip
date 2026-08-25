package datetime;

import exception.LittleRException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Parses date/time strings from user input into LocalDateTime objects.
 * Accepts several common formats (see DATE_TIME_FORMATS and
 * DATE_ONLY_FORMATS). If the matched format has no time component,
 * the time defaults to midnight.
 */
public final class DateTimeParser {

  private static final LocalTime DEFAULT_TIME = LocalTime.MIDNIGHT;

  // Formats that include a time component (HHmm, 24-hour)
  private static final List<DateTimeFormatter> DATE_TIME_FORMATS = List.of(
    DateTimeFormatter.ofPattern("d-M-yyyy HHmm"),
    DateTimeFormatter.ofPattern("yyyy-M-d HHmm")
  );

  // Date-only formats; time will default to DEFAULT_TIME
  private static final List<DateTimeFormatter> DATE_ONLY_FORMATS = List.of(
    DateTimeFormatter.ofPattern("d-M-yyyy"),
    DateTimeFormatter.ofPattern("yyyy-M-d")
  );

  private DateTimeParser() {}

  /**
   * Parses a user-provided date/time string against each accepted format
   * in turn, returning the first successful match.
   * @throws LittleRException if the string matches none of the accepted formats
   */
  public static LocalDateTime parse(String input) throws LittleRException {
    String trimmed = input.trim();

    for (DateTimeFormatter format : DATE_TIME_FORMATS) {
      try {
        return LocalDateTime.parse(trimmed, format);
      } catch (DateTimeParseException ignored) {
        // try next format
      }
    }

    for (DateTimeFormatter format : DATE_ONLY_FORMATS) {
      try {
        LocalDate date = LocalDate.parse(trimmed, format);
        return date.atTime(DEFAULT_TIME);
      } catch (DateTimeParseException ignored) {
        // try next format
      }
    }

    throw new LittleRException(
      "Invalid date format: '" + input + "'. Accepted formats: d/M/yyyy, "
      + "d-M-yyyy, or yyyy-M-d, each optionally followed by a time as HHmm "
      + "(e.g. 2/12/2019 1800). Time defaults to 0000 if omitted.");
  }
}