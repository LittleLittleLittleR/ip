package littler.ui;

import java.util.ArrayList;

import littler.datetime.StringDateTimeConverter.ParsedDateTime;
import littler.task.Task;

/**
 * Handles all user interface interactions for the application, including formatting
 * and printing output messages, task lists, banners, and reading user commands.
 */
public class UI {

    // TASK PRINTING METHODS

    /**
     * Displays all tasks currently stored in the task list along with their 1-based index numbers.
     * @param tasks the list of tasks to be printed
     * @return a formatted string representation of the task list
     */
    public static String taskList(ArrayList<Task> tasks) {
        String output = "Tasks in your list:\n";
        for (int i = 0; i < tasks.size(); i++) {
            output += (i + 1) + ". " + tasks.get(i) + "\n";
        }
        return output;
    }

    /**
     * Displays a confirmation message indicating that a task has been marked.
     * @param task the task that was marked
     * @return a string on the confirmation of the marked task
     */
    public static String taskMarked(Task task) {
        return "Marked: " + task;
    }

    /**
     * Displays a confirmation message indicating that a task has been unmarked.
     * @param task the task that was unmarked
     * @return a string on the confirmation of the unmarked task
     */
    public static String taskUnmarked(Task task) {
        return "Unmarked: " + task;
    }

    /**
     * Displays a confirmation message indicating that a task was deleted, along with the updated task count.
     * @param task the task that was deleted
     * @param remaining the number of remaining tasks in the list
     * @return a string on the confirmation of the deleted task and the updated task count
     */
    public static String taskDeleted(Task task, int remaining) {
        return "Deleted: " + task + "\nNow you have " + remaining + " tasks in the list.";
    }

    /**
     * Displays a confirmation message indicating that a task was added, along with the total task count.
     * @param task the task that was added
     * @param total the total number of tasks in the list
     * @return a string on the confirmation of the added task and the updated task count
     */
    public static String taskAdded(Task task, int total) {
        return "Added: " + task + "\nNow you have " + total + " tasks in the list.";
    }

    /**
     * Displays a header message preceding a list of tasks scheduled for a specific date.
     * @param date the target parsed date
     * @return a header string indicating that tasks on the specified date will be listed
     */
    public static String tasksOnDateHeader(ParsedDateTime date) {
        return "Tasks on " + date + ":";
    }

    /**
     * Displays a header message preceding a list of tasks that match a search keyword.
     * @return a header string indicating that matching tasks will be listed
     */
    public static String findResultsHeader() {
        return "Here are the matching tasks in your list:";
    }

    /**
     * Displays a single task formatted with its specific index number.
     * @param index the 1-based index of the task
     * @param task the task to be displayed
     * @return a formatted string representation of the task with its index
     */
    public static String taskWithIndex(int index, Task task) {
        return index + ". " + task;
    }

    /**
     * Displays a notification message when no tasks match a given date filter.
     * @return a string about no tasks found on the specified date
     */
    public static String noTasksFound() {
        return "No tasks found on this date.";
    }

    /**
     * Displays a notification message when no tasks match a given search keyword.
     * @return a string about no matching tasks found
     */
    public static String noMatchingTasksFound() {
        return "No matching tasks found.";
    }

    // ERROR PRINTING METHODS

    /**
     * Prints an error message when an invalid command is entered and displays available help options.
     * @return a string of an invalid command error message
     */
    public static String commandNotFoundError() {
        return "Invalid command. Please use one of the commands shown below. \n" + help();
    }

    /**
     * Prints a formatted general error message.
     * @param message the error message details to be displayed
     * @return a string of the formatted error message
     */
    public static String error(String message) {
        return "Error: " + message;
    }

    // GENERAL PRINTING METHODS

    /**
     * Prints the help menu containing all available commands and accepted date/time formats.
     * @return a string of the help menu with available commands and formats
     */
    public static String help() {
        return "Available commands:\n"
            + "1. list - List all tasks\n"
            + "2. mark <task number> - Mark a task as completed\n"
            + "3. unmark <task number> - Unmark a task as not completed\n"
            + "4. todo <task description> - Add a Todo task\n"
            + "5. deadline <task description> /by <due datetime> - Add a Deadline task\n"
            + "6. event <task description> /from <start datetime> /to <end datetime> - Add an Event task\n"
            + "7. on <date> - List tasks due or occurring on a given date\n"
            + "8. bye - Exit the program\n\n"
            + "Date is in the format of d-M-yyyy or yyyy-M-d\n"
            + "Datetime is similar to date, with an optional time in the format of HHmm\n";
    }

    /**
     * Prints the ASCII art application header banner.
     * @return a string of the ASCII art banner
     */
    public static String banner() {
        return "     ___                   ___           ___           ___       ___           ___     \n"
            + "    /\\__\\      ___        /\\  \\         /\\  \\"
            + "         /\\__\\     /\\  \\         /\\  \\    \n"
            + "   /:/  /     /\\  \\       \\:\\  \\        \\:\\"
            + "  \\       /:/  /    /::\\  \\       /::\\  \\   \n"
            + "  /:/  /      \\:\\  \\       \\:\\  \\        \\:\\"
            + "  \\     /:/  /    /:/\\:\\  \\     /:/\\:\\  \\  \n"
            + " /:/  /       /::\\__\\      /::\\  \\       /::\\  "
            + "\\   /:/  /    /::\\~\\:\\  \\   /::\\~\\:\\  \\ \n"
            + "/:/__/     __/:/\\/__/     /:/\\:\\__\\     /:/\\:\\"
            + "__\\ /:/__/    /:/\\:\\ \\:\\__\\ /:/\\:\\ \\:\\__\\\n"
            + "\\:\\  \\    /\\/:/  /       /:/  \\/__/    /:/  \\/"
            + "__/ \\:\\  \\    \\:\\~\\:\\ \\/__/ \\/_|::\\/:/  /\n"
            + " \\:\\  \\   \\::/__/       /:/  /        /:/  /    "
            + "   \\:\\  \\    \\:\\ \\:\\__\\      |:|::/  / \n"
            + "  \\:\\  \\   \\:\\__\\       \\/__/         \\/__"
            + "/         \\:\\  \\    \\:\\ \\/__/      |:|\\/__/  \n"
            + "   \\:\\__\\   \\/__/                            "
            + "        \\:\\__\\    \\:\\__\\        |:|  |    \n"
            + "    \\/__/                                        "
            + "     \\/__/     \\/__/         \\|__|      \n";
    }

    /**
     * Prints the greeting welcome message.
     * @return a string of the greeting welcome message
     */
    public static String welcome() {
        return "Hello! I'm LittleR \nWhat can I do for you?\n";
    }

    /**
     * Prints the exit farewell message.
     * @return a string of the exit farewell message
     */
    public static String goodbye() {
        return "Bye. Hope to see you again soon!\n";
    }
}
