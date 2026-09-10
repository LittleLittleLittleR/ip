package littler.task;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

import littler.datetime.StringDateTimeConverter.ParsedDateTime;
import littler.exception.LittleRException;

/**
 * Manages an encapsulated list of tasks and handles internal task operations,
 * including bounds checking and querying tasks by date.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Constructs an empty TaskList.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Constructs a TaskList initialized with an existing list of tasks.
     *
     * @param tasks the initial list of tasks to manage
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Replaces the task at the specified 0-based index with a new task instance.
     *
     * @param index the index of the task to replace
     * @param updatedTask the new task to put at that index
     * @return the updated task
     * @throws LittleRException if the index is out of bounds
     */
    public Task update(int index, Task updatedTask) throws LittleRException {
        checkIndex(index);
        tasks.set(index, updatedTask);
        return updatedTask;
    }

    /**
     * Adds a task to the task list.
     *
     * @param task the task to be added
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Removes all tasks from the list.
     */
    public void clear() {
        tasks.clear();
    }

    /**
     * Removes and returns the task at the specified 0-based index.
     *
     * @param index the index of the task to be removed
     * @return the removed task
     * @throws LittleRException if the index is out of bounds
     */
    public Task delete(int index) throws LittleRException {
        checkIndex(index);
        assert index >= 0 && index < tasks.size() : "checkIndex should have already validated this index";
        return tasks.remove(index);
    }

    /**
     * Marks the task at the specified 0-based index as completed.
     *
     * @param index the index of the task to be marked
     * @return the marked task
     * @throws LittleRException if the index is out of bounds
     */
    public Task mark(int index) throws LittleRException {
        checkIndex(index);
        assert index >= 0 && index < tasks.size() : "checkIndex should have already validated this index";
        Task task = tasks.get(index);
        task.mark();
        return task;
    }

    /**
     * Unmarks the task at the specified 0-based index, setting it to incomplete.
     *
     * @param index the index of the task to be unmarked
     * @return the unmarked task
     * @throws LittleRException if the index is out of bounds
     */
    public Task unmark(int index) throws LittleRException {
        checkIndex(index);
        assert index >= 0 && index < tasks.size() : "checkIndex should have already validated this index";
        Task task = tasks.get(index);
        task.unmark();
        return task;
    }

    /**
     * Retrieves the task at the specified 0-based index.
     *
     * @param index the index of the task to retrieve
     * @return the task at the specified index
     * @throws LittleRException if the index is out of bounds
     */
    public Task get(int index) throws LittleRException {
        checkIndex(index);
        assert index >= 0 && index < tasks.size() : "checkIndex should have already validated this index";
        return tasks.get(index);
    }

    /**
     * Returns the most recently added task in the list.
     *
     * @return the last task in the task list
     */
    public Task getLast() {
        assert !tasks.isEmpty() : "getLast() should not be called on an empty task list";
        return tasks.get(tasks.size() - 1);
    }

    /**
     * Returns the total number of tasks currently in the list.
     *
     * @return the number of tasks
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Checks whether the task list is completely empty.
     *
     * @return true if there are no tasks in the list; false otherwise
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Filters and returns all schedulable tasks that occur on or match the given date.
     *
     * @param date the parsed date to check against task occurrences
     * @return an ArrayList containing all matching Schedulable tasks
     */
    public ArrayList<Task> getTasksOn(ParsedDateTime date) {
        return tasks.stream()
            .filter(task -> task instanceof Schedulable schedulable && schedulable.isOccurringOn(date))
            .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Returns all tasks whose description contains the given keyword,
     * matched case-insensitively as a substring (partial matches included).
     *
     * @param keyword the search term to match against task descriptions
     * @return a list of tasks whose description contains the keyword
     */
    public ArrayList<Task> findByKeyword(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return tasks.stream()
            .filter(task -> task.getName().toLowerCase().contains(lowerKeyword))
            .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Returns the underlying list of tasks for storage or UI display operations.
     *
     * @return the internal ArrayList of tasks
     */
    public ArrayList<Task> getTasks() {
        return tasks;
    }

    /**
     * Validates whether the given index falls within the valid bounds of the task list.
     *
     * @param index the 0-based index to validate
     * @throws LittleRException if the index is negative or greater than or equal to the list size
     */
    private void checkIndex(int index) throws LittleRException {
        if (index < 0 || index >= tasks.size()) {
            throw new LittleRException("That task number doesn't exist.");
        }
    }

    /**
     * Checks whether a task with the same type, description, and date(s) (if any)
     * already exists in the list.
     *
     * @param task the task to check for duplicates against
     * @return true if an equivalent task already exists; false otherwise
     */
    public boolean containsDuplicate(Task task) {
        return tasks.stream().anyMatch(existing -> existing.equals(task));
    }

    /**
     * Returns a new list of all tasks sorted alphabetically by their first (earliest-added) tag.
     * Untagged tasks are placed after all tagged tasks in ascending order (this flips, along with
     * everything else, when descending is true). The underlying task list is left unchanged.
     *
     * @param descending if true, reverses the sort order
     * @return a new sorted ArrayList of tasks
     */
    public ArrayList<Task> getSortedByTag(boolean descending) {
        Comparator<Task> comparator = TaskList::compareByTag;
        if (descending) {
            comparator = comparator.reversed();
        }
        return tasks.stream().sorted(comparator).collect(Collectors.toCollection(ArrayList::new));
    }

    private static int compareByTag(Task a, Task b) {
        Optional<String> aTag = a.getFirstTag();
        Optional<String> bTag = b.getFirstTag();
        if (aTag.isPresent() != bTag.isPresent()) {
            return aTag.isPresent() ? -1 : 1;
        }
        if (aTag.isEmpty()) {
            return 0;
        }
        return aTag.get().compareToIgnoreCase(bTag.get());
    }

    /**
     * Returns a new list of all tasks sorted by their priority levels.
     * Tasks without a priority are considered lowest and are placed after all prioritized tasks.
     * The underlying task list is left unchanged.
     *
     * @param descending if true, sorts from highest to lowest priority; if false, lowest to highest
     * @return a new sorted ArrayList of tasks
     */
    public ArrayList<Task> getSortedByPriority(boolean descending) {
        Comparator<Task> comparator = Comparator.comparing(
            task -> task.getPriority() == null ? Integer.MAX_VALUE : task.getPriority().ordinal());
        if (descending) {
            comparator = comparator.reversed();
        }
        return tasks.stream().sorted(comparator).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Returns a new list of all tasks sorted chronologically by their relevant date.
     * Tasks without a date (Todos) are placed after all dated tasks, sorted alphabetically
     * among themselves. The underlying task list is left unchanged.
     *
     * @param descending if true, sorts dated tasks latest-first; if false, earliest-first
     * @return a new sorted ArrayList of tasks
     */
    public ArrayList<Task> getSortedByDate(boolean descending) {
        Comparator<Task> comparator = TaskList::compareByDate;
        if (descending) {
            comparator = comparator.reversed();
        }
        return tasks.stream()
            .sorted(comparator)
            .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Returns a new list of all tasks sorted alphabetically by description.
     * The underlying task list is left unchanged.
     *
     * @param descending if true, sorts Z to A; if false, A to Z
     * @return a new sorted ArrayList of tasks
     */
    public ArrayList<Task> getSortedByName(boolean descending) {
        Comparator<Task> comparator = Comparator.comparing(task -> task.getName().toLowerCase());
        if (descending) {
            comparator = comparator.reversed();
        }
        return tasks.stream()
            .sorted(comparator)
            .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Compares two tasks for date-based ordering: tasks with a date (Deadline, Event) sort
     * before tasks without one (Todo). Dated tasks are compared by their date; undated tasks
     * are compared alphabetically among themselves.
     */
    private static int compareByDate(Task a, Task b) {
        boolean aHasDate = a instanceof Schedulable;
        boolean bHasDate = b instanceof Schedulable;

        if (aHasDate != bHasDate) {
            return aHasDate ? -1 : 1;
        }
        if (aHasDate) {
            ParsedDateTime aDate = ((Schedulable) a).getSortDate();
            ParsedDateTime bDate = ((Schedulable) b).getSortDate();
            return aDate.compareTo(bDate);
        }
        return a.getName().compareToIgnoreCase(b.getName());
    }
}
