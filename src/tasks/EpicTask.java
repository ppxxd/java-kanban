package tasks;

import TaskManager.TaskManager.TaskStatus;

import java.util.ArrayList;

public class EpicTask extends Task {
    private ArrayList<SubTask> subTaskArrayList;

    private TaskStatus taskStatus;

    public EpicTask(String name, String description) {
        super(name, description);
        subTaskArrayList = new ArrayList<>();
        this.taskStatus = TaskStatus.NEW;
    }

    public ArrayList<SubTask> getSubTaskArrayList() {
        return subTaskArrayList;
    }

    public void removeSubTaskFromArray(SubTask subTask) {
        subTaskArrayList.remove(subTask);
        checkEpicStatus(); //После удаления проверяем, не надо ли обновить статус Эпика
    }

    public SubTask createSubTask(String name, String description) {
        SubTask newSubTask = new SubTask(name, description, this);
        subTaskArrayList.add(newSubTask);
        return newSubTask;
    }

    @Override
    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    @Override
    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public void checkEpicStatus() {
        int countNew = 0;
        int countDone = 0;
        // Нет необходимости считать кол-во в прогрессе, так как если countNew == кол-во задач, то ВСЕ задачи новые,
        // аналогично с countDone все задачи будут ВЫПОЛНЕНЫМИ.

        for (SubTask subTask : subTaskArrayList) {
            if (subTask.getTaskStatus() == TaskStatus.NEW) {
               countNew++;
            } else if (subTask.getTaskStatus() == TaskStatus.DONE) {
                countDone++;
            }
        }

        if (countNew == subTaskArrayList.size()) {
            setTaskStatus(TaskStatus.NEW);
        } else if (countDone == subTaskArrayList.size()) {
            setTaskStatus(TaskStatus.DONE);
        } else {
            setTaskStatus(TaskStatus.IN_PROGRESS);
        }

    }
}
