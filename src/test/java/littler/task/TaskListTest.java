package littler.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import littler.datetime.StringDateTimeConverter.ParsedDateTime;
import littler.exception.LittleRException;

public class TaskListTest {

  // ---- add / size / isEmpty ----

  @Test
  public void isEmpty_newTaskList_returnsTrue() {
    TaskList tasks = new TaskList();
    assertTrue(tasks.isEmpty());
    assertEquals(0, tasks.size());
  }

  @Test
  public void add_singleTask_increasesSizeAndIsRetrievable() throws LittleRException {
    TaskList tasks = new TaskList();
    tasks.add(new Todo("read book"));
    assertEquals(1, tasks.size());
    assertFalse(tasks.isEmpty());
    assertEquals("read book", tasks.get(0).getName());
  }

  // ---- delete ----

  @Test
  public void delete_validIndex_removesAndReturnsTask() throws LittleRException {
    TaskList tasks = new TaskList();
    tasks.add(new Todo("read book"));
    tasks.add(new Todo("write essay"));

    Task removed = tasks.delete(0);

    assertEquals("read book", removed.getName());
    assertEquals(1, tasks.size());
    assertEquals("write essay", tasks.get(0).getName());
  }

  @Test
  public void delete_negativeIndex_throwsException() {
    TaskList tasks = new TaskList();
    tasks.add(new Todo("read book"));
    assertThrows(LittleRException.class, () -> tasks.delete(-1));
  }

  @Test
  public void delete_indexEqualToSize_throwsException() {
    TaskList tasks = new TaskList();
    tasks.add(new Todo("read book"));
    assertThrows(LittleRException.class, () -> tasks.delete(1));
  }

  // ---- mark / unmark ----

  @Test
  public void mark_validIndex_marksTaskAsDone() throws LittleRException {
    TaskList tasks = new TaskList();
    tasks.add(new Todo("read book"));

    Task marked = tasks.mark(0);

    assertTrue(marked.toString().startsWith("[T][X]"));
  }

  @Test
  public void mark_outOfBoundsIndex_throwsException() {
    TaskList tasks = new TaskList();
    assertThrows(LittleRException.class, () -> tasks.mark(0));
  }

  @Test
  public void unmark_markedTask_returnsToUnmarkedState() throws LittleRException {
    TaskList tasks = new TaskList();
    tasks.add(new Todo("read book"));
    tasks.mark(0);

    Task unmarked = tasks.unmark(0);

    assertTrue(unmarked.toString().startsWith("[T][ ]"));
  }

  @Test
  public void unmark_outOfBoundsIndex_throwsException() {
    TaskList tasks = new TaskList();
    assertThrows(LittleRException.class, () -> tasks.unmark(0));
  }

  // ---- get / getLast ----

  @Test
  public void get_outOfBoundsIndex_throwsException() {
    TaskList tasks = new TaskList();
    tasks.add(new Todo("read book"));
    assertThrows(LittleRException.class, () -> tasks.get(5));
  }

  @Test
  public void getLast_afterMultipleAdds_returnsMostRecent() {
    TaskList tasks = new TaskList();
    tasks.add(new Todo("first"));
    tasks.add(new Todo("second"));
    assertEquals("second", tasks.getLast().getName());
  }

  // ---- getTasksOn ----

  @Test
  public void getTasksOn_mixedTaskTypes_returnsOnlyMatchingSchedulableTasks() {
    TaskList tasks = new TaskList();

    ParsedDateTime targetDate = new ParsedDateTime(LocalDate.of(2026, 8, 6), null);
    ParsedDateTime otherDate = new ParsedDateTime(LocalDate.of(2026, 8, 7), null);

    tasks.add(new Todo("no date, should never match"));
    tasks.add(new Deadline("due on target", targetDate));
    tasks.add(new Deadline("due on other day", otherDate));
    tasks.add(new Event("spans target date",
        new ParsedDateTime(LocalDate.of(2026, 8, 5), null),
        new ParsedDateTime(LocalDate.of(2026, 8, 8), null)));

    ArrayList<Task> matches = tasks.getTasksOn(targetDate);

    assertEquals(2, matches.size());
  }

  @Test
  public void getTasksOn_noMatches_returnsEmptyList() {
    TaskList tasks = new TaskList();
    tasks.add(new Todo("no date"));
    ParsedDateTime date = new ParsedDateTime(LocalDate.of(2026, 8, 6), null);
    assertTrue(tasks.getTasksOn(date).isEmpty());
  }
}