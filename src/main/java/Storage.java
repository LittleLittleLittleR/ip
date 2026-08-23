import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import task.*;

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
   * @return an ArrayList of tasks loaded from the file, or an empty list if the file doesn't exist
   */
  public ArrayList<Task> load() {
    ArrayList<Task> tasks = new ArrayList<>();
    if (!Files.exists(filePath)) {
      // Returns an empty list if the save file doesn't exist yet. 
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
          // Skips any corrupted lines instead of failing the whole load.
          System.out.println("Skipping corrupted line in save file: " + line);
        }
      }
    } catch (IOException e) {
      // File exists but couldn't be read.
      System.out.println("Warning: could not read save file. Starting with an empty list."); 
    }
    return tasks;
  }

  /**
   * Writes the given task list to disk.
   * Creates the parent folder first if it doesn't exist.
   * @param tasks the list of tasks to be saved
   */
  public void save(ArrayList<Task> tasks) {
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
      System.out.println("Warning: could not save tasks to disk.");
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
          task = new Deadline(name, parts[3]);
          break;
        case "E":
          task = new Event(name, parts[3], parts[4]);
          break;
        default:
          throw new LittleRException("Unknown task type: " + type);
      }
    } catch (ArrayIndexOutOfBoundsException e) {
      throw new LittleRException("Missing fields in line: " + line);
    }

    if (marked) {
      task.mark();
    }
    return task;
  }
}