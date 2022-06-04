package tasks;


import java.time.LocalDateTime;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String name, String desc, Status status, int epicId) {
        super(name, desc, status);
        this.epicId = epicId;
        this.id = 0;
    }

    public Subtask(String name, String desc, int id, Status status, int epicId) {
        super(name, desc, id, status);
        this.epicId = epicId;
    }

    public Subtask(String name, String desc, Status status, LocalDateTime startTime, int duration, int epicId) {
        super(name, desc, status, startTime, duration);
        this.epicId = epicId;
        this.id = 0;
    }

    public Subtask(String name, String desc, int id, Status status, LocalDateTime startTime, int duration, int epicId) {
        super(name, desc, id, status, startTime, duration);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public TaskType getType() {
        return TaskType.SUBTASK;
    }

    @Override
    public String toString() {
        return "tasks.Subtask{" +
                "title='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                ", epicId='" + epicId + '\'' +
                '}';
    }
}
