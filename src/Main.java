import manager.Manager;
import tasks.Task;
import tasks.Status;
import tests.Tests;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager(new HashMap<>(), new HashMap<>(), new HashMap<>());

        Task newTask = new Task("test title", "test desc", 0, Status.NEW);
        manager.createTask(newTask);
        Task newTask = manager.getTaskById(newTask.getId());
        if (!Task.equals(newTask.getId())) {
            System.out.println("Ошибка задачи не находится по ид " + Task.getId());
        }
        System.out.println(manager.getTaskById(newTask.getId()));
        manager.deleteTask(Task.getId(newTask.getId()));

    Tests.doTest;

    }
}
