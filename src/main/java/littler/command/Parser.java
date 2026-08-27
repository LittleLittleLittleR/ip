package littler.command;

import littler.datetime.StringDateTimeConverter;
import littler.datetime.StringDateTimeConverter.ParsedDateTime;
import littler.exception.LittleRException;
import littler.task.Deadline;
import littler.task.Event;
import littler.task.Task;
import littler.task.Todo;

/**
 * Parses raw user input into usable values.
 */
public final class Parser {
    
    private Parser() {}
    
    /**
     * Parses the task index argument from user input.
     * @param input the user input containing the index
     * @param command the command keyword to be removed from the input
     * @throws LittleRException if the index is not a valid integer
     */
    public static int parseIndex(String input, Command command) throws LittleRException {
        String indexString = input.substring(command.getKeyword().length()).trim();
        try {
            return Integer.parseInt(indexString) - 1;
        } catch (NumberFormatException e) {
            throw new LittleRException("Please provide a valid task number.");
        }
    }
    
    /**
     * Parses the date argument from user input.
     * @param input the user input containing the date
     * @param command the command keyword to be removed from the input
     * @throws LittleRException if the date string doesn't match any accepted format
     */
    public static ParsedDateTime parseDate(String input, Command command) throws LittleRException {
        String dateString = input.substring(command.getKeyword().length()).trim();
        return StringDateTimeConverter.parse(dateString);
    }
    
    /**
     * Parses a todo/deadline/event command into the corresponding Task.
     * @param input the user input containing the task details
     * @param type the type of task to be created
     * @throws LittleRException if the command's arguments are malformed
     */
    public static Task parseTask(String input, Command type) throws LittleRException {
        String taskText = input.substring(type.getKeyword().length()).strip();
        
        switch (type) {
            case DEADLINE:
            String[] parts = taskText.split("/by");
            if (parts.length < 2) {
                throw new LittleRException("Invalid deadline format. \nUse: deadline <task description> /by <due date>");
            }
            return new Deadline(parts[0].trim(), StringDateTimeConverter.parse(parts[1]));
            case EVENT:
            String[] eventParts = taskText.split("/from|/to");
            if (eventParts.length < 3) {
                throw new LittleRException("Invalid event format. \nUse: event <task description> /from <start datetime> /to <end datetime>");
            }
            return new Event(eventParts[0].trim(), StringDateTimeConverter.parse(eventParts[1]), StringDateTimeConverter.parse(eventParts[2]));
            case TODO:
            if (taskText.isEmpty()) {
                throw new LittleRException("The description of a todo cannot be empty.");
            }
            return new Todo(taskText);
            default:
            throw new LittleRException("Unrecognized task type.");
        }
    }
}