import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskmanager.infile.FileBackedTasksManager;
import taskmanager.inmemory.InMemoryTaskManager;
import tasks.EpicTask;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileBackedTasksManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    public static final Path path = Path.of("test/FileBackendTaskManagerFileTest.csv");
    File file = new File(String.valueOf(path));

    @BeforeEach
    public void beforeEach() {
        manager = new FileBackedTasksManager(file);
    }

    @AfterEach
    public void afterEach() {
        try {
            Files.delete(path);
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Test
    void shouldReturnTrueIfGetsEmptyFileToWorkWith() {
        FileBackedTasksManager fileManager = new FileBackedTasksManager(file);
        fileManager.save();
        FileBackedTasksManager fil = FileBackedTasksManager.loadFromFile(file);

        assertTrue(manager.getTasksList().isEmpty());
        assertTrue(manager.getEpicTasksList().isEmpty());
        assertTrue(manager.getSubTasksList().isEmpty());
    }

    @Test
    void shouldReturnTrueIfEpicWasCorrectlySavedInFile() {
        EpicTask epicTask = new EpicTask("name", "descr");
        manager.createEpicTask(epicTask);
        EpicTask epicTask1 = new EpicTask("name", "descr");
        manager.createEpicTask(epicTask1);
        FileBackedTasksManager fileManager = FileBackedTasksManager.loadFromFile(file);
        assertEquals(epicTask, fileManager.getEpicTaskById(epicTask.getId()));
        assertEquals(epicTask.getSubTaskArrayList().size(),
                fileManager.getEpicTaskById(epicTask.getId()).getSubTaskArrayList().size());
    }

    @Test
    public void shouldSaveAndLoadEmptyHistory() {
        FileBackedTasksManager fileManager = new FileBackedTasksManager(file);
        fileManager.save();
        FileBackedTasksManager.loadFromFile(file);
        assertTrue(manager.getHistory().isEmpty());
    }
}

