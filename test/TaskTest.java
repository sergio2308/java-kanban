package test;
import tasks.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    public void shouldReturnEquals() {
        int id = 1;
        Task task1 = new Task("Задача_1", "Описание_1", Status.NEW);
        task1.setId(id);
        Task task2 = new Task("Задача_2", "Описание_2", Status.NEW);
        task2.setId(id);
        assertEquals(task1, task2, "Задачи не равны!");
    }
}