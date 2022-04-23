package managers.interfaces;

import tasks.Subtask;
import tasks.Task;
import tasks.Epic;

import java.util.List;

/**
 * Менеджер задач.
 */
public interface Manager {

    List<Task> getTasks();

    void deleteTasks();

    Task getTask(int id);

    void createTask(Task task);

    void updateTask(Task task);

    void deleteTask(int id);

    Subtask getSubtask(int id);

    void createSubtask(Subtask subtask);

    void updateSubtask(Subtask subtask);

    void deleteSubtask(int id);

    List<Epic> getEpics();

    void deleteEpics();

    Epic getEpic(int id);

    void createEpic(Epic epic);

    void updateEpic(Epic epic);

    void deleteEpic(int id);

    Task getByID(int id);

    void deleteByID(int id);

    List<Task> getHistory();
}