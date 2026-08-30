package littler;

import java.util.ArrayList;

import littler.command.Command;
import littler.command.Parser;
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
    private final UI ui;

    /**
     * Constructs a new LittleR application instance and initializes the storage,
     * user interface, and task list from the specified file path.
     * @param filePath the file path where tasks are saved and loaded from
     */
    public LittleR(String filePath) {
        ui = new UI();
        storage = new Storage(filePath);

        ArrayList<Task> loadedTasks;
        try {
            loadedTasks = storage.load();
        } catch (LittleRException e) {
            ui.error("Could not load tasks: " + e.getMessage());
            loadedTasks = new ArrayList<>();
        }
        tasks = new TaskList(loadedTasks);
    }

    /**
     * Runs the conversation loop until the user exits.
     */
    public void run() {
        ui.lineBreak();
        ui.banner();
        ui.welcome();
        ui.lineBreak();

        // Conversation loop
        converse();

        // End of the program
        ui.goodbye();
        ui.lineBreak();
    }

    /**
     * Handles the interactive conversation loop by reading user input, parsing commands,
     * executing the corresponding actions, and persisting data changes.
     */
    private void converse() {
        while (true) {
            String input = ui.readInput().strip();
            ui.lineBreak();

            try {
                Command command = Command.fromInput(input);

                if (command == null) {
                    ui.commandNotFoundError();
                    ui.lineBreak();
                    continue;
                }

                // Exit
                switch (command) {
                    case EXIT:
                        saveQuietly();
                        ui.closeScanner();
                        return;

                    // List tasks
                    case LIST:
                        ui.taskList(tasks.getTasks());
                        break;

                    // Mark or unmark a task
                    case MARK:
                        ui.taskMarked(tasks.mark(getIndex(input, command)));
                        break;

                    case UNMARK:
                        ui.taskUnmarked(tasks.unmark(getIndex(input, command)));
                        break;

                    // Delete a task
                    case DELETE:
                        Task removed = tasks.delete(getIndex(input, command));
                        ui.taskDeleted(removed, tasks.size());
                        break;

                    // Add a new specified task (Todo, Deadline, or Event)
                    case TODO:
                    case DEADLINE:
                    case EVENT:
                        addItem(input, command);
                        break;

                    case ON:
                        printTasksOnDate(Parser.parseDate(input, command));
                        break;

                    case FIND:
                        printMatchingTasks(Parser.parseKeyword(input, command));
                        break;

                    default:
                        throw new LittleRException("Unrecognized command: " + command.getKeyword());
                }
            } catch (LittleRException e) {
                ui.error(e.getMessage());
            }
            saveQuietly();
            ui.lineBreak();
        }
    }

    /**
     * Saves the current task list to disk, catching and displaying any exceptions
     * to avoid terminating the application unexpectedly.
     */
    private void saveQuietly() {
        try {
            storage.save(tasks.getTasks());
        } catch (LittleRException e) {
            ui.error("Could not save: " + e.getMessage());
        }
    }

    /**
     * Print all tasks whose description contains the given keyword.
     * @param keyword the search term to match against task descriptions
     */
    private void printMatchingTasks(String keyword) {
        ArrayList<Task> matches = tasks.findByKeyword(keyword);
        if (matches.isEmpty()) {
            ui.noMatchingTasksFound();
            return;
        }
        ui.findResultsHeader();
        for (int i = 0; i < matches.size(); i++) {
            ui.taskWithIndex(i + 1, matches.get(i));
        }
    }

    /**
     * Prints all tasks that are due or occurring on the specified date.
     * @param dateInput the parsed date object to check tasks against
     */
    private void printTasksOnDate(ParsedDateTime dateInput) {
        ui.tasksOnDateHeader(dateInput);
        ArrayList<Task> matches = tasks.getTasksOn(dateInput);
        if (matches.isEmpty()) {
            ui.noTasksFound();
            return;
        }
        for (int i = 0; i < matches.size(); i++) {
            ui.taskWithIndex(i + 1, matches.get(i));
        }
    }

    /**
     * Extracts and validates the target task index from the user input string.
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
     * @param input the full raw user input string
     * @param type the type of task to create (TODO, DEADLINE, or EVENT)
     * @throws LittleRException if the task parameters or formatting are invalid
     */
    private void addItem(String input, Command type) throws LittleRException {
        Task task = Parser.parseTask(input, type);
        tasks.add(task);
        ui.taskAdded(tasks.getLast(), tasks.size());
    }

    /**
     * Starts the LittleR application with the default storage file path.
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        new LittleR("./data/littler.txt").run();
    }
}
