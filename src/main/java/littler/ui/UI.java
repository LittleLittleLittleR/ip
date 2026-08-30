package littler.ui;

import java.util.ArrayList;
import java.util.Scanner;

import littler.datetime.StringDateTimeConverter.ParsedDateTime;
import littler.task.Task;

/**
 * Handles all user interface interactions for the application, including formatting
 * and printing output messages, task lists, banners, and reading user commands.
 */
public class UI {
    private final Scanner scanner;

    /**
     * Constructs a new UI instance and initializes the underlying standard input scanner.
     */
    public UI() {
        this.scanner = new Scanner(System.in);
    }

    // TASK PRINTING METHODS

    /**
     * Displays all tasks currently stored in the task list along with their 1-based index numbers.
     * @param tasks the list of tasks to be printed
     */
    public void taskList(ArrayList<Task> tasks) {
        System.out.println("Tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
    }

    /**
     * Displays a confirmation message indicating that a task has been marked.
     * @param task the task that was marked
     */
    public void taskMarked(Task task) {
        System.out.println("Marked: " + task);
    }

    /**
     * Displays a confirmation message indicating that a task has been unmarked.
     * @param task the task that was unmarked
     */
    public void taskUnmarked(Task task) {
        System.out.println("Unmarked: " + task);
    }

    /**
     * Displays a confirmation message indicating that a task was deleted, along with the updated task count.
     * @param task the task that was deleted
     * @param remaining the number of remaining tasks in the list
     */
    public void taskDeleted(Task task, int remaining) {
        System.out.println("Deleted: " + task);
        System.out.println("Now you have " + remaining + " tasks in the list.");
    }

    /**
     * Displays a confirmation message indicating that a task was added, along with the total task count.
     * @param task the task that was added
     * @param total the total number of tasks in the list
     */
    public void taskAdded(Task task, int total) {
        System.out.println("Added: " + task + "\nNow you have " + total + " tasks in the list.");
    }

    /**
     * Displays a header message preceding a list of tasks scheduled for a specific date.
     * @param date the target parsed date
     */
    public void tasksOnDateHeader(ParsedDateTime date) {
        System.out.println("Tasks on " + date + ":");
    }

    /**
     * Displays a header message preceding a list of tasks that match a search keyword.
     */
    public void findResultsHeader() {
        System.out.println("Here are the matching tasks in your list:");
    }

    /**
     * Displays a single task formatted with its specific index number.
     * @param index the 1-based index of the task
     * @param task the task to be displayed
     */
    public void taskWithIndex(int index, Task task) {
        System.out.println(index + ". " + task);
    }

    /**
     * Displays a notification message when no tasks match a given date filter.
     */
    public void noTasksFound() {
        System.out.println("No tasks found on this date.");
    }

    /**
     * Displays a notification message when no tasks match a given search keyword.
     */
    public void noMatchingTasksFound() {
        System.out.println("No matching tasks found.");
    }

    // ERROR PRINTING METHODS

    /**
     * Prints an error message when an invalid command is entered and displays available help options.
     */
    public void commandNotFoundError() {
        System.out.println("Invalid command. Please use one of the commands shown below.");
        help();
    }

    /**
     * Prints a formatted general error message.
     * @param message the error message details to be displayed
     */
    public void error(String message) {
        System.out.println("Error: " + message);
    }

    // GENERAL PRINTING METHODS

    /**
     * Prints the help menu containing all available commands and accepted date/time formats.
     */
    public void help() {
        String helpMessage = "Available commands:\n"
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

        System.out.println(helpMessage);
    }

    /**
     * Prompts the user for input and captures their response.
     * @return the user input string read from the console
     */
    public String readInput() {
        System.out.print(">> ");
        return scanner.nextLine();
    }

    /**
     * Prints a visual divider line to separate distinct visual output sections.
     */
    public void lineBreak() {
        String lineBreak = "____________________________________________________________\n";
        System.out.println(lineBreak);
    }

    /**
     * Prints the ASCII art application header banner.
     */
    public void banner() {
        String banner =
            "     ___                   ___           ___           ___       ___           ___     \n"
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
        System.out.println(banner);
    }

    /**
     * Prints the greeting welcome message.
     */
    public void welcome() {
        String welcomeMessage = "Hello! I'm LittleR \nWhat can I do for you?\n";
        System.out.println(welcomeMessage);
    }

    /**
     * Prints the exit farewell message.
     */
    public void goodbye() {
        String goodbyeMessage = "Bye. Hope to see you again soon!\n";
        System.out.println(goodbyeMessage);
    }

    /**
     * Closes the standard input scanner. Call once upon program termination.
     */
    public void closeScanner() {
        scanner.close();
    }
}
