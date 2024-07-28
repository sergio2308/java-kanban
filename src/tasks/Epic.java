package tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends Task {

    protected LocalDateTime endTime;
    protected ArrayList<Integer> subTasksIds;
    private HashMap<Integer, SubTask> subTasks;


    public Epic(String name, String description) {
        super(name, description);
        this.taskType = TaskType.EPIC;
        status = Status.NEW;
        subTasksIds = new ArrayList<>();
    }

    public Epic(String name, String description, int id, Status status) {
        super();
    }

    public void addSubTaskId(int id) {
        subTasksIds.add(id);
    }

    public ArrayList<Integer> getSubTasksIds() {
        return subTasksIds;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subTasksIds=" + subTasksIds +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", taskType=" + taskType +
                '}';
    }
}
