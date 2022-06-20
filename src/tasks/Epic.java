package tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private ArrayList<Subtask> subtasks = new ArrayList<>();

    public Epic(String name, String desc, Status status) {
        super(name, desc, status);
    }

    public Epic(String name, String desc, Status status, ArrayList<Subtask> subtasks) {
        super(name, desc, status);
        this.subtasks = subtasks;
        this.id = 0;
    }

    public Epic(String name, String desc, Status status, LocalDateTime startTime, int duration, ArrayList<Subtask> subtasks) {
        super(name, desc, status, startTime, duration);
        this.subtasks = subtasks;
        this.id = 0;
    }

    public Epic(String name, String desc, int id, Status status, LocalDateTime startTime, int duration, ArrayList<Subtask> subtasks) {
        super(name, desc, status, startTime, duration);
        this.subtasks = subtasks;
        this.id = id;
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(ArrayList<Subtask> subtasks) {
        this.subtasks = subtasks;
    }

    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }

    public void calculateEpicDuration(Epic epic) {
        int duration = 0;
        for (Subtask subtask: epic.getSubtasks()) {
            duration+=subtask.getDuration();
        }
        epic.setDuration(duration);
    }

    public void setEpicStartAndEndTime(Epic epic) {
        List<LocalDateTime> startTimeList = new ArrayList<>();
        for (Subtask subtask: epic.getSubtasks()) {
            startTimeList.add(subtask.getStartTime());
        }
        if (!startTimeList.isEmpty()) {
            LocalDateTime startTime = startTimeList.stream().min(LocalDateTime::compareTo).get();
            epic.setStartTime(startTime);
        }
    }

    public String toString() {
        return "tasks.Epic{" +
                "title='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                ", subtasks='" + subtasks.size() + '\'' +
                '}';
    }
}
