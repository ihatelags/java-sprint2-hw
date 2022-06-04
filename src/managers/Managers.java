package managers;

import java.io.File;

public class Managers {

    public static TaskManager getDefault() {
        return new FileBackedTaskManager(new File("tasks.csv"));
        //return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistoryManager() {
        return new InMemoryHistoryManager();
    }

}