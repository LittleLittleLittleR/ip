package datetime;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import exception.LittleRException;


/**
 * Parses date/time strings from user input into LocalDateTime objects.
 */
public final class DateTimeParser {

  private static final DateTimeFormatter INPUT_FORMAT =
      DateTimeFormatter.ofPattern("d/M/yyyy HHmm");

  private DateTimeParser() {}

  /**
   * Parses a user-provided date/time string, e.g. "2/12/2019 1800".
   * @throws LittleRException if the string doesn't match the expected format
   */
  public static LocalDateTime parse(String input) throws LittleRException {
    try {
      return LocalDateTime.parse(input.trim(), INPUT_FORMAT);
    } catch (DateTimeParseException e) {
      throw new LittleRException(
          "Invalid date format: '" + input + "'. Use: d/M/yyyy HHmm (e.g. 2/12/2019 1800)");
    }
  }
}