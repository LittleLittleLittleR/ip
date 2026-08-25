package datetime;

import java.time.format.DateTimeFormatter;

/**
 * Shared date/time formatting constants for task display and storage.
 */
public final class DateFormats {
  public static final DateTimeFormatter DISPLAY_FORMAT =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

  private DateFormats() {}
}