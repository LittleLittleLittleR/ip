package littler;

import java.util.ArrayList;

import littler.command.Command;
import littler.command.EditRequest;
import littler.command.Parser;
import littler.command.SortCriteria;
import littler.command.SortOrder;
import littler.command.SortRequest;
import littler.datetime.StringDateTimeConverter.ParsedDateTime;
import littler.exception.LittleRException;
import littler.storage.Storage;
import littler.task.Task;
import littler.task.TaskList;
import littler.ui.UI;

/**
 * Represents the main entry point for the LittleR task management application.
 * Manages the interaction loop between user input, task storage, and user interface display.
 */
public class LittleR {

    private final Storage storage;
    private final TaskList tasks;

    /**
     * Constructs a new LittleR application instance and initializes the storage,
     * user interface, and task list from the specified file path.
     *
     * @param filePath the file path where tasks are saved and loaded from
     */
    public LittleR(String filePath) {
        storage = new Storage(filePath);

        ArrayList<Task> loadedTasks;
        try {
            loadedTasks = storage.load();
        } catch (LittleRException e) {
            System.err.println(UI.error("Could not load tasks: " + e.getMessage()));
            loadedTasks = new ArrayList<>();
        }
        tasks = new TaskList(loadedTasks);
    }

    /**
     * Handles the interactive conversation loop by reading user input, parsing commands,
     * executing the corresponding actions, and persisting data changes.
     *
     * @param input the raw user input string to process
     * @return the formatted output string to display to the user
     */
    public String converse(String input) {
        StringBuilder output = new StringBuilder();

        try {
            Command command = Command.fromInput(input);

            if (command == null) {
                output.append(UI.commandNotFoundError());
                return output.toString();
            }

            // Exit
            switch (command) {
                case EXIT:
                    output.append(UI.goodbye());
                    break;

                // List tasks
                case LIST:
                    output.append(UI.taskList(tasks.getTasks()));
                    break;

                // Mark or unmark a task
                case MARK:
                    output.append(UI.taskMarked(tasks.mark(getIndex(input, command))));
                    break;

                case UNMARK:
                    output.append(UI.taskUnmarked(tasks.unmark(getIndex(input, command))));
                    break;

                case EDIT:
                    output.append(editItem(Parser.parseEdit(input, command)));
                    break;

                case DUPLICATE:
                    output.append(duplicateItem(Parser.parseDuplicate(input, command)));
                    break;

                // Delete a task
                case DELETE:
                    Task removed = tasks.delete(getIndex(input, command));
                    output.append(UI.taskDeleted(removed, tasks.size()));
                    break;

                // Add a new specified task (Todo, Deadline, or Event)
                case TODO:
                case DEADLINE:
                case EVENT:
                    output.append(addItem(input, command));
                    break;

                case ON:
                    output.append(printTasksOnDate(Parser.parseDate(input, command)));
                    break;

                case FIND:
                    output.append(printMatchingTasks(Parser.parseKeyword(input, command)));
                    break;

                case SORT:
                    output.append(printSortedTasks(Parser.parseSort(input, command)));
                    break;

                default:
                    throw new LittleRException("Unrecognized command: " + command.getKeyword());
            }
        } catch (LittleRException e) {
            output.append(UI.error(e.getMessage()));
        }
        output.append(saveQuietly());
        return output.toString();
    }

    /**
     * Saves the current task list to disk, catching and displaying any exceptions
     * to avoid terminating the application unexpectedly.
     */
    private String saveQuietly() {
        try {
            storage.save(tasks.getTasks());
            return "";
        } catch (LittleRException e) {
            return UI.error("Could not save: " + e.getMessage());
        }
    }

    /**
     * Prints all tasks sorted by the specified criteria and order.
     *
     * @param sortRequest the sort request containing the criteria and order
     * @return a formatted string of sorted tasks
     */
    private String printSortedTasks(SortRequest sortRequest) {
        boolean descending = sortRequest.getOrder() == SortOrder.DESCENDING;
        ArrayList<Task> sorted = sortRequest.getCriteria() == SortCriteria.DATE
            ? tasks.getSortedByDate(descending)
            : tasks.getSortedByName(descending);
        return UI.taskList(sorted);
    }

    /**
     * Print all tasks whose description contains the given keyword.
     *
     * @param keyword the search term to match against task descriptions
     * @return a formatted string of matching tasks or a message if none are found
     */
    private String printMatchingTasks(String keyword) {
        ArrayList<Task> matches = tasks.findByKeyword(keyword);
        if (matches.isEmpty()) {
            return UI.noMatchingTasksFound();
        }
        StringBuilder output = new StringBuilder();
        output.append(UI.findResultsHeader());
        appendTaskLines(output, matches);
        return output.toString();
    }

    /**
     * Prints all tasks that are due or occurring on the specified date.
     *
     * @param dateInput the parsed date object to check tasks against
     * @return a formatted string of tasks or a message if none are found
     */
    private String printTasksOnDate(ParsedDateTime dateInput) {
        StringBuilder output = new StringBuilder();
        output.append(UI.tasksOnDateHeader(dateInput));

        ArrayList<Task> matches = tasks.getTasksOn(dateInput);
        if (matches.isEmpty()) {
            output.append(UI.noTasksFound());
        }
        appendTaskLines(output, matches);
        return output.toString();
    }

    /**
     * Appends a formatted list of tasks to the provided StringBuilder, each with its index.
     *
     * @param output the StringBuilder to append the task lines to
     * @param taskList the list of tasks to format and append
     */
    private void appendTaskLines(StringBuilder output, ArrayList<Task> taskList) {
        for (int i = 0; i < taskList.size(); i++) {
            output.append(UI.taskWithIndex(i + 1, taskList.get(i)));
        }
    }

    /**
     * Extracts and validates the target task index from the user input string.
     *
     * @param input the user input string containing the target index
     * @param command the command keyword to strip from the input
     * @return the parsed task index
     * @throws LittleRException if the task list is empty, the index is invalid, or out of bounds
     */
    private int getIndex(String input, Command command) throws LittleRException {
        if (tasks.isEmpty()) {
            throw new LittleRException("There are no tasks yet.");
        }
        return Parser.parseIndex(input, command);
    }

    /**
     * Creates and adds a new task to the task list based on the input string and task command type.
     *
     * @param input the full raw user input string
     * @param type the type of task to create (TODO, DEADLINE, or EVENT)
     * @return a confirmation message indicating the task was added and the updated task count
     * @throws LittleRException if the task parameters or formatting are invalid
     */
    private String addItem(String input, Command type) throws LittleRException {
        Task task = Parser.parseTask(input, type);
        boolean isDuplicate = tasks.containsDuplicate(task);
        tasks.add(task);
        assert tasks.getLast() == task : "the just-added task should be the last task in the list";
        String confirmation = UI.taskAdded(tasks.getLast(), tasks.size());
        return isDuplicate ? UI.duplicateTaskWarning() + confirmation : confirmation;
    }

    /**
     * Edits an existing task in the task list based on the provided edit request.
     *
     * @param editRequest the request containing the index and updates for the task
     * @return a confirmation message indicating the task was updated
     * @throws LittleRException if the index is invalid or the updates are malformed
     */
    private String editItem(EditRequest editRequest) throws LittleRException {
        Task existing = tasks.get(editRequest.getIndex());
        Task updated = existing.withUpdates(editRequest.getUpdates());
        tasks.update(editRequest.getIndex(), updated);
        return UI.taskEdited(updated);
    }

    /**
     * Duplicates an existing task in the task list based on the provided duplicate request,
     * optionally applying field overrides to the new task.
     *
     * @param duplicateRequest the request containing the index of the task to duplicate and any field overrides
     * @return a confirmation message indicating the task was duplicated and the updated task count
     * @throws LittleRException if the index is invalid or the overrides are malformed
     */
    private String duplicateItem(EditRequest duplicateRequest) throws LittleRException {
        Task source = tasks.get(duplicateRequest.getIndex());
        Task duplicate = source.withUpdates(duplicateRequest.getUpdates());
        duplicate.unmark();
        boolean isDuplicate = tasks.containsDuplicate(duplicate);
        tasks.add(duplicate);
        String confirmation = UI.taskDuplicated(duplicate, tasks.size());
        return isDuplicate ? UI.duplicateTaskWarning() + confirmation : confirmation;
    }
}
