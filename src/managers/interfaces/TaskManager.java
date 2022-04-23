package managers.interfaces;

import java.util.List;
import tasks.Task;

public interface TaskManager<T extends Task>{

    /**
     * Список всех задач
     */
    List<T> getAll();

    /**
     * Удаление всех задач
     */
    void deleteTasks();

    /**
     * Получение по идентификатору
     */
    T getByID(int id);

    void deleteByID(int id);

    /**
     * Создание. Сам объект должен передаваться в качестве параметра.
     */
    void create(T task);

    /**
     * Обновление. Новая версия объекта с верным идентификатором передаются в видемпараметра.
     */
    void update(T task);


    /**
     * Удаление по идентификатору
     */
    void delete(int id);

}
