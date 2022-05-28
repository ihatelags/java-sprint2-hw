package managers;

import managers.interfaces.Manager;
import managers.interfaces.HistoryManager;

import java.io.File;

public class Managers {

    public static Manager getDefault() {
        return new FileBackedTasksManager(new File("tasks.csv"));
        //return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistoryManager() {
        return new InMemoryHistoryManager();
    }

}