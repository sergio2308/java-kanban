package tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Epic extends Task {
    private LocalDateTime endTime;
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

    public Epic(String name, String description, int id, Status status, HashMap<Integer, SubTask> subTasks) {
        this.subTasks = subTasks;
        this.startTime = subTasks.get(0).getStartTime();
        this.endTime = subTasks.get(subTasks.size() - 1).getEndTime();
    }

    public void addSubTaskId(int id) {
        subTasksIds.add(id);
    }

    public ArrayList<Integer> getSubTasksIds() {
        return subTasksIds;
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
