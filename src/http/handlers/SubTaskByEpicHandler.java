package http.handlers;

import http.adapters.InstantAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import taskmanager.interfaces.TaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.time.Instant;

public class SubTaskByEpicHandler implements HttpHandler {
    private final Gson gson = new GsonBuilder().registerTypeAdapter(Instant.class, new InstantAdapter()).create();
    private final TaskManager taskManager;

    public SubTaskByEpicHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        int statusCode = 400;
        String response;
        String method = httpExchange.getRequestMethod();
        String path = String.valueOf(httpExchange.getRequestURI());

        System.out.println("Обрабатывается запрос " + path + " с методом " + method);

        switch (method) {
            case "GET":
                String queryAtGet = httpExchange.getRequestURI().getQuery();
                try {
                    int id = Integer.parseInt(queryAtGet.substring(queryAtGet.indexOf("id=") + 3));
                    statusCode = 200;
                    response = gson.toJson(taskManager.getEpicsSubTaskList(taskManager.getEpicTaskById(id)));
                } catch (StringIndexOutOfBoundsException | NullPointerException e) {
                    response = "В запросе отсутствует необходимый параметр - id";
                } catch (NumberFormatException e) {
                    response = "Неверный формат id";
                }
                break;
            default:
                response = "Некорректный запрос";
        }

        httpExchange.getResponseHeaders().set("Content-Type", "application/json");
        httpExchange.sendResponseHeaders(statusCode, 0);

        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
