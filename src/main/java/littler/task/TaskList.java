package littler.task;

import java.util.ArrayList;

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
     * @param tasks the initial list of tasks to manage
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }
    
    /**
     * Adds a task to the task list.
     * @param task the task to be added
     */
    public void add(Task task) {
        tasks.add(task);
    }
    
    /**
     * Removes and returns the task at the specified 0-based index.
     * @param index the index of the task to be removed
     * @return the removed task
     * @throws LittleRException if the index is out of bounds
     */
    public Task delete(int index) throws LittleRException {
        checkIndex(index);
        return tasks.remove(index);
    }
    
    /**
     * Marks the task at the specified 0-based index as completed.
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
     * Unmarks the task at the specified 0-based index, setting it to incomplete.
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
     * Retrieves the task at the specified 0-based index.
     * @param index the index of the task to retrieve
     * @return the task at the specified index
     * @throws LittleRException if the index is out of bounds
     */
    public Task get(int index) throws LittleRException {
        checkIndex(index);
        return tasks.get(index);
    }
    
    /**
     * Returns the most recently added task in the list.
     * @return the last task in the task list
     */
    public Task getLast() {
        return tasks.get(tasks.size() - 1);
    }
    
    /**
     * Returns the total number of tasks currently in the list.
     * @return the number of tasks
     */
    public int size() {
        return tasks.size();
    }
    
    /**
     * Checks whether the task list is completely empty.
     * @return true if there are no tasks in the list; false otherwise
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }
    
    /**
     * Filters and returns all schedulable tasks that occur on or match the given date.
     * @param date the parsed date to check against task occurrences
     * @return an ArrayList containing all matching Schedulable tasks
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
     * Returns the underlying list of tasks for storage or UI display operations.
     * @return the internal ArrayList of tasks
     */
    public ArrayList<Task> getTasks() {
        return tasks;
    }
    
    /**
     * Validates whether the given index falls within the valid bounds of the task list.
     * @param index the 0-based index to validate
     * @throws LittleRException if the index is negative or greater than or equal to the list size
     */
    private void checkIndex(int index) throws LittleRException {
        if (index < 0 || index >= tasks.size()) {
            throw new LittleRException("That task number doesn't exist.");
        }
    }
}