package managers;

import tasks.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Класс Manager содержит список CRUD методов для всех типов задач.
 */
public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private int generatedID = 0;
    private final HistoryManager historyManager = Managers.getDefaultHistoryManager();



    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Task> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Task> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }


    @Override
    public void clearTasks() {
        tasks.clear();
    }

    @Override
    public void clearEpics() {
        for (Epic epic : epics.values()) {
            for (Subtask subtask : epic.getSubtasks()) {
                historyManager.remove(subtask);
                deleteSubtask(subtask.getId());
            }
            historyManager.remove(epic);
        }
        epics.clear();

    }

    @Override
    public void clearSubtasks() {
        for (Epic epic : epics.values()) {
            for (Subtask subtask : epic.getSubtasks()) {
                historyManager.remove(subtask);
            }
            epic.getSubtasks().clear();
            updateEpicStatus(epic);
        }
        subtasks.clear();
    }


    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        if(task!=null){
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        if(epic!=null){
            historyManager.add(epic);
        }
        return epic;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        if(subtask!=null){
            historyManager.add(subtask);
        }
        return subtask;
    }

    @Override
    public Task getByID(int id) {
        if (tasks.containsKey(id) || epics.containsKey(id) || subtasks.containsKey(id)) {
            Task tempTask = null;

            if (tasks.containsKey(id)) {
                tempTask = tasks.get(id);
            } else if (epics.containsKey(id)) {
                tempTask = epics.get(id);
            } else if (subtasks.containsKey(id)) {
                tempTask = subtasks.get(id);
            }
            historyManager.add(tempTask);
            return tempTask;
        } else {
            System.out.println("Ошибка! Такого ID не существует");
        }
        return null;
    }


    @Override
    public void createTask(Task task) {
        task.setId(++generatedID);
        tasks.put(task.getId(), task);
    }

    @Override
    public void createEpic(Epic epic) {
        epic.setId(++generatedID);
        epics.put(epic.getId(), epic);
    }

    @Override
    public void createSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        if(epic!=null){
            subtask.setId(++generatedID);
            subtasks.put(subtask.getId(), subtask);
            epic.getSubtasks().add(subtask);
            updateEpicStatus(epic);
        }
    }

    @Override
    public void updateTask(Task task) {
        if (!tasks.containsKey(task.getId())) {
            return;
        }
        tasks.put(task.getId(), task);
        historyManager.update(task);
    }

    @Override
    public void updateEpic(Epic epic) {
        if (!epics.containsKey(epic.getId())) {
            return;
        }
        epics.put(epic.getId(), epic);
        historyManager.update(epic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (!subtasks.containsKey(subtask.getId())) {
            return;
        }
        //обновляем список подзадач в эпике
        Epic epic = epics.get(subtask.getEpicId());
        if(epic!=null){
            Subtask subtaskToRemove = subtasks.get(subtask.getId());
            epic.getSubtasks().remove(subtaskToRemove);
            epic.getSubtasks().add(subtask);
            updateEpicStatus(epic);
        }
        subtasks.put(subtask.getId(), subtask);
        historyManager.update(subtask);
    }

    @Override
    public void deleteTask(int id) {
        tasks.remove(id);
        historyManager.remove(tasks.get(id));
    }

    @Override
    public void deleteEpic(int id) {
        for (Subtask subtask : epics.get(id).getSubtasks()) {
            subtasks.remove(subtask.getId());
            historyManager.remove(subtask);
        }
        epics.remove(id);
        historyManager.remove(epics.get(id));
    }

    @Override
    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        if (epics.containsKey(subtask.getEpicId())) {
            Epic epic = epics.get(subtask.getEpicId());
            epic.getSubtasks().remove(subtask);
            updateEpicStatus(epic);
        }
        subtasks.remove(subtask.getId());
        historyManager.remove(subtasks.get(subtask.getId()));

    }

    @Override
    public void deleteByID(int id) {
        if (tasks.containsKey(id) || epics.containsKey(id) || subtasks.containsKey(id)) {
            if (tasks.containsKey(id)) {
                historyManager.remove(tasks.get(id));
                tasks.remove(id);
            } else if (epics.containsKey(id)) {
                ArrayList<Subtask> subtasksID = epics.get(id).getSubtasks();
                for (Subtask subtask : subtasksID) {
                    historyManager.remove(subtask);
                    subtasks.remove(id);
                }
                historyManager.remove(epics.get(id));
                epics.remove(id);
            } else if (subtasks.containsKey(id)) {
                deleteSubtask(id);
                historyManager.remove(subtasks.get(id));
                subtasks.remove(id);
            }
        } else {
            System.out.println("Ошибка! Такого ID не существует");
        }
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
}
