package tests;

import java.util.ArrayList;

import managers.Managers;
import managers.TaskManager;
import tasks.*;

public class Tests {

    public static void doTest() {
        TaskManager taskManager = Managers.getDefault();

        //Создайте 2 задачи, один эпик с 2 подзадачами, а другой эпик с 1 подзадачей.
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

        //Распечатайте списки эпиков, задач и подзадач, через System.out.println(..)
        System.out.println("Список эпиков: \n" + taskManager.getEpics());
        System.out.println();
        System.out.println("Список задач: \n" + taskManager.getTasks());
        System.out.println();
        System.out.println("Список подзадач: \n" + taskManager.getSubtasks());
        System.out.println();

        //Получение задач по ид
        System.out.println("Получаем задачи по их ид...");
        System.out.println(taskManager.getTask(task1.getId()));
        System.out.println(taskManager.getSubtask(subtask1.getId()));
        System.out.println(taskManager.getEpic(epic1.getId()));
        System.out.println();

        //Измените статусы созданных объектов, распечатайте. Проверьте, что статус задачи и подзадачи сохранился,
        // а статус эпика рассчитался по статусам подзадач.

        //меняем статус задачи task1
        System.out.println("Меняем статус задачи1...\n");
        Task updatedTask = new Task(task1.getTitle(), task1.getDesc(), task1.getId(), Status.IN_PROGRESS);
        taskManager.updateTask(updatedTask);

        //меняем статус подзадачи subtask1
        System.out.println("Меняем статус подзадачи1 в эпике1...\n");
        Subtask updatedSubtask = new Subtask(subtask1.getTitle(),
                subtask1.getDesc(),
                subtask1.getId(),
                Status.IN_PROGRESS,
                subtask1.getEpicId());
        taskManager.updateSubtask(updatedSubtask);
        taskManager.updateEpic(epic1);

        System.out.println("Списки задач после смены статуса:");
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getSubtasks());
        System.out.println();

        //попробуйте удалить одну из задач и один из эпиков.
        System.out.println("Удаляем одну из задач и один из эпиков...\n");
        taskManager.deleteEpic(epic2.getId());
        taskManager.deleteTask(task2.getId());
        taskManager.deleteSubtask(subtask2.getId());

        System.out.println("Списки задач после удалений:");
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getSubtasks());
        System.out.println();



        //очищение списков задач
        System.out.println("Очищаем все списки задач...\n");
        taskManager.clearTasks();
        taskManager.clearEpics();
        taskManager.clearSubtasks();

        System.out.println("Списки задач после очищения:");
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getSubtasks());
        System.out.println();



    }

}
