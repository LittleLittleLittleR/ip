package littler.command;

import littler.datetime.StringDateTimeConverter;
import littler.datetime.StringDateTimeConverter.ParsedDateTime;
import littler.exception.LittleRException;
import littler.task.Deadline;
import littler.task.Event;
import littler.task.Task;
import littler.task.Todo;

/**
 * Utility class that parses raw user input strings into usable application values and tasks.
 */
public final class Parser {
    
    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private Parser() {}
    
    /**
     * Parses the task index argument from user input string and converts it to a 0-based index.
     * @param input the raw user input containing the task index argument
     * @param command the command keyword to be stripped from the front of the input
     * @return the zero-based task index integer
     * @throws LittleRException if the index argument is missing or not a valid integer
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
     * Parses a date argument from user input string into a {@link ParsedDateTime} instance.
     * @param input the raw user input containing the date string
     * @param command the command keyword to be stripped from the front of the input
     * @return the parsed date/time object
     * @throws LittleRException if the date string is empty or does not match any accepted format
     */
    public static ParsedDateTime parseDate(String input, Command command) throws LittleRException {
        String dateString = input.substring(command.getKeyword().length()).trim();
        return StringDateTimeConverter.parse(dateString);
    }
    
    /**
     * Parses task details from user input and constructs the corresponding concrete Task object.
     * @param input the raw user input containing the task description and optional date parameters
     * @param type the type of task command being processed (TODO, DEADLINE, or EVENT)
     * @return the constructed concrete {@link Task} instance (Todo, Deadline, or Event)
     * @throws LittleRException if the task arguments are empty, missing required delimiters, or malformed
     */
    public static Task parseTask(String input, Command type) throws LittleRException {
        String taskText = input.substring(type.getKeyword().length()).strip();
        
        switch (type) {
            case DEADLINE:
            String[] parts = taskText.split("/by");
            if (parts.length < 2) {
                throw new LittleRException(
                "Invalid deadline format. \nUse: deadline <task description> /by <due date>");
            }
            return new Deadline(parts[0].trim(), StringDateTimeConverter.parse(parts[1]));
            case EVENT:
            String[] eventParts = taskText.split("/from|/to");
            if (eventParts.length < 3) {
                throw new LittleRException(
                "Invalid event format. \nUse: event <task description> /from <start datetime> /to <end datetime>");
            }
            return new Event(
            eventParts[0].trim(), 
            StringDateTimeConverter.parse(eventParts[1]), 
            StringDateTimeConverter.parse(eventParts[2]));
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