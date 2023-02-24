import java.util.ArrayList;
import manager.Manager;
import task.Epic;
import task.Status;
import task.SubTask;
import task.Task;

public class Main {

    public static void main(String[] args) {
        int id = 0;
        Manager manager = new Manager();

        Task firstTask = manager.addTask(new Task(id, "Прочитать статью",
                "Прочитать статью на tproger", Status.NEW));
        Task secondTask = manager.addTask(new Task(id, "Купить тетрадь",
                "Купить тетрать для конспектов", Status.NEW));
        Epic firstEpic = manager.addEpic(new Epic(id, "Новый алгоритм",
                "Разработать новый алгоритм", Status.NEW, new ArrayList<>()));
        Epic secondEpic = manager.addEpic(new Epic(id, "Выучить испанский",
                "Выучить испаский язык на продвинутом уровне", Status.IN_PROGRESS, new ArrayList<>()));
        SubTask firstSubTask = manager.addSubTask(new SubTask(id, "Купить книгу",
                "Купить книгу про алгоритмы", Status.NEW, firstEpic.getId()));
        SubTask secondSubTask = manager.addSubTask(new SubTask(id, "Прочитать книгу",
                "Прочитать книгу про алгоритмы", Status.IN_PROGRESS, firstEpic.getId()));
        SubTask thirdSubTask = manager.addSubTask(new SubTask(id, "Выучить алфавит",
                "Выучить испанский алфавит", Status.NEW, secondEpic.getId()));

        System.out.println("Task: " + manager.tasks);
        System.out.println("Epics: " + manager.epics);
        System.out.println("SubTask: " + manager.subTasks);

        manager.updateTask(firstTask = new Task(firstTask.getId(), "Прочитать статью",
                "Прочитать статью на tproger", Status.IN_PROGRESS));
        manager.updateTask(secondTask = new Task(secondTask.getId(), "Прочитать статью",
                "Прочитать статью на tproger", Status.DONE));
        manager.updateSubTask(new SubTask(firstSubTask.getId(), "Купить книгу",
                "Купить книгу про алгоритмы", Status.DONE, firstEpic.getId()));
        manager.updateSubTask(new SubTask(secondSubTask.getId(), "Прочитать книгу",
                "Прочитать книгу про алгоритмы", Status.DONE, firstEpic.getId()));
        manager.updateSubTask(new SubTask(thirdSubTask.getId(), "Выучить алфавит",
                "Выучить испанский алфавит", Status.IN_PROGRESS, secondEpic.getId()));

        System.out.println("Task: " + manager.getTasks()); //TODO попробовать вывод через метод в менеджере
        System.out.println("Epics: " + manager.getEpics());
        System.out.println("SubTask: " + manager.getSubTasks());

        manager.removeTaskById(firstTask.getId());
        manager.removeEpicById(firstEpic.getId());

        System.out.println("Task: " + manager.tasks);
        System.out.println("Epics: " + manager.epics);
        System.out.println("SubTask: " + manager.subTasks);
    }
}
