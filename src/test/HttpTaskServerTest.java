package test;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import server.*;
import task.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskServerTest {

    private HttpTaskServer httpTaskServer;
    private KVServer kvServer;
    private HttpClient client;
    private File file;

    @BeforeEach
    public void beginTest() {
        try {
            httpTaskServer = new HttpTaskServer(file);
            httpTaskServer.start();
            kvServer = new KVServer();
            kvServer.start();
            client = HttpClient.newHttpClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
        URI uri = URI.create("http://localhost:" + HttpTaskServer.PORT + "/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println("Запрос провалился.");
        }
        uri = URI.create("http://localhost:" + HttpTaskServer.PORT + "/tasks/subtask");
        request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println("Запрос провалился.");
        }
        uri = URI.create("http://localhost:" + HttpTaskServer.PORT + "/tasks/epic");
        request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println("Запрос провалился.");
        }
    }

    @AfterEach
    public void endTest() {
        httpTaskServer.stop();
        kvServer.stop();
    }

    @Test
    public void getTasksTest() {
        URI uri = URI.create("http://localhost:" + HttpTaskServer.PORT + "/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode(), "Неверный ответ.");
        } catch (IOException | InterruptedException e) {
            System.out.println("Запрос провалился.");
        }
    }

    @Test
    public void getTaskTest() {
        URI uri = URI.create("http://localhost:" + HttpTaskServer.PORT + "/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode(), "Неверный ответ.");
        } catch (IOException | InterruptedException e) {
            System.out.println("Запрос провалился.");
        }
    }

    @Test
    public void getTaskidTest() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new TypeAdapterForLocalDateTime())
                .create();
        Task task = new Task(1, "Задача", "Тест");
        String json = gson.toJson(task);
        URI uri = URI.create("http://localhost:" + HttpTaskServer.PORT + "/tasks/task");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(request, handler);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Сохранение было прервано.");
        }

        uri = URI.create("http://localhost:" + HttpTaskServer.PORT + "/tasks/task?id=1");
        request = HttpRequest.newBuilder().uri(uri).GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode(), "Неверный ответ.");
        } catch (IOException | InterruptedException e) {
            System.out.println("Запрос провалился.");
        }
    }

    @Test
    public void getSubtaskTest() {
        URI uri = URI.create("http://localhost:" + HttpTaskServer.PORT + "/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode(), "Неверный ответ.");
        } catch (IOException | InterruptedException e) {
            System.out.println("Запрос провалился.");
        }
    }

    @Test
    public void getSubtaskidTest() {
        ArrayList<Integer> sIds = new ArrayList<>();
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new TypeAdapterForLocalDateTime())
                .create();
        Epic epic = new Epic(0, "Эпик", "Тест", sIds);
        String json = gson.toJson(epic);
        URI uri = URI.create("http://localhost:" + HttpTaskServer.PORT + "/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(request, handler);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Сохранение было прервано.");
        }

        SubTask subTask = new SubTask(2, "Подзадача", "Тест", Status.NEW, 0);
        json = gson.toJson(subTask);
        uri = URI.create("http://localhost:" + HttpTaskServer.PORT + "/tasks/subtask");
        request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();
        handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(request, handler);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Сохранение было прервано.");
        }

        uri = URI.create("http://localhost:" + HttpTaskServer.PORT + "/tasks/subtask?id=2");
        request = HttpRequest.newBuilder().uri(uri).GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode(), "Неверный ответ.");
        } catch (IOException | InterruptedException e) {
            System.out.println("Запрос провалился.");
        }
    }

    @Test
    public void getEpicTest() {
        URI uri = URI.create("http://localhost:" + HttpTaskServer.PORT + "/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode(), "Неверный ответ.");
        } catch (IOException | InterruptedException e) {
            System.out.println("Запрос провалился.");
        }
    }

    @Test
    public void getEpicidTest() {
        ArrayList<Integer> sIds = new ArrayList<>();
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new TypeAdapterForLocalDateTime())
                .create();
        Epic epic = new Epic(0, "Эпик", "Тест", sIds);
        String json = gson.toJson(epic);
        URI uri = URI.create("http://localhost:" + HttpTaskServer.PORT + "/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(request, handler);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Сохранение было прервано.");
        }

        uri = URI.create("http://localhost:" + HttpTaskServer.PORT + "/tasks/epic?id=1");
        request = HttpRequest.newBuilder().uri(uri).GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode(), "Неверный ответ.");
        } catch (IOException | InterruptedException e) {
            System.out.println("Запрос провалился.");
        }
    }

    @Test
    public void getHistoryTest() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new TypeAdapterForLocalDateTime())
                .create();
        Task task1 = new Task(1,"Задача", "Начальная", Status.NEW);
        String json = gson.toJson(task1);
        URI uri = URI.create("http://localhost:" + HttpTaskServer.PORT + "/tasks/task");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(request, handler);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Сохранение было прервано.");
        }

        uri = URI.create("http://localhost:" + HttpTaskServer.PORT + "/tasks/task?id=1");
        request = HttpRequest.newBuilder().uri(uri).GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println("Запрос провалился.");
        }

        uri = URI.create("http://localhost:" + HttpTaskServer.PORT + "/tasks/history");
        request = HttpRequest.newBuilder().uri(uri).GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode(), "Неверный ответ.");
        } catch (IOException | InterruptedException e) {
            System.out.println("Запрос провалился.");
        }
    }

    @Test
    public void postTaskCreateTest() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new TypeAdapterForLocalDateTime())
                .create();
        Task task1 = new Task(1,"Задача", "Начальная", Status.NEW);
        String json = gson.toJson(task1);
        URI uri = URI.create("http://localhost:" + HttpTaskServer.PORT + "/tasks/task");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(request, handler);
            assertEquals(201, response.statusCode(), "Неверный ответ.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Сохранение было прервано.");
        }
    }

    @Test
    public void postTaskUpdateTest() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new TypeAdapterForLocalDateTime())
                .create();
        Task task1 = new Task(1,"Задача", "Начальная", Status.NEW);
        String json = gson.toJson(task1);
        URI uri = URI.create("http://localhost:" + HttpTaskServer.PORT + "/tasks/task");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(request, handler);
            assertEquals(201, response.statusCode(), "Неверный ответ.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Сохранение было прервано.");
        }
    }

    @Test
    public void postSubtaskTest() {
        ArrayList<Integer> sIds = new ArrayList<>();
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new TypeAdapterForLocalDateTime())
                .create();
        Epic epic = new Epic(0, "Эпик", "Тест", sIds);
        String json = gson.toJson(epic);
        URI uri = URI.create("http://localhost:" + HttpTaskServer.PORT + "/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(request, handler);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Сохранение было прервано.");
        }

        SubTask subTask = new SubTask(2, "Подзадача", "Тест", Status.NEW, 0);
        json = gson.toJson(subTask);
        uri = URI.create("http://localhost:" + HttpTaskServer.PORT + "/tasks/subtask");
        request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();
        handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(request, handler);
            assertEquals(201, response.statusCode(), "Неверный ответ.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Сохранение было прервано.");
        }
    }

    @Test
    public void postSubtaskidTest() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new TypeAdapterForLocalDateTime())
                .create();
        ArrayList<Integer> sIds = new ArrayList<>();
        Epic epic = new Epic(0, "Эпик", "Тест", sIds);
        String json = gson.toJson(epic);
        URI uri = URI.create("http://localhost:" + HttpTaskServer.PORT + "/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(request, handler);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Сохранение было прервано.");
        }

        SubTask subTask = new SubTask(2, "Подзадача", "Тест", null, 0);
        json = gson.toJson(subTask);
        uri = URI.create("http://localhost:" + HttpTaskServer.PORT + "/tasks/subtask?id=1");
        request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();
        handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(request, handler);
            assertEquals(201, response.statusCode(), "Неверный ответ.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Сохранение было прервано.");
        }
    }

    @Test
    public void postEpicCreateTest() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new TypeAdapterForLocalDateTime())
                .create();
        ArrayList<Integer> sIds = new ArrayList<>();
        Epic epic = new Epic(0, "Эпик", "Тест", sIds);
        String json = gson.toJson(epic);
        URI uri = URI.create("http://localhost:" + HttpTaskServer.PORT + "/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(request, handler);
            assertEquals(201, response.statusCode(), "Неверный ответ.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Сохранение было прервано.");
        }
    }

    @Test
    public void postEpicUpdateTest() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new TypeAdapterForLocalDateTime())
                .create();
        ArrayList<Integer> sIds = new ArrayList<>();
        Epic epic = new Epic(0, "Эпик", "Тест", sIds);
        String json = gson.toJson(epic);
        URI uri = URI.create("http://localhost:" + HttpTaskServer.PORT + "/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(request, handler);
            assertEquals(201, response.statusCode(), "Неверный ответ.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Сохранение было прервано.");
        }
    }

    @Test
    public void deleteTaskTest() {
        URI uri = URI.create("http://localhost:" + HttpTaskServer.PORT + "/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(204, response.statusCode(), "Неверный ответ.");
        } catch (IOException | InterruptedException e) {
            System.out.println("Запрос провалился.");
        }
    }

    @Test
    public void deleteTaskidTest() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new TypeAdapterForLocalDateTime())
                .create();
        Task task1 = new Task(1,"Задача", "Начальная", Status.NEW);
        String json = gson.toJson(task1);
        URI uri = URI.create("http://localhost:" + HttpTaskServer.PORT + "/tasks/task");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(request, handler);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Сохранение было прервано.");
        }

        uri = URI.create("http://localhost:" + HttpTaskServer.PORT + "/tasks/task?id=1");
        request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(204, response.statusCode(), "Неверный ответ.");
        } catch (IOException | InterruptedException e) {
            System.out.println("Запрос провалился.");
        }
    }

    @Test
    public void deleteSubtaskTest() {
        URI uri = URI.create("http://localhost:" + HttpTaskServer.PORT + "/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(204, response.statusCode(), "Неверный ответ.");
        } catch (IOException | InterruptedException e) {
            System.out.println("Запрос провалился.");
        }
    }

    @Test
    public void deleteSubtaskidTest() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new TypeAdapterForLocalDateTime())
                .create();
        ArrayList<Integer> sIds = new ArrayList<>();
        Epic epic = new Epic(0, "Эпик", "Тест", sIds);
        String json = gson.toJson(epic);
        URI uri = URI.create("http://localhost:" + HttpTaskServer.PORT + "/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(request, handler);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Сохранение было прервано.");
        }

        SubTask subTask = new SubTask(2, "Подзадача", "Тест", Status.NEW, 0);
        json = gson.toJson(subTask);
        uri = URI.create("http://localhost:" + HttpTaskServer.PORT + "/tasks/subtask");
        request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();
        handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(request, handler);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Сохранение было прервано.");
        }

        uri = URI.create("http://localhost:" + HttpTaskServer.PORT + "/tasks/subtask?id=2");
        request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(204, response.statusCode(), "Неверный ответ.");
        } catch (IOException | InterruptedException e) {
            System.out.println("Запрос провалился.");
        }
    }

    @Test
    public void deleteEpicTest() {
        URI uri = URI.create("http://localhost:" + HttpTaskServer.PORT + "/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(204, response.statusCode(), "Неверный ответ.");
        } catch (IOException | InterruptedException e) {
            System.out.println("Запрос провалился.");
        }
    }

    @Test
    public void deleteEpicidTest() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new TypeAdapterForLocalDateTime())
                .create();
        ArrayList<Integer> sIds = new ArrayList<>();
        Epic epic = new Epic(0, "Эпик", "Тест", sIds);
        String json = gson.toJson(epic);
        URI uri = URI.create("http://localhost:" + HttpTaskServer.PORT + "/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(request, handler);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Сохранение было прервано.");
        }

        uri = URI.create("http://localhost:" + HttpTaskServer.PORT + "/tasks/epic?id=1");
        request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(204, response.statusCode(), "Неверный ответ.");
        } catch (IOException | InterruptedException e) {
            System.out.println("Запрос провалился.");
        }
    }
}