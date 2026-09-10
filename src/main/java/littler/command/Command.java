package littler.command;

/**
 * Represents the set of commands recognized by the LittleR application along with their keywords.
 */
public enum Command {

    // GENERAL COMMANDS

    /** Command to exit the application. */
    EXIT("bye", "b"),
    /** Command to display the help menu. */
    HELP("help", "h"),
    /** Command to list all tasks. */
    LIST("list", "l"),
    /** Command to view statistics about the current task list. */
    STATS("stats", "st"),
    /** Command to view tasks sorted by a given criteria. */
    SORT("sort", "so"),

    // TASK MODIFICATION COMMANDS

    /** Command to mark a task as completed. */
    MARK("mark", "m"),
    /** Command to unmark a completed task. */
    UNMARK("unmark", "um"),
    /** Command to tag an existing task. */
    TAG("tag", "tg"),
    /** Command to remove a tag from an existing task. */
    UNTAG("untag", "ut"),
    /** Command to set an existing task's priority level. */
    PRIORITY("priority", "p"),

    /** Command to edit fields of an existing task. */
    EDIT("edit", "ed"),
    /** Command to create a copy of an existing task, with optional field overrides. */
    DUPLICATE("duplicate", "dp"),
    /** Command to delete a task from the list. */
    DELETE("delete", "d"),
    /** Command to undo the most recent task-list-affecting command. */
    UNDO("undo", "un"),
    /** Command to archive all current tasks and clear the list. */
    ARCHIVE("archive", "a"),

    // TASK CREATION COMMANDS

    /** Command to add a Todo task. */
    TODO("todo", "t"),
    /** Command to add a Deadline task. */
    DEADLINE("deadline", "dl"),
    /** Command to add an Event task. */
    EVENT("event", "ev"),

    // DATE QUERY COMMANDS

    /** Command to query tasks occurring on a specific date. */
    ON("on", "o"),

    // SEARCH COMMANDS

    /** Command to search for tasks containing a specific keyword. */
    FIND("find", "f"),;

    private final String keyword;
    private final String alias;

    /**
     * Constructs a Command enum constant with its corresponding user input keyword.
     *
     * @param keyword the command string keyword typed by the user
     * @param alias the command string alias typed by the user
     */
    Command(String keyword, String alias) {
        this.keyword = keyword;
        this.alias = alias;
    }

    /**
     * Returns the keyword typed by the user to invoke this command.
     *
     * @return the command's string keyword
     */
    public String getKeyword() {
        return keyword;
    }

    public String getAlias() {
        return alias;
    }

    /**
     * Matches and returns the Command corresponding to the beginning of a given user input string.
     *
     * @param input the full raw user input string
     * @return the matching Command enum, or null if no command matches
     */
    public static Command fromInput(String input) {
        for (Command command : values()) {
            if (matchesToken(input, command.keyword) || matchesToken(input, command.alias)) {
                return command;
            }
        }
        return null;
    }

    /**
     * Rewrites the leading command word in the input to its canonical (full) keyword if it
     * matches a recognized alias, leaving the rest of the input untouched. This lets all
     * downstream parsing keep stripping based on the canonical keyword's length, regardless
     * of whether the user typed the full word or its short alias.
     *
     * @param input the raw user input
     * @return the input with any leading alias expanded to its full keyword, unchanged if none matches
     */
    public static String normalizeAlias(String input) {
        for (Command command : values()) {
            if (matchesToken(input, command.alias)) {
                String remainder = input.length() > command.alias.length()
                    ? input.substring(command.alias.length())
                    : "";
                return command.keyword + remainder;
            }
        }
        return input;
    }

    private static boolean matchesToken(String input, String token) {
        return input.equals(token) || input.startsWith(token + " ");
    }
}
