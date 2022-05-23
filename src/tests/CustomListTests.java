package tests;

import managers.Managers;
import managers.interfaces.Manager;
import managers.FileBackedTaskManager;
import tasks.*;

import java.util.ArrayList;
import java.io.File;

public class CustomListTests {

    public static void doCustomListTests() {

        Manager taskManager = Managers.getDefault();
        Менеджер менеджер2 = new FileBackedTasksManager(new File("task.csv"), true);

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
        System.out.println(taskManager.getHistory());
        taskManager.getByID(2);
        System.out.println(taskManager.getHistory());
        taskManager.getByID(1);
        System.out.println(taskManager.getHistory());
        taskManager.getByID(2);
        System.out.println(taskManager.getHistory());
        taskManager.getByID(5);
        System.out.println(taskManager.getHistory());
        taskManager.getByID(3);
        System.out.println(taskManager.getHistory());
        taskManager.getByID(7);
        System.out.println(taskManager.getHistory());
        System.out.println();

        //Удалить задачу из списка задач
        System.out.println("Удаляем задачу...");
        taskManager.deleteByID(task2.getId());
        System.out.println(taskManager.getHistory());
        System.out.println();

        //Удалить эпик из списка задач
        System.out.println("Удаляем эпик с подзадачами...");
        taskManager.deleteByID(epic1.getId());
        System.out.println(taskManager.getHistory());

    }
}
