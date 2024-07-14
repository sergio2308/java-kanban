package tasks;

import java.util.ArrayList;

public class Epic extends Task {
    protected ArrayList<Integer> subTasksIds;

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
