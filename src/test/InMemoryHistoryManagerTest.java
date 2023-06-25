package test;

import manager.HistoryManager;
import manager.InMemoryHistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Status;
import task.Task;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private static HistoryManager historyManager;

    @BeforeEach
    public void newHistoryManager() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    public void addTaskToHistory() {
        assertEquals(0, historyManager.getHistory().size(), "История не пустая.");
        Task task = new Task(1, "Задача", "Тест", Status.NEW);
        historyManager.add(task);
        assertEquals(1, historyManager.getHistory().size(), "Неверный размер списка!");
        historyManager.add(task);
        assertEquals(1, historyManager.getHistory().size(), "Неверный размер списка!");
    }

    @Test
    void removeFromHistory() {
        assertEquals(0, historyManager.getHistory().size(), "История не пустая.");
        assertEquals(0, historyManager.getHistory().size(), "История не пустая.");
        Task task1 = new Task(1, "Задача1", "Тест", Status.NEW);
        Task task2 = new Task(2, "Задача2", "Тест", Status.NEW);
        Task task3 = new Task(3, "Задача3", "Тест", Status.NEW);
        Task task4 = new Task(4, "Задача3", "Тест", Status.NEW);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(task4);
        assertEquals(4, historyManager.getHistory().size(), "Неверный размер списка!");
        int id1 = historyManager.getHistory().get(0).getId();
        int id2 = historyManager.getHistory().get(1).getId();
        historyManager.remove(task1);
        assertEquals(3, historyManager.getHistory().size(), "Неверный размер списка!");
        assertEquals(id2, historyManager.getHistory().get(0).getId(), "Задачи не совпадают");
        int id3 = historyManager.getHistory().get(2).getId();
        historyManager.remove(task2);
        assertEquals(2, historyManager.getHistory().size(), "Неверный размер списка!");
        assertEquals(id3, historyManager.getHistory().get(1).getId(), "Задачи не совпадают");
        id1 = historyManager.getHistory().get(0).getId();
        historyManager.remove(task3);
        assertEquals(1, historyManager.getHistory().size(), "Неверный размер списка!");
        assertEquals(id1, historyManager.getHistory().get(0).getId(), "Задачи не совпадают");
        historyManager.remove(task1);
        assertEquals(0, historyManager.getHistory().size(), "Неверный размер списка!");
    }
}