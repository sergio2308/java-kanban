package manager;
import task.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InMemoryTaskManager implements TaskManager {

    public HistoryManager historyManager = new InMemoryHistoryManager();
    protected int nextId = 0;
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, SubTask> subTasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();

    public InMemoryTaskManager(InMemoryHistoryManager inMemoryHistoryManager) {
        this.historyManager = new InMemoryHistoryManager();
    }

    public InMemoryTaskManager() {

    }


    @Override
    public List<Task> getTasks() {
        return historyManager.getHistory();
    }

    @Override
    public List<Task> getEpics() {
        return historyManager.getHistory();
    }

    @Override
    public List<Task> getSubTasks() {
        return historyManager.getHistory();
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
    public List<Task> getHistory() {
        return null;
    }

    @Override
    public Task getTaskById(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }
    
    @Override
    public Epic getEpicById(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }
    
    @Override
    public SubTask getSubTaskById(int id) {
        historyManager.add(subTasks.get(id));
        return subTasks.get(id);
    }
    
    @Override
    public void addTask(Task task) {
        int newId = ++nextId;
        task.setId(newId);
        tasks.put(newId, task);
    }
    
    @Override
    public void addSubTask(SubTask subTask) {
        subTask.setId(++nextId);
        subTasks.put(subTask.getId(), subTask);
    }
    
    @Override
    public void addEpic(Epic epic) {
        epic.setId(++nextId);
        updateEpicStatus(epic);
        epics.put(epic.getId(), epic);
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
    
    @Override
    public void removeTaskById(int id) {
        tasks.remove(id);
        historyManager.remove(tasks.get(id));
    }
    
    @Override
    public void removeEpicById(int id) {
        epics.remove(id);
        historyManager.remove(tasks.get(id));
    }
    
    @Override
    public void removeSubTaskById(int id) {
        subTasks.remove(id);
        historyManager.remove(tasks.get(id));
    }
    
    @Override
    public void getEpicSubs(int id) {
        ArrayList<Integer> idents = getEpicById(id).getSubTasksIds();
        for (Integer ident : idents) {
            getSubTaskById(ident);
        }
    }
}
