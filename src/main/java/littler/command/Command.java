package littler.command;

/**
* Represents the set of commands recognized by the LittleR application along with their keywords.
*/
public enum Command {
	
	// GENERAL COMMANDS
	
	/** Command to exit the application. */
	EXIT("bye"),
	/** Command to list all tasks. */
	LIST("list"),
	/** Command to mark a task as completed. */
	MARK("mark"),
	/** Command to unmark a completed task. */
	UNMARK("unmark"),
	/** Command to delete a task from the list. */
	DELETE("delete"),
	
	// TASK CREATION COMMANDS

	/** Command to add a Todo task. */
  TODO("todo"),
  /** Command to add a Deadline task. */
  DEADLINE("deadline"),
  /** Command to add an Event task. */
  EVENT("event"),
	
	// DATE QUERY COMMANDS

	/** Command to query tasks occurring on a specific date. */
  ON("on");
	
	private final String keyword;
	
	/**
   * Constructs a Command enum constant with its corresponding user input keyword.
   * @param keyword the command string keyword typed by the user
   */
	Command(String keyword) {
		this.keyword = keyword;
	}
	
	/**
   * Returns the keyword typed by the user to invoke this command.
   * @return the command's string keyword
   */
	public String getKeyword() {
		return keyword;
	}
	
	/**
   * Matches and returns the Command corresponding to the beginning of a given user input string.
   * @param input the full raw user input string
   * @return the matching Command enum, or null if no command matches
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