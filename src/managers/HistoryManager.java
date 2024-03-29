package managers;

import tasks.Task;
import java.util.List;

public interface HistoryManager {
    /**
     * Добавление задачи в историю
     */
    void add(Task task);

    /**
     * Получение истории задач
     */
    List<Task> getHistory();

    /**
     * Удаление задачи из истории
     */
    void remove(int id);

}
