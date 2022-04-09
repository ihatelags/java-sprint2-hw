package tests;

import managers.*;
import tasks.*;

import java.util.ArrayList;


public class HistoryTests {

    public static void doHistoryTests() {

        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistoryManager();

        //создайте несколько задач разного типа
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
        Epic epic2 = new Epic("epic2 title",
                "epic2 desc",
                Status.NEW,
                new ArrayList<>());
        taskManager.createEpic(epic2);
        Subtask subtask3 = new Subtask("subtask3 title",
                "subtask3 title",
                Status.NEW,
                epic2.getId());
        taskManager.createSubtask(subtask3);

        //вызовите разные методы интерфейса TaskManager и напечатайте историю просмотров после каждого вызова.

        //наполняем историю просмотров
        System.out.println("Наполняем историю просмотров...");
        taskManager.getByID(epic1.getId());
        taskManager.getByID(epic2.getId());
        taskManager.getByID(subtask1.getId());
        taskManager.getByID(task1.getId());
        taskManager.getByID(task2.getId());

        System.out.println();
        System.out.println("История" + historyManager.getHistory());
        System.out.println();

        //меняем статус подзадачи subtask1
        Subtask updatedSubtask = new Subtask(subtask1.getTitle(),
                subtask1.getDesc(),
                subtask1.getId(),
                Status.IN_PROGRESS,
                subtask1.getEpicId());
        taskManager.updateSubtask(updatedSubtask);
        taskManager.updateEpic(epic1);

        Task updatedTask = new Task(task1.getTitle(), task1.getDesc(), task1.getId(), Status.IN_PROGRESS);
        taskManager.updateTask(updatedTask);

        //обновляем историю просмотров
        taskManager.getByID(task1.getId());
        taskManager.getByID(task1.getId());
        taskManager.getByID(task1.getId());
        taskManager.getByID(task1.getId());
        taskManager.getByID(task1.getId());

        System.out.println("Обновленная история (" + historyManager.getHistory().size() + ")\n" + historyManager.getHistory());

        //обновляем историю просмотров
        taskManager.getByID(task1.getId());
        taskManager.getByID(task1.getId());

        System.out.println();
        System.out.println("Проверяем лимит истории 10." + "\n" + historyManager.getHistory());

    }

}
