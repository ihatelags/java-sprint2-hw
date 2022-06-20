package managers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.List;

import static managers.HttpTaskServer.getGson;


public class HTTPTaskManager extends FileBackedTaskManager {
    private final KVTaskClient kvsClient;
    private final HistoryManager historyManager = Managers.getDefaultHistoryManager();
    private final Gson gson = getGson();

    public HTTPTaskManager(String url) {
        this.kvsClient = new KVTaskClient(url);
    }

    public void load() {
        List<Task> allTasks = new Gson().fromJson(kvsClient.load("task"), new TypeToken<List<Task>>() {
        }.getType());
        List<Epic> allEpics = new Gson().fromJson(kvsClient.load("epic"), new TypeToken<List<Epic>>() {
        }.getType());
        List<Subtask> allSubtasks = new Gson().fromJson(kvsClient.load("subtask"), new TypeToken<List<Subtask>>() {
        }.getType());
        List<Integer> history = new Gson().fromJson(kvsClient.load("history"), new TypeToken<List<Integer>>() {
        }.getType());

        for (Task task : allTasks) {
            tasks.put(task.getId(),task);
        }
        for (Epic epic : allEpics) {
            epics.put(epic.getId(),epic);
        }
        for (Subtask subtask : allSubtasks) {
            subtasks.put(subtask.getId(),subtask);
        }
        for (Integer id : history) {
            Task task = getByID(id);
            historyManager.add(task);
        }
    }

    @Override
    protected void save() {
        String allTasks = gson.toJson(getTasks());
        String allEpics = gson.toJson(getEpics());
        String allSubTasks = gson.toJson(getSubtasks());
        String history = gson.toJson(historyToString(historyManager));
        if (getTasks().size() > 0) {
            kvsClient.put("task", allTasks);
        }
        if (getEpics().size() > 0) {
            kvsClient.put("epic", allEpics);
        }
        if (getSubtasks().size() > 0) {
            kvsClient.put("subtask", allSubTasks);
        }
        if (historyManager.getHistory().size() > 0) {
            kvsClient.put("history", history);
        }

    }

}
