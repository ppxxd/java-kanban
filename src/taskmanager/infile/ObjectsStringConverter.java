package taskmanager.infile;

import taskmanager.interfaces.HistoryManager;
import tasks.*;

import java.util.ArrayList;
import java.util.List;

public class ObjectsStringConverter {

    //Task to String
    public String toString(Task task) {
        String epicIdIfSubTask = "";

        if (task.getTaskType() == TasksTypes.SUBTASK) {
            epicIdIfSubTask = ((SubTask) task).getEpicTaskID().toString();
        }

        return String.format(
                "%s,%s,%s,%s,%s,%s\n",
                task.getId(),
                task.getTaskType(),
                task.getName(),
                task.getTaskStatus(),
                task.getDescription(),
                epicIdIfSubTask);
    }

    public Task fromString(String value) {
        String[] line = value.split(","); //id,type,name,status,description,epic

        Integer id = Integer.parseInt(line[0]);
        TasksTypes taskType = TasksTypes.valueOf(line[1]);
        String name = line[2];
        TaskStatus taskStatus = TaskStatus.valueOf(line[3]);
        String description = line[4];
        Integer epicIdIfSubTask = line.length == 6 ? Integer.parseInt(line[5]) : null;

        switch (taskType) {
            case TASK:
                return new Task(id, taskType, name, taskStatus, description);
            case EPICTASK:
                return new EpicTask(id, taskType, name, taskStatus, description);
            case SUBTASK:
                return new SubTask(id, taskType, name, taskStatus, description, epicIdIfSubTask);
            default:
                return null;
        }
    }

    public static String historyToString(HistoryManager manager) {
        StringBuilder line = new StringBuilder();
        if (!manager.getHistory().isEmpty()) {
            for (Task task : manager.getHistory()) {
                line.append(task.getId()).append(",");
            }
            line.deleteCharAt(line.length() - 1);
        }
        return line.toString();
    }

    public static List<Integer> historyFromString(String value) {
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

}
