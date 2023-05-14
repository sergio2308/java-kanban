import manager.*;
import task.*;

import java.io.File;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        Status status;
        int id = 0;
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Task task1 = manager.addTask(new Task(id, "Прочитать статью",
                "Прочитать статью на tproger"));
        manager.getTaskById(task1.getId());
        Task task2 = manager.addTask(new Task(id, "Купить тетрадь",
                "Купить тетрать для конспектов"));
        manager.getTaskById(task2.getId());
        Epic epic1 = manager.addEpic(new Epic(id, "Новый алгоритм",
                "Разработать новый алгоритм", new ArrayList<>()));
        manager.getEpicById(epic1.getId());
        Epic epic2 = manager.addEpic(new Epic(id, "Выучить испанский",
                "Выучить испанский язык на продвинутом уровне", new ArrayList<>()));
        manager.getEpicById(epic2.getId());
        epic2.setStatus(Status.IN_PROGRESS);
        manager.addSubTask(new SubTask(id, "Купить книгу",
                "Купить книгу про алгоритмы", epic1.getId()));
        
        SubTask subTask1 = manager.getSubTaskById(id);
                SubTask subTask2 = manager.addSubTask(new SubTask(id, "Прочитать книгу",
                "Прочитать книгу про алгоритмы", epic1.getId()));
        subTask2.setStatus(Status.IN_PROGRESS);
        SubTask subTask3 = manager.addSubTask(new SubTask(id, "Выучить алфавит",
                "Выучить испанский алфавит", epic2.getId()));
        manager.getSubTaskById(subTask3.getId());

        System.out.println("Task: " + manager.getTasks());
        System.out.println("Epics: " + manager.getEpics());
        System.out.println("SubTask: " + manager.getSubTasks());

        manager.updateTask(task1 = new Task(task1.getId(), "Прочитать статью",
                "Прочитать статью на tproger"));
        manager.getTaskById(task1.getId());
        task1.setStatus(Status.IN_PROGRESS);
        manager.updateTask(task2 = new Task(task2.getId(), "Прочитать статью",
                "Прочитать статью на tproger"));
        task2.setStatus(Status.DONE);
        manager.updateSubTask(new SubTask(subTask1.getId(), "Купить книгу",
                "Купить книгу про алгоритмы", epic1.getId()));
        subTask1.setStatus(Status.DONE);
        manager.updateSubTask(new SubTask(subTask2.getId(), "Прочитать книгу",
                "Прочитать книгу про алгоритмы", epic1.getId()));
        subTask2.setStatus(Status.DONE);
        manager.updateSubTask(new SubTask(subTask3.getId(), "Выучить алфавит",
                "Выучить испанский алфавит", epic2.getId()));
        subTask3.setStatus(Status.IN_PROGRESS);

        System.out.println("Task: " + manager.getTasks());
        System.out.println("Epics: " + manager.getEpics());
        System.out.println("SubTask: " + manager.getSubTasks());
        System.out.println(manager.historyManager.getHistory());

        manager.removeTaskById(task1.getId());
        manager.removeEpicById(epic1.getId());
        System.out.println(manager.historyManager.getHistory());

        System.out.println("Task: " + manager.getTasks());
        System.out.println("Epics: " + manager.getEpics());
        System.out.println("SubTask: " + manager.getSubTasks());

        System.out.println(manager.historyManager.getHistory());

        TaskManager taskManagerReload = FileBackedTaskManager(new File("resources/task.csv"));

    }
}