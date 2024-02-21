package taskmanager.interfaces;

import tasks.EpicTask;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    //Получение списка всех задач.
    List<Task> getTasksList();

    List<EpicTask> getEpicTasksList();

    List<SubTask>getSubTasksList();

    //Удаление всех задач.
    void clearTasks();

    void clearEpicTasks();

    void clearSubTasks();

    //Получение по идентификатору.
    Task getTaskById(Integer id);

    EpicTask getEpicTaskById(Integer id);

    SubTask getSubTaskById(Integer id);

    //Создание. Сам объект должен передаваться в качестве параметра.
    Task createTask(Task task);

    EpicTask createEpicTask(EpicTask epicTask);

    SubTask createSubTask(SubTask subTask);

    //Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    void updateTask(Task task);

    void updateEpicTask(EpicTask epicTask);

    void updateSubTask(SubTask subTask);

    //Удаление по идентификатору.
    void deleteTaskById(Integer id); //TODO TEST FROM THERE
    void deleteEpicTaskById(Integer id);

    void deleteSubTaskById(Integer id);

    //Получение списка всех подзадач определённого эпика.
    ArrayList<SubTask> getEpicsSubTaskList(EpicTask epic);

    //История просмотров задач
    List<Task> getHistory();
}


