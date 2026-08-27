package littler.ui;
import java.util.ArrayList;
import java.util.Scanner;

import littler.datetime.StringDateTimeConverter.ParsedDateTime;
import littler.task.Task;

public class UI {
    private final Scanner scanner;
    
    public UI() {
        this.scanner = new Scanner(System.in);
    }
    
    // TASK PRINTING METHODS
    
    public void taskList(ArrayList<Task> tasks) {
        System.out.println("Tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
    }
    
    public void taskMarked(Task task) {
        System.out.println("Marked: " + task);
    }
    
    public void taskUnmarked(Task task) {
        System.out.println("Unmarked: " + task);
    }
    
    public void taskDeleted(Task task, int remaining) {
        System.out.println("Deleted: " + task);
        System.out.println("Now you have " + remaining + " tasks in the list.");
    }
    
    public void taskAdded(Task task, int total) {
        System.out.println("Added: " + task + "\nNow you have " + total + " tasks in the list.");
    }
    
    public void tasksOnDateHeader(ParsedDateTime date) {
        System.out.println("Tasks on " + date + ":");
    }
    
    public void taskWithIndex(int index, Task task) {
        System.out.println(index + ". " + task);
    }
    
    public void noTasksFound() {
        System.out.println("No tasks found on this date.");
    }
    
    // ERROR PRINTING METHODS
    
    /**
     * Print an error message for commands not found
     */
    public void commandNotFoundError() {
        System.out.println("Invalid command. Please use one of the commands shown below.");
        help();
    }
    
    public void error(String message) {
        System.out.println("Error: " + message);
    }
    
    // GENERAL PRINTING METHODS
    
    /**
     * Print the help message with available commands
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
     * Prompt user for an input
     * @return the user input as a String
     */
    public String readInput() {
        System.out.print(">> "); 
        return scanner.nextLine();
    }
    
    /**
     * Print a line break for better readability
     */
    public void lineBreak() {
        String lineBreak = "____________________________________________________________\n";
        System.out.println(lineBreak);
    }
    
    /**
     * Print the banner at the start of the program
     */
    public void banner() {
        String banner =
        "     ___                   ___           ___           ___       ___           ___     \n"
        + "    /\\__\\      ___        /\\  \\         /\\  \\         /\\__\\     /\\  \\         /\\  \\    \n"
        + "   /:/  /     /\\  \\       \\:\\  \\        \\:\\  \\       /:/  /    /::\\  \\       /::\\  \\   \n"
        + "  /:/  /      \\:\\  \\       \\:\\  \\        \\:\\  \\     /:/  /    /:/\\:\\  \\     /:/\\:\\  \\  \n"
        + " /:/  /       /::\\__\\      /::\\  \\       /::\\  \\   /:/  /    /::\\~\\:\\  \\   /::\\~\\:\\  \\ \n"
        + "/:/__/     __/:/\\/__/     /:/\\:\\__\\     /:/\\:\\__\\ /:/__/    /:/\\:\\ \\:\\__\\ /:/\\:\\ \\:\\__\\\n"
        + "\\:\\  \\    /\\/:/  /       /:/  \\/__/    /:/  \\/__/ \\:\\  \\    \\:\\~\\:\\ \\/__/ \\/_|::\\/:/  /\n"
        + " \\:\\  \\   \\::/__/       /:/  /        /:/  /       \\:\\  \\    \\:\\ \\:\\__\\      |:|::/  / \n"
        + "  \\:\\  \\   \\:\\__\\       \\/__/         \\/__/         \\:\\  \\    \\:\\ \\/__/      |:|\\/__/  \n"
        + "   \\:\\__\\   \\/__/                                    \\:\\__\\    \\:\\__\\        |:|  |    \n"
        + "    \\/__/                                             \\/__/     \\/__/         \\|__|      \n";
        System.out.println(banner);
    }
    
    /**
     * Print the welcome message
     */
    public void welcome() {
        String welcomeMessage = "Hello! I'm LittleR \nWhat can I do for you?\n";
        System.out.println(welcomeMessage);
    }
    
    /**
     * Print the goodbye message
     */
    public void goodbye() {
        String goodbyeMessage = "Bye. Hope to see you again soon!\n";
        System.out.println(goodbyeMessage);
    }
    
    /**
     * Closes the input scanner. Call once, on program exit.
     */
    public void closeScanner() {
        scanner.close();
    }
}
