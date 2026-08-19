import java.util.Scanner;

public class LittleR {

    // Command keywords
    private static final String EXIT_COMMAND = "bye";
    private static final String LIST_COMMAND = "list";

    // Variables
    private static String[] list = new String[100];
    private static int size = 0;

    public static void main(String[] args) {

        // Start of the program
        printLineBreak();
        printBanner();
        printWelcome();
        printLineBreak();

        // Conversation loop
        converse();

        // End of the program
        printGoodbye();
        printLineBreak();
    }

    /**
     * Handle the conversation loop
     */
    private static void converse() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String input = readInput(scanner);
            printLineBreak();

            if (input.equals(EXIT_COMMAND)) {
                scanner.close();
                break;
            } else if (input.equals(LIST_COMMAND)) {
                printList();
            } else {
                addItem(input);
                printAddResponse(input);
            }
            printLineBreak();
        }
    }

    /**
     * Add an item to the list
     * @param input the item to be added to the list
     */
    private static void addItem(String input) {
        list[size] = input;
        size++;
    }

    /**
     * Print the list of items added by the user so far
     */
    private static void printList() {
        for (int i = 0; i < size; i++) {
            System.out.println((i + 1) + ". " + list[i]);
        }
    }
    
    /**
     * Print the item added to the list
     * @param input the item added to the list
     */
    private static void printAddResponse(String input) {
        System.out.println("Added: " + input);
    }

    /** Prompt user for an input
     * @param scanner Scanner object to read user input
     * @return the user input as a String
     */
    private static String readInput(Scanner scanner) {
        System.out.print(">> "); 
        return scanner.nextLine();
    }

    /**
     * Print the banner at the start of the program
     */
    private static void printBanner() {
        String banner = "  _         _      _       _        _              ___ \n"
                + " │ │       (_)    │ │_    │ │_     │ │     ___    │ _ \\\n"
                + " │ │__     │ │    │  _│   │  _│    │ │    / ─_)   │   /\n"
                + " │____│   _│_│_   _\\__│   _\\__│   _│_│_   \\___│   │_│_\\\n"
                + "_│\"\"\"\"\"│_│\"\"\"\"\"│_│\"\"\"\"\"│_│\"\"\"\"\"│_│\"\"\"\"\"│_│\"\"\"\"\"│_│\"\"\"\"\"│\n"
                + "\"`─0─0─'\"`─0─0─'\"`─0─0─'\"`─0─0─'\"`─0─0─'\"`─0─0─'\"`─0─0─'\n";
        System.out.println(banner);
    }

    /**
     * Print the welcome message
     */
    private static void printWelcome() {
        String welcomeMessage = "Hello! I'm LittleR\n"
                + "What can I do for you?\n";
        System.out.println(welcomeMessage);
    }

    /**
     * Print the goodbye message
     */
    private static void printGoodbye() {
        String goodbyeMessage = "Bye. Hope to see you again soon!\n";
        System.out.println(goodbyeMessage);
    }

    /**
     * Print a line break for better readability
     */
    private static void printLineBreak() {
        String lineBreak = "____________________________________________________________\n";
        System.out.println(lineBreak);
    }
}
