package tasks;

import java.time.Duration;
import java.time.LocalDateTime;

public class Task {
    protected String name;
    protected String description;
    protected int id;
    protected Status status;
    protected TaskType taskType;
    protected Duration duration;
    protected LocalDateTime startTime;

    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.taskType = TaskType.TASK;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Task(String name, String description, int id, Status status, LocalDateTime startTime, Duration duration) {
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public Task() {

    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id/* && Objects.equals(name, task.name)*/;
    }

    @Override
    public int hashCode() {
        int hash = 17;
        if (name != null) {
            hash = name.hashCode();
        }
        if (id != 0) {
            hash = hash + id;
        }
        return hash;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        LocalDateTime endTime;
        if (startTime == null) {
            endTime = null;
        } else {
            endTime = startTime.plus(duration);
        }
        return endTime;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", taskType=" + taskType +
                ", startTime=" + startTime +
                ", duration=" + duration +
                '}';
    }

    public void setId(int newId) {
        id = newId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
