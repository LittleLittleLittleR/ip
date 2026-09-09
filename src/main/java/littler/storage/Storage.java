package littler.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import littler.datetime.StringDateTimeConverter;
import littler.exception.LittleRException;
import littler.task.Deadline;
import littler.task.Event;
import littler.task.Task;
import littler.task.Todo;

/**
 * Handles reading tasks from disk and persisting tasks back to disk storage.
 */
public class Storage {
    private static final int TODO_BASE_FIELDS = 3;
    private static final int DEADLINE_BASE_FIELDS = 4;
    private static final int EVENT_BASE_FIELDS = 5;

    private final Path filePath;

    /**
     * Constructs a new Storage instance initialized with the target file path.
     *
     * @param filePath the string path pointing to the data storage file
     */
    public Storage(String filePath) {
        this.filePath = Paths.get(filePath);
    }

    /**
     * Loads saved tasks from disk storage into an ArrayList.
     *
     * @return an ArrayList of tasks loaded from the file, or an empty list if the file does not exist yet
     * @throws LittleRException if the target file exists but cannot be read due to an I/O error
     */
    public ArrayList<Task> load() throws LittleRException {
        ArrayList<Task> tasks = new ArrayList<>();
        if (!Files.exists(filePath)) {
            // Not an error: this is a normal first run on a new machine.
            return tasks;
        }
        try {
            for (String line : Files.readAllLines(filePath)) {
                if (line.isBlank()) {
                    continue;
                }
                try {
                    tasks.add(parseLine(line));
                } catch (LittleRException e) {
                    // Skips corrupted lines instead of failing the whole load.
                }
            }
        } catch (IOException e) {
            throw new LittleRException("Could not read save file: " + e.getMessage());
        }
        return tasks;
    }

    /**
     * Writes the provided task list to disk, creating missing parent directories if necessary.
     *
     * @param tasks the list of tasks to be saved to file storage
     * @throws LittleRException if parent directories cannot be created or file writing fails
     */
    public void save(ArrayList<Task> tasks) throws LittleRException {
        try {
            if (filePath.getParent() != null) {
                Files.createDirectories(filePath.getParent());
            }
            List<String> lines = tasks.stream()
                .map(Task::toFileString)
                .toList();
            Files.write(filePath, lines);
        } catch (IOException e) {
            throw new LittleRException("Could not save tasks to disk: " + e.getMessage());
        }
    }

    /**
     * Reconstructs a concrete Task instance from a single formatted line of the save file.
     *
     * @param line a single formatted line read from storage
     * @return the reconstructed Task object (Todo, Deadline, or Event)
     * @throws LittleRException if the line is malformed, missing required fields, or has an unknown task type
     */
    // Storage.java
    private Task parseLine(String line) throws LittleRException {
        String[] parts = line.split(" \\| ");
        if (parts.length < 3) {
            throw new LittleRException("Malformed line: " + line);
        }

        String type = parts[0];
        boolean isMarked = parts[1].equals("1");
        String name = parts[2];
        Task task;

        switch (type) {
            case Todo.TYPE_CODE:
                task = new Todo(name);
                applyStoredTags(task, parts, TODO_BASE_FIELDS);
                break;
            case Deadline.TYPE_CODE:
                if (parts.length < DEADLINE_BASE_FIELDS) {
                    throw new LittleRException("Missing due date in line: " + line);
                }
                task = new Deadline(name, StringDateTimeConverter.fromStorageString(parts[3]));
                applyStoredTags(task, parts, DEADLINE_BASE_FIELDS);
                break;
            case Event.TYPE_CODE:
                if (parts.length < EVENT_BASE_FIELDS) {
                    throw new LittleRException("Missing from/to date in line: " + line);
                }
                task = new Event(name,
                    StringDateTimeConverter.fromStorageString(parts[3]),
                    StringDateTimeConverter.fromStorageString(parts[4]));
                applyStoredTags(task, parts, EVENT_BASE_FIELDS);
                break;
            default:
                throw new LittleRException("Unknown task type: " + type);
        }

        if (isMarked) {
            task.mark();
        }
        return task;
    }

    /**
     * Applies any tags found in the trailing storage field to a reconstructed task, if present.
     * Older save files without a tags field are unaffected, since baseFieldCount reflects the
     * exact number of fields that type has without tags.
     *
     * @param task the task to apply tags to
     * @param parts the split storage line fields
     * @param baseFieldCount the number of fields that type has when untagged
     */
    private void applyStoredTags(Task task, String[] parts, int baseFieldCount) {
        if (parts.length <= baseFieldCount) {
            return;
        }
        for (String tag : parts[baseFieldCount].split(",")) {
            task.addTag(tag);
        }
    }
}
