package manager;
import task.Status;
import task.Task;
import task.Epic;
import task.SubTask;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    protected int nextId = 0;
    public HashMap<Integer, Task> tasks = new HashMap<>();
    public HashMap<Integer, SubTask> subTasks = new HashMap<>();
    public HashMap<Integer, Epic> epics = new HashMap<>();

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public HashMap<Integer, SubTask> getSubTasks() {
        return subTasks;
    }

    public void removeTasks() {
        tasks.clear();
    }

    public void removeEpics(int id) {
        epics.clear();
    }

    public void removeSubTasks(int id) {
        subTasks.clear();
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public SubTask getSubTaskById(int id) {
        return subTasks.get(id);
    }

    public Task addTask(Task task) {
        int newId = ++nextId;
        task.setId(newId);
        tasks.put(newId, task);
        return task;
    }

    public SubTask addSubTask(SubTask subTask) {
        subTask.setId(++nextId);
        subTasks.put(subTask.getId(), subTask);
        return subTask;
    }

    public Epic addEpic(Epic epic) {
        epic.setId(++nextId);
        updateEpicStatus(epic);
        epics.put(epic.getId(), epic);
        return epic;
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void updateSubTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
        Epic epic = epics.get(subTask.getEpicId());
        updateEpicStatus(epic);
    }

    private void updateEpicStatus(Epic epic) {
        if (epic.getSubTasksIds().isEmpty()) {
            epic.setStatus(Status.NEW);
            epics.put(epic.getId(), epic);
        }
        int newStatusCounter = 0;
        int doneStatusCounter = 0;
        for (int i = 0; i < epic.getSubTasksIds().size(); i++) {
            SubTask subTask = getSubTaskById(epic.getSubTasksIds().get(i));
            if (subTask.getStatus() == Status.NEW) {
                newStatusCounter++;
            } else if (subTask.getStatus().equals(Status.DONE)) {
                doneStatusCounter++;
            }
        }
        if (newStatusCounter == epic.getSubTasksIds().size()) {
            epic.setStatus(Status.NEW);
        } else if (doneStatusCounter == epic.getSubTasksIds().size()) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
        epics.put(epic.getId(), epic);
    }

    public void removeTaskById(int id) {
        tasks.remove(id);
    }

    public void removeEpicById(int id) {
        epics.remove(id);
    }

    public void removeSubTaskById(int id) {
        subTasks.remove(id);
    }

    public void getEpicSubs(int id) {
        ArrayList<Integer> idents = getEpicById(id).getSubTasksIds();
        for (Integer ident : idents) {
            getSubTaskById(ident);
        }
    }
}