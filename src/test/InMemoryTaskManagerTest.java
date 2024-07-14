package test;

import manager.InMemoryTaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    @Test
    void shouldAddAndFindDifferentTasks() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Task task = new Task("Задача", "Описание задачи", Status.NEW);
        taskManager.createNewTask(task);
        Epic epic = new Epic("Эпик", "Описание эпика'");
        taskManager.createNewEpic(epic);
        SubTask subTask = new SubTask("Подзадача", "Описание подзадачи", epic.getId(), Status.NEW);
        subTask.setEpicId(epic.getId());
        taskManager.createNewSubTask(subTask, subTask.getEpicId());
        assertEquals(taskManager.getTaskById(task.getId()), task, "Задача не найдена!");
        assertEquals(taskManager.getEpicById(epic.getId()), epic, "Эпик не найден!");
        assertEquals(taskManager.getSubTaskById(subTask.getId()), subTask, "Подзадача не найдена!");
    }

    @Test
    void taskShouldDoNotChange() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Task task1 = new Task("Задача", "Описание задачи", Status.NEW);
        taskManager.createNewTask(task1);
        assertEquals(task1.getId(), 4, "Id не совпадают");
        assertEquals(task1.getName(), "Задача", "Имена задач не совпадают");
        assertEquals(task1.getDescription(), "Описание задачи", "Описания не совпадают");
    }

}