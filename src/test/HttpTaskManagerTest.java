package test;

import manager.*;
import server.*;
import task.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskManagerTest  {

    private HttpTaskServer httpTaskServer;
    private KVServer kvServer;
    private HttpTaskManager manager;
    private File file;

    @BeforeEach
    public void beginTest() {
        try {
            httpTaskServer = new HttpTaskServer(file);
            httpTaskServer.start();
            kvServer = new KVServer();
            kvServer.start();
            manager = new HttpTaskManager("http://localhost:" + KVServer.PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    public void endTest() {
        httpTaskServer.stop();
        kvServer.stop();
    }

    @Test
    public void createTest() {
        assertEquals(0, manager.getTasks().size(), "Неверное количество задач.");
        assertEquals(0, manager.getSubTasks().size(), "Неверное количество задач.");
        assertEquals(0, manager.getEpics().size(), "Неверное количество задач.");
        Task task1 = new Task(1,"Задача", "Начальная", Status.NEW);
        manager.addTask(task1);
        ArrayList<Integer> sIds = new ArrayList<>();
        Epic epic = new Epic(0, "Эпик", "Тест", sIds);
        SubTask subTask = new SubTask(2, "Подзадача", "Тест", Status.NEW, 0);
        manager.addSubTask(subTask);
        assertEquals(1, manager.getTasks().size(), "Неверное количество задач.");
        assertEquals(1, manager.getSubTasks().size(), "Неверное количество задач.");
        assertEquals(1, manager.getEpics().size(), "Неверное количество задач.");
    }

    @Test
    public void getTest() {
        Task task = new Task(0, "Задача", "Тест", null, LocalDateTime.now());
        manager.addTask(task);
        int idTask = task.getId();
        ArrayList<Integer> sIds = new ArrayList<>();
        Epic epic = new Epic(0, "Эпик", "Тест", sIds);
        SubTask subTask = new SubTask(1, "Подзадача", "Тест", null, 0);
        manager.addSubTask(subTask);
        int idSubTask = subTask.getId();
        assertEquals(idTask, manager.getTaskById(idTask).getId(), "Задача отличается.");
        assertEquals(idSubTask, manager.getSubTaskById(idSubTask).getId(), "Подзадача отличается.");
        assertEquals(1, manager.getEpicById(1).getId(), "Эпик отличается.");
        assertEquals(1, manager.getTasks().size(), "Неверное количество задач.");
        assertEquals(1, manager.getSubTasks().size(), "Неверное количество задач.");
        assertEquals(1, manager.getEpics().size(), "Неверное количество задач.");
    }

    @Test
    public void removeTest() {
        Task task = new Task(1, "Задача", "Тест", null, LocalDateTime.now());
        manager.addTask(task);
        ArrayList<Integer> sIds = new ArrayList<>();
        int idTask = task.getId();
        Epic epic = new Epic(0, "Эпик", "Тест", Status.NEW, sIds);
        SubTask subTask = new SubTask(2,"Подзадача", "Тест", null, 0);
        manager.addSubTask(subTask);
        int idSubTask = subTask.getId();
        assertEquals(1, manager.getTasks().size(), "Неверное количество задач.");
        assertEquals(1, manager.getSubTasks().size(), "Неверное количество задач.");
        assertEquals(1, manager.getEpics().size(), "Неверное количество задач.");
        manager.removeTasks();
        manager.removeSubTasks();
        manager.removeEpics();
        assertEquals(0, manager.getTasks().size(), "Неверное количество задач.");
        assertEquals(0, manager.getSubTasks().size(), "Неверное количество задач.");
        assertEquals(0, manager.getEpics().size(), "Неверное количество задач.");
        manager.updateTask(task);
        manager.updateEpicStatus(epic);
        manager.updateSubTask(subTask);
        assertEquals(1, manager.getTasks().size(), "Неверное количество задач.");
        assertEquals(1, manager.getSubTasks().size(), "Неверное количество задач.");
        assertEquals(1, manager.getEpics().size(), "Неверное количество задач.");
        manager.removeTaskById(idTask);
        manager.removeSubTaskById(idSubTask);
        manager.removeEpicById(epic.getId());
        assertEquals(0, manager.getTasks().size(), "Неверное количество задач.");
        assertEquals(0, manager.getSubTasks().size(), "Неверное количество задач.");
        assertEquals(0, manager.getEpics().size(), "Неверное количество задач.");
    }

    @Test
    public void updateTest() {
        Task task = new Task(0,"Задача", "Тест", null, LocalDateTime.now());
        manager.addTask(task);
        int idTask = task.getId();
        ArrayList<Integer> sIds = new ArrayList<>();
        Epic epic = new Epic(0, "Эпик", "Тест", sIds);
        SubTask subTask = new SubTask(2,"Подзадача", "Тест", null, 0);
        manager.addSubTask(subTask);
        int idSubTask = subTask.getId();
        assertEquals("Задача", manager.getTaskById(idTask).getName(), "Задача отличается.");
        assertEquals("Подзадача", manager.getSubTaskById(idSubTask).getName(), "Подзадача отличается.");
        assertEquals("Эпик", manager.getEpicById(epic.getId()).getName(), "Эпик отличается.");
        Task taskNew = new Task(task.getId(), task.getName(),"Задача новая",
                task.getStatus(), task.getStartTime());
        Epic epicNew = new Epic(epic.getId(), epic.getName(), "Эпик новый",
                epic.getStatus(), epic.getSubTasksIds());
        SubTask subTaskNew = new SubTask(subTask.getId(), subTask.getName(), "Подзадача новая", subTask.getStatus(),
                epicNew.getId());
        manager.updateTask(taskNew);
        manager.updateEpicStatus(epicNew);
        manager.updateSubTask(subTaskNew);
        assertEquals("Задача новая", manager.getTaskById(idTask).getName(), "Задача не обновилась.");
        assertEquals("Подзадача новая", manager.getSubTaskById(idSubTask).getName(),
                "Подзадача не обновилась.");
        assertEquals("Эпик новый", manager.getEpicById(epic.getId()).getName(), "Эпик не обновился.");
    }
}
