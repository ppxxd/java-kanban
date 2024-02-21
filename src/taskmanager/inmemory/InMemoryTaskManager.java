package taskmanager.inmemory;

import exceptions.ManagerValidateException;
import taskmanager.Managers;
import taskmanager.interfaces.HistoryManager;
import taskmanager.interfaces.TaskManager;
import tasks.*;

import java.time.Instant;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

import tasks.TaskStatus;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, EpicTask> epics;
    private final HashMap<Integer, SubTask> subtasks;
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private final TasksComparator taskComparator = new TasksComparator();
    //private final Comparator<Task> taskComparator = Comparator.nullsLast(Comparator.comparing(Task::getStartTime));
    Set<Task> prioritizedTasks = new TreeSet<>(taskComparator);

    private Integer id = 0;

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
    }

    private Integer generateId() {
        id++;
        return id;
    }

    //Получение списка всех задач.
    @Override
    public ArrayList<Task> getTasksList() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<EpicTask> getEpicTasksList() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<SubTask>getSubTasksList() {
        return new ArrayList<>(subtasks.values());
    }

    //Удаление всех задач.
    @Override
    public void clearTasks() {
        for (Integer id : tasks.keySet()) { //Удаляем все задачи из истории
            historyManager.remove(id);
        }

        tasks.clear();
    }

    @Override
    public void clearEpicTasks() {
        for (Integer id : epics.keySet()) { //Удаляем все эпики из истории
            historyManager.remove(id);
        }

        for (Integer id : subtasks.keySet()) { //Удаляем все сабтаски из истории
            historyManager.remove(id);
        }

        epics.clear();
        subtasks.clear();
    }

    @Override
    public void clearSubTasks() {
        for (Integer id : subtasks.keySet()) { //Удаляем все сабтаски из истории
            historyManager.remove(id);
        }

        subtasks.clear();

        //Поскольку сабТаски привязаны к Эпикам, то нужно почистить еще их у Эпиков
        for (Integer id : epics.keySet()) {
            epics.get(id).clearSubTaskArrayList();
        }
    }

    //Получение по идентификатору.
    @Override
    public Task getTaskById(Integer id) {
        Task task = tasks.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public EpicTask getEpicTaskById(Integer id) {
        EpicTask epicTask = epics.get(id);
        historyManager.add(epicTask);
        return epicTask;
    }

    @Override
    public SubTask getSubTaskById(Integer id) {
        SubTask subTask = subtasks.get(id);
        historyManager.add(subTask);
        return subTask;
    }

    //Создание. Сам объект должен передаваться в качестве параметра.
    @Override
    public Task createTask(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
        addNewPrioritizedTask(task);
        return task;
    }

    @Override
    public EpicTask createEpicTask(EpicTask epicTask) {
        epicTask.setId(generateId());
        epics.put(epicTask.getId(), epicTask);
        return epicTask;
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
        subTask.setId(generateId());
        subtasks.put(subTask.getId(), subTask);
        if (epics.get(subTask.getEpicTaskID()) != null) {
            EpicTask epicTask = epics.get(subTask.getEpicTaskID()); //Узнаем к какому эпику она привязана
            epicTask.addSubTaskToArrayList(subTask.getId()); //Добавляем к этому эпику ее айди в список
        } else {
            System.out.printf("%s%s%s%s%s", "No SubTask's epic found with this ID: subID ", subTask.getId(), " epicID ", epics.get(subTask.getEpicTaskID()), "\n");
        }
        return subTask;
    }

    //Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    //Из условия понял, что таска подается со старым айди, но по сути это новый объект
    //Поэтому проверяю есть ли в списке таска с таким айди и перезаписываю туда новый объект
    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) { //Проверяем, что таска c таким айди существует
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void updateEpicTask(EpicTask epicTask) {
        if (epics.containsKey(epicTask.getId())) {
            EpicTask oldEpicTask = epics.get(epicTask.getId());

            oldEpicTask.setName(epicTask.getName());
            oldEpicTask.setDescription(epicTask.getDescription());

            epics.put(oldEpicTask.getId(), oldEpicTask);
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        if (subtasks.containsKey(subTask.getId())) {
            subtasks.put(subTask.getId(), subTask);
            updateEpicTaskStatus(epics.get(subTask.getEpicTaskID())); /* Проверять, есть ли эпик не нужно, так как по
            условию информация передается с верным идентификатором, то есть ошибок не должно быть */
        }
    }

    //Удаление по идентификатору.
    @Override
    public void deleteTaskById(Integer id) {
        tasks.remove(id);
        historyManager.remove(id);
    }
    @Override
    public void deleteEpicTaskById(Integer id) {
        ArrayList<Integer> idDeletedSubTasks = new ArrayList<>();
        for (Integer subId : subtasks.keySet()) { //Собираем айдишники тех сабтасков, которые надо удалить
            if (subtasks.get(subId).getEpicTaskID().equals(id)) {
                idDeletedSubTasks.add(subId);
            }
        }

        for (Integer idSubTask : idDeletedSubTasks) { //удаляем сабтаски
            subtasks.remove(idSubTask);
            historyManager.remove(idSubTask);
        }

        epics.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteSubTaskById(Integer id) {
        //Удаляем из менеджера и удаляем из списка у Эпика
        if (subtasks.containsKey(id)) {
            EpicTask epicTask = epics.get(subtasks.get(id).getEpicTaskID());
            epicTask.removeSubTaskFromArray(subtasks.get(id));
            subtasks.remove(id);
            updateEpicTaskStatus(epicTask); // Проверяем не надо ли перевести статус эпика на NEW
        }
        historyManager.remove(id);
    }

    //Получение списка всех подзадач определённого эпика.
    @Override
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

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    public TasksTypes findTaskTypeByID(Integer id) {
        if (tasks.containsKey(id)){
            return TasksTypes.TASK;
        }
        if (epics.containsKey(id)){
            return TasksTypes.EPICTASK;
        }
        if (subtasks.containsKey(id)){
            return TasksTypes.SUBTASK;
        }
        return null;
    }

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    private void addNewPrioritizedTask(Task task) {
        prioritizedTasks.add(task);
        validateTaskPriority();
    }

    private void validateTaskPriority() {
        List<Task> tasks = getPrioritizedTasks();

        for (int i = 1; i < tasks.size(); i++) {
            Task task = tasks.get(i);

            boolean taskHasIntersections = checkTime(task);

            if (taskHasIntersections) {
                throw new ManagerValidateException(
                        "Задачи №" + task.getId() + " и №" + tasks.get(i - 1).getId() + "пересекаются.");
            }
        }
    }

    private boolean checkTime(Task task) {
        List<Task> tasks = getPrioritizedTasks();
        Instant taskStartTime = task.getStartTime();
        Instant taskEndTime = task.getEndTime();

        //Если задача началась 01.01.2000 в 10:00 и длится 1 час,
        // то следующая задача может быть лишь после 11:00 утра
        for (Task taskToCheck : tasks) {
            if (taskToCheck.getStartTime() != null && taskToCheck.getEndTime() != null) {
                Instant taskToCheckStartTime = taskToCheck.getStartTime();
                Instant taskToCheckEndTime = taskToCheck.getEndTime();
                if (taskToCheckStartTime.isBefore(taskStartTime) && taskToCheckEndTime.isAfter(taskEndTime)) {
                    //Если начало другой задачи (из цикла) раньше данной, а окончание другой задачи позже данной,
                    //то есть пересечение, то есть данная задача вставлена во время выполнения другой задачи
                    return true;
                } else if (taskToCheckStartTime.isAfter(taskStartTime) && taskToCheckEndTime.isBefore(taskEndTime)) {
                    //Если начало другой задачи (из цикла) позже данной, а окончание другой задачи раньше окончания
                    //данной задачи, то есть пересечение, то есть данная задача закончится позже, чем начнется другая
                    return true;
                }
            }
        }
        return false;
    }

//    public static void main(String[] args) { //Ручное тестирование работы валидации таймингов
//        TaskManager inMemoryTaskManager = Managers.getDefault();
//        inMemoryTaskManager.createTask(new Task(1, TasksTypes.TASK, "test1", TaskStatus.NEW, "test1 description", Instant.now(), 10));
//        inMemoryTaskManager.createTask(new Task(2, TasksTypes.TASK, "test2", TaskStatus.NEW, "test2 description", Instant.now().minus(Period.ofDays(10)), 1));
//    }
}
