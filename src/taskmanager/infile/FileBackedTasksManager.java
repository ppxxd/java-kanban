package taskmanager.infile;

import taskmanager.inmemory.InMemoryTaskManager;
import taskmanager.interfaces.HistoryManager;
import tasks.EpicTask;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    File file;

    enum TasksTypes {
        TASK,
        EPICTASK,
        SUBTASK
    }

    public FileBackedTasksManager(File file) { //TODO
        super();
        this.file = file;
    }

    private void save() {

    }

    //Task to String
    private String toString(Task task) {
        String epicIdIfSubTask = "";

        if (task instanceof SubTask) {
            epicIdIfSubTask = ((SubTask) task).getEpicTaskID().toString();
        }

//        String[] taskStrings = {task.getId().toString(), task.getClass().toString(), task.getName(),
//                task.getTaskStatus().toString(), task.getDescription(), epicIdIfSubTask};
//        return String.join(",", taskStrings);

        return String.format(
                "%s,%s,%s,%s,%s,%s\n",
                task.getId(),
                task.getClass(),
                task.getName(),
                task.getTaskStatus(),
                task.getDescription(),
                epicIdIfSubTask);
    }

    private Task fromString(String value) {
        String[] line = value.split(","); //id,type,name,status,description,epic

        Integer id = Integer.parseInt(line[0]);
        TasksTypes taskType = TasksTypes.valueOf(line[1]);
        String name = line[2];
        TaskStatus taskStatus = TaskStatus.valueOf(line[3]);
        String description = line[4];
        Integer epicIdIfSubTask = Integer.parseInt(line[5]);

        switch (taskType) {
            case TASK:
                return new Task(id, name, taskStatus, description);
            case EPICTASK:
                return new EpicTask(id, name, taskStatus, description);
            case SUBTASK:
                return new SubTask(id, name, taskStatus, description, epicIdIfSubTask);
            default:
                return null;
        }
    }

    private static String historyToString(HistoryManager manager) {
        StringBuilder line = new StringBuilder();
        if (!manager.getHistory().isEmpty()) {
            for (Task task : manager.getHistory()) {
                line.append(task.getId()).append(",");
            }
        }
        return line.toString();
    }

    private static List<Integer> historyFromString(String value) {
        List<Integer> history = new ArrayList<>();

        if (value.isEmpty() || value.isBlank()) {
            return history;
        }

        String[] line = value.split(",");

        for (String id : line) {
            history.add(Integer.parseInt(id));
        }
        return history;
    }

    @Override
    public void clearTasks() {
        super.clearTasks();
        save();
    }

    @Override
    public void clearEpicTasks() {
        super.clearEpicTasks();
        save();
    }

    @Override
    public void clearSubTasks() {
        super.clearSubTasks();
        save();
    }

    @Override
    public Task getTaskById(Integer id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public EpicTask getEpicTaskById(Integer id) {
        EpicTask epicTask = super.getEpicTaskById(id);
        save();
        return epicTask;
    }

    @Override
    public SubTask getSubTaskById(Integer id) {
        SubTask subTask = super.getSubTaskById(id);
        save();
        return subTask;
    }

    @Override
    public Task createTask(Task task) {
        super.createTask(task);
        save();
        return task;
    }

    @Override
    public EpicTask createEpicTask(EpicTask epicTask) {
        super.createEpicTask(epicTask);
        save();
        return epicTask;
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
        super.createSubTask(subTask);
        save();
        return subTask;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpicTask(EpicTask epicTask) {
        super.updateEpicTask(epicTask);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void deleteTaskById(Integer id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicTaskById(Integer id) {
        super.deleteEpicTaskById(id);
        save();
    }

    @Override
    public void deleteSubTaskById(Integer id) {
        super.deleteSubTaskById(id);
        save();
    }
}
