package Exceptions;

public class ManagerLoadException extends RuntimeException {
    public ManagerLoadException() {
        System.out.println("Ошибка чтения файла.");
    }
}
