import manager.Manager;
import tasks.Task;
import tasks.Status;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();

        Task newTask = new Task("test title", "test desc", 0, Status.NEW);
        manager.createTask(newTask);
        final Task newTask = manager.getTask(newTask.getId());
        if (!Task.equals(newTask.getId())) {
            System.out.println("Ошибка задачи не находится по ид " + Task.getId());
        }
        System.out.println(manager.getTask(newTask.getId()));
        manager.deleteTask(Task.getId(newTask.getId()));
    }
}
