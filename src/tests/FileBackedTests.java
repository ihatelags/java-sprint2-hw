package tests;

import managers.FileBackedTasksManager;
import managers.Managers;
import managers.interfaces.Manager;
import tasks.*;

import java.util.ArrayList;
import java.io.File;

public class FileBackedTests {

    public static void doFileBackedTests() {

        Manager taskManager = Managers.getDefault();

        //создайте две задачи, эпик с тремя подзадачами и эпик без подзадач;
        System.out.println("Создаем тестовые объекты...");
        Task task1 = new Task("task1 title", "task1 desc", Status.NEW);
        taskManager.createTask(task1);
        Task task2 = new Task("task2 title", "task2 desc", Status.NEW);
        taskManager.createTask(task2);
        Epic epic1 = new Epic("epic1 title",
                "epic1 desc",
                Status.NEW,
                new ArrayList<>());
        taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("subtask1 title",
                "subtask1 desc",
                Status.NEW,
                epic1.getId());
        taskManager.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("subtask2 title",
                "subtask2 title",
                Status.NEW,
                epic1.getId());
        taskManager.createSubtask(subtask2);
        Subtask subtask3 = new Subtask("subtask3 title",
                "subtask3 title",
                Status.NEW,
                epic1.getId());
        taskManager.createSubtask(subtask3);
        Epic epic2 = new Epic("epic2 title",
                "epic2 desc",
                Status.NEW,
                new ArrayList<>());
        taskManager.createEpic(epic2);

        //запросите созданные задачи несколько раз в разном порядке
        //после каждого запроса выведите историю и убедитесь, что в ней нет повторов
        System.out.println("Наполняем и выводим историю просмотров...");
        taskManager.getByID(1);
        taskManager.getByID(2);
        taskManager.getByID(1);
        taskManager.getByID(2);
        taskManager.getByID(5);
        taskManager.getByID(3);
        taskManager.getByID(7);
        System.out.println("Выводим историю...");
        System.out.println(taskManager.getHistory());
        System.out.println();

        // Создайте новый FileBackedTasksManager менеджер из этого же файла
        // Проверьте, что история просмотра восстановилась верно и все задачи, эпики, подзадачи, которые были в старом,
        // есть в новом менеджере
        FileBackedTasksManager newFileBackedTasksManager = FileBackedTasksManager.loadFromFile(
                new File("tasks.csv"));
        System.out.println("Выводим историю файловой версии...");
        System.out.println(newFileBackedTasksManager.getByID(1));
        System.out.println(newFileBackedTasksManager.getHistory());
    }
}
