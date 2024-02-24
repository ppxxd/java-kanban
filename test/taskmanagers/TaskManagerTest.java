package taskmanagers;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskmanager.interfaces.TaskManager;
import tasks.*;

import java.time.Instant;
import java.util.List;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T manager;
    private int id;

    @BeforeEach
    public void beforeEach() {
        id = 1;
    }

    private EpicTask createEpic() {
        return new EpicTask(id++, TasksTypes.EPICTASK, "test", TaskStatus.NEW, "test description",
                Instant.now(), 0);
    }

    private SubTask createSub(int epicTaskID) {
        return new SubTask(id++, TasksTypes.SUBTASK, "test", TaskStatus.NEW,
                "test description", epicTaskID, Instant.now(), 0);
    }

    private Task createTask() {
        return new Task(id++, TasksTypes.TASK, "test", TaskStatus.NEW, "test description",
                Instant.now(), 0);
    }

    @Test
    void shouldReturnTrueIfSubTasksListIsEmpty() {
        EpicTask epicTask = createEpic();
        manager.createEpicTask(epicTask);
        assertTrue(manager.getEpicsSubTaskList(epicTask).isEmpty());
    }

    @Test
    void shouldReturnTrueIfEpicsSubTasksStatusesAreNew() {
        EpicTask epicTask = createEpic();
        manager.createEpicTask(epicTask);
        manager.createSubTask(createSub(epicTask.getId()));
        manager.createSubTask(createSub(epicTask.getId()));
        manager.createSubTask(createSub(epicTask.getId()));

        assertEquals(3, manager.getEpicsSubTaskList(epicTask).stream().
                filter(subTask -> subTask.getTaskStatus().equals(TaskStatus.NEW)).count());
    }

    @Test
    void shouldReturnTrueIfEpicsSubTasksStatusesAreDone() {
        EpicTask epicTask = createEpic();
        manager.createEpicTask(epicTask);
        SubTask sub1 = createSub(epicTask.getId());
        SubTask sub2 = createSub(epicTask.getId());
        sub1.setTaskStatus(TaskStatus.DONE);
        sub2.setTaskStatus(TaskStatus.DONE);
        manager.createSubTask(sub1);
        manager.createSubTask(sub2);

        assertEquals(2, manager.getEpicsSubTaskList(epicTask).stream().
                filter(subTask -> subTask.getTaskStatus().equals(TaskStatus.DONE)).count());
    }

    @Test
    void shouldReturnTrueIfEpicsSubTasksStatusesAreDoneAndNew() {
        EpicTask epicTask = createEpic();
        manager.createEpicTask(epicTask);
        SubTask sub1 = createSub(epicTask.getId());
        sub1.setTaskStatus(TaskStatus.DONE);
        manager.createSubTask(createSub(epicTask.getId()));
        manager.createSubTask(sub1);


        assertEquals(1, manager.getEpicsSubTaskList(epicTask).stream().
                filter(subTask -> subTask.getTaskStatus().equals(TaskStatus.DONE)).count());
        assertEquals(1, manager.getEpicsSubTaskList(epicTask).stream().
                filter(subTask -> subTask.getTaskStatus().equals(TaskStatus.NEW)).count());
    }

    @Test
    void shouldReturnTrueIfEpicsSubTaskStatusIsInProgress() {
        EpicTask epicTask = createEpic();
        manager.createEpicTask(epicTask);
        SubTask sub1 = createSub(epicTask.getId());
        sub1.setTaskStatus(TaskStatus.IN_PROGRESS);
        manager.createSubTask(sub1);

        assertEquals(TaskStatus.IN_PROGRESS, manager.getEpicsSubTaskList(epicTask).get(0).getTaskStatus());
    }

    @Test
    void shouldCreateTask() {
        Task task = createTask();
        manager.createTask(task);
        List<Task> tasks = manager.getTasksList();
        assertNotNull(task.getTaskStatus());
        assertEquals(TaskStatus.NEW, task.getTaskStatus());
        assertEquals(List.of(task), tasks);
    }

    @Test
    void shouldCreateEpicTask() {
        EpicTask epic = createEpic();
        manager.createEpicTask(epic);
        List<EpicTask> epics = manager.getEpicTasksList();
        assertNotNull(epic.getTaskStatus());
        assertEquals(TaskStatus.NEW, epic.getTaskStatus());
        assertEquals(List.of(epic), epics);
    }

    @Test
    void shouldCreateSubTask() {
        EpicTask epic = createEpic();
        SubTask sub = createSub(epic.getId());
        manager.createTask(sub);
        List<Task> subs = manager.getTasksList();
        assertNotNull(sub.getTaskStatus());
        assertEquals(TaskStatus.NEW, sub.getTaskStatus());
        assertEquals(List.of(sub), subs);
    }

    @Test
    void shouldReturnNullWhenCreateTaskWithNull() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> manager.createTask(null));
        assertNull(ex.getMessage());
    }

    @Test
    void shouldThrowWhenCreateEpicTaskWithNull() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> manager.createEpicTask(null));
        assertNull(ex.getMessage());
    }

    @Test
    void shouldThrowNullWhenCreateSubtaskWithNull() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> manager.createSubTask(null));
        assertNull(ex.getMessage());
    }

    @Test
    void shouldReturnTrueIfTasksListWorksCorrectly() {
        assertTrue(manager.getTasksList().isEmpty());

        Task task = createTask();
        manager.createTask(task);
        manager.createTask(createTask());

        assertEquals(2, manager.getTasksList().size());

        manager.deleteTaskById(task.getId());

        assertEquals(1, manager.getTasksList().size());
    }

    @Test
    void shouldReturnTrueIfSubTasksListWorksCorrectly() {
        assertTrue(manager.getSubTasksList().isEmpty());

        EpicTask epic = createEpic();
        manager.createEpicTask(epic);
        SubTask sub = createSub(epic.getId());
        manager.createSubTask(sub);
        manager.createSubTask(createSub(epic.getId()));

        assertEquals(2, manager.getSubTasksList().size());

        manager.deleteSubTaskById(sub.getId());

        assertEquals(1, manager.getSubTasksList().size());
    }

    @Test
    void shouldReturnTrueIfEpicTasksListWorksCorrectly() {
        assertTrue(manager.getEpicTasksList().isEmpty());

        EpicTask epicTask = createEpic();
        manager.createEpicTask(epicTask);
        manager.createEpicTask(createEpic());

        assertEquals(2, manager.getEpicTasksList().size());

        manager.deleteEpicTaskById(epicTask.getId());

        assertEquals(1, manager.getEpicTasksList().size());
    }

    @Test
    void shouldClearTasksList() {
        manager.clearTasks(); //clear if tasks are clear

        assertTrue(manager.getTasksList().isEmpty());

        manager.createTask(createTask());
        manager.clearTasks();

        assertTrue(manager.getTasksList().isEmpty());
    }

    @Test
    void shouldClearSubTasksList() {
        manager.clearSubTasks(); //clear if subtasks are clear

        assertTrue(manager.getSubTasksList().isEmpty());

        manager.createSubTask(createSub(manager.createEpicTask(createEpic()).getId()));
        manager.clearSubTasks();

        assertTrue(manager.getSubTasksList().isEmpty());
    }

    @Test
    void shouldClearEpicTasksList() {
        manager.clearEpicTasks(); //clear if tasks are clear

        assertTrue(manager.getEpicTasksList().isEmpty());

        manager.createEpicTask(createEpic());
        manager.clearEpicTasks();

        assertTrue(manager.getEpicTasksList().isEmpty());
    }

    @Test
    void shouldReturnTaskIfTaskFoundById() {
        //if id given is null
        assertNull(manager.getTaskById(null));

        Task task = manager.createTask(createTask()); //id given is correct
        Integer id = task.getId();
        assertEquals(task, manager.getTaskById(id));

        //if id given is incorrect
        assertNull(manager.getTaskById(12312));
    }

    @Test
    void shouldReturnSubTaskIfSubTaskFoundById() {
        //if id given is null -> assertnull
        assertNull(manager.getSubTaskById(null));

        SubTask sub = manager.createSubTask(createSub(manager.createEpicTask(createEpic()).getId())); //id given is correct
        Integer id = sub.getId();
        assertEquals(sub, manager.getSubTaskById(id));

        //if id given is incorrect -> throws null
        assertNull(manager.getSubTaskById(12312));
    }

    @Test
    void shouldReturnEpicTaskIfEpicTaskFoundById() {
        //if id given is null
        assertNull(manager.getEpicTaskById(null));

        EpicTask epic = manager.createEpicTask(createEpic()); //id given is correct
        Integer id = epic.getId();
        assertEquals(epic, manager.getEpicTaskById(id));

        //if id given is incorrect
        assertNull(manager.getEpicTaskById(12312));
    }

    @Test
    void shouldUpdateTaskAndThrowExceptionWhenNull() {
        //if task given is null -> throws null
        NullPointerException ex = assertThrows(NullPointerException.class, () -> manager.updateTask(null));
        assertNull(ex.getMessage());

        Task task1 = createTask();
        manager.createTask(task1);

        Task taskNew = new Task(task1.getId(), task1.getTaskType(),
                "taskNew", TaskStatus.NEW, "taskNew", task1.getStartTime(), task1.getDuration());

        manager.updateTask(taskNew);
        assertEquals("taskNew", manager.getTaskById(taskNew.getId()).getName());
    }

    @Test
    void shouldUpdateEpicTaskAndThrowExceptionWhenNull() {
        //if task given is null -> throws null
        NullPointerException ex = assertThrows(NullPointerException.class, () -> manager.updateEpicTask(null));
        assertNull(ex.getMessage());


        EpicTask epic = createEpic();
        manager.createEpicTask(epic);

        EpicTask taskNew = new EpicTask(epic.getId(), epic.getTaskType(),
                "taskNew", TaskStatus.NEW, "taskNew", epic.getStartTime(), epic.getDuration());

        manager.updateEpicTask(taskNew);
        assertEquals("taskNew", manager.getEpicTaskById(taskNew.getId()).getName());
    }

    @Test
    void shouldUpdateSubTaskAndThrowExceptionWhenNull() {
        //if task given is null -> throws null
        NullPointerException ex = assertThrows(NullPointerException.class, () -> manager.updateSubTask(null));
        assertNull(ex.getMessage());

        EpicTask epic = createEpic();
        manager.createEpicTask(epic);

        SubTask sub = createSub(epic.getId());
        manager.createSubTask(sub);

        SubTask taskNew = new SubTask(sub.getId(), sub.getTaskType(),
                "taskNew", TaskStatus.NEW, "taskNew", epic.getId(),
                sub.getStartTime(), sub.getDuration());

        manager.updateSubTask(taskNew);
        assertEquals("taskNew", manager.getSubTaskById(taskNew.getId()).getName());
    }

    @Test
    void shouldReturnTrueIfTaskWasDeleted() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> manager.deleteTaskById(null));
        assertNull(ex.getMessage());

        Task task = createTask();
        manager.createTask(task);

        manager.deleteTaskById(task.getId());

        assertTrue(manager.getTasksList().isEmpty());
        assertNull(manager.getTaskById(task.getId()));
    }

    @Test
    void shouldReturnTrueIfEpicTaskWasDeleted() { //also deletes epicTasks if there were ones
        NullPointerException ex = assertThrows(NullPointerException.class, () -> manager.deleteEpicTaskById(null));
        assertNull(ex.getMessage());

        EpicTask epic = createEpic();
        manager.createEpicTask(epic);
        manager.deleteEpicTaskById(epic.getId());

        assertTrue(manager.getEpicTasksList().isEmpty());
        assertNull(manager.getEpicTaskById(epic.getId()));

        manager.createEpicTask(epic);
        manager.createSubTask(createSub(epic.getId()));
        manager.createSubTask(createSub(epic.getId()));
        manager.deleteEpicTaskById(epic.getId());

        assertEquals(manager.getEpicTasksList().isEmpty(), manager.getSubTasksList().isEmpty());
    }

    @Test
    void shouldReturnTrueIfSubTaskWasDeleted() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> manager.deleteSubTaskById(null));
        assertNull(ex.getMessage());

        EpicTask epic = createEpic();
        manager.createEpicTask(epic);

        SubTask sub = createSub(epic.getId());
        manager.createTask(sub);

        manager.deleteSubTaskById(sub.getId());

        assertTrue(manager.getSubTasksList().isEmpty());
        assertNull(manager.getSubTaskById(sub.getId()));

        assertTrue(manager.getEpicsSubTaskList(epic).isEmpty());
    }

    @Test
    void shouldReturnTrueIfCorrectEpicsSubTasksListReceived() {
        //if epic was given null -> prints "No epic found with this ID"
        assertNull(manager.getEpicsSubTaskList(null));

        //if epic was given correct and there are some subs
        EpicTask epic = createEpic();
        manager.createEpicTask(epic);
        manager.createSubTask(createSub(epic.getId()));
        manager.createSubTask(createSub(epic.getId()));
        assertEquals(2, manager.getEpicsSubTaskList(epic).size());

        EpicTask epic2 = createEpic();
        manager.createEpicTask(epic2);
        assertEquals(0, manager.getEpicsSubTaskList(epic2).size());
    }

    @Test
    void shouldReturnTrueHistory() {
        //if history is clear
        assertTrue(manager.getHistory().isEmpty());

        Task task1 = createTask();
        manager.createTask(task1);
        manager.getTaskById(task1.getId());

        assertEquals(1, manager.getHistory().size());
    }

    @Test
    void shouldReturnTrueIfSubsEpicWasFound() {
        EpicTask epic = createEpic();
        manager.createEpicTask(epic);
        SubTask sub = createSub(epic.getId());
        manager.createSubTask(sub);

        assertEquals(epic.getId(), manager.getSubTaskById(sub.getId()).getEpicTaskID());
    }

    @Test
    void shouldUpdateEpicToStatusInProgress() {
        EpicTask epic = createEpic();
        manager.createEpicTask(epic);
        SubTask sub = createSub(epic.getId());
        SubTask sub2 = createSub(epic.getId());
        manager.createSubTask(sub);
        manager.createSubTask(sub2);
        manager.updateSubTask(new SubTask(sub.getId(), sub.getTaskType(), "new", TaskStatus.IN_PROGRESS,
                "desc", epic.getId(), sub.getStartTime(), sub.getDuration()));

        assertEquals(TaskStatus.IN_PROGRESS, manager.getEpicTaskById(epic.getId()).getTaskStatus());
    }

    @Test
    void shouldUpdateEpicToStatusDone() {
        EpicTask epic = createEpic();
        manager.createEpicTask(epic);
        SubTask sub = createSub(epic.getId());
        SubTask sub2 = createSub(epic.getId());
        manager.createSubTask(sub);
        manager.createSubTask(sub2);
        manager.updateSubTask(new SubTask(sub.getId(), sub.getTaskType(), "new", TaskStatus.DONE,
                "desc", epic.getId(), sub.getStartTime(), sub.getDuration()));
        manager.updateSubTask(new SubTask(sub2.getId(), sub2.getTaskType(), "new", TaskStatus.DONE,
                "desc", epic.getId(), sub2.getStartTime(), sub2.getDuration()));

        assertEquals(TaskStatus.DONE, manager.getEpicTaskById(epic.getId()).getTaskStatus());
    }
}
