package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    protected ArrayList<Integer> subTasksIds;

    public Epic(int id, String name, String description, ArrayList<Integer> subTasksIds) {
        super(id, name, description);
        this.subTasksIds = subTasksIds;
    }

    public Epic(int id, String name, String description, Status status, ArrayList<Integer> subTasksIds) {
        super(id, name, description, status);
        this.subTasksIds = subTasksIds;
        setStartTime(LocalDateTime.MIN.format(FORMATTER));
        setDuration(Duration.ZERO);
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }

    public void addSubTask(int subTaskId) {
        this.subTasksIds.add(subTaskId);
    }

    public ArrayList<Integer> getSubTasksIds() {
        return subTasksIds;
    }

    public void setSubTasksIds(ArrayList<Integer> subTasksIds) {
        this.subTasksIds = subTasksIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subTasksIds, epic.subTasksIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subTasksIds);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subTasksIds=" + subTasksIds  +
                getStartTime().format(FORMATTER) +
                "," + getDuration().toMinutes() +
                '}';
    }
}