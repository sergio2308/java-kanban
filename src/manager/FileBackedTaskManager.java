package manager;

import task.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;
    private boolean load;

    public FileBackedTaskManager(File file) {
        super(new InMemoryHistoryManager());
        this.file = file;
    }

    private String toString(Task task) {
        TaskType taskType;
        String idEpic = "";
        if (task instanceof Epic) {
            taskType = TaskType.EPIC;
        } else {
            if (task instanceof SubTask) {
                taskType = TaskType.SUBTASK;
                idEpic += ((SubTask) task).getEpicId();
            } else {
                taskType = TaskType.TASK;
            }
        }
        return String.format("%d,%s,%s,%s,%s,%s\n",
                task.getId(), taskType, task.getName(),
                task.getStatus(), task.getDescription(), idEpic);
    }

    private Task fromString(String value) {
        Task task = null;
        String[] taskLines = value.split(",");
        int id = Integer.parseInt(taskLines[0]);
        TaskType type = TaskType.valueOf(taskLines[1]);
        String name = taskLines[2];
        Status status = Status.valueOf(taskLines[3]);
        String description = taskLines[4];
        switch (type) {
            case TASK:
                task = new Task(id, name, description, status);
                break;
            case EPIC:
                task = new Epic(id, name, description, status, new ArrayList<Integer>());
                break;
            case SUBTASK:
                int idEpic = Integer.parseInt(taskLines[5]);
                task = new SubTask(id, name, description, status, idEpic);
                break;
        }
        return task;
    }
    
    @Override
    public void addSubTask(SubTask subTask) {
        super.addSubTask(subTask);
        save();
    }

    @Override
    public void removeTasks() {
        super.removeTasks();
        save();
    }

    @Override
    public void removeEpics(int id) {
        super.removeEpics(id);
        save();
    }

    @Override
    public void removeSubTasks(int id) {
        super.removeSubTasks(id);
        save();
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
        save();
    }

    @Override
    public Task getTaskById(int id) {
        return super.getTaskById(id);
        save();
    }

    @Override
    public Epic getEpicById(int id) {
        return super.getEpicById(id);
        save();
    }

    @Override
    public SubTask getSubTaskById(int id) {
        return super.getSubTaskById(id);
        save();
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeEpicById(int id) {
        super.removeEpicById(id);
        save();
    }

    @Override
    public void removeSubTaskById(int id) {
        super.removeSubTaskById(id);
        save();
    }

    @Override
    public void getEpicSubs(int id) {
        super.getEpicSubs(id);
        save();
    }
}
