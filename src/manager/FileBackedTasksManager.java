package manager;

import exceptions.ManagerSaveException;
import task.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final File file;
    private boolean load;

    public FileBackedTasksManager(File file) {
        super(new InMemoryHistoryManager());
        this.file = file;
    }

    public static void main(String[] args) {

        String pathProjectDir = System.getProperty("user.dir");
        File pathFile = new File(pathProjectDir + File.separator + "resources", "tasks.csv");

        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(pathFile);

        Task task1 = new Task(1, "Обычная задача", "Описание обычной задачи", Status.NEW);
        fileBackedTasksManager.addTask(task1);

        Task task2 = new Task(2, "Обычная задача 2", "Описание 2", Status.NEW);
        fileBackedTasksManager.addTask(task2);
        ArrayList<Integer> sIds = new ArrayList<>();
        sIds.add(4);
        sIds.add(5);
        sIds.add(6);
        Epic epic1 = new Epic(3, "Эпическая задача", "Описание эпической задачи", sIds);
        SubTask subTask1 = new SubTask(4, "Подзадача1", "Описание Подзадачи 1", 3);
        SubTask subTask2 = new SubTask(5, "Подзадача2", "Описание Подзадачи 2",3);
        SubTask subTask3 = new SubTask(6, "Подзадача3", "Описание Подзадачи 3", 3);
        fileBackedTasksManager.addSubTask(subTask1);
        fileBackedTasksManager.addSubTask(subTask2);
        fileBackedTasksManager.addSubTask(subTask3);

        ArrayList<Integer> sIds2 = new ArrayList<>();
        sIds2.add(4);
        sIds2.add(5);
        Epic epic2 = new Epic(7, "Эпик 2", "Описание эпика2", sIds2);
        fileBackedTasksManager.addEpic(epic2);

        fileBackedTasksManager.getTaskById(task2.getId());
        fileBackedTasksManager.getEpicById(epic2.getId());
        fileBackedTasksManager.getSubTaskById(subTask2.getId());
        fileBackedTasksManager.getEpicById(epic2.getId());

        FileBackedTasksManager fileBackedTasksManager2 = FileBackedTasksManager.loadFromFile(pathFile);

        fileBackedTasksManager2.getEpicById(epic1.getId());
        fileBackedTasksManager2.getSubTaskById(subTask1.getId());
        fileBackedTasksManager2.getTaskById(task1.getId());
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
                if (fileLines.length > 1) {
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
                    }
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
            throw new ManagerSaveException("Чтение данных из файла было прервано.");
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

    private void save() {
        String header = "id,type,name,status,description,epic\n";
        StringBuilder stringForFile = new StringBuilder(header);
        for(Task task : getTasks()) {
            stringForFile.append(toString(task));
        }
        for(Task epic : getEpics()) {
            stringForFile.append(toString(epic));
        }
        for(Task subTask : getSubTasks()) {
            stringForFile.append(toString(subTask));
        }
        stringForFile.append(historyToString(Managers.getDefaultHistory()));
        try (Writer fileWriter = new FileWriter(file)) {
            fileWriter.write(String.valueOf(stringForFile));
        } catch (IOException e) {
            throw new ManagerSaveException("При cохранении в файл произошла ошибка.");
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
