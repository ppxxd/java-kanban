package taskmanager;

import tasks.*;
import java.util.ArrayList;
import java.util.HashMap;
import tasks.TaskStatus;

public class TaskManager {
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, EpicTask> epics;
    private HashMap<Integer, SubTask> subtasks;

    private static Integer id = 0;

    public TaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
    }

    private Integer generateId() {
        id++;
        return id;
    }

    //Получение списка всех задач.
    public ArrayList<Task> getTasksList() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<EpicTask> getEpicTasksList() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<SubTask>getSubTasksList() {
        return new ArrayList<>(subtasks.values());
    }

    //Удаление всех задач.
    public void clearTasks() {
        this.tasks.clear();
    }

    public void clearEpicTasks() {
        this.epics.clear();
        this.subtasks.clear();
    }

    public void clearSubTasks() {
        this.subtasks.clear();
        //Поскольку сабТаски привязаны к Эпикам, то нужно почистить еще их у Эпиков
        for (Integer id : epics.keySet()) {
            epics.get(id).clearSubTaskArrayList();
        }
    }

    //Получение по идентификатору.
    public Task getTaskById(Integer id) {
        return tasks.get(id);
    }

    public EpicTask getEpicTaskById(Integer id) {
        return epics.get(id);
    }

    public SubTask getSubTaskById(Integer id) {
        return subtasks.get(id);
    }

    //Создание. Сам объект должен передаваться в качестве параметра.
    public Task createTask(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
        return task;
    }

    public EpicTask createEpicTask(EpicTask epicTask) {
        epicTask.setId(generateId());
        epics.put(epicTask.getId(), epicTask);
        return epicTask;
    }

    public SubTask createSubTask(SubTask subTask) {
        subTask.setId(generateId());
        subtasks.put(subTask.getId(), subTask);
        if (epics.get(subTask.getEpicTaskID()) != null) {
            EpicTask epicTask = epics.get(subTask.getEpicTaskID()); //Узнаем к какому эпику она привязана
            epicTask.addSubTaskToArrayList(subTask.getId()); //Добавляем к этому эпику ее айди в список
        } else {
            System.out.println("No SubTask's epic found with this ID.");
        }
        return subTask;
    }

    //Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    //Из условия понял, что таска подается со старым айди, но по сути это новый объект
    //Поэтому проверяю есть ли в списке таска с таким айди и перезаписываю туда новый объект
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) { //Проверяем, что таска c таким айди существует
            tasks.put(task.getId(), task);
        }
    }

    public void updateEpicTask(EpicTask epicTask) {
        if (epics.containsKey(epicTask.getId())) {
            EpicTask oldEpicTask = epics.get(epicTask.getId());

            oldEpicTask.setName(epicTask.getName());
            oldEpicTask.setDescription(epicTask.getDescription());

            epics.put(oldEpicTask.getId(), oldEpicTask);
        }
    }

    public void updateSubTask(SubTask subTask) {
        if (subtasks.containsKey(subTask.getId())) {
            subtasks.put(subTask.getId(), subTask);
            updateEpicTaskStatus(epics.get(subTask.getEpicTaskID())); /* Проверять, есть ли эпик не нужно, так как по
            условию информация передается с верным идентификатором, то есть ошибок не должно быть */
        }
    }

    //Удаление по идентификатору.
    public void deleteTaskById(Integer id) {
        tasks.remove(id);
    }
    public void deleteEpicTaskById(Integer id) {
        for (Integer subId : subtasks.keySet()) {
            if (subtasks.get(subId).getEpicTaskID().equals(id)) {
                subtasks.remove(subId);
            }
        }
        epics.remove(id);
    }

    public void deleteSubTaskById(Integer id) {
        //Удаляем из менеджера и удаляем из списка у Эпика
        if (subtasks.containsKey(id)) {
            EpicTask epicTask = epics.get(subtasks.get(id).getEpicTaskID());
            epicTask.removeSubTaskFromArray(subtasks.get(id));
            subtasks.remove(id);
            updateEpicTaskStatus(epicTask); // Проверяем не надо ли перевести статус эпика на NEW
        }
    }

    //Получение списка всех подзадач определённого эпика.
    public ArrayList<SubTask> getEpicsSubTaskList(EpicTask epic) {
        if (epic == null) { //Если эпик удалили, а потом пытаются вытащить его сабТаски, то вылетает NPE
            System.out.println("No epic found with this ID");
            return null;
        }

        ArrayList<Integer> subTaskIDs = epic.getSubTaskArrayList();
        ArrayList<SubTask> epicsSubTasks = new ArrayList<>();
        for (Integer id : subTaskIDs) {
            epicsSubTasks.add(subtasks.get(id));
        }
        return epicsSubTasks;
    }

    //Обновление статуса задачи
    private void updateEpicTaskStatus(EpicTask epicTask) { //private потому что не может менять свой статус сам
        int countNew = 0;
        int countDone = 0;
        // Нет необходимости считать кол-во в прогрессе, так как если countNew == кол-во задач, то ВСЕ задачи новые,
        // аналогично с countDone все задачи будут ВЫПОЛНЕНЫМИ.

        ArrayList<Integer> subTaskIDs = epicTask.getSubTaskArrayList();

        if (subTaskIDs.isEmpty()) {
            epicTask.setTaskStatus(TaskStatus.NEW); //Очищается эпик, значит статус становится NEW, т. к. если
            // у эпика нет подзадач или все они имеют статус NEW, то статус должен быть NEW.
            return;
        }

        for (Integer id : subTaskIDs) {
            if (subtasks.get(id).getTaskStatus() == TaskStatus.NEW) {
                countNew++;
            } else if (subtasks.get(id).getTaskStatus() == TaskStatus.DONE) {
                countDone++;
            }
        }

        if (countNew == subTaskIDs.size()) {
            epicTask.setTaskStatus(TaskStatus.NEW);
        } else if (countDone == subTaskIDs.size()) {
            epicTask.setTaskStatus(TaskStatus.DONE);
        } else {
            epicTask.setTaskStatus(TaskStatus.IN_PROGRESS);
        }

    }
}
