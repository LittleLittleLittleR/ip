import java.util.ArrayList;
import datetime.StringDateTimeConverter.ParsedDateTime;
import exception.LittleRException;

import task.Task;

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
        }
      } catch (LittleRException e) {
        ui.error(e.getMessage());
      }
      storage.save(tasks.getTasks());
      ui.lineBreak();
    }
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
   * Parses for the task index
   * @param input the user input containing the index
   * @param command the command keyword to be removed from the input
   * @throws LittleRException if the index is not a valid integer or is out of bounds
   * @return the parsed index as an integer
   */
  private static int getIndex(String input, Command command) throws LittleRException {
    if (tasks.isEmpty()) {
      throw new LittleRException("There are no tasks yet.");
    }
    return Parser.parseIndex(input, command);
  }

  /**
   * Add an item to the list
   * @param input the item to be added to the list
   * @param type the type of the item to be added
   * @throws LittleRException if the item format is invalid
   */
  private static void addItem(String input, Command type) throws LittleRException {
    Task task = Parser.parseTask(input, type);
    tasks.add(task);
    ui.taskAdded(tasks.getLast(), tasks.size());
  }
}