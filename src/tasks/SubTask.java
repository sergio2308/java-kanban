package tasks;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {
    protected int epicId;

        public SubTask(String name, String description, int epicId, Status status) {
        super(name, description, status);
        this.taskType = TaskType.SUBTASK;
    }

    public SubTask(String name, String description, int id, Status status, int epicId) {
        super();
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int newEpicId) {
        epicId = newEpicId;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", taskType=" + taskType +
                ", epicId=" + epicId +
                '}';
    }
}
