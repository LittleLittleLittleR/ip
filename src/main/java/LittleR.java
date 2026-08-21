import java.util.Scanner;
import java.util.ArrayList;
import task.*;

public class LittleR {
  
  // Command keywords
  private static final String EXIT_COMMAND = "bye";
  private static final String LIST_COMMAND = "list";
  private static final String MARK_COMMAND = "mark";
  private static final String UNMARK_COMMAND = "unmark";
  private static final String DELETE_COMMAND = "delete";
 
  // Task type keywords
  private static final String TODO_COMMAND = "todo";
  private static final String DEADLINE_COMMAND = "deadline";
  private static final String EVENT_COMMAND = "event";
  
  // Variables
  private static ArrayList<Task> list = new ArrayList<>();
  
  public static void main(String[] args) {
    
    // Start of the program
    printLineBreak();
    printBanner();
    printWelcome();
    printLineBreak();
    
    // Conversation loop
    converse();
    
    // End of the program
    printGoodbye();
    printLineBreak();
  }
  
  /**
  * Handle the conversation loop
  */
  private static void converse() {
    Scanner scanner = new Scanner(System.in);
    
    while (true) {
      String input = readInput(scanner).strip();
      printLineBreak();
      
      try {
        // Exit
        if (input.equals(EXIT_COMMAND)) {
          scanner.close();
          break;
 
        // List tasks
        } else if (input.equals(LIST_COMMAND)) {
          printList();
 
        // Mark or unmark tasks
        } else if (isCommand(input, MARK_COMMAND)) {
          int index = parseIndex(input, MARK_COMMAND);
          markTask(index);
        } else if (isCommand(input, UNMARK_COMMAND)) {
          int index = parseIndex(input, UNMARK_COMMAND);
          unmarkTask(index);
 
        // Delete a task
        } else if (isCommand(input, DELETE_COMMAND)) {
          int index = parseIndex(input, DELETE_COMMAND);
          deleteTask(index);
 
        // Add a new specified task (Todo, Deadline, or Event)
        } else if (isCommand(input, TODO_COMMAND)) {
          addItem(input, TODO_COMMAND);
        } else if (isCommand(input, DEADLINE_COMMAND)) {
          addItem(input, DEADLINE_COMMAND);
        } else if (isCommand(input, EVENT_COMMAND)) {
          addItem(input, EVENT_COMMAND);
        } else {
          printCommandNotFoundError();
        }
      } catch (LittleRException e) {
        System.out.println("Error: " + e.getMessage());
      }
      printLineBreak();
    }
  }

  /**
   * Check if the input matches the command or starts with the command followed by a space
   * @param input the user input
   * @param command the command keyword to check against
   * @return true if the input matches the command, false otherwise
   */
  private static boolean isCommand(String input, String command) {
    return input.equals(command) || input.startsWith(command + " ");
  }

  /**
   * Mark a task as completed
   * @param index the index of the task to be marked
   * @throws LittleRException if the index is out of bounds
   */
  private static void markTask(int index) throws LittleRException {
    if (index < 0 || index >= list.size()) {
      throw new LittleRException("That task number doesn't exist.");
    }
    list.get(index).mark();
    System.out.println("Marked: " + list.get(index));
  }

  /**
   * Unmark a task as not completed
   * @param index the index of the task to be unmarked
   * @throws LittleRException if the index is out of bounds
   */
  private static void unmarkTask(int index) throws LittleRException {
    if (index < 0 || index >= list.size()) {
      throw new LittleRException("That task number doesn't exist.");
    }
    list.get(index).unmark();
    System.out.println("Unmarked: " + list.get(index));
  }

  /**
   * Parse the index from the user input
   * @param input the user input containing the index
   * @param command the command keyword to be removed from the input
   * @throws LittleRException if the index is not a valid integer or out of bounds
   * @return the parsed index as an integer
   */
  private static int parseIndex(String input, String command) throws LittleRException {
    if (list.size() == 0) {
      throw new LittleRException("There are no tasks to mark/unmark yet.");
    }
    String indexString = input.substring(command.length()).trim();
    try {
      return Integer.parseInt(indexString) - 1;
    } catch (NumberFormatException e) {
      throw new LittleRException("Please provide a valid task number.");
    }
  }

  /**
   * Delete a task from the list
   * @param index the index of the task to be deleted
   * @throws LittleRException if the index is out of bounds
   */
  private static void deleteTask(int index) throws LittleRException {
    if (index < 0 || index >= list.size()) {
      throw new LittleRException("That task number doesn't exist.");
    }
    Task removedTask = list.remove(index);
    System.out.println("Deleted: " + removedTask);
    System.out.println("Now you have " + list.size() + " tasks in the list.");
  }

  /**
   * Add an item to the list
   * @param input the item to be added to the list
   * @param type the type of the item to be added
   * @throws LittleRException if the item format is invalid
   */
  private static void addItem(String input, String type) throws LittleRException {
    String taskText = input.substring(type.length()).strip();

    switch (type) {
      case DEADLINE_COMMAND:
        // Parse deadline details and create Deadline task
        String[] parts = taskText.split("/by");
        if (parts.length < 2) {
          throw new LittleRException("Invalid deadline format. \nUse: deadline <task description> /by <due date>");
        }
        list.add(new Deadline(parts[0].trim(), parts[1].trim()));
        break;
      case EVENT_COMMAND:
        // Parse event details and create Event task
        String[] eventParts = taskText.split("/from|/to");
        if (eventParts.length < 3) {
          throw new LittleRException("Invalid event format. \nUse: event <task description> /from <start time> /to <end time>");
        }
        list.add(new Event(eventParts[0].trim(), eventParts[1].trim(), eventParts[2].trim()));
        break;
      case TODO_COMMAND:
        if (taskText.isEmpty()) {
          throw new LittleRException("The description of a todo cannot be empty.");
        }
        list.add(new Todo(taskText));
        break;
    }
    System.out.println("Added: " + list.get(list.size() - 1)
      + "\nNow you have " + list.size() + " tasks in the list.");
  }
  
  // GENERAL PRINTING METHODS

  /**
   * Print the help message with available commands
   */
  private static void printHelp() {
    String helpMessage = "Available commands:\n"
    + "1. list - List all tasks\n"
    + "2. mark <task number> - Mark a task as completed\n"
    + "3. unmark <task number> - Unmark a task as not completed\n"
    + "4. todo <task description> - Add a Todo task\n"
    + "5. deadline <task description> /by <due date> - Add a Deadline task\n"
    + "6. event <task description> /from <start time> /to <end time> - Add an Event task\n"
    + "7. bye - Exit the program";

    System.out.println(helpMessage);
  }

  /**
   * Print the list of items added by the user so far
   */
  private static void printList() {
    System.out.println("Tasks in your list:");
    for (int i = 0; i < list.size(); i++) {
      System.out.println((i + 1) + ". " + list.get(i));
    }
  }
  
  /**
   * Prompt user for an input
   * @param scanner Scanner object to read user input
   * @return the user input as a String
   */
  private static String readInput(Scanner scanner) {
    System.out.print(">> "); 
    return scanner.nextLine();
  }
  
  /**
   * Print the banner at the start of the program
   */
  private static void printBanner() {
    String banner = "  _         _      _       _        _              ___ \n"
    + " │ │       (_)    │ │_    │ │_     │ │     ___    │ _ \\\n"
    + " │ │__     │ │    │  _│   │  _│    │ │    / ─_)   │   /\n"
    + " │____│   _│_│_   _\\__│   _\\__│   _│_│_   \\___│   │_│_\\\n"
    + "_│\"\"\"\"\"│_│\"\"\"\"\"│_│\"\"\"\"\"│_│\"\"\"\"\"│_│\"\"\"\"\"│_│\"\"\"\"\"│_│\"\"\"\"\"│\n"
    + "\"`─0─0─'\"`─0─0─'\"`─0─0─'\"`─0─0─'\"`─0─0─'\"`─0─0─'\"`─0─0─'\n";
    System.out.println(banner);
  }
  
  /**
   * Print the welcome message
   */
  private static void printWelcome() {
    String welcomeMessage = "Hello! I'm LittleR\n"
    + "What can I do for you?\n";
    System.out.println(welcomeMessage);
  }
  
  /**
   * Print the goodbye message
   */
  private static void printGoodbye() {
    String goodbyeMessage = "Bye. Hope to see you again soon!\n";
    System.out.println(goodbyeMessage);
  }
  
  /**
   * Print a line break for better readability
   */
  private static void printLineBreak() {
    String lineBreak = "____________________________________________________________\n";
    System.out.println(lineBreak);
  }

  // ERROR PRINTING METHODS

  /**
   * Print an error message for commands not found
   */
  private static void printCommandNotFoundError() {
    System.out.println("Invalid command. Please use one of the commands shown below.");
    printHelp();
  }
}
