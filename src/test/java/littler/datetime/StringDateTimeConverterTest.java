package littler.datetime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import littler.datetime.StringDateTimeConverter.ParsedDateTime;
import littler.exception.LittleRException;

public class StringDateTimeConverterTest {

    // ---- parse: accepted formats ----

    @Test
    public void parse_dMyyyyWithTime_parsesCorrectly() throws LittleRException {
        ParsedDateTime result = StringDateTimeConverter.parse("2-12-2019 1800");
        assertTrue(result.hasTime());
        assertEquals("2019-12-02 18:00", result.toString());
    }

    @Test
    public void parse_yyyyMdWithTime_parsesCorrectly() throws LittleRException {
        ParsedDateTime result = StringDateTimeConverter.parse("2019-12-2 1800");
        assertTrue(result.hasTime());
        assertEquals("2019-12-02 18:00", result.toString());
    }

    @Test
    public void parse_dMyyyyDateOnly_hasNoTimeComponent() throws LittleRException {
        ParsedDateTime result = StringDateTimeConverter.parse("2-12-2019");
        assertFalse(result.hasTime());
        assertEquals("2019-12-02", result.toString());
    }

    @Test
    public void parse_yyyyMdDateOnly_hasNoTimeComponent() throws LittleRException {
        ParsedDateTime result = StringDateTimeConverter.parse("2019-12-2");
        assertFalse(result.hasTime());
        assertEquals("2019-12-02", result.toString());
    }

    @Test
    public void parse_inputWithSurroundingWhitespace_isTrimmedAndParsed() throws LittleRException {
        ParsedDateTime result = StringDateTimeConverter.parse("  2-12-2019  ");
        assertEquals("2019-12-02", result.toString());
    }

    // ---- parse: rejected input ----

    @Test
    public void parse_completelyInvalidString_throwsException() {
        assertThrows(LittleRException.class, () -> StringDateTimeConverter.parse("notadate"));
    }

    @Test
    public void parse_slashSeparatedDate_throwsException() {
        // Slash-separated dates were deliberately dropped in favour of dash-only formats.
        assertThrows(LittleRException.class, () -> StringDateTimeConverter.parse("2/12/2019"));
    }

    @Test
    public void parse_invalidCalendarDate_throwsException() {
        // 32nd day of the month doesn't exist in any accepted format.
        assertThrows(LittleRException.class, () -> StringDateTimeConverter.parse("32-12-2019"));
    }

    // ---- format ----

    @Test
    public void format_dateWithTime_includesTimeSuffix() {
        ParsedDateTime dateTime = new ParsedDateTime(LocalDate.of(2026, 8, 6), java.time.LocalTime.of(9, 5));
        assertEquals("2026-08-06 09:05", StringDateTimeConverter.format(dateTime));
    }

    @Test
    public void format_dateWithoutTime_omitsTimeSuffix() {
        ParsedDateTime dateTime = new ParsedDateTime(LocalDate.of(2026, 8, 6), null);
        assertEquals("2026-08-06", StringDateTimeConverter.format(dateTime));
    }

    // ---- formatDate ----

    @Test
    public void formatDate_bareLocalDate_formatsWithoutTime() {
        assertEquals("2026-08-06", StringDateTimeConverter.formatDate(LocalDate.of(2026, 8, 6)));
    }

    // ---- ParsedDateTime.compareDate ----

    @Test
    public void compareDate_earlierDate_returnsNegative() {
        ParsedDateTime earlier = new ParsedDateTime(LocalDate.of(2026, 8, 5), null);
        ParsedDateTime later = new ParsedDateTime(LocalDate.of(2026, 8, 6), null);
        assertEquals(-1, earlier.compareDate(later));
    }

    @Test
    public void compareDate_laterDate_returnsPositive() {
        ParsedDateTime earlier = new ParsedDateTime(LocalDate.of(2026, 8, 5), null);
        ParsedDateTime later = new ParsedDateTime(LocalDate.of(2026, 8, 6), null);
        assertEquals(1, later.compareDate(earlier));
    }

    @Test
    public void compareDate_sameDate_returnsZero() {
        ParsedDateTime a = new ParsedDateTime(LocalDate.of(2026, 8, 6), null);
        ParsedDateTime b = new ParsedDateTime(LocalDate.of(2026, 8, 6), null);
        assertEquals(0, a.compareDate(b));
    }

    // ---- ParsedDateTime.compareTime ----

    @Test
    public void compareTime_earlierTime_returnsNegative() {
        ParsedDateTime earlier = new ParsedDateTime(LocalDate.of(2026, 8, 6), java.time.LocalTime.of(9, 0));
        ParsedDateTime later = new ParsedDateTime(LocalDate.of(2026, 8, 6), java.time.LocalTime.of(17, 0));
        assertEquals(-1, earlier.compareTime(later));
    }

    @Test
    public void compareTime_eitherSideMissingTime_returnsZero() {
        // Documents the current "treat as equal" behaviour when a time is absent on either side.
        ParsedDateTime withTime = new ParsedDateTime(LocalDate.of(2026, 8, 6), java.time.LocalTime.of(9, 0));
        ParsedDateTime withoutTime = new ParsedDateTime(LocalDate.of(2026, 8, 6), null);
        assertEquals(0, withTime.compareTime(withoutTime));
    }
}
