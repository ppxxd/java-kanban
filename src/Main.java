import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import http.KVServer;
import http.adapters.InstantAdapter;
import taskmanager.Managers;
import taskmanager.inmemory.InMemoryTaskManager;
import taskmanager.interfaces.TaskManager;
import tasks.*;

import java.time.Instant;

public class Main {

    public static void main(String[] args) {
        try {
            Gson gson = new GsonBuilder().serializeNulls().
                    registerTypeAdapter(Instant.class, new InstantAdapter()).create();

            KVServer server = new KVServer();
            server.start();
            TaskManager httpTaskManager = Managers.getDefault();

            Task task1 = new Task(InMemoryTaskManager.generateId(), TasksTypes.TASK, "test", TaskStatus.NEW, "test description",
                    Instant.now(), 1);
            httpTaskManager.createTask(task1);

            EpicTask epic1 = new EpicTask(InMemoryTaskManager.generateId(), TasksTypes.EPICTASK, "test", TaskStatus.NEW, "test description",
                    Instant.now(), 2);
            httpTaskManager.createEpicTask(epic1);

            SubTask subtask1 = new SubTask(InMemoryTaskManager.generateId(), TasksTypes.SUBTASK, "test", TaskStatus.NEW,
                    "test description", epic1.getId(), Instant.now(), 3);
            httpTaskManager.createSubTask(subtask1);


            httpTaskManager.getTaskById(task1.getId());
            httpTaskManager.getEpicTaskById(epic1.getId());
            httpTaskManager.getSubTaskById(subtask1.getId());

            System.out.println(gson.toJson(httpTaskManager.getTasksList()));
            System.out.println(gson.toJson(httpTaskManager.getEpicTasksList()));
            System.out.println(gson.toJson(httpTaskManager.getSubTasksList()));
            server.stop();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
