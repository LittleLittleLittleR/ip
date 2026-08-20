import java.util.Scanner;
import task.*;

public class LittleR {
  
  // Command keywords
  private static final String EXIT_COMMAND = "bye";
  private static final String LIST_COMMAND = "list";
  private static final String MARK_COMMAND = "mark ";
  private static final String UNMARK_COMMAND = "unmark ";

  // Task type keywords
  private static final String TODO_COMMAND = "todo ";
  private static final String DEADLINE_COMMAND = "deadline ";
  private static final String EVENT_COMMAND = "event ";
  
  // Variables
  private static Task[] list = new Task[100];
  private static int size = 0;
  
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
      
      // Exit
      if (input.equals(EXIT_COMMAND)) {
        scanner.close();
        break;

      // List tasks
      } else if (input.equals(LIST_COMMAND)) {
        printList();

      // Mark or unmark tasks
      } else if (input.startsWith(MARK_COMMAND)) {
        int index = parseIndex(input, MARK_COMMAND);
        markTask(index);
      } else if (input.startsWith(UNMARK_COMMAND)) {
        int index = parseIndex(input, UNMARK_COMMAND);
        unmarkTask(index);

      // Add a new specified task (Todo, Deadline, or Event)
      } else if (input.startsWith(TODO_COMMAND)) {
        addItem(input, TODO_COMMAND);
      } else if (input.startsWith(DEADLINE_COMMAND)) {
        addItem(input, DEADLINE_COMMAND);
      } else if (input.startsWith(EVENT_COMMAND)) {
        addItem(input, EVENT_COMMAND);
      } else {
        System.out.println("Invalid command. Please try again.");
      }
      printLineBreak();
    }
  }

  /**
  * Mark a task as completed
  * @param index the index of the task to be marked
  */
  private static void markTask(int index) {
    if (index >= 0 && index < size) {
      list[index].mark();
      System.out.println("Marked: " + list[index]);
    } else {
      System.out.println("Task not found.");
    }
  }

  /**
  * Unmark a task as not completed
  * @param index the index of the task to be unmarked
  */
  private static void unmarkTask(int index) {
    if (index >= 0 && index < size) {
      list[index].unmark();
      System.out.println("Unmarked: " + list[index]);
    } else {
      System.out.println("Task not found.");
    }
  }

  /**
  * Parse the index from the user input
  * @param input the user input containing the index
  * @param command the command keyword to be removed from the input
  * @return the parsed index as an integer
  */
  private static int parseIndex(String input, String command) {
    return Integer.parseInt(input.substring(command.length()).trim()) - 1;
  }
  
  /**
  * Add an item to the list
  * @param input the item to be added to the list
  * @param type the type of the item to be added
  */
  private static void addItem(String input, String type) {
    String taskText = input.substring(type.length()).strip();

    switch (type) {
      case DEADLINE_COMMAND:
        // Parse deadline details and create Deadline task
        String[] parts = taskText.split("/by");
        list[size] = new Deadline(parts[0].trim(), parts[1].trim());
        break;
      case EVENT_COMMAND:
        // Parse event details and create Event task
        String[] eventParts = taskText.split("/from|/to");
        list[size] = new Event(eventParts[0].trim(), eventParts[1].trim(), eventParts[2].trim());
        break;
      case TODO_COMMAND:
        list[size] = new Todo(taskText);
        break;
    }
    size++;
    System.out.println("Added: " + list[size - 1]
        + "\nNow you have " + size + " tasks in the list.");
  }
  
  /**
  * Print the list of items added by the user so far
  */
  private static void printList() {
    System.out.println("Tasks in your list:");
    for (int i = 0; i < size; i++) {
      System.out.println((i + 1) + ". " + list[i]);
    }
  }
  
  /** Prompt user for an input
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
}
