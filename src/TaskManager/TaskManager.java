package TaskManager;

import tasks.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class TaskManager {
    private HashMap<Integer, Task> tasksList;
    private HashMap<Integer, EpicTask> epicTasksList;
    private HashMap<Integer, SubTask> subTasksList;

    public enum TaskStatus {
        NEW,
        IN_PROGRESS,
        DONE
    }

    public TaskManager() {
        tasksList = new HashMap<>();
        epicTasksList = new HashMap<>();
        subTasksList = new HashMap<>();
    }

    //Получение списка всех задач.
    public HashMap<Integer, Task> getTasksList() {
        return tasksList;
    }

    public HashMap<Integer, EpicTask> getEpicTasksList() {
        return epicTasksList;
    }

    public HashMap<Integer, SubTask> getSubTasksArrayList() {
        return subTasksList;
    }

    //Удаление всех задач.
    public void clearTasksArrayList() {
        this.tasksList.clear();
    }

    public void clearEpicTasksArrayList() {
        this.epicTasksList.clear();
    }

    public void clearSubTasksArrayList() {
        this.subTasksList.clear();
        //Поскольку сабТаски привязаны к Эпикам, то нужно почистить еще их у Эпиков
        for (Integer id : epicTasksList.keySet()) {
            epicTasksList.get(id).clearSubTaskArrayList();
        }
    }

    //Получение по идентификатору.
    public Task getTaskById(Integer id) {
        for (Integer taskId : tasksList.keySet()) {
            if (Objects.equals(taskId, id)) {
                return tasksList.get(taskId);
            }
        }
        return null;
    }

    public EpicTask getEpicTaskById(Integer id) {
        for (Integer epicId : epicTasksList.keySet()) {
            if (Objects.equals(epicId, id)) {
                return epicTasksList.get(epicId);
            }
        }
        return null;
    }

    public SubTask getSubTaskById(Integer id) {
        for (Integer subId : subTasksList.keySet()) {
            if (Objects.equals(subId, id)) {
                return subTasksList.get(subId);
            }
        }
        return null;
    }

    //Создание. Сам объект должен передаваться в качестве параметра.
    public Task createTask(Task task) {
        tasksList.put(task.getId(), task);
        return task;
    }

    public EpicTask createEpicTask(EpicTask epicTask) {
        epicTasksList.put(epicTask.getId(), epicTask);
        return epicTask;
    }

    public SubTask createSubTask(SubTask subTask) {
        subTasksList.put(subTask.getId(), subTask);
        return subTask;
    }

    //Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    public void updateTask(Task task) {
        tasksList.put(task.getId(), task);
    }

    public void updateEpicTask(EpicTask epicTask) {
        epicTasksList.put(epicTask.getId(), epicTask);
    }

    public void updateSubTask(SubTask subTask) {
        subTasksList.put(subTask.getId(), subTask);
    }

    //Удаление по идентификатору.
    public void deleteTaskById(Integer id) {
        tasksList.remove(id);
    }
    public void deleteEpicTaskById(Integer id) {
        epicTasksList.remove(id);
    }

    public void deleteSubTaskById(Integer id) {
        //Удаляем из менеджера и удаляем из списка у Эпика
        EpicTask epicTask = subTasksList.get(id).getEpicTask();
        epicTask.removeSubTaskFromArray(subTasksList.get(id));
        subTasksList.remove(id);
    }

    //Получение списка всех подзадач определённого эпика.
    public ArrayList<SubTask> getEpicsSubTaskList(EpicTask epic) {
        return epic.getSubTaskArrayList();
    }

    //Обновление статуса задачи
    public void updateTaskStatus(TaskStatus taskStatus, Integer id) {
        if (tasksList.containsKey(id)) {
            Task task = tasksList.get(id);
            task.setTaskStatus(taskStatus);
            updateTask(task); // Предпологаю, что это не нужно, так как ключ для Мапы создан вручную,
                              // то есть коллизий не долно быть
        } else {
            System.out.println("No task found with this ID");
        }
    }

    public void updateSubTaskStatus(TaskStatus taskStatus, Integer id) {
        if (subTasksList.containsKey(id)) {
            SubTask subTask = subTasksList.get(id);
            subTask.setTaskStatus(taskStatus);
            updateSubTask(subTask); // Предпологаю, что это не нужно, так как ключ для Мапы создан вручную,
                                    // то есть коллизий не долно быть
        } else {
            System.out.println("No SubTask found with this ID");
        }
    }

    // Эпик не может сам поменять свой статус, то есть нет необходимости в методе апдейта его статуса,
    // так как при апдейте сабТаски, чекается нужно ли обновлять его Эпик
}
