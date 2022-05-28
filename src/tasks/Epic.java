package tasks;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Subtask> subtasks = new ArrayList<>();

    public Epic(String title, String desc, Status status) {
        super(title, desc, status);
    }

    public Epic(String title, String desc, Status status, ArrayList<Subtask> subtasks) {
        super(title, desc, status);
        this.subtasks = subtasks;
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(ArrayList<Subtask> subtasks) {
        this.subtasks = subtasks;
    }

    public TaskType getType() {
        return TaskType.EPIC;
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
