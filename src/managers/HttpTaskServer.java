package managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import exceptions.ManagerSaveException;
import tasks.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private static final int PORT = 8082;
    private final HttpServer server;
    private final Gson gson = getGson();
    TaskManager manager;

    public HttpTaskServer(TaskManager manager) {
        this.manager = manager;
        try {
            server = HttpServer.create(new InetSocketAddress(PORT), 0);
            server.createContext("/tasks", this::handle);
        } catch (IOException e) {
            throw new ManagerSaveException("Сервер не создан: " + e.getMessage());
        }
    }

    public HttpTaskServer(int altPort) {
        try {
            server = HttpServer.create();
            server.bind(new InetSocketAddress(altPort), 0);
            server.createContext("/tasks", this::handle);
        } catch (IOException e) {
            throw new ManagerSaveException("Сервер не создан: " + e.getMessage());
        }
    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        return gsonBuilder.create();
    }

    public void start() {
        server.start();
        System.out.println("Запущен сервер HTTPTaskServer на порту " + PORT);
    }

    public void stop() {
        server.stop(1);
        System.out.println("Остановлен сервер HTTPTaskServer на порту " + PORT);
    }

    public void handle(HttpExchange h) throws IOException {
        String uri = h.getRequestURI().toString();
        System.out.println("Началась обработка запроса " + uri + " от клиента.");
        String method = h.getRequestMethod();
        String[] arrUri = uri.split("/");
        String taskType = arrUri[2];
        String response = "";
        int id = 0;
        int code = 200;
        if (arrUri[arrUri.length - 1].contains("?")) {
            id = Integer.parseInt(arrUri[arrUri.length - 1].split("=")[1]);
        }

        switch (method) {
            case "GET":
                switch (taskType) {
                    case "task":
                        if (id != 0) {
                            response = gson.toJson(manager.getTask(id));
                        } else {
                            response = gson.toJson(manager.getTasks());
                        }
                        break;
                    case "epic":
                        if (id != 0) {
                            response = gson.toJson(manager.getEpic(id));
                        } else {
                            response = gson.toJson(manager.getEpics());
                        }
                        break;
                    case "subtask":
                        if (id != 0) {
                            response = gson.toJson(manager.getSubtask(id));
                        } else {
                            response = gson.toJson(manager.getSubtasks());
                        }
                        break;
                    case "history":
                        response = gson.toJson(manager.getHistory());
                    default:
                        if (arrUri.length == 5 && arrUri[2].equals("subtask") && arrUri[3].equals("epic")) {
                            response = gson.toJson(manager.getEpic(manager.getSubtask(id).getEpicId()));
                        }
                        if (response.isEmpty()) {
                            code = 400;
                        }
                }
                break;
            case "POST":
                InputStream inputStream = h.getRequestBody();
                String body = new String(inputStream.readAllBytes());
                switch (taskType) {
                    case "task":
                        Task task = gson.fromJson(body, Task.class);
                        if (id == 0) {
                            manager.createTask(task);
                        } else {
                            task.setId(id);
                            manager.updateTask(task);
                        }
                    case "epic":
                        Epic epic = gson.fromJson(body, Epic.class);
                        if (id == 0) {
                            manager.createEpic(epic);
                        } else {
                            epic.setId(id);
                            manager.updateEpic(epic);
                        }
                    case "subtask":
                        Subtask subtask = gson.fromJson(body, Subtask.class);
                        if (id == 0) {
                            manager.createSubtask(subtask);
                        } else {
                            subtask.setId(id);
                            manager.updateSubtask(subtask);
                        }
                }
                code = 201;
                break;
            case "DELETE":
                switch (taskType) {
                    case "task":
                        if (id == 0) {
                            manager.deleteTasks();
                        } else {
                            manager.deleteTask(id);
                        }
                    case "epic":
                        if (id == 0) {
                            manager.deleteEpics();
                        } else {
                            manager.deleteEpic(id);
                        }
                    case "subtask":
                        if (id == 0) {
                            manager.deleteSubtasks();
                        } else {
                            manager.deleteSubtask(id);
                        }
                }
                code = 205;
                break;
            default:
        }

        h.sendResponseHeaders(code, 0);
        try (OutputStream os = h.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
