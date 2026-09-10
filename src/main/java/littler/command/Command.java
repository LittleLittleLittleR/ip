package littler.command;

import java.util.Arrays;

/**
 * Represents the set of commands recognized by the LittleR application along with their keywords.
 */
public enum Command {

    // GENERAL COMMANDS

    /** Command to exit the application. */
    EXIT("bye"),
    /** Command to list all tasks. */
    LIST("list"),
    /** Command to view statistics about the current task list. */
    STATS("stats"),
    /** Command to view tasks sorted by a given criteria. */
    SORT("sort"),

    // TASK MODIFICATION COMMANDS

    /** Command to mark a task as completed. */
    MARK("mark"),
    /** Command to unmark a completed task. */
    UNMARK("unmark"),
    /** Command to tag an existing task. */
    TAG("tag"),
    /** Command to remove a tag from an existing task. */
    UNTAG("untag"),
    /** Command to set an existing task's priority level. */
    PRIORITY("priority"),

    /** Command to edit fields of an existing task. */
    EDIT("edit"),
    /** Command to create a copy of an existing task, with optional field overrides. */
    DUPLICATE("duplicate"),
    /** Command to delete a task from the list. */
    DELETE("delete"),
    /** Command to archive all current tasks and clear the list. */
    ARCHIVE("archive"),

    // TASK CREATION COMMANDS

    /** Command to add a Todo task. */
    TODO("todo"),
    /** Command to add a Deadline task. */
    DEADLINE("deadline"),
    /** Command to add an Event task. */
    EVENT("event"),

    // DATE QUERY COMMANDS

    /** Command to query tasks occurring on a specific date. */
    ON("on"),

    // SEARCH COMMANDS

    /** Command to search for tasks containing a specific keyword. */
    FIND("find");

    private final String keyword;

    /**
     * Constructs a Command enum constant with its corresponding user input keyword.
     *
     * @param keyword the command string keyword typed by the user
     */
    Command(String keyword) {
        this.keyword = keyword;
    }

    /**
     * Returns the keyword typed by the user to invoke this command.
     *
     * @return the command's string keyword
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * Matches and returns the Command corresponding to the beginning of a given user input string.
     *
     * @param input the full raw user input string
     * @return the matching Command enum, or null if no command matches
     */
    public static Command fromInput(String input) {
        return Arrays.stream(values())
            .filter(command -> input.equals(command.keyword) || input.startsWith(command.keyword + " "))
            .findFirst()
            .orElse(null);
    }
}
