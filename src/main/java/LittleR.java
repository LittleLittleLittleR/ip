public class LittleR {
    public static void main(String[] args) {
        printLineBreak();
        printBanner();
        printWelcome();
        printLineBreak();
        printGoodbye();
        printLineBreak();
    }

    public static void printBanner() {
        String banner = "  _         _      _       _        _              ___ \n"
                + " │ │       (_)    │ │_    │ │_     │ │     ___    │ _ \\\n"
                + " │ │__     │ │    │  _│   │  _│    │ │    / ─_)   │   /\n"
                + " │____│   _│_│_   _\\__│   _\\__│   _│_│_   \\___│   │_│_\\\n"
                + "_│\"\"\"\"\"│_│\"\"\"\"\"│_│\"\"\"\"\"│_│\"\"\"\"\"│_│\"\"\"\"\"│_│\"\"\"\"\"│_│\"\"\"\"\"│\n"
                + "\"`─0─0─'\"`─0─0─'\"`─0─0─'\"`─0─0─'\"`─0─0─'\"`─0─0─'\"`─0─0─'\n";
        System.out.println(banner);
    }

    public static void printWelcome() {
        String welcomeMessage = "Hello! I'm LittleR\n"
                + "What can I do for you?\n";
        System.out.println(welcomeMessage);
    }

    public static void printGoodbye() {
        String goodbyeMessage = "Bye. Hope to see you again soon!\n";
        System.out.println(goodbyeMessage);
    }

    public static void printLineBreak() {
        String lineBreak = "____________________________________________________________\n";
        System.out.println(lineBreak);
    }
}
