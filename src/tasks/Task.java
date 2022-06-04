package tasks;

import java.time.LocalDateTime;
import java.util.Objects;


public class Task {
    protected String name;
    protected String desc;
    protected int id;
    protected Status status;
    protected LocalDateTime startTime;
    protected int duration;
    protected LocalDateTime endTime;

    public Task(String name, String desc, int id, Status status, LocalDateTime startTime, int duration) {
        this.name = name;
        this.desc = desc;
        this.id = id;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
        this.endTime  = startTime.plusMinutes(duration);
    }

    public Task(String name, String desc, Status status, LocalDateTime startTime, int duration) {
        this.name = name;
        this.desc = desc;
        this.id = 0;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
        this.endTime  = startTime.plusMinutes(duration);
    }

    public Task(String name, String desc, int id, Status status) {
        this.name = name;
        this.desc = desc;
        this.id = id;
        this.status = status;
        this.startTime = null;
        this.duration = 0;
        this.endTime  = null;
    }

    public Task(String name, String desc, Status status) {
        this.name = name;
        this.desc = desc;
        this.id = 0;
        this.status = status;
        this.startTime = null;
        this.duration = 0;
        this.endTime  = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task Task = (Task) o;
        return id == Task.id && Objects.equals(name, Task.name) && Objects.equals(desc, Task.desc) && Objects.equals(status, Task.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, desc, id, status);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public TaskType getType() {
        return TaskType.TASK;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "tasks.Task{" +
                "title='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                ", startTime='" + startTime + '\'' +
                ", duration='" + duration + '\'' +
                '}';
    }
}
