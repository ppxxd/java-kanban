package http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import http.adapters.InstantAdapter;
import taskmanager.infile.FileBackedTasksManager;
import tasks.EpicTask;
import tasks.SubTask;
import tasks.Task;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class HttpTaskManager extends FileBackedTasksManager {
    private static String url;
    private static KVTaskClient client;
    private static final Gson gson = new GsonBuilder().serializeNulls().
            registerTypeAdapter(Instant.class, new InstantAdapter()).create();
    public HttpTaskManager(String url) throws InterruptedException, IOException {
        HttpTaskManager.url = url;
        client = new KVTaskClient(url);
    }

    @Override
    public void save() {
        var tasks = gson.toJson(getTasksList());
        client.put("/tasks/task", tasks);

        var epics = gson.toJson(getEpicTasksList());
        client.put("/tasks/epic", epics);

        var subtasks = gson.toJson(getSubTasksList());
        client.put("/tasks/subtask", subtasks);

        var history = gson.toJson(getHistory());
        client.put("/tasks/history", history);

        var priorTasks = gson.toJson(getPrioritizedTasks());
        client.put("/tasks", priorTasks);
    }
    public static HttpTaskManager load() throws IOException, InterruptedException {
        HttpTaskManager httpTaskManager = new HttpTaskManager(url);
        var loadTasks = client.load("/tasks/task");
        Type taskType = new TypeToken<ArrayList<Task>>() {}.getType();
        List<Task> tasksList = gson.fromJson(loadTasks, taskType);
        for(Task task : tasksList) {
            httpTaskManager.tasks.put(task.getId(), task);
        }

        var loadEpics = client.load("/tasks/epic");
        Type epicType = new TypeToken<List<EpicTask>>() {}.getType();
        List<EpicTask> epicsList = gson.fromJson(loadEpics, epicType);
        for (EpicTask epic : epicsList) {
            httpTaskManager.epics.put(epic.getId(), epic);
        }

        var loadSubtasks = client.load("/tasks/subtask");
        Type subtaskType = new TypeToken<List<SubTask>>() {}.getType();
        List<SubTask> subtasksList = gson.fromJson(loadSubtasks, subtaskType);
        for (SubTask subtask : subtasksList) {
            httpTaskManager.subtasks.put(subtask.getId(), subtask);
        }

        Type historyType = new TypeToken<List<Task>>() {}.getType();
        ArrayList<Task> historyList = gson.fromJson(client.load("/tasks/history"), historyType);
        for (Task task: historyList) {
            httpTaskManager.historyManager.add(task);
        }

        Type priorTaskType = new TypeToken<Set<Task>>() {}.getType();
        httpTaskManager.prioritizedTasks = gson.fromJson(client.load("/tasks"), priorTaskType);

        return httpTaskManager;
    }
}
