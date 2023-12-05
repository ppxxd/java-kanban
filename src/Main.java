import taskmanager.TaskManager;
import tasks.*;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Task task = new Task("test1", "test1 description");
        taskManager.createTask(task);
////
//        Task task2 = new Task("test2", "test2 description");
//        task2.setId(1);
//        task2.setTaskStatus(Task.TaskStatus.DONE);
//
//        System.out.println(task.getTaskStatus());
//        System.out.println(task2.getTaskStatus());
//
//        taskManager.updateTask(task2);
//
//        System.out.println(taskManager.getTaskById(1).getTaskStatus());

        EpicTask epicTask1 = new EpicTask("test2", "test2 description");
        taskManager.createEpicTask(epicTask1);

        SubTask sub1test = epicTask1.createSubTask("sub1", "sub1 desc");
        taskManager.createSubTask(sub1test);

        SubTask sub2test = epicTask1.createSubTask("sub2", "sub2 desc");
        taskManager.createSubTask(sub2test);

        SubTask sub3test = new SubTask("sub3", "sub3 desc", epicTask1.getId());
        sub3test.setId(sub1test.getId());
        sub3test.setTaskStatus(TaskStatus.DONE);

        sub2test.setTaskStatus(TaskStatus.IN_PROGRESS);

        taskManager.updateSubTask(sub3test);

        System.out.println(taskManager.getEpicTaskById(epicTask1.getId()).getTaskStatus());




////        taskManager.createTask(task2);
////
//        EpicTask epicTask1 = new EpicTask("test2", "test2 description");
//        taskManager.createEpicTask(epicTask1);
//
//        EpicTask epicTask2 = new EpicTask("test3", "test3 description");
//        epicTask2.setId(2);
//
//        SubTask sub1test = epicTask1.createSubTask("sub1", "sub1 desc");
//        taskManager.createSubTask(sub1test);
//        SubTask sub2test = epicTask1.createSubTask("sub2", "sub2 desc");
////        taskManager.createSubTask(sub2test);
//        sub2test.setId(3);
//        System.out.println(taskManager.getSubTasksList());
//        System.out.println(taskManager.getSubTaskById(3));
//        taskManager.updateSubTask(sub2test);
//        System.out.println(taskManager.getSubTasksList());
//        System.out.println(taskManager.getSubTaskById(3));
//        System.out.println(taskManager.getEpicTaskById(2).getName());


//        System.out.println(taskManager.getSubTasksList());
//        System.out.println(taskManager.getEpicTasksList());

//
//
////        taskManager.clearTasks();
//////        taskManager.clearEpicTasks();
////        taskManager.clearSubTasks();
////
////        System.out.println(taskManager.getTasksList());
////        System.out.println(taskManager.getEpicTasksList());
////        System.out.println(taskManager.getSubTasksList());
////        System.out.println(taskManager.getEpicsSubTaskList(taskManager.getEpicTaskById(2)));
//
////        System.out.println(taskManager.getEpicsSubTaskList(taskManager.getEpicTaskById(2)));
////
//////        taskManager.deleteTaskById(1); //works
////        taskManager.deleteEpicTaskById(2);
//////        taskManager.deleteSubTaskById(3);
////
////        System.out.println(taskManager.getTasksList());
////        System.out.println(taskManager.getEpicTasksList());
////        System.out.println(taskManager.getSubTasksList());
////        System.out.println(taskManager.getEpicsSubTaskList(taskManager.getEpicTaskById(2)));
//
    }
}
