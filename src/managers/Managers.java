package managers;

public class Managers {

    private final static HistoryManager historyManager = new InMemoryHistoryManager();
    private final static TaskManager taskManager = new InMemoryTaskManager();

    public static TaskManager getDefault() {
        return taskManager;
    }

    public static HistoryManager getDefaultHistoryManager() {
        return historyManager;
    }

}