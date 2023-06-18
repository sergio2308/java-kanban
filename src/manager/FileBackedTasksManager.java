package manager;

import exceptions.ManagerSaveException;
import task.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static task.TaskType.EPIC;
import static task.TaskType.SUBTASK;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final File file;
    private boolean isLoad;

    public FileBackedTasksManager(File file) {
        super(new InMemoryHistoryManager());
        this.file = file;
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        HistoryManager historyManager = Managers.getDefaultHistory();
        try {
            if (!file.isFile()) {
                file.createNewFile();
            } else {
                String fileLine = Files.readString(Path.of(String.valueOf(file)));
                String[] fileLines = fileLine.split("\n");
                int i = 1;
                Map<Integer, Epic> epics = new HashMap<>();
                Map<Integer, Task> tasks = new HashMap<>();
                Map<Integer, SubTask> subTasks = new HashMap<>();
                Map<Integer, Task> tasksAll = new HashMap<>();
                if (fileLines.length < 1) {
                    throw new ManagerSaveException("Файл пуст!");
                }
                Task task;
                Epic epic;
                SubTask subTask;
                while (i < fileLines.length && !fileLines[i].isBlank()) {
                    task = fileBackedTasksManager.fromString(fileLines[i]);
                    tasksAll.put(task.getId(), task);
                    switch (task.getType()) {
                        case EPIC:
                            epic = (Epic) task;
                            epics.put(epic.getId(), epic);
                        case SUBTASK:
                            subTask = (SubTask) task;
                            subTasks.put(subTask.getId(), subTask);
                            epic = epics.get(subTask.getEpicId());
                            epic.addSubTask(subTask.getId());
                            epics.put(epic.getId(), epic);
                        case TASK:
                            tasks.put(task.getId(), task);
                    }
                }
                    i++;
                fileBackedTasksManager.setTasks(tasks);
                fileBackedTasksManager.setEpics(epics);
                fileBackedTasksManager.setSubTasks(subTasks);
                if (fileLines.length == (i + 2)) {
                    List<Integer> historyViews = historyFromString(fileLines[i + 1]);
                    for (int id : historyViews) {
                        historyManager.add(tasksAll.get(id));
                    }
                    fileBackedTasksManager.save();
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Чтение данных из файла было прервано:" + e.getMessage());
        }
        return fileBackedTasksManager;
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
    public void setTasks(Map<Integer, Task> tasks) {
        super.setTasks(tasks);
        save();
    }

    @Override
    public void setSubTasks(Map<Integer, SubTask> subTasks) {
        super.setSubTasks(subTasks);
        save();
    }

    @Override
    public void setEpics(Map<Integer, Epic> epics) {
        super.setEpics(epics);
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

    private String toString(Task task) {
        TaskType taskType;
        String idEpic = "";
        if (task.getType() == EPIC) {
            taskType = EPIC;
        } else {
            if (task.getType() == SUBTASK) {
                taskType = SUBTASK;
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
        String startTime = taskLines[5];
        Duration duration = Duration.ofMinutes(Integer.parseInt(taskLines[6]));
        switch (type) {
            case TASK:
                task = new Task(id, name, description, status);
                task.setStartTime(startTime);
                task.setDuration(duration);
                break;
            case EPIC:
                task = new Epic(id, name, description, status, new ArrayList<Integer>());
                task.setStartTime(startTime);
                task.setDuration(duration);
                ;
                break;
            case SUBTASK:
                int idEpic = Integer.parseInt(taskLines[5]);
                task = new SubTask(id, name, description, status, idEpic);
                task.setStartTime(startTime);
                task.setDuration(duration);
                break;
        }
        return task;
    }

    private void save() {
        String header = "id,type,name,status,description,epic\n";
        StringBuilder stringForFile = new StringBuilder(header);
        for(Task task : getTasks()) {
            stringForFile.append(toString(task));
        }
        for(Task epic : getTasks()) {
            stringForFile.append(toString(epic));
        }
        for(Task subTask : getTasks()) {
            stringForFile.append(toString(subTask));
        }
        stringForFile.append(historyToString(Managers.getDefaultHistory()));
        try (Writer fileWriter = new FileWriter(file)) {
            fileWriter.write(String.valueOf(stringForFile));
        } catch (IOException e) {
            throw new ManagerSaveException("При cохранении в файл произошла ошибка:" + e.getMessage());
        }
    }

    private static String historyToString(HistoryManager manager) {
        List<Task> historyViews = manager.getHistory();
        StringBuilder lineViews = new StringBuilder("\n");
        for (Task task : historyViews) {
            lineViews.append(task.getId() + ",");
        }
        lineViews.setLength(lineViews.length() - 1);
        return lineViews.toString();
    }

    private static List<Integer> historyFromString(String value) {
        String[] lineHistoryViews = value.split(",");
        List<Integer> historyViews = new ArrayList<>();
        for (int i = lineHistoryViews.length - 1; i >= 0; i--) {
            historyViews.add(Integer.parseInt(lineHistoryViews[i]));
        }
        return historyViews;
    }
}