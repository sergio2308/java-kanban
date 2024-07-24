package manager;

import exceptions.ManagerSaveException;
import exceptions.ManagerLoadException;
import tasks.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        super();
        this.file = file;
    }

    private void save() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            bw.write("name,description,id,status,type,epic\n");

            for (Task task : getTasks()) {
                bw.write(task.toString() + "\n");
            }

            for (Epic epic : getEpics()) {
                bw.write(epic.toString() + "\n");
            }
            for (SubTask subTask : getSubTasks()) {
                bw.write(subTask.toString() + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения в файл.");
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fm = new FileBackedTaskManager(file);
        Task task = null;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine();
            while (br.ready()) {
                String line = br.readLine();
                if (!line.isEmpty() || !line.isBlank()) {
                    task = fromString(line);
                }
                switch (task.getTaskType()) {
                    case TASK:
                        tasks.put(task.getId(), task);
                    case EPIC:
                        epics.put(task.getId(), (Epic) task);
                    case SUBTASK:
                        assert task instanceof SubTask;
                        subTasks.put(task.getId(), (SubTask) task);
                }
            }
        } catch (IOException e) {
            throw new ManagerLoadException("Ошибка чтения из файла.");
        }
        return fm;
    }

    public static Task fromString(String value) {
        String[] elements = value.split(",");
        String name = elements[0];
        String description = elements[1];
        int id = Integer.parseInt(elements[2]);
        Status status = Status.valueOf(elements[3]);
        TaskType type = TaskType.valueOf(elements[4]);
        LocalDateTime startTime = LocalDateTime.parse(elements[5]);
        Duration duration = Duration.parse(elements[6]);

        switch (type) {
            case TASK:
                return new Task(name, description, id, status, startTime, duration);
            case EPIC:
                return new Epic(name, description, id, status);
            case SUBTASK:
                int epicId = Integer.parseInt(elements[5]);
                return new SubTask(name, description, id, status, epicId);
            default:
                return null;
        }
    }

    @Override
    public void clearAllTasks() {
        super.clearAllTasks();
        save();
    }

    @Override
    public void clearAllEpics() {
        super.clearAllEpics();
        save();
    }

    @Override
    public void clearAllSubTasks() {
        super.clearAllSubTasks();
        save();
    }

    @Override
    public Task getTaskById(int id) {
        save();
        return super.getTaskById(id);
    }

    @Override
    public Epic getEpicById(int id) {
        save();
        return super.getEpicById(id);
    }

    @Override
    public SubTask getSubTaskById(int id) {
        save();
        return super.getSubTaskById(id);
    }

    @Override
    public Integer createNewTask(Task task) {
        save();
        return super.createNewTask(task);
    }

    @Override
    public Integer createNewEpic(Epic epic) {
        save();
        return super.createNewEpic(epic);
    }

    @Override
    public Integer createNewSubTask(SubTask subTask, int epicId) {
        save();
        return super.createNewSubTask(subTask, epicId);
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic newEpic) {
        super.updateEpic(newEpic);
        save();
    }

    @Override
    public void updateSubTask(SubTask newSubTask, int epicId) {
        super.updateSubTask(newSubTask, epicId);
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
}
