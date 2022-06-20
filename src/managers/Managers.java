package managers;

public class Managers {

    public static TaskManager getDefault() {
        return new HTTPTaskManager("http://localhost:8081");
    }

    public static TaskManager getFileBackedTaskManager() {
        return new FileBackedTaskManager();
    }

    public static HistoryManager getDefaultHistoryManager() {
        return new InMemoryHistoryManager();
    }

}