package littler.task;

/**
 * Represents a task's priority level, along with the input keywords used to select it.
 */
public enum PriorityLevel {
    HIGH("high", "1"),
    MEDIUM("medium", "2"),
    LOW("low", "3");

    private static final String STORAGE_PREFIX = "PRIORITY:";

    private final String keyword;
    private final String numericKeyword;

    PriorityLevel(String keyword, String numericKeyword) {
        this.keyword = keyword;
        this.numericKeyword = numericKeyword;
    }

    public String getKeyword() {
        return keyword;
    }

    /**
     * Matches a user-typed keyword (either a word like "high" or a number like "1")
     * to its corresponding PriorityLevel.
     *
     * @param input the raw text typed by the user
     * @return the matching PriorityLevel, or null if no level matches
     */
    public static PriorityLevel fromInput(String input) {
        String normalized = input.trim().toLowerCase();
        for (PriorityLevel level : values()) {
            if (level.keyword.equals(normalized) || level.numericKeyword.equals(normalized)) {
                return level;
            }
        }
        return null;
    }

    public static boolean isPriorityField(String field) {
        return field.startsWith(STORAGE_PREFIX);
    }

    public static String toStorageField(PriorityLevel level) {
        return STORAGE_PREFIX + level.name();
    }

    public static PriorityLevel fromStorageField(String field) {
        return PriorityLevel.valueOf(field.substring(STORAGE_PREFIX.length()));
    }
}
