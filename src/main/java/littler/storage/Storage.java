package littler.storage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import littler.datetime.StringDateTimeConverter;
import littler.exception.LittleRException;
import littler.task.*;

/**
 * Handles reading and writing tasks from and to a file on disk.
 */
public class Storage {
  private final Path filePath;

  public Storage(String filePath) {
    this.filePath = Paths.get(filePath);
  }

  /**
   * Loads tasks from disk.
   * @return an ArrayList of tasks loaded from the file, or an empty list if the file doesn't exist yet
   * @throws LittleRException if the file exists but couldn't be read
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
   * Writes the given task list to disk.
   * Creates the parent folder first if it doesn't exist.
   * @param tasks the list of tasks to be saved
   * @throws LittleRException if the file couldn't be written
   */
  public void save(ArrayList<Task> tasks) throws LittleRException {
    try {
      if (filePath.getParent() != null) {
        Files.createDirectories(filePath.getParent());
      }
      ArrayList<String> lines = new ArrayList<>();
      for (Task task : tasks) {
        lines.add(task.toFileString());
      }
      Files.write(filePath, lines);
    } catch (IOException e) {
      throw new LittleRException("Could not save tasks to disk: " + e.getMessage());
    }
  }

  /**
   * Reconstructs a Task from one line of the save file.
   * @throws LittleRException if the line is malformed or has an unknown type
   */
  private Task parseLine(String line) throws LittleRException {
    String[] parts = line.split(" \\| ");
    if (parts.length < 3) {
      throw new LittleRException("Malformed line: " + line);
    }

    String type = parts[0];
    boolean marked = parts[1].equals("1");
    String name = parts[2];
    Task task;

    try {
      switch (type) {
        case "T":
          task = new Todo(name);
          break;
        case "D":
          task = new Deadline(name, StringDateTimeConverter.fromStorageString(parts[3]));
          break;
        case "E":
          task = new Event(name,
            StringDateTimeConverter.fromStorageString(parts[3]),
            StringDateTimeConverter.fromStorageString(parts[4]));
          break;
        default:
          throw new LittleRException("Unknown task type: " + type);
      }
    } catch (ArrayIndexOutOfBoundsException e) {
      throw new LittleRException("Missing or invalid fields in line: " + line);
    }

    if (marked) {
      task.mark();
    }
    return task;
  }
}