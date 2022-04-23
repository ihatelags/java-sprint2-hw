package managers;

import managers.interfaces.Manager;
import managers.interfaces.HistoryManager;

public class Managers {

    public static Manager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistoryManager() {
        return new InMemoryHistoryManager();
    }

}