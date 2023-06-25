package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    protected int id;
    protected String name;
    protected String description;
    protected Status status;
    protected LocalDateTime startTime;
    protected Duration duration;
    protected LocalDateTime endTime;
    protected static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy/mm:HH");

    public Task(int id, String name, String description, Status status, LocalDateTime startTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
        this.startTime = startTime;
    }

    public Task(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public void setStartTime(String startTime) {
        this.startTime = LocalDateTime.parse(startTime, FORMATTER);
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
        setEndTime(startTime.plus(duration));
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }
    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    public Duration getDuration() {
        return duration;
    }
    public Task(int id, String name, String description, Status status) {
    }

    public TaskType getType() {
        return TaskType.TASK;
    }

    public int getId() {
        return id;
    }

    public static DateTimeFormatter getFORMATTER() {
        return FORMATTER;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status + "," +
                getStartTime().format(FORMATTER) +
                "," + getDuration().toMinutes() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name)
                && Objects.equals(description, task.description)
                && status == task.status
                && Objects.equals(startTime, task.startTime)
                && Objects.equals(duration, task.duration)
                && Objects.equals(endTime, task.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, status, startTime, duration, endTime);
    }
}