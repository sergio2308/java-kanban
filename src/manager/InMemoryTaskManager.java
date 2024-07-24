package manager;

import tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.reflect.Array.get;

public class InMemoryTaskManager implements TaskManager {
    protected static HashMap<Integer, Task> tasks;
    protected static HashMap<Integer, Epic> epics;
    protected static HashMap<Integer, SubTask> subTasks;

    protected Set<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    private static int idGenerator = 1;
    private final HistoryManager historyManager = Managers.getDefaultHistoryManager();

    public InMemoryTaskManager() {
        this.tasks = new HashMap<Integer, Task>();
        this.epics = new HashMap<Integer, Epic>();
        this.subTasks = new HashMap<Integer, SubTask>();
    }

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public void clearAllTasks() {
        prioritizedTasks.removeIf(task -> task.getTaskType().equals(TaskType.TASK));
        tasks.clear();
    }

    @Override
    public void clearAllEpics() {
        prioritizedTasks.removeIf(task -> !task.getTaskType().equals(TaskType.TASK));
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void clearAllSubTasks() {
        prioritizedTasks.removeIf(task -> !task.getTaskType().equals(TaskType.TASK));
        subTasks.clear();
        for (Integer epicId : epics.keySet()) {
            Epic epic = (Epic) getEpicById(epicId);
            epic.getSubTasksIds().clear();
            updateEpic(epic);
        }
    }

    @Override
    public Task getTaskById(int id) {
        historyManager.addHistory(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        historyManager.addHistory(epics.get(id));
        return epics.get(id);
    }

    @Override
    public SubTask getSubTaskById(int id) {
        historyManager.addHistory(subTasks.get(id));
        return subTasks.get(id);
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public Integer createNewTask(Task task) {
        int id = idGenerator++;
        task.setId(id);
        if (task.getStartTime() != null && taskIntersectionsCheck(task)) {
            tasks.put(id, task);
        } else {
            task.setStartTime(LocalDateTime.MAX);
            tasks.put(id, task);
        }
        return id;
    }

    @Override
    public Integer createNewEpic(Epic epic) {
        int id = idGenerator++;
        epic.setId(id);
        epics.put(id, epic);
        return id;
    }

    @Override
    public Integer createNewSubTask(SubTask subTask, int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return null;
        }
        int id = idGenerator++;
        subTask.setId(id);
        if (subTask.getStartTime() != null && taskIntersectionsCheck(subTask)) {
            subTasks.put(id, subTask);
            epic.addSubTaskId(id);
            updateEpic(epics.get(epicId));
        } else {
            subTask.setStartTime(LocalDateTime.MAX);
            subTasks.put(id, subTask);
            epic.addSubTaskId(id);
            updateEpic(epics.get(epicId));
        }
        return id;
    }

    @Override
    public void updateTask(Task task) {
        prioritizedTasks.remove(task);
        if (taskIntersectionsCheck(task)) {
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void updateEpic(Epic newEpic) {
        Epic epicToUpdate = getEpicById(newEpic.getId());

        if (epicToUpdate.getSubTasksIds().isEmpty()) {
            epicToUpdate.setStatus(Status.NEW);
            epics.put(epicToUpdate.getId(), epicToUpdate);
        }
        int newStatusCounter = 0;
        int doneStatusCounter = 0;

        for (int i = 0; i < epicToUpdate.getSubTasksIds().size(); i++) {
            SubTask subTask = (SubTask) getSubTaskById(epicToUpdate.getSubTasksIds().get(i));
            if (subTask.getStatus() == Status.NEW) {
                newStatusCounter++;
            } else if (subTask.getStatus().equals(Status.DONE)) {
                doneStatusCounter++;
            }
        }
        if (newStatusCounter == epicToUpdate.getSubTasksIds().size()) {
            epicToUpdate.setStatus(Status.NEW);
        } else if (doneStatusCounter == epicToUpdate.getSubTasksIds().size()) {
            epicToUpdate.setStatus(Status.DONE);
        } else {
            epicToUpdate.setStatus(Status.IN_PROGRESS);
        }
        epics.put(epicToUpdate.getId(), epicToUpdate);
    }

    @Override
    public void updateSubTask(SubTask newSubTask, int epicId) {
        SubTask subTaskToUpdate = getSubTaskById(newSubTask.getId());
        prioritizedTasks.remove(subTaskToUpdate);
        if(taskIntersectionsCheck(subTaskToUpdate)) {
        subTaskToUpdate.setStatus(newSubTask.getStatus());
        Epic epicToUpdate = getEpicById(epicId);
        updateEpic(epicToUpdate);
        }
    }

    @Override
    public void removeTaskById(int id) {
        Task task = tasks.get(id);
        historyManager.remove(id);
        tasks.remove(id);
        prioritizedTasks.remove(task);
    }

    @Override
    public void removeEpicById(int id) {
        historyManager.remove(id);
        Epic epicToRemove = epics.get(id);
        if (epicToRemove != null) {
            epicToRemove.getSubTasksIds().stream()
                    .forEach(subTaskId -> prioritizedTasks.remove(subTasks.get(subTaskId)));
            epicToRemove.getSubTasksIds().forEach(subTasks::remove);
        }
        epics.remove(id);
    }

    @Override
    public void removeSubTaskById(int id) {
        historyManager.remove(id);
        SubTask subTask = subTasks.get(id);
        prioritizedTasks.remove(subTask);
        if (subTask != null) {
            Epic epic = getEpicById(subTask.getEpicId());
            if (epic != null) {
                epic.getSubTasksIds().removeIf(subTaskId -> subTaskId == id);
                updateEpic(epic);
            }
        }
        subTasks.remove(id);
    }

    @Override
    public void getEpicSubTasks(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            epic.getSubTasksIds().forEach(subTaskId -> subTasks.get(subTaskId));
        }
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return prioritizedTasks.stream()
                .filter(task -> task.getStartTime() != null)
                .collect(Collectors.toList());
    }

    private boolean checkIntersections(Task task1, Task task2) {
        return task1.getEndTime().isAfter(task2.getStartTime()) ||
                task1.getStartTime().isBefore(task2.getEndTime()) ||
                (task1.getStartTime().isAfter(task2.getStartTime()) && task1.getEndTime().isBefore(task2.getEndTime())) ||
                (task2.getStartTime().isAfter(task1.getStartTime()) && task2.getEndTime().isBefore(task1.getEndTime()));
    }

    private boolean taskIntersectionsCheck(Task task) {
        Optional<Task> intersectionsList = Optional.empty();
        intersectionsList = getPrioritizedTasks().stream()
                .filter(t -> checkIntersections(task, t))
                .findAny();
        return intersectionsList.isEmpty();
    }

    private void calculateEpicStartTime(int id) {
        Epic epic = epics.get(id);
        List<SubTask> epicsSubTasks = new ArrayList<>();
        for (Integer subTaskId : epic.getSubTasksIds()) {
            epicsSubTasks.add(subTasks.get(subTaskId));
        }
        epics.get(id).setStartTime(epicsSubTasks.stream()
                .map(Task::getStartTime)
                .min(LocalDateTime::compareTo)
                .orElse(LocalDateTime.MAX));
    }

    private void calculateEpicEndTime(int id) {
        Epic epic = epics.get(id);
        List<SubTask> epicsSubTasks = new ArrayList<>();
        for (Integer subTaskId : epic.getSubTasksIds()) {
            epicsSubTasks.add(subTasks.get(subTaskId));
        }
        epics.get(id).setEndTime(epicsSubTasks.stream()
                .map(Task::getEndTime)
                .min(LocalDateTime::compareTo)
                .orElse(LocalDateTime.MAX));
    }

    private void calculateEpicDuration(int id) {
        Epic epic = epics.get(id);
        long totalDuration = epic.getSubTasksIds().stream()
                .map(subTasks::get)
                .mapToLong(subTask -> subTask.getDuration().toMinutes())
                .sum();
        epics.get(id).setDuration(Duration.ofMinutes(totalDuration));
    }
}