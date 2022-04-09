package managers;

import tasks.Task;
import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private static final List<Task> history = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (history.size() > 9) {
            history.remove(0);
        }
        history.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }

    @Override
    public void remove(Task task) {
        history.remove(task);
    }

    @Override
    public void update(Task task) {
        for (int i = 0; i < history.size(); i++) {
            if (history.get(i).getId() == task.getId()) {
                history.remove(i);
                history.add(i, task);
            }
        }
    }
}