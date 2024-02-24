package taskmanagers;

import org.junit.jupiter.api.BeforeEach;
import taskmanager.inmemory.InMemoryTaskManager;

public class InMemoryTasksManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @BeforeEach
    public void beforeEach() {
        manager = new InMemoryTaskManager();
    }
}
