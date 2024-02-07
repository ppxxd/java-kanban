package taskmanager.infile;

import exceptions.ManagerSaveException;
import taskmanager.inmemory.InMemoryTaskManager;
import taskmanager.interfaces.HistoryManager;
import taskmanager.interfaces.TaskManager;
import tasks.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {
    File file;

    public static void main(String[] args) {
        File fileForExample = new File("src/files/testing1.csv");
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(fileForExample);
        fileBackedTasksManager.createTask(new Task("test1", "test1 description"));
        fileBackedTasksManager.createTask(new Task("test2", "test2 description"));

        fileBackedTasksManager.getTaskById(1);
        fileBackedTasksManager.getTaskById(2);

        fileBackedTasksManager.createEpicTask(new EpicTask("test3", "test3 description"));
        fileBackedTasksManager.createSubTask(new SubTask("test4", "test4 description", 3));
        fileBackedTasksManager.createSubTask(new SubTask("test5", "test5 description", 3));

        fileBackedTasksManager.getEpicTaskById(3);
        fileBackedTasksManager.getSubTaskById(4);
        fileBackedTasksManager.getTaskById(1);

        FileBackedTasksManager fileBackedTasksManager2 = loadFromFile(fileForExample);
    }

    public FileBackedTasksManager(File file) {
        super();
        this.file = file;
    }

    private static FileBackedTasksManager loadFromFile(File file) throws ManagerSaveException {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        try {
            String fileLine = Files.readString(Path.of(String.valueOf(file)));

            if (fileLine.isBlank() || fileLine.isEmpty()) {
                return fileBackedTasksManager;
            }

            String[] lines = fileLine.split("\n");
            int k = 1; //lines[0] = id,type,name,status,description,epic;
            while (!lines[k].isEmpty() && !lines[k].isBlank()) { //stop at blank and empty line before history
                Task task = fileBackedTasksManager.fromString(lines[k++]);

                if (task == null) {
                    return fileBackedTasksManager;
                }

                if (task.getTaskType() == TasksTypes.TASK) {
                    fileBackedTasksManager.createTask(task);
                } else if (task.getTaskType() == TasksTypes.EPICTASK) {
                    fileBackedTasksManager.createEpicTask((EpicTask) task);
                } else if (task.getTaskType() == TasksTypes.SUBTASK) {
                    fileBackedTasksManager.createSubTask((SubTask) task);
                }
            }

            List<Integer> history = historyFromString(lines[lines.length - 1]);
            for (Integer id : history) {
                TasksTypes type = fileBackedTasksManager.findTaskTypeByID(id);
                switch (type) {
                    case TASK:
                        fileBackedTasksManager.getTaskById(id);
                        break;
                    case EPICTASK:
                        fileBackedTasksManager.getEpicTaskById(id);
                        break;
                    case SUBTASK:
                        fileBackedTasksManager.getSubTaskById(id);
                        break;
                    default:
                        return fileBackedTasksManager;
                }
            }
            return fileBackedTasksManager;
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

    private void save() throws ManagerSaveException {
        try (FileWriter fileWriter = new FileWriter(this.file)) {
            fileWriter.write("id,type,name,status,description,epic\n");

            ArrayList<Task> tasks = super.getTasksList();
            ArrayList<EpicTask> epicTasks = super.getEpicTasksList();
            ArrayList<SubTask> subTasks = super.getSubTasksList();

            for (Task task : tasks) {
                fileWriter.write(toString(task));
            }
            for (EpicTask epic : epicTasks) {
                fileWriter.write(toString(epic));
            }
            for (SubTask sub : subTasks) {
                fileWriter.write(toString(sub));
            }

            fileWriter.write("\n" + historyToString(super.getHistoryManager()));
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
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
                task.getTaskType(),
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

    private static String historyToString(HistoryManager manager) {
        StringBuilder line = new StringBuilder();
        if (!manager.getHistory().isEmpty()) {
            for (Task task : manager.getHistory()) {
                line.append(task.getId()).append(",");
            }
            line.deleteCharAt(line.length() - 1);
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

    @Override
    public TasksTypes findTaskTypeByID(Integer id) {
        return super.findTaskTypeByID(id);
    }
}