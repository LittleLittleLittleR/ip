package littler.command;

import java.util.regex.Pattern;

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
     *
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
     *
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
     * Parses the search keyword argument from user input.
     *
     * @param input the user input containing the keyword
     * @param command the command keyword to be removed from the input
     * @throws LittleRException if no keyword was provided
     */
    public static String parseKeyword(String input, Command command) throws LittleRException {
        String keyword = input.substring(command.getKeyword().length()).trim();
        if (keyword.isEmpty()) {
            throw new LittleRException("Please provide a keyword to search for.");
        }
        return keyword;
    }

    /**
     * Parses the task description and optional date parameters
     * from user input and constructs a concrete {@link Task} instance.
     *
     * @param input the raw user input containing the task description and optional date parameters
     * @param type the type of task command being processed (TODO, DEADLINE, or EVENT)
     * @return the constructed concrete {@link Task} instance (Todo, Deadline, or Event)
     * @throws LittleRException if the task arguments are empty, missing required delimiters, or malformed
     */
    public static Task parseTask(String input, Command type) throws LittleRException {
        assert type == Command.DEADLINE || type == Command.EVENT || type == Command.TODO
            : "parseTask should only be called for task-creation commands";

        String taskText = input.substring(type.getKeyword().length()).strip();

        switch (type) {
            case DEADLINE:
                return parseDeadline(taskText);
            case EVENT:
                return parseEvent(taskText);
            case TODO:
                return parseTodo(taskText);
            default:
                throw new LittleRException("Unrecognized task type.");
        }
    }

    /**
     * Parses a deadline task from the provided task text.
     *
     * @param taskText the raw user input containing the task description and due date
     * @return the constructed {@link Deadline} instance
     * @throws LittleRException if the task text is malformed or missing required delimiters
     */
    private static Task parseDeadline(String taskText) throws LittleRException {
        String[] deadlineParts = taskText.split(Deadline.INPUT_DELIMITER);
        if (deadlineParts.length < 2) {
            throw new LittleRException(
                "Invalid deadline format. \nUse: deadline <task description> "
                + Deadline.INPUT_DELIMITER + " <due date>");
        }
        return new Deadline(deadlineParts[0].trim(), StringDateTimeConverter.parse(deadlineParts[1]));
    }

    /**
     * Parses an event task from the provided task text.
     *
     * @param taskText the raw user input containing the task description and start/end dates
     * @return the constructed {@link Event} instance
     * @throws LittleRException if the task text is malformed or missing required delimiters
     */
    private static Task parseEvent(String taskText) throws LittleRException {
        String[] eventParts = taskText.split(
            Pattern.quote(Event.FROM_DELIMITER) + "|" + java.util.regex.Pattern.quote(Event.TO_DELIMITER));
        if (eventParts.length < 3) {
            throw new LittleRException(
                "Invalid event format."
                + "\nUse: event <task description> "
                + Event.FROM_DELIMITER + " <start datetime> " + Event.TO_DELIMITER + " <end datetime>");
        }
        return new Event(
            eventParts[0].trim(),
            StringDateTimeConverter.parse(eventParts[1]),
            StringDateTimeConverter.parse(eventParts[2]));
    }

    /**
     * Parses a todo task from the provided task text.
     *
     * @param taskText the raw user input containing the task description
     * @return the constructed {@link Todo} instance
     * @throws LittleRException if the task text is empty
     */
    private static Task parseTodo(String taskText) throws LittleRException {
        if (taskText.isEmpty()) {
            throw new LittleRException("The description of a todo cannot be empty.");
        }
        return new Todo(taskText);
    }
}
