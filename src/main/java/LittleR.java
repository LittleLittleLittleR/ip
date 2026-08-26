import java.util.Scanner;

import datetime.StringDateTimeConverter;
import datetime.StringDateTimeConverter.ParsedDateTime;
import exception.LittleRException;

import java.time.LocalDate;
import java.util.ArrayList;
import task.*;

public class LittleR {
  
  // Variables
  private static final String SAVE_FILE_PATH = "./data/littler.txt";
  private static ArrayList<Task> list = new ArrayList<>();
  private static Storage storage = new Storage(SAVE_FILE_PATH);
  private static final UI ui = new UI();
  
  public static void main(String[] args) {
    list = storage.load();
    
    // Start of the program
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
  * Handle the conversation loop
  */
  private static void converse() {
    Scanner scanner = new Scanner(System.in);
    
    while (true) {
      String input = ui.readInput();
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
            storage.save(list);
            scanner.close();
            return;
 
          // List tasks
          case LIST:
            ui.taskList(list);
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
          
          // Query tasks on a specific date
          case ON:
            printTasksOnDate(parseDate(input, command));
            break;
        }
      } catch (LittleRException e) {
        System.out.println("Error: " + e.getMessage());
      }
      storage.save(list);
      ui.lineBreak();
    }
  }

  /**
   * Parse the date argument from the user input
   * @param input the user input containing the date
   * @param command the command keyword to be removed from the input
   * @throws LittleRException if the date string doesn't match any accepted format
   * @return the parsed date as a LocalDate
   */
  private static ParsedDateTime parseDate(String input, Command command) throws LittleRException {
    String dateString = input.substring(command.getKeyword().length()).trim();
    return StringDateTimeConverter.parse(dateString);
  }

  /**
   * Print all tasks that are due or occurring on the given date.
   * @param dateInput the raw date string from user input
   * @throws LittleRException if the date can't be parsed
   */
  private static void printTasksOnDate(ParsedDateTime dateInput) throws LittleRException {
    ui.tasksOnDateHeader(dateInput);
    int count = 0;
    for (Task task : list) {
      if (task instanceof Schedulable schedulable && schedulable.occursOn(dateInput)) {
        count++;
        ui.taskWithIndex(count, task);
      }
    }
    if (count == 0) {
      ui.noTasksFound();
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
    ui.taskMarked(list.get(index));
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
    ui.taskUnmarked(list.get(index));
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
    ui.taskDeleted(removedTask, list.size());
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
        list.add(new Deadline(parts[0].trim(), StringDateTimeConverter.parse(parts[1])));
        break;
      case EVENT:
        // Parse event details and create Event task
        String[] eventParts = taskText.split("/from|/to");
        if (eventParts.length < 3) {
          throw new LittleRException("Invalid event format. \nUse: event <task description> /from <start datetime> /to <end datetime>");
        }
        list.add(new Event(eventParts[0].trim(), StringDateTimeConverter.parse(eventParts[1]), StringDateTimeConverter.parse(eventParts[2])));
        break;
      case TODO:
        if (taskText.isEmpty()) {
          throw new LittleRException("The description of a todo cannot be empty.");
        }
        list.add(new Todo(taskText));
        break;
    }
    ui.taskAdded(list.get(list.size() - 1), list.size());
  }

  
}
