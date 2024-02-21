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

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {
    File file;
    static ObjectsStringConverter converter = new ObjectsStringConverter();

//    public static void main(String[] args) {
//        File fileForExample = new File("src/files/testing1.csv");
//        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(fileForExample);
//        fileBackedTasksManager.createTask(new Task("test1", "test1 description"));
//        fileBackedTasksManager.createTask(new Task("test2", "test2 description"));
//
//        fileBackedTasksManager.getTaskById(1);
//        fileBackedTasksManager.getTaskById(2);
//
//        fileBackedTasksManager.createEpicTask(new EpicTask("test3", "test3 description"));
//        fileBackedTasksManager.createSubTask(new SubTask("test4", "test4 description", 3));
//        fileBackedTasksManager.createSubTask(new SubTask("test5", "test5 description", 3));
//
//        fileBackedTasksManager.getEpicTaskById(3);
//        fileBackedTasksManager.getSubTaskById(4);
//        fileBackedTasksManager.getTaskById(1);
//
//        FileBackedTasksManager fileBackedTasksManager2 = loadFromFile(fileForExample);
//    }

    public FileBackedTasksManager(File file) {
        super();
        this.file = file;
    }

    public static FileBackedTasksManager loadFromFile(File file) throws ManagerSaveException {
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
                Task task = converter.fromString(lines[k++]);

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
                fileWriter.write(converter.toString(task));
            }
            for (EpicTask epic : epicTasks) {
                fileWriter.write(converter.toString(epic));
            }
            for (SubTask sub : subTasks) {
                fileWriter.write(converter.toString(sub));
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