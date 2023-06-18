package test;

import manager.HistoryManager;
import manager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.SubTask;
import task.Task;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager>{

    public HistoryManager historyManager = new InMemoryHistoryManager();
    public InMemoryTaskManager taskManager = new InMemoryTaskManager((InMemoryHistoryManager) historyManager);

    @Test
    void addTask() {
        Task task = new Task(1, "Задача", "Тест");
        taskManager.addTask(task);
        int id = task.getId();
        assertEquals(1, taskManager.getTasks().size(), "Неверное количество задач.");
        assertEquals(id, taskManager.getTaskById(id).getId(), "Задачи не совпадают.");
    }

    @Test
    void addNewEpicAndSubTask() {
        SubTask subTask = new SubTask(2, "Подзадача", "Тест", Status.NEW, 0);
        taskManager.addSubTask(subTask);
        ArrayList<Integer> sIds = new ArrayList<>();
        sIds.add(2);
        Epic epic = new Epic(0, "Эпик", "Тест", sIds);
        int idEpic = epic.getId();
        int idSubTask = subTask.getId();
        assertEquals(1, taskManager.getSubTasks().size(), "Неверное количество задач.");
        assertEquals(idSubTask, taskManager.getSubTaskById(idSubTask).getId(), "Задачи не совпадают.");
        assertEquals(1, taskManager.getTasks().size(), "Неверное количество задач.");
        assertEquals(idEpic, taskManager.getEpicById(idEpic).getId(), "Задачи не совпадают.");
    }

    @Test
    void getTaskById() {
        Task task = new Task(1, "Задача", "Тест", Status.NEW);
        assertNull(taskManager.getTaskById(1), "Задачи в списке нет.");
        taskManager.addTask(task);
        int id = task.getId();
        assertEquals(id, taskManager.getTaskById(id).getId(), "Вернулась другая задача");
        assertNull(taskManager.getTaskById(id + 1), "Задачи в списке нет.");
    }

    @Test
    public void getSubTaskAndEpicById() {
        SubTask subTask = new SubTask(2, "Подзадача", "Тест", Status.NEW, 0);
        taskManager.addSubTask(subTask);
        ArrayList<Integer> sIds = new ArrayList<>();
        sIds.add(2);
        Epic epic = new Epic(0, "Эпик", "Тест", sIds);
        assertNull(taskManager.getEpicById(0), "Эпика в списке нет.");
        assertNull(taskManager.getSubTaskById(2), "Подзадачи в списке нет.");
        int idEpic = epic.getId();
        taskManager.addSubTask(subTask);
        int idSubTask = subTask.getId();
        assertEquals(idEpic, taskManager.getEpicById(idEpic).getId(), "Вернулся другой эпик");
        assertEquals(idSubTask, taskManager.getSubTaskById(idSubTask).getId(), "Вернулась другая подзадача");
        assertNull(taskManager.getEpicById(idEpic + 1), "Эпика в списке нет.");
        assertNull(taskManager.getSubTaskById(idSubTask + 1), "Подзадачи в списке нет.");
    }

    @Test
    public void updatingTask() {
        Task task1 = new Task(1,"Задача", "Начальная", Status.NEW);
        taskManager.addTask(task1);
        int id = task1.getId();
        Task task2 = new Task(id, "Задача", "Обновлённая",
                task1.getStatus());
        assertEquals(id, taskManager.getTaskById(id).getId(), "Задачи не совпадают.");
        assertEquals("Начальная", taskManager.getTaskById(id).getDescription(), "Задачи не совпадают.");
        taskManager.updateTask(task2);
        assertEquals(id, taskManager.getTaskById(id).getId(), "Задачи не совпадают.");
        assertEquals("Обновлённая", taskManager.getTaskById(id).getDescription(), "Задача не обновилась.");
        Task task3 = new Task(id + 1, "Задача", "Ошибочная", Status.NEW);
        taskManager.updateTask(task3);
        assertEquals(id + 1, taskManager.getTaskById(id + 1).getId(), "Задачи не совпадают.");
        assertEquals("Ошибочная", taskManager.getTaskById(id + 1).getDescription(),
                "Задача не обновилась.");
    }

    @Test
    void updatingSubTaskAndEpic() {
        SubTask subTask = new SubTask(2, "Подзадача", "Тест", Status.NEW, 0);
        taskManager.addSubTask(subTask);
        ArrayList<Integer> sIds = new ArrayList<>();
        sIds.add(2);
        Epic epic = new Epic(0, "Эпик", "Тест", sIds);
        int idEpic = epic.getId();
        int idSubTask = subTask.getId();
        assertEquals(idSubTask, taskManager.getSubTaskById(idSubTask).getId(), "Задачи не совпадают.");
        assertEquals(idEpic, taskManager.getEpicById(idEpic).getId(), "Задачи не совпадают.");
        assertEquals("Начальная", taskManager.getSubTaskById(idSubTask).getDescription(),
                "Задачи не совпадают.");
        assertEquals("Начальная", taskManager.getEpicById(idEpic).getDescription(),
                "Задачи не совпадают.");
        Epic epic2 = new Epic(idEpic, "Эпик", "Обновлённая",
                epic.getStatus(), epic.getSubTasksIds());
        SubTask subTask2 = new SubTask(idSubTask, "Подзадача", "Обновлённая",
                subTask.getStatus(), idEpic);
        taskManager.updateTask(epic2);
        taskManager.updateSubTask(subTask2);
        assertEquals(idSubTask, taskManager.getSubTaskById(idSubTask).getId(), "Задачи не совпадают.");
        assertEquals(idEpic, taskManager.getEpicById(idEpic).getId(), "Задачи не совпадают.");
        assertEquals("Обновлённая", taskManager.getSubTaskById(idSubTask).getDescription(),
                "Задача не обновилась.");
        assertEquals("Обновлённая", taskManager.getEpicById(idEpic).getDescription(),
                "Задача не обновилась.");
        Epic epic3 = new Epic(3, "Эпик", "Ошибочная",
                Status.NEW, new ArrayList<>());
        SubTask subTask3 = new SubTask(4, "Подзадача", "Ошибочная",
                Status.NEW, 3);
        taskManager.updateTask(epic3);
        epic3.addSubTask(4);
        taskManager.updateSubTask(subTask3);
        taskManager.updateTask(epic3);
        assertEquals(4, taskManager.getSubTaskById(4).getId(), "Задачи не совпадают.");
        assertEquals(3, taskManager.getEpicById(3).getId(), "Задачи не совпадают.");
        assertEquals("Ошибочная", taskManager.getSubTaskById(4).getDescription(),
                "Задача не обновилась.");
        assertEquals("Ошибочная", taskManager.getEpicById(3).getDescription(),
                "Задача не обновилась.");
    }

    @Test
    void removeTaskById() {
        Task task = new Task(1,"Задача", "Тест",Status.NEW);
        taskManager.addTask(task);
        int id = task.getId();
        taskManager.removeTaskById(1);
        assertEquals(id, taskManager.getTaskById(id).getId(), "Задачи не совпадают.");
        assertEquals(1, taskManager.getTasks().size(), "Неверное количество.");
        taskManager.removeSubTaskById(id + 1);
        assertEquals(1, taskManager.getTasks().size(), "Неверное количество.");
        taskManager.removeTaskById(id);
        assertEquals(0, taskManager.getTasks().size(), "Неверное количество.");
    }

    @Test
    public void deleteSubTaskAndEpicById() {
        SubTask subTask = new SubTask(2, "Подзадача", "Тест", Status.NEW, 0);
        taskManager.addSubTask(subTask);
        ArrayList<Integer> sIds = new ArrayList<>();
        sIds.add(2);
        Epic epic = new Epic(0, "Эпик", "Тест", sIds);
        int idEpic = epic.getId();
        int idSubTask = subTask.getId();
        taskManager.removeTaskById(1);
        taskManager.removeEpicById(1);
        assertEquals(idEpic, taskManager.getEpicById(idEpic).getId(), "Задачи не совпадают.");
        assertEquals(idSubTask, taskManager.getSubTaskById(idSubTask).getId(), "Задачи не совпадают.");
        assertEquals(1, taskManager.getTasks().size(), "Неверное количество задач.");
        assertEquals(1, taskManager.getSubTasks().size(), "Неверное количество задач.");
        taskManager.removeTaskById(idSubTask + 1);
        taskManager.removeEpicById(idEpic + 1);
        assertEquals(1, taskManager.getTasks().size(), "Неверное количество задач.");
        assertEquals(1, taskManager.getSubTasks().size(), "Неверное количество задач.");
        taskManager.removeSubTaskById(idSubTask);
        taskManager.removeEpicById(idEpic);
        assertEquals(0, taskManager.getTasks().size(), "Неверное количество задач.");
        assertEquals(0, taskManager.getSubTasks().size(), "Неверное количество задач.");
    }
}