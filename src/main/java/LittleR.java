import java.util.ArrayList;

import datetime.StringDateTimeConverter;
import datetime.StringDateTimeConverter.ParsedDateTime;
import exception.LittleRException;

import task.*;

public class LittleR {
  
  // Variables
  private static final String SAVE_FILE_PATH = "./data/littler.txt";
  private static Storage storage = new Storage(SAVE_FILE_PATH);
  private static TaskList tasks = new TaskList(storage.load());
  private static final UI ui = new UI();
  
  public static void main(String[] args) {
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
            storage.save(tasks.getTasks());
            ui.closeScanner();
            return;
 
          // List tasks
          case LIST:
            ui.taskList(tasks.getTasks());
            break;

          case MARK:
            ui.taskMarked(tasks.mark(parseIndex(input, command)));
            break;
          case UNMARK:
            ui.taskUnmarked(tasks.unmark(parseIndex(input, command)));
            break;
  
          // Delete a task
          case DELETE:
            Task removed = tasks.delete(parseIndex(input, command));
            ui.taskDeleted(removed, tasks.size());
            break;
 
          // Add a new specified task (Todo, Deadline, or Event)
          case TODO:
          case DEADLINE:
          case EVENT:
            addItem(input, command);
            break;

          case ON:
            printTasksOnDate(parseDate(input, command));
            break;
        }
      } catch (LittleRException e) {
        ui.error(e.getMessage());
      }
      storage.save(tasks.getTasks());
      ui.lineBreak();
    }
  }

  /**
   * Parse the date argument from the user input
   * @param input the user input containing the date
   * @param command the command keyword to be removed from the input
   * @throws LittleRException if the date string doesn't match any accepted format
   * @return the parsed date as a ParsedDateTime
   */
  private static ParsedDateTime parseDate(String input, Command command) throws LittleRException {
    String dateString = input.substring(command.getKeyword().length()).trim();
    return StringDateTimeConverter.parse(dateString);
  }

  /**
   * Print all tasks that are due or occurring on the given date.
   * @param dateInput the parsed date to check tasks against
   */
  private static void printTasksOnDate(ParsedDateTime dateInput) {
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
   * Parse the index from the user input
   * @param input the user input containing the index
   * @param command the command keyword to be removed from the input
   * @throws LittleRException if the index is not a valid integer or is out of bounds
   * @return the parsed index as an integer
   */
  private static int parseIndex(String input, Command command) throws LittleRException {
    if (tasks.isEmpty()) {
      throw new LittleRException("There are no tasks yet.");
    }
    String indexString = input.substring(command.getKeyword().length()).trim();
    try {
      return Integer.parseInt(indexString) - 1;
    } catch (NumberFormatException e) {
      throw new LittleRException("Please provide a valid task number.");
    }
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
        tasks.add(new Deadline(parts[0].trim(), StringDateTimeConverter.parse(parts[1])));
        break;
      case EVENT:
        // Parse event details and create Event task
        String[] eventParts = taskText.split("/from|/to");
        if (eventParts.length < 3) {
          throw new LittleRException("Invalid event format. \nUse: event <task description> /from <start datetime> /to <end datetime>");
        }
        tasks.add(new Event(eventParts[0].trim(), StringDateTimeConverter.parse(eventParts[1]), StringDateTimeConverter.parse(eventParts[2])));
        break;
      case TODO:
        if (taskText.isEmpty()) {
          throw new LittleRException("The description of a todo cannot be empty.");
        }
        tasks.add(new Todo(taskText));
        break;
    }
    ui.taskAdded(tasks.getLast(), tasks.size());
  }
}