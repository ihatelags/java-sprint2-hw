import exceptions.HttpException;
import managers.*;
import tasks.*;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {
        new KVServer().start();
        TaskManager httpTaskManager = Managers.getDefault();
        HttpTaskServer httpTaskServer = new HttpTaskServer(httpTaskManager);
        httpTaskServer.start();
        createTestObjects(httpTaskManager);
        testClient("tasks/");
        testClient("tasks/task/?id=1");
        testClient("tasks/history");
        TaskManager fileTaskManager = new FileBackedTaskManager();
        createTestObjects(fileTaskManager);
    }

    public static void createTestObjects(TaskManager taskManager) {
        LocalDateTime now = LocalDateTime.now();
        Task task = new Task("task title", "task1 desc", 1,  Status.NEW, now, 15);
        taskManager.createTask(task);
        Epic epic = new Epic("epic1 title","epic1 desc", Status.NEW,new ArrayList<>());
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask("subtask1 title","subtask1 desc",3,  Status.NEW,
                now.plusMinutes(16), 15,epic.getId());
        taskManager.createSubtask(subtask);

        Task task2 = new Task("task2 title", "task2 desc", Status.NEW, LocalDateTime.now().plusMinutes(60), 15);
        taskManager.createTask(task2);
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
    private static void testClient(String path){
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8082/" + path);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200){
                throw new HttpException("Неверный код ответа: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(response.body());
    }

}
