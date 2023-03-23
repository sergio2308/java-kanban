package manager;
import task.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class InMemoryTaskManager implements TaskManager {

    public HistoryManager historyManager = Managers.getDefaultHistory();
    protected int nextId = 0;
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, SubTask> subTasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();


    @Override
    public Map<Integer, Task> getTasks() {
        return tasks;
    }

    @Override
    public Map<Integer, Epic> getEpics() {
        return epics;
    }

    @Override
    public Map<Integer, SubTask> getSubTasks() {
        return subTasks;
    }

    @Override
    public void removeTasks() {
        tasks.clear();
    }
    
    @Override
    public void removeEpics(int id) {
        epics.clear();
        subTasks.clear();
    }
    
    @Override
    public void removeSubTasks(int id) {
        subTasks.clear();
    }
    
    @Override
    public Task getTaskById(int id) {
        historyManager.addViewedTask(tasks.get(id));
        return tasks.get(id);
    }
    
    @Override
    public Epic getEpicById(int id) {
        historyManager.addViewedTask(epics.get(id));
        return epics.get(id);
    }
    
    @Override
    public SubTask getSubTaskById(int id) {
        historyManager.addViewedTask(subTasks.get(id));
        return subTasks.get(id);
    }
    
    @Override
    public Task addTask(Task task) {
        int newId = ++nextId;
        task.setId(newId);
        tasks.put(newId, task);
        return task;
    }
    
    @Override
    public SubTask addSubTask(SubTask subTask) {
        subTask.setId(++nextId);
        subTasks.put(subTask.getId(), subTask);
        return subTask;
    }
    
    @Override
    public Epic addEpic(Epic epic) {
        epic.setId(++nextId);
        updateEpicStatus(epic);
        epics.put(epic.getId(), epic);
        return epic;
    }
    
    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }
    
    @Override
    public void updateSubTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
        Epic epic = epics.get(subTask.getEpicId());
        updateEpicStatus(epic);
    }
    
    @Override
    public void updateEpicStatus(Epic epic) {
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
    
    @Override
    public void removeTaskById(int id) {
        tasks.remove(id);
    }
    
    @Override
    public void removeEpicById(int id) {
        epics.remove(id);
    }
    
    @Override
    public void removeSubTaskById(int id) {
        subTasks.remove(id);
    }
    
    @Override
    public void getEpicSubs(int id) {
        ArrayList<Integer> idents = getEpicById(id).getSubTasksIds();
        for (Integer ident : idents) {
            getSubTaskById(ident);
        }
    }
}
