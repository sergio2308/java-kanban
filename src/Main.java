import manager.*;
import task.*;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        Status status;
        int id = 0;
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Task firstTask = manager.addTask(new Task(id, "Прочитать статью",
                "Прочитать статью на tproger"));
        manager.getTaskById(firstTask.getId());
        Task secondTask = manager.addTask(new Task(id, "Купить тетрадь",
                "Купить тетрать для конспектов"));
        manager.getTaskById(secondTask.getId());
        Epic firstEpic = manager.addEpic(new Epic(id, "Новый алгоритм",
                "Разработать новый алгоритм", new ArrayList<>()));
        manager.getEpicById(firstEpic.getId());
        Epic secondEpic = manager.addEpic(new Epic(id, "Выучить испанский",
                "Выучить испанский язык на продвинутом уровне", new ArrayList<>()));
        manager.getEpicById(secondEpic.getId());
        secondEpic.setStatus(Status.IN_PROGRESS);
        SubTask firstSubTask = manager.addSubTask(new SubTask(id, "Купить книгу",
                "Купить книгу про алгоритмы", firstEpic.getId()));
        SubTask secondSubTask = manager.addSubTask(new SubTask(id, "Прочитать книгу",
                "Прочитать книгу про алгоритмы", firstEpic.getId()));
        secondSubTask.setStatus(Status.IN_PROGRESS);
        SubTask thirdSubTask = manager.addSubTask(new SubTask(id, "Выучить алфавит",
                "Выучить испанский алфавит", secondEpic.getId()));
        manager.getSubTaskById(thirdSubTask.getId());

        System.out.println("Task: " + manager.getTasks());
        System.out.println("Epics: " + manager.getEpics());
        System.out.println("SubTask: " + manager.getSubTasks());

        manager.updateTask(firstTask = new Task(firstTask.getId(), "Прочитать статью",
                "Прочитать статью на tproger"));
        manager.getTaskById(firstTask.getId());
        firstTask.setStatus(Status.IN_PROGRESS);
        manager.updateTask(secondTask = new Task(secondTask.getId(), "Прочитать статью",
                "Прочитать статью на tproger"));
        secondTask.setStatus(Status.DONE);
        manager.updateSubTask(new SubTask(firstSubTask.getId(), "Купить книгу",
                "Купить книгу про алгоритмы", firstEpic.getId()));
        firstSubTask.setStatus(Status.DONE);
        manager.updateSubTask(new SubTask(secondSubTask.getId(), "Прочитать книгу",
                "Прочитать книгу про алгоритмы", firstEpic.getId()));
        secondSubTask.setStatus(Status.DONE);
        manager.updateSubTask(new SubTask(thirdSubTask.getId(), "Выучить алфавит",
                "Выучить испанский алфавит", secondEpic.getId()));
        thirdSubTask.setStatus(Status.IN_PROGRESS);

        System.out.println("Task: " + manager.getTasks());
        System.out.println("Epics: " + manager.getEpics());
        System.out.println("SubTask: " + manager.getSubTasks());
        System.out.println(manager.historyManager.getHistory());

        manager.removeTaskById(firstTask.getId());
        manager.removeEpicById(firstEpic.getId());
        System.out.println(manager.historyManager.getHistory());

        System.out.println("Task: " + manager.getTasks());
        System.out.println("Epics: " + manager.getEpics());
        System.out.println("SubTask: " + manager.getSubTasks());

        System.out.println(manager.historyManager.getHistory());

    }
}