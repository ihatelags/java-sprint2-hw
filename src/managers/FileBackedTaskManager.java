package managers;

import managers.interfaces.HistoryManager;
import managers.interfaces.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskTypes;

import java.io.*;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager() {
        this(new File("task.csv"), false);
    }

    public FileBackedTaskManager(File file) {
        this(file, false);
    }

    public FileBackedTaskManager(File file, boolean load) {
        this.file = file;
        if (load) {
            load();
        }
    }

    private void save() {
        try (FileWriter writer = new FileWriter(String.valueOf(getUriDb()));
             BufferedWriter bw = new BufferedWriter(writer)) {
            for (Task task : Managers.getDefault().getAllTaskMap().values()) {
                bw.write(task.toString() + "\n");
            }
            bw.newLine();
            List<Task> taskMap = Managers.getDefaultHistoryManager().getHistory();
            for (Task task : taskMap) {
                bw.write(task.getId() + ",");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Восстановление из в файла
    private void load() {

        int maxId = 0;
        try (final BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(file), UTF_8))
        ) {
            reader.readLine(); // Пропускаем заголовок
            while (true) {
                String line = reader.readLine();
                // Задачи
                final Task task = fromString(line);
                // TODO добавить задачу в менеджер
                final int id = task.getId();
                if (task.getType() == TaskTypes.TASK) {
                    tasks.put(id, task);
//				} else if () { // TODO

                }

                if (maxId < id) {
                    maxId = id;
                }
                if (line.isEmpty()) {
                    break;
                }
            }

            String line = reader.readLine();
            // История

        } catch (IOException e) {
            throw new RuntimeException(e); // TODO ManagerSaveException
        }
        // генератор
        generatedID = maxId;

    }

    public static FileBackedTaskManager loadFromFile(File file) {
        final FileBackedTaskManager manager = new FileBackedTaskManager(file, true);
        return manager;
    }
    
    public String toString(Task task) {
        if (task.getType() == TaskTypes.SUBTASK) {
            Subtask subtask = (Subtask) task;
            return subtask.getId() + "," + subtask.getType() + "," + subtask.getTitle() + "," + subtask.getStatus() +
                    "," + subtask.getDesc() + "," + subtask.getEpicId() + "\n";
        }
        return task.getId() + "," + task.getType() + "," + task.getTitle() + "," + task.getStatus() + "," +
                task.getDesc() + "\n";
    }

    private Task fromString(String value) {
        return null;
    }

    public static String toString(HistoryManager manager) {
        StringBuilder history = new StringBuilder();
        for (Task task : manager.getHistory()) {
            history.append(task.getId()).append(", ");
        }
        return history.toString();
    }

    @Override
    public List<Task> getTasks() {
        final List<Task> tasks = super.getTasks();
        save();
        return tasks;
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteByID(int id) {
        super.deleteByID(id);
        save();
    }



}
