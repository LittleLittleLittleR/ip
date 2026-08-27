package littler.command;

/**
 * Represents the set of commands LittleR recognizes. 
 */
public enum Command {
    // General commands
    EXIT("bye"),
    LIST("list"),
    MARK("mark"),
    UNMARK("unmark"),
    DELETE("delete"),
    
    // Task type commands
    TODO("todo"),
    DEADLINE("deadline"),
    EVENT("event"),
    
    // Date query commands
    ON("on");
    
    private final String keyword;
    
    Command(String keyword) {
        this.keyword = keyword;
    }
    
    /**
     * Returns the keyword typed by the user to invoke this command.
     * @return the command's keyword
     */
    public String getKeyword() {
        return keyword;
    }
    
    /**
     * Returns the Command corresponding to the given user input.
     * @param input the full user input
     * @return the matching Command, or null if no command matches
     */
    public static Command fromInput(String input) {
        for (Command command : values()) {
            if (input.equals(command.keyword) || input.startsWith(command.keyword + " ")) {
                return command;
            }
        }
        return null;
    }
}