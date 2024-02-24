package http.handlers;

import http.adapters.InstantAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import taskmanager.interfaces.TaskManager;
import tasks.SubTask;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

public class SubTaskHandler implements HttpHandler {
    private final Gson gson = new GsonBuilder().registerTypeAdapter(Instant.class, new InstantAdapter()).create();
    private final TaskManager taskManager;

    public SubTaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        int statusCode;
        String response;

        String method = exchange.getRequestMethod();

        switch (method) {
            case "GET":
                String queryAtGet = exchange.getRequestURI().getQuery();
                if (queryAtGet == null) {
                    statusCode = 200;
                    response = gson.toJson(taskManager.getSubTasksList());
                } else {
                    try {
                        int id = Integer.parseInt(queryAtGet.substring(queryAtGet.indexOf("id=") + 3));
                        SubTask subtask = taskManager.getSubTaskById(id);
                        if (subtask != null) {
                            response = gson.toJson(subtask);
                        } else {
                            response = "Подзадача с данным ID не найдена";
                        }
                        statusCode = 200;
                    } catch (StringIndexOutOfBoundsException e) {
                        statusCode = 400;
                        response = "В запросе отсутствует необходимый параметр id";
                    } catch (NumberFormatException e) {
                        statusCode = 400;
                        response = "Неверный формат id";
                    }
                }
                break;
            case "POST":
                String bodyRequest = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                try {
                    SubTask subtask = gson.fromJson(bodyRequest, SubTask.class);
                    int id = subtask.getId();
                    if (taskManager.getSubTaskById(id) != null) {
                        taskManager.updateSubTask(subtask);
                        statusCode = 200;
                        response = String.valueOf(id);
                    } else {
                        System.out.println("CREATED");
                        SubTask subtaskCreated = taskManager.createSubTask(subtask);
                        System.out.println("CREATED SUBTASK: " + subtaskCreated);
                        int idCreated = subtaskCreated.getId();
                        statusCode = 201;
                        response = String.valueOf(idCreated);
                    }
                } catch (JsonSyntaxException e) {
                    response = "Неверный формат запроса";
                    statusCode = 400;
                }
                break;
            case "DELETE":
                response = "";
                String queryAtDelete = exchange.getRequestURI().getQuery();
                if (queryAtDelete == null) {
                    taskManager.clearSubTasks();
                    statusCode = 200;
                } else {
                    try {
                        int id = Integer.parseInt(queryAtDelete.substring(queryAtDelete.indexOf("id=") + 3));
                        taskManager.deleteSubTaskById(id);
                        statusCode = 200;
                    } catch (StringIndexOutOfBoundsException e) {
                        statusCode = 400;
                        response = "В запросе отсутствует необходимый параметр id";
                    } catch (NumberFormatException e) {
                        statusCode = 400;
                        response = "Неверный формат id";
                    }
                }
                break;
            default:
                statusCode = 400;
                response = "Некорректный запрос";
        }

        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, 0);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
