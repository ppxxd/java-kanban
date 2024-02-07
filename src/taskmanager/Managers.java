package taskmanager;

import taskmanager.inmemory.InMemoryHistoryManager;
import taskmanager.inmemory.InMemoryTaskManager;
import taskmanager.interfaces.HistoryManager;
import taskmanager.interfaces.TaskManager;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
