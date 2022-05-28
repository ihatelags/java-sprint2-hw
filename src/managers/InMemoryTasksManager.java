package managers;

import managers.interfaces.Manager;
import managers.interfaces.HistoryManager;
import tasks.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Класс InMemoryTaskManager содержит список CRUD методов для всех типов задач.
 */
public class InMemoryTasksManager implements Manager {
    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();
    protected final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected int generatedID = 0;
    protected final HistoryManager historyManager = Managers.getDefaultHistoryManager();

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void deleteTasks() {
        for (Task task : tasks.values()) {
            historyManager.remove(task.getId());
        }
        tasks.clear();
    }

    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public void createTask(Task task) {
        task.setId(++generatedID);
        tasks.put(task.getId(), task);
    }


    @Override
    public void updateTask(Task task) {
        if (!tasks.containsKey(task.getId())) {
            return;
        }
        if (task instanceof Subtask) {
            Subtask subtask = (Subtask) task;
            if (!subtasks.containsKey(subtask.getId())) {
                return;
            }
            //обновляем список подзадач в эпике
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                Subtask subtaskToRemove = subtasks.get(subtask.getId());
                epic.getSubtasks().remove(subtaskToRemove);
                epic.getSubtasks().add(subtask);
                updateEpicStatus(epic);
            }
            subtasks.put(subtask.getId(), subtask);
        }
        tasks.put(task.getId(), task);
    }

    @Override
    public void deleteTask(int id) {
        historyManager.remove(id);
        tasks.remove(id);
    }

    @Override
    public Subtask getSubtask(int id) {
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public void createSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            subtask.setId(++generatedID);
            subtasks.put(subtask.getId(), subtask);
            epic.getSubtasks().add(subtask);
            updateEpicStatus(epic);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (!subtasks.containsKey(subtask.getId())) {
            return;
        }
        //обновляем список подзадач в эпике
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            Subtask subtaskToRemove = subtasks.get(subtask.getId());
            epic.getSubtasks().remove(subtaskToRemove);
            epic.getSubtasks().add(subtask);
            updateEpicStatus(epic);
        }
        subtasks.put(subtask.getId(), subtask);
    }

    @Override
    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            if (epics.containsKey(subtask.getEpicId())) {
                Epic epic = epics.get(subtask.getEpicId());
                epic.getSubtasks().remove(subtask);
                updateEpicStatus(epic);
            }
            historyManager.remove(id);
            subtasks.remove(id);
        }
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void deleteEpics() {
        for (Epic epic : epics.values()) {
            for (Subtask subtask : epic.getSubtasks()) {
                historyManager.remove(subtask.getId());
                deleteTask(subtask.getId());
            }
            historyManager.remove(epic.getId());
        }
        epics.clear();
        subtasks.clear();
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic);
        }
        return epic;
    }

    @Override
    public void createEpic(Epic epic) {
        epic.setId(++generatedID);
        epics.put(epic.getId(), epic);
    }

    public void deleteEpic(int id) {
        for (Subtask subtask : epics.get(id).getSubtasks()) {
            historyManager.remove(subtask.getId());
            subtasks.remove(subtask.getId());
        }
        historyManager.remove(id);
        epics.remove(id);
    }

    @Override
    public void updateEpic(Epic epic) {
        if (!epics.containsKey(epic.getId())) {
            return;
        }
        epics.put(epic.getId(), epic);
    }

    /**
     * Обновление статуса Эпика
     */
    private void updateEpicStatus(Epic epic) {
        HashSet<Status> subtasksSet = new HashSet<>();

        int countSubtask = epic.getSubtasks().size();
        // если у эпика нет подзадач, то статус должен быть NEW.
        if (countSubtask == 0) {
            epic.setStatus(Status.NEW);
        } else {
            for (Subtask subtask : epic.getSubtasks()) {
                if (subtask.getStatus().equals(Status.NEW)) {
                    subtasksSet.add(Status.NEW);
                } else if (subtask.getStatus().equals(Status.DONE)) {
                    subtasksSet.add(Status.DONE);
                } else {
                    subtasksSet.add(Status.IN_PROGRESS);
                }
            }
            if (subtasksSet.size() == 1 && subtasksSet.toArray()[0] == (Status.NEW)) {
                epic.setStatus(Status.NEW);
            } else if (subtasksSet.size() == 1 && subtasksSet.toArray()[0] == (Status.DONE)) {
                epic.setStatus(Status.DONE);
            } else {
                epic.setStatus(Status.IN_PROGRESS);
            }
        }
    }

    @Override
    public Task getByID(int id) {
        if (tasks.containsKey(id) || epics.containsKey(id) || subtasks.containsKey(id)) {
            if (tasks.containsKey(id)) {
                return getTask(id);
            } else if (epics.containsKey(id)) {
                return getEpic(id);
            } else if (subtasks.containsKey(id)) {
                return getSubtask(id);
            }
        }
        return null;
    }

    public void deleteByID(int id) {
        if (tasks.containsKey(id) || epics.containsKey(id) || subtasks.containsKey(id)) {
            if (tasks.containsKey(id)) {
                deleteTask(id);
            } else if (epics.containsKey(id)) {
                deleteEpic(id);
            } else if (subtasks.containsKey(id)) {
                deleteSubtask(id);
            }
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
