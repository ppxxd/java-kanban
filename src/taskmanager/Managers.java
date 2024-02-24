package taskmanager;

import http.HttpTaskManager;
import taskmanager.inmemory.InMemoryHistoryManager;
import taskmanager.inmemory.InMemoryTaskManager;
import taskmanager.interfaces.HistoryManager;
import taskmanager.interfaces.TaskManager;

import java.io.IOException;

public class Managers {
    public static TaskManager getDefault() throws IOException, InterruptedException {
        return new HttpTaskManager("http://localhost:8078");
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static InMemoryTaskManager getInMemoryTaskManager() {
        return new InMemoryTaskManager();
    }
}
