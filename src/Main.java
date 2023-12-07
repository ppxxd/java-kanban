import taskmanager.*;
import tasks.*;

public class Main {

    public static void main(String[] args) {
        TaskManager inMemoryTaskManager = Managers.getDefault();

        inMemoryTaskManager.createTask(new Task("test1", "test1 description"));
        inMemoryTaskManager.createTask(new Task("test2", "test2 description"));
        inMemoryTaskManager.createTask(new Task("test3", "test3 description"));
        inMemoryTaskManager.createTask(new Task("test4", "test4 description"));
        inMemoryTaskManager.createTask(new Task("test5", "test5 description"));
        inMemoryTaskManager.createTask(new Task("test6", "test6 description"));
        inMemoryTaskManager.createTask(new Task("test7", "test7 description"));
        inMemoryTaskManager.createTask(new Task("test8", "test8 description"));
        inMemoryTaskManager.createTask(new Task("test9", "test9 description"));
        inMemoryTaskManager.createTask(new Task("test10", "test10 description"));

        inMemoryTaskManager.createEpicTask(new EpicTask("test11", "test11 description"));
        inMemoryTaskManager.createEpicTask(new EpicTask("test12", "test12 description"));

        inMemoryTaskManager.createSubTask(new SubTask("test13", "test13", 11));
        inMemoryTaskManager.createSubTask(new SubTask("test14", "test14", 11));
        inMemoryTaskManager.createSubTask(new SubTask("test15", "test15", 12));

        System.out.println(inMemoryTaskManager.getHistory());

        inMemoryTaskManager.getTaskById(1);
        inMemoryTaskManager.getTaskById(2);

        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println(inMemoryTaskManager.getHistory().size());

        for (int i = 3; i < 11; i++) {
            inMemoryTaskManager.getTaskById(i);
        }

        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println(inMemoryTaskManager.getHistory().size());

        inMemoryTaskManager.getEpicTaskById(11);

        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println(inMemoryTaskManager.getHistory().size());

        inMemoryTaskManager.getSubTaskById(13);
        inMemoryTaskManager.getSubTaskById(15);
        inMemoryTaskManager.getTaskById(6);
        inMemoryTaskManager.getTaskById(5);

        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println(inMemoryTaskManager.getHistory().size());

    }
}
