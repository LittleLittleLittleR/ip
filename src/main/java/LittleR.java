import java.util.Scanner;

public class LittleR {
  
  // Command keywords
  private static final String EXIT_COMMAND = "bye";
  private static final String LIST_COMMAND = "list";
  private static final String MARK_COMMAND = "mark";
  private static final String UNMARK_COMMAND = "unmark";
  
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
      String input = readInput(scanner);
      printLineBreak();
      
      if (input.equals(EXIT_COMMAND)) {
        scanner.close();
        break;
      } else if (input.equals(LIST_COMMAND)) {
        printList();
      } else if (input.startsWith(MARK_COMMAND)) {
        String taskName = input.substring(MARK_COMMAND.length()).trim();
        markTask(taskName);
      } else if (input.startsWith(UNMARK_COMMAND)) {
        String taskName = input.substring(UNMARK_COMMAND.length()).trim();
        unmarkTask(taskName);
      } else {
        addItem(input);
      }
      printLineBreak();
    }
  }

  /**
  * Mark a task as completed
  * @param taskName the name of the task to be marked
  */
  private static void markTask(String taskName) {
    Task task = findTaskByName(taskName);
    if (task != null) {
      task.mark();
      System.out.println("Marked: " + task);
    } else {
      System.out.println("Task not found: " + taskName);
    }
  }

  /**
  * Unmark a task as not completed
  * @param taskName the name of the task to be unmarked
  */
  private static void unmarkTask(String taskName) {
    Task task = findTaskByName(taskName);
    if (task != null) {
      task.unmark();
      System.out.println("Unmarked: " + task);
    } else {
      System.out.println("Task not found: " + taskName);
    }
  }

  /**
  * Find a task by its name
  * @param name the name of the task to be found
  * @return the Task object if found, null otherwise
  */
  private static Task findTaskByName(String name) {
    for (int i = 0; i < size; i++) {
      if (list[i].compareName(name)) {
        return list[i];
      }
    }
    return null;
  }
  
  /**
  * Add an item to the list
  * @param input the item to be added to the list
  */
  private static void addItem(String input) {
    list[size] = new Task(input);
    size++;
    System.out.println("Added: " + input);
  }
  
  /**
  * Print the list of items added by the user so far
  */
  private static void printList() {
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
