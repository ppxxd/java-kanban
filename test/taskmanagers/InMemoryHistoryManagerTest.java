package taskmanagers;

import org.junit.jupiter.api.*;
import taskmanager.inmemory.InMemoryHistoryManager;
import taskmanager.interfaces.HistoryManager;
import tasks.Task;
import tasks.TaskStatus;
import tasks.TasksTypes;

import java.time.Instant;

public class InMemoryHistoryManagerTest {
    HistoryManager manager;
    private int id;

    private Task createTask() {
        return new Task(id++, TasksTypes.TASK, "test", TaskStatus.NEW, "test description",
                Instant.now(), 0);
    }

    @BeforeEach
    public void beforeEach() {
        id = 1;
        manager = new InMemoryHistoryManager();
    }

    @Test
    public void shouldReturnTrueIfHistoryIsEmpty() {
        Assertions.assertTrue(manager.getHistory().isEmpty());

        manager.add(createTask());
        manager.remove(1);

        Assertions.assertTrue(manager.getHistory().isEmpty());
    }

    @Test
    public void shouldReturnTrueIfTask1WasLastInHistoryAfterDuplicateAdding() {
        Task task1 = createTask();
        manager.add(task1);
        manager.add(createTask());
        manager.add(createTask());
        manager.add(task1);
        Assertions.assertEquals(task1, manager.getHistory().get(2));
    }

    @Test
    public void shouldDoTaskRemovalAtDifferentIndexCorrectly() {
        Task startTask = createTask();
        manager.add(createTask());
        manager.add(createTask());
        manager.add(createTask());
        Task middleTask = createTask();
        manager.add(createTask());
        manager.add(createTask());
        manager.add(createTask());
        Task endTask = createTask();

        manager.remove(startTask.getId()); //removal at beginning
        Assertions.assertFalse(manager.getHistory().contains(startTask));
        manager.remove(middleTask.getId()); //removal at middle
        Assertions.assertFalse(manager.getHistory().contains(middleTask));
        manager.remove(endTask.getId()); //removal at end
        Assertions.assertFalse(manager.getHistory().contains(endTask));
    }

    @Test
    public void shouldNotRemoveTaskWithIncorrectId() {
        manager.add(createTask());
        manager.add(createTask());
        manager.add(createTask());

        int historySize = manager.getHistory().size();

        manager.remove(1234);

        Assertions.assertEquals(3, historySize);
    }
}
