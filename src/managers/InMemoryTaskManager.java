package managers;

import tasks.*;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Класс InMemoryTaskManager содержит список CRUD методов для всех типов задач.
 */
public class InMemoryTaskManager implements TaskManager {
    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();
    protected final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected int generatedID = 0;
    protected final HistoryManager historyManager = Managers.getDefaultHistoryManager();
    private final TreeSet<Task> sortedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime,
            Comparator.nullsLast(Comparator.naturalOrder())));

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        return sortedTasks;
    }

    @Override
    public boolean checkTimeIsValid(Task newTask) {
        LocalDateTime newTaskStart = newTask.getStartTime();
        LocalDateTime newTaskEnd = newTask.getEndTime();
        if (newTaskStart == null) {
            return true;
        }
        for (Task task : sortedTasks) {
            LocalDateTime taskStart = task.getStartTime();
            LocalDateTime taskEnd = task.getEndTime();
            if (newTaskStart.isEqual(taskStart) || newTaskEnd.isEqual(taskEnd)) {
                return false;
            }
            if (newTaskStart.isAfter(taskStart) && (newTaskStart.isBefore(taskEnd))) {
                return false;
            }
            if (newTaskEnd.isAfter(taskStart) && (newTaskEnd.isBefore(taskEnd))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void deleteTasks() {
        for (Task task : tasks.values()) {
            deleteTask(task.getId());
        }
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
        if (!checkTimeIsValid(task)){
            return;
        }
        if (task.getId() == 0) {
            task.setId(++generatedID);
        }
        tasks.put(task.getId(), task);
        sortedTasks.add(task);
    }


    @Override
    public void updateTask(Task task) {
        if (!checkTimeIsValid(task)){
            return;
        }
        if (!tasks.containsKey(task.getId())) {
            return;
        }
        tasks.put(task.getId(), task);
        sortedTasks.remove(tasks.get(task.getId()));
        sortedTasks.add(task);
    }

    @Override
    public void deleteTask(int id) {
        historyManager.remove(id);
        sortedTasks.remove(tasks.get(id));
        tasks.remove(id);
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.add(subtask);
        }
        return subtask;
     }

    @Override
    public void createSubtask(Subtask subtask) {
        if (!checkTimeIsValid(subtask)){
            return;
        }
        if (subtask.getId() == 0) {
            subtask.setId(++generatedID);
        }
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            subtasks.put(subtask.getId(), subtask);
            epic.getSubtasks().add(subtask);
            updateEpic(epic);
            sortedTasks.add(subtask);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        int id = subtask.getId();
        Subtask oldSubtask = subtasks.get(id);
        sortedTasks.remove(oldSubtask);
        if (!checkTimeIsValid(subtask)){
            sortedTasks.add(oldSubtask);
            return;
        }
        if (!subtasks.containsKey(id)) {
            return;
        }
        //обновляем список подзадач в эпике
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            epic.getSubtasks().remove(oldSubtask);
            epic.getSubtasks().add(subtask);
            updateEpic(epic);
        }
        subtasks.put(id, subtask);
        sortedTasks.add(subtask);
    }

    @Override
    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask == null) {
            return;
        }
        Epic epic = epics.get(subtask.getEpicId());
        if (epic == null) {
            return;
        }
        // to avoid concurrent changing of arraylist
        ArrayList<Subtask> newSubtasks = new ArrayList<>(epic.getSubtasks());
        newSubtasks.remove(subtask);
        epic.setSubtasks(newSubtasks);
        updateEpic(epic);
        historyManager.remove(id);
        sortedTasks.remove(subtasks.get(id));
        subtasks.remove(id);
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void deleteEpics() {
        for (Epic epic : epics.values()) {
            deleteEpic(epic.getId());
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
        if (epic.getId() == 0) {
            epic.setId(++generatedID);
        }
        epics.put(epic.getId(), epic);
        updateEpic(epic);
    }

    public void deleteEpic(int id) {
        for (Subtask subtask : epics.get(id).getSubtasks()) {
            deleteSubtask(subtask.getId());
        }
        historyManager.remove(id);
        epics.remove(id);
    }

    @Override
    public void updateEpic(Epic epic) {
        if (!epics.containsKey(epic.getId())) {
            return;
        }
        epic.calculateEpicDuration(epic);
        epic.setEpicStartAndEndTime(epic);
        updateEpicStatus(epic);
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
