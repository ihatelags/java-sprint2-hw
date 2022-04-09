package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;

public interface TaskManager{

    /**
     * Список всех задач
     */
    ArrayList<Task> getTasks();

    ArrayList<Task> getEpics();

    ArrayList<Task> getSubtasks();

    /**
     * Удаление всех задач
     */
    void clearTasks();

    void clearEpics();

    void clearSubtasks();

    /**
     * Получение по идентификатору
     */
    Task getTask(int id);

    Task getEpic(int id);

    Task getSubtask(int id);

    void deleteByID(int id);

    /**
     * Создание. Сам объект должен передаваться в качестве параметра.
     */
    void createTask(Task task);

    void createEpic(Epic epic);

    void createSubtask(Subtask subtask);

    /**
     * Обновление. Новая версия объекта с верным идентификатором передаются в видемпараметра.
     */
    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    /**
     * Удаление по идентификатору
     */
    void deleteTask(int id);

    void deleteEpic(int id);

    void deleteSubtask(Subtask subtask);

    Task getByID(int id);
}
