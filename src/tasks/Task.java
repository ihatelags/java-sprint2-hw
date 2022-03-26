package tasks;

import java.util.Objects;

public class Task {
    protected String title;
    protected String desc;
    protected int id;
    protected String status;

    public Task(String title, String desc, int id, String status) {
        this.title = title;
        this.desc = desc;
        this.id = id;
        this.status = status;
    }

    public Task(String title, String desc, String status) {
        this.title = title;
        this.desc = desc;
        this.id = 0;
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task Task = (Task) o;
        return id == Task.id && Objects.equals(title, Task.title) && Objects.equals(desc, Task.desc) && Objects.equals(status, Task.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, desc, id, status);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "tasks.Task{" +
                "title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}';
    }
}
