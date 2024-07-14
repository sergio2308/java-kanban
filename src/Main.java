import manager.InMemoryTaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        Task task1 = new Task("Покупка продуктов", "Помидоры, молоко, хлеб, чай", Status.NEW);
        Task task2 = new Task("Оправить посылку", "Сходить на почту и отправить посылку", Status.NEW);
        Epic epic1 = new Epic("Выучить испанский", "Выучить испанский язык");

//        SubTask subTask1 = new SubTask("Словарь", "Купить словарь испанского языка", epic1.getId(),
//                Status.NEW);
//        SubTask subTask2 = new SubTask("Преподаватель", "Найти преподавателя испанского языка",
//                epic1.getId(), Status.NEW);
//        Epic epic2 = new Epic("Отпуск", "Подготовиться к отпуску");
//        SubTask subTask3 = new SubTask("Билет", "Купить билет", epic2.getId(), Status.NEW);

        taskManager.createNewTask(task1);
        taskManager.createNewTask(task2);
        System.out.println(taskManager.getTasks().toString());
        taskManager.createNewEpic(epic1);
        System.out.println(epic1.toString());
        SubTask subTask1 = new SubTask("Словарь", "Купить словарь испанского языка", epic1.getId(),
                Status.NEW);
        SubTask subTask2 = new SubTask("Преподаватель", "Найти преподавателя испанского языка",
                epic1.getId(), Status.NEW);
        subTask1.setEpicId(epic1.getId());
        subTask2.setEpicId(epic1.getId());
        taskManager.createNewSubTask(subTask1, subTask1.getEpicId());
        taskManager.createNewSubTask(subTask2, subTask1.getEpicId());
        System.out.println(taskManager.getSubTasks().toString());
        subTask1.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubTask(subTask1, epic1.getId());
        taskManager.updateEpic(epic1);
        System.out.println(epic1.toString());
        System.out.println(taskManager.getSubTaskById(subTask1.getId()));
        System.out.println(taskManager.getHistory());
    }
}