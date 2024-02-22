package taskmanager.infile;

import exceptions.ManagerSaveException;
import taskmanager.inmemory.InMemoryTaskManager;
import taskmanager.interfaces.TaskManager;
import tasks.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    File file;

    public FileBackedTasksManager(File file) {
        super();
        this.file = file;
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        try {
            String fileLine = Files.readString(Path.of(String.valueOf(file)));

            if (fileLine.isBlank() || fileLine.isEmpty()) {
                return fileBackedTasksManager;
            }

            String[] lines = fileLine.split("\n");

            if (lines.length == 1) {
                return fileBackedTasksManager;
            }

            int k = 1; //lines[0] = id,type,name,status,description,epic;
            String lastLine = null;


            while (k < lines.length && !lines[k].isEmpty() && !lines[k].isBlank()) {
                //stop at blank and empty line before history
                lastLine = lines[k];
                Task task = ObjectsStringConverter.fromString(lines[k++]);

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
            if (lines[lines.length - 1].equals(lastLine)) {
                return fileBackedTasksManager;
            }
            List<Integer> history = ObjectsStringConverter.historyFromString(lines[lines.length - 1]);
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

    public void save() throws ManagerSaveException {
        try (FileWriter fileWriter = new FileWriter(this.file)) {
            fileWriter.write("id,type,name,status,description,epic\n");

            ArrayList<Task> tasks = super.getTasksList();
            ArrayList<EpicTask> epicTasks = super.getEpicTasksList();
            ArrayList<SubTask> subTasks = super.getSubTasksList();

            for (Task task : tasks) {
                fileWriter.write(ObjectsStringConverter.toString(task));
            }
            for (EpicTask epic : epicTasks) {
                fileWriter.write(ObjectsStringConverter.toString(epic));
            }
            for (SubTask sub : subTasks) {
                fileWriter.write(ObjectsStringConverter.toString(sub));
            }

            fileWriter.write("\n" + ObjectsStringConverter.historyToString(super.getHistoryManager()));
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
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