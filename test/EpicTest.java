package test;
import tasks.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    public void shouldReturnEquals() {
        int id = 1;
        Epic epic1 = new Epic("Эпик 1", "Описание 1");
        epic1.setId(id);
        Epic epic2 = new Epic("Эпик 2", "Описание 2");
        epic2.setId(id);
        assertEquals(epic1, epic2, "Эпики не равны!");
    }
}