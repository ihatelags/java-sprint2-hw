package tasks;


public class Subtask extends Task {
    int epicId;

    public Subtask(String title, String desc, String status, int epicId) {
        super(title, desc, status);
        this.epicId = epicId;
    }

    public Subtask(String title, String desc, int id, String status, int epicId) {
        super(title, desc, id, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "tasks.Subtask{" +
                "title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                ", epicId='" + epicId + '\'' +
                '}';
    }
}
