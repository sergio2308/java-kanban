import manager.*;
import task.*;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        Status status;
        int id = 0;
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Task firstTask = manager.addTask(new Task(id, "Прочитать статью",
                "Прочитать статью на tproger", Status.NEW));
        manager.getTaskById(firstTask.getId());
        Task secondTask = manager.addTask(new Task(id, "Купить тетрадь",
                "Купить тетрать для конспектов", Status.NEW));
        manager.getTaskById(secondTask.getId());
        Epic firstEpic = manager.addEpic(new Epic(id, "Новый алгоритм",
                "Разработать новый алгоритм", Status.NEW, new ArrayList<>()));
        manager.getEpicById(firstEpic.getId());
        Epic secondEpic = manager.addEpic(new Epic(id, "Выучить испанский",
                "Выучить испаский язык на продвинутом уровне", Status.IN_PROGRESS, new ArrayList<>()));
        manager.getEpicById(secondEpic.getId());
        SubTask firstSubTask = manager.addSubTask(new SubTask(id, "Купить книгу",
                "Купить книгу про алгоритмы", Status.NEW, firstEpic.getId()));
        SubTask secondSubTask = manager.addSubTask(new SubTask(id, "Прочитать книгу",
                "Прочитать книгу про алгоритмы", Status.IN_PROGRESS, firstEpic.getId()));
        SubTask thirdSubTask = manager.addSubTask(new SubTask(id, "Выучить алфавит",
                "Выучить испанский алфавит", Status.NEW, secondEpic.getId()));
        manager.getSubTaskById(thirdSubTask.getId());

        System.out.println("Task: " + manager.tasks);
        System.out.println("Epics: " + manager.epics);
        System.out.println("SubTask: " + manager.subTasks);

        manager.updateTask(firstTask = new Task(firstTask.getId(), "Прочитать статью",
                "Прочитать статью на tproger", Status.IN_PROGRESS));
        manager.getTaskById(firstTask.getId());
        manager.updateTask(secondTask = new Task(secondTask.getId(), "Прочитать статью",
                "Прочитать статью на tproger", Status.DONE));
        manager.updateSubTask(new SubTask(firstSubTask.getId(), "Купить книгу",
                "Купить книгу про алгоритмы", Status.DONE, firstEpic.getId()));
        manager.updateSubTask(new SubTask(secondSubTask.getId(), "Прочитать книгу",
                "Прочитать книгу про алгоритмы", Status.DONE, firstEpic.getId()));
        manager.updateSubTask(new SubTask(thirdSubTask.getId(), "Выучить алфавит",
                "Выучить испанский алфавит", Status.IN_PROGRESS, secondEpic.getId()));

        System.out.println("Task: " + manager.getTasks());
        System.out.println("Epics: " + manager.getEpics());
        System.out.println("SubTask: " + manager.getSubTasks());
        System.out.println(manager.historyManager.getViewedTasks());

        manager.removeTaskById(firstTask.getId());
        manager.removeEpicById(firstEpic.getId());
        System.out.println(manager.historyManager.getViewedTasks());

        System.out.println("Task: " + manager.tasks);
        System.out.println("Epics: " + manager.epics);
        System.out.println("SubTask: " + manager.subTasks);

        System.out.println(manager.historyManager.getViewedTasks());

    }
}