package managers;

import com.google.gson.Gson;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

import static managers.HttpTaskServer.getGson;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HTTPTaskManagerTest extends TaskManagerTest<HTTPTaskManager>{
    Gson gson = getGson();
    KVServer kvServer;
    HttpTaskServer httpTaskServer;
    HttpClient client = HttpClient.newHttpClient();

    public void setTaskManager(TaskManager taskManager) {
        this.taskManager = (HTTPTaskManager) taskManager;
    }

    public HTTPTaskManager getTaskManager() {
        return this.taskManager;
    }

    @BeforeAll
    void beforeAll() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        setTaskManager(Managers.getDefault());
        taskManager = getTaskManager();
        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();
    }

    @BeforeEach
    @Override
    public void init() {
        try {
            taskManager = (HTTPTaskManager) Managers.getDefault();
            super.init();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    public void stop() {
        if (httpTaskServer != null) {
            httpTaskServer.stop();
        }
    }


    @Test
    void shouldLoadHistoryFromServer() {
        HTTPTaskManager h = new HTTPTaskManager("http://localhost:8081");
        h.load();
        assertEquals(taskManager.getHistory(), h.getHistory(), "История не совпадает");
        assertEquals(taskManager.getEpics(), h.getEpics(), "История не совпадает");
        assertEquals(taskManager.getTasks(), h.getTasks(), "История не совпадает");
    }

    @Test
    void shouldSaveAndLoadTasksViaHTTP() {
        HTTPTaskManager h = new HTTPTaskManager("http://localhost:8081");
        h.load();
        assertEquals(task, h.getByID(1));
        assertEquals(epic, h.getByID(2));
        assertEquals(subtask, h.getByID(3));
    }

    @Test
    public void shouldSaveEmptyTasksAndEmptyHistoryAndLoadEmpty() {
        taskManager.deleteTasks();
        taskManager.deleteEpics();
        HTTPTaskManager h = new HTTPTaskManager("http://localhost:8081");
        h.load();
        assertNull(h.getTask(0));
        assertEquals(new ArrayList<>(), h.getHistory());
    }


    @Test
    public void shouldGetTaskHttp() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8082/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String json = gson.toJson(taskManager.getTask(1));
        assertEquals(200, response.statusCode());
        assertEquals(json, response.body());
    }

    @Test
    public void shouldGetSubtaskHttp() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8082/tasks/subtask/?id=3");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String json = gson.toJson(taskManager.getSubtask(3));
        assertEquals(200, response.statusCode());
        assertEquals(json, response.body());
    }

    @Test
    public void shouldGetEpicHttp() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8082/tasks/epic/?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String json = gson.toJson(taskManager.getEpic(2));
        assertEquals(200, response.statusCode());
        assertEquals(json, response.body());
    }

    @Test
    void shouldCreateTaskHttp() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8082/tasks/task/");
        String json = gson.toJson(taskManager.getTask(1));
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
    }

    @Test
    void shouldDeleteTaskHttp() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8082/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(205, response.statusCode());
    }
}