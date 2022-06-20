package managers;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

import static managers.HttpTaskServer.getGson;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class HTTPTaskManagerTest extends TaskManagerTest<HTTPTaskManager>{
    Gson gson = getGson();
    HttpTaskServer httpTaskServer;
    KVServer kvServer;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @BeforeEach
    @Override
    void init() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        HttpTaskServer httpTaskServer = new HttpTaskServer(8082);
        httpTaskServer.start();
        taskManager = (HTTPTaskManager) Managers.getDefault();
        super.init();
    }

    @AfterEach
    void stop() {
        if (httpTaskServer != null) {
            httpTaskServer.stop();
        }
        if (kvServer != null) {
            kvServer.stop();
        }
    }

    @Test
    void shouldLoadHistoryFromServer() {
        HTTPTaskManager h = new HTTPTaskManager("http://localhost:8083");
        h.load();
        assertEquals(taskManager.getHistory(), h.getHistory(), "История не совпадает");
        assertEquals(taskManager.getEpics(), h.getEpics(), "История не совпадает");
        assertEquals(taskManager.getTasks(), h.getTasks(), "История не совпадает");
    }

    @Test
    void shouldSaveAndLoadTasksViaHTTP() {
        HTTPTaskManager h = new HTTPTaskManager("http://localhost:8083");
        h.load();
        assertEquals(task, h.getByID(1));
        assertEquals(epic, h.getByID(2));
        assertEquals(subtask, h.getByID(3));
    }

    @Test
    public void shouldSaveEmptyTasksAndEmptyHistoryAndLoadEmpty() {
        taskManager.deleteTasks();
        taskManager.deleteEpics();
        HTTPTaskManager h = new HTTPTaskManager("http://localhost:8083");
        h.load();
        assertNull(h.getTask(0));
        assertEquals(new ArrayList<>(), h.getHistory());
    }

    private HttpResponse<String> testGetRequest(String path) {
        URI url = URI.create("http://localhost:8082/" + path);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = null;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    @Test
    public void shouldGetTask() {
        String json = gson.toJson(task);
        assertEquals(json, testGetRequest("/tasks/task/?id=1").body());
    }
}