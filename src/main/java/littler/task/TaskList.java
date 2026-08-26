package littler.task;
import java.util.ArrayList;

import littler.datetime.StringDateTimeConverter.ParsedDateTime;
import littler.exception.LittleRException;

/**
 * Wraps the list of tasks and owns all operations on it, including
 * bounds-checking, so callers never touch the raw list directly.
 */
public class TaskList {
  private final ArrayList<Task> tasks;

  public TaskList() {
    this.tasks = new ArrayList<>();
  }

  public TaskList(ArrayList<Task> tasks) {
    this.tasks = tasks;
  }

  /**
   * Adds a task to the list.
   * @param task the task to be added
   */
  public void add(Task task) {
    tasks.add(task);
  }

  /**
   * Removes and returns the task at the given index.
   * @param index the index of the task to be removed
   * @return the removed task
   * @throws LittleRException if the index is out of bounds
   */
  public Task delete(int index) throws LittleRException {
    checkIndex(index);
    return tasks.remove(index);
  }

  /**
   * Marks the task at the given index as completed, returning it.
   * @param index the index of the task to be marked
   * @return the marked task
   * @throws LittleRException if the index is out of bounds
   */
  public Task mark(int index) throws LittleRException {
    checkIndex(index);
    Task task = tasks.get(index);
    task.mark();
    return task;
  }

  /**
   * Unmarks the task at the given index, returning it.
   * @param index the index of the task to be unmarked
   * @return the unmarked task
   * @throws LittleRException if the index is out of bounds
   */
  public Task unmark(int index) throws LittleRException {
    checkIndex(index);
    Task task = tasks.get(index);
    task.unmark();
    return task;
  }

  /**
   * Returns the task at the given index.
   * @param index the index of the task to be returned
   * @return the task at the given index
   * @throws LittleRException if the index is out of bounds
   */
  public Task get(int index) throws LittleRException {
    checkIndex(index);
    return tasks.get(index);
  }

  /**
   * Returns the most recently added task.
   * @return the most recently added task
   */
  public Task getLast() {
    return tasks.get(tasks.size() - 1);
  }

  public int size() {
    return tasks.size();
  }

  public boolean isEmpty() {
    return tasks.isEmpty();
  }

  /**
   * Returns all Schedulable tasks that occur on the given date.
   * @param date the date to check for task occurrences
   * @return a list of tasks that occur on the given date
   */
  public ArrayList<Task> getTasksOn(ParsedDateTime date) {
    ArrayList<Task> matches = new ArrayList<>();
    for (Task task : tasks) {
      if (task instanceof Schedulable schedulable && schedulable.occursOn(date)) {
        matches.add(task);
      }
    }
    return matches;
  }

  /**
   * Returns the underlying task list for Storage to save or Ui to display.
   * @return the ArrayList of tasks
   */
  public ArrayList<Task> getTasks() {
    return tasks;
  }

  /**
   * Checks if the given index is valid for the task list.
   * @param index the index to check
   * @throws LittleRException if the index is out of bounds
   */
  private void checkIndex(int index) throws LittleRException {
    if (index < 0 || index >= tasks.size()) {
      throw new LittleRException("That task number doesn't exist.");
    }
  }
}