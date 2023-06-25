package manager;
import exceptions.IntersectionCheckException;
import task.*;

import java.util.*;


public class InMemoryTaskManager implements TaskManager {

    public HistoryManager historyManager = new InMemoryHistoryManager();
    protected int nextId = 0;
    protected static Map<Integer, Task> tasks = new HashMap<>();
    protected static Map<Integer, SubTask> subTasks = new HashMap<>();
    protected static Map<Integer, Epic> epics = new HashMap<>();

    protected final Set<Task> getPrioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    public InMemoryTaskManager(InMemoryHistoryManager inMemoryHistoryManager) {
        this.historyManager = new InMemoryHistoryManager();
    }

    private void addPriorityTask(Task task) {
        getPrioritizedTasks.add(task);
        validateTaskPriority();
    }

    void validateTaskPriority() {
        List<Task> listTask = new ArrayList<>(getPrioritizedTasks);
        for (int i = 1; i < listTask.size(); i++) {
            Task newTask = listTask.get(i);
            Task lastTask = listTask.get(i-1);
            System.out.println();
            boolean check = newTask.getStartTime().isAfter(lastTask.getEndTime());
            if (!check) {
                throw new IntersectionCheckException("задача " + newTask.getStartTime() + " пересекается с " +
                        "задачей " + lastTask.getStartTime());
            }
        }
    }
    @Override
    public List<Task> getTasks() {
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
    public void removeEpics() {
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void removeSubTasks() {
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

    public void setTasks(Map<Integer, Task> tasks) {
        this.tasks = tasks;
    }

    public void setEpics(Map<Integer, Epic> epics) {
        this.epics = epics;
    }

    public void setSubTasks(Map<Integer, SubTask> subTasks) {
        this.subTasks = subTasks;
    }

    @Override
    public void addTask(Task task) {
        int newId = ++nextId;
        task.setId(newId);
        addPriorityTask(task);
        tasks.put(newId, task);
    }

    @Override
    public void addSubTask(SubTask subTask) {
        subTask.setId(++nextId);
        addPriorityTask(subTask);
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

    @Override
    public void removeTaskById(int id) {
        getPrioritizedTasks.removeIf(task -> task.getId() == id);
        tasks.remove(id);
        historyManager.remove(tasks.get(id));
    }

    @Override
    public void removeEpicById(int id) {
        getPrioritizedTasks.removeIf(task -> task.getId() == id);
        epics.remove(id);
        historyManager.remove(tasks.get(id));
    }

    @Override
    public void removeSubTaskById(int id) {
        getPrioritizedTasks.removeIf(task -> task.getId() == id);
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
            } else if (subTask.getStatus() == Status.DONE) {
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
        getPrioritizedTasks.removeIf(task -> task.getId() == epic.getId());
    }

}