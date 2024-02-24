package http.handlers;

import http.adapters.InstantAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import taskmanager.interfaces.TaskManager;
import tasks.EpicTask;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

public class EpicHandler implements HttpHandler {
    private final Gson gson = new GsonBuilder().registerTypeAdapter(Instant.class, new InstantAdapter()).create();
    private final TaskManager taskManager;

    public EpicHandler(TaskManager taskManager) {
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
                    response = gson.toJson(taskManager.getEpicTasksList());
                    System.out.println("GET EPICS: " + response);
                } else {
                    try {
                        int id = Integer.parseInt(queryAtGet.substring(queryAtGet.indexOf("id=") + 3));
                        EpicTask epic = taskManager.getEpicTaskById(id);
                        if (epic != null) {
                            response = gson.toJson(epic);
                        } else {
                            response = "Эпик с данным id не найден";
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
                    EpicTask epic = gson.fromJson(bodyRequest, EpicTask.class);
                    int id = epic.getId();
                    if (taskManager.getEpicTaskById(id) != null) {
                        taskManager.updateEpicTask(epic);
                        statusCode = 200;
                        response = String.valueOf(id);
                    } else {
                        EpicTask epicCreated = taskManager.createEpicTask(epic);
                        System.out.println("CREATED EPIC: " + epicCreated);
                        int idCreated = epicCreated.getId();
                        statusCode = 201;
                        response = String.valueOf(idCreated);
                    }
                } catch (JsonSyntaxException e) {
                    statusCode = 400;
                    response = "Неверный формат запроса";
                }
                break;
            case "DELETE":
                response = "";
                String queryAtDelete = exchange.getRequestURI().getQuery();
                if (queryAtDelete == null) {
                    taskManager.clearEpicTasks();
                    statusCode = 200;
                } else {
                    try {
                        int id = Integer.parseInt(queryAtDelete.substring(queryAtDelete.indexOf("id=") + 3));
                        taskManager.deleteEpicTaskById(id);
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
