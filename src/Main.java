import managers.*;
import tasks.*;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {
        new KVServer().start();
        TaskManager httpTaskManager = Managers.getDefault();
        HttpTaskServer httpTaskServer = new HttpTaskServer(httpTaskManager);
        httpTaskServer.start();
        createTestObjects(httpTaskManager);
//        TaskManager fileTaskManager = new FileBackedTaskManager();
//        createTestObjects(fileTaskManager);
    }

    public static void createTestObjects(TaskManager taskManager) {
        Task task = new Task("task title", "task1 desc", Status.NEW, LocalDateTime.now(), 15);
        taskManager.createTask(task);
        Task task2 = new Task("task2 title", "task2 desc", Status.NEW, LocalDateTime.now().plusMinutes(60), 15);
        taskManager.createTask(task2);
        Epic epic = new Epic("epic1 title","epic1 desc",Status.NEW,new ArrayList<>());
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask("subtask1 title","subtask1 desc",Status.NEW,
                LocalDateTime.now().plusMinutes(16), 15,epic.getId());
        taskManager.createSubtask(subtask);
        Subtask subtask2 = new Subtask("subtask2 title","subtask2 desc",Status.NEW,
                LocalDateTime.now().plusMinutes(35), 15,epic.getId());
        taskManager.createSubtask(subtask2);
        taskManager.getByID(1);
        taskManager.getByID(2);
        taskManager.getByID(1);
        taskManager.getByID(2);
        taskManager.getByID(4);
        taskManager.getByID(3);
        taskManager.updateTask(task);
    }
}
