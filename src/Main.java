import manager.*;
import task.*;

import java.io.File;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        Status status;
        int id = 0;

        String path = System.getProperty("user.dir");
        File pathFile = new File(path + File.separator + "resources", "tasks.csv");

        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(pathFile);
        InMemoryTaskManager manager = new InMemoryTaskManager();
        String pathProjectDir = System.getProperty("user.dir");

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
        //Разрешите, пожалуйста, пока оставить эти старые тесты в коде закомментированными
        //Я отстаю от группы и хотел кое-что потом еще проверить, для себя

        /*Task task1 = manager.addTask(new Task(id, "Прочитать статью",
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
*/
        TaskManager taskManagerReload = new FileBackedTasksManager(new File("resources/task.csv"));
        System.out.println("Task: " + manager.getTasks());
        System.out.println("Epics: " + manager.getTasks());
        System.out.println("SubTask: " + manager.getTasks());

        System.out.println(manager.historyManager.getHistory());
    }
}