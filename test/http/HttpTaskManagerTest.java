package http;

import taskmanager.inmemory.InMemoryTaskManager;
import tasks.*;
import taskmanager.interfaces.*;
import taskmanager.Managers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpTaskManagerTest {
    private KVServer server;
    private TaskManager manager;
    private int duration;

    @BeforeEach
    public void createManager() {
        try {
            server = new KVServer();
            server.start();
            manager = Managers.getDefault();
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при создании менеджера");
        }
    }

    @AfterEach
    public void stopServer() {
        server.stop();
    }

    @BeforeEach
    public void beforeEach() {
        duration = 1;
    }

    private EpicTask createEpic() {
        return new EpicTask(InMemoryTaskManager.generateId(), TasksTypes.EPICTASK, "test", TaskStatus.NEW, "test description",
                Instant.now(), duration++);
    }

    private SubTask createSub(int epicTaskID) {
        return new SubTask(InMemoryTaskManager.generateId(), TasksTypes.SUBTASK, "test", TaskStatus.NEW,
                "test description", epicTaskID, Instant.now(), duration++);
    }

    private Task createTask() {
        return new Task(InMemoryTaskManager.generateId(), TasksTypes.TASK, "test", TaskStatus.NEW, "test description",
                Instant.now(), duration++);
    }

    @Test
    public void shouldLoadTasks() {
        Task task1 = createTask();
        Task task2 = createTask();
        manager.createTask(task1);
        manager.createTask(task2);
        manager.getTaskById(task1.getId());
        manager.getTaskById(task2.getId());
        List<Task> list = manager.getHistory();
        assertEquals(manager.getTasksList(), list);
    }

    @Test
    public void shouldLoadEpics() {
        EpicTask epic1 = createEpic();
        EpicTask epic2 = createEpic();
        manager.createEpicTask(epic1);
        manager.createEpicTask(epic2);
        manager.getEpicTaskById(epic1.getId());
        manager.getEpicTaskById(epic2.getId());
        List<Task> list = manager.getHistory();
        assertEquals(manager.getEpicTasksList(), list);
    }

    @Test
    public void shouldLoadSubTasks() {
        EpicTask epic1 = createEpic();
        SubTask subtask1 = createSub(epic1.getId());
        SubTask subtask2 = createSub(epic1.getId());
        manager.createSubTask(subtask1);
        manager.createSubTask(subtask2);
        manager.getSubTaskById(subtask1.getId());
        manager.getSubTaskById(subtask2.getId());
        List<Task> list = manager.getHistory();
        assertEquals(manager.getSubTasksList(), list);
    }

}
