package tasks;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Subtask> subtasks;

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

    public String toString() {
        return "tasks.Epic{" +
                "title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                ", subtasks='" + subtasks.size() + '\'' +
                '}';
    }
}
