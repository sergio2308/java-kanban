package exceptions;

public class ManagerSaveException extends RuntimeException {
    public ManagerSaveException() {
        System.out.println("Ошибка сохранения файла.");
    }
}
