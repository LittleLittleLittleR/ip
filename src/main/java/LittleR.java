import java.util.Scanner;

import datetime.DateTimeParser;
import exception.LittleRException;

import java.util.ArrayList;
import task.*;

public class LittleR {
  
  // Variables
  private static final String SAVE_FILE_PATH = "./data/littler.txt";
  private static ArrayList<Task> list = new ArrayList<>();
  private static Storage storage = new Storage(SAVE_FILE_PATH);
  
  public static void main(String[] args) {
    list = storage.load();
    
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
        Command command = Command.fromInput(input);

        if (command == null) {
          printCommandNotFoundError();
          printLineBreak();
          continue;
        }

        // Exit
        switch (command) {
          case EXIT:
            storage.save(list);
            scanner.close();
            return;
 
          // List tasks
          case LIST:
            printList();
            break;
 
          // Mark or unmark tasks
          case MARK:
            markTask(parseIndex(input, command));
            break;
          case UNMARK:
            unmarkTask(parseIndex(input, command));
            break;
  
          // Delete a task
          case DELETE:
            deleteTask(parseIndex(input, command));
            break;
 
          // Add a new specified task (Todo, Deadline, or Event)
          case TODO:
          case DEADLINE:
          case EVENT:
            addItem(input, command);
            break;
        }
      } catch (LittleRException e) {
        System.out.println("Error: " + e.getMessage());
      }
      storage.save(list);
      printLineBreak();
    }
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
  private static int parseIndex(String input, Command command) throws LittleRException {
    if (list.size() == 0) {
      throw new LittleRException("There are no tasks to mark/unmark yet.");
    }
    String indexString = input.substring(command.getKeyword().length()).trim();
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
  private static void addItem(String input, Command type) throws LittleRException {
    String taskText = input.substring(type.getKeyword().length()).strip();

    switch (type) {
      case DEADLINE:
        // Parse deadline details and create Deadline task
        String[] parts = taskText.split("/by");
        if (parts.length < 2) {
          throw new LittleRException("Invalid deadline format. \nUse: deadline <task description> /by <due date>");
        }
        list.add(new Deadline(parts[0].trim(), DateTimeParser.parse(parts[1])));
        break;
      case EVENT:
        // Parse event details and create Event task
        String[] eventParts = taskText.split("/from|/to");
        if (eventParts.length < 3) {
          throw new LittleRException("Invalid event format. \nUse: event <task description> /from <start time> /to <end time>");
        }
        list.add(new Event(eventParts[0].trim(), DateTimeParser.parse(eventParts[1]), DateTimeParser.parse(eventParts[2])));
        break;
      case TODO:
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
