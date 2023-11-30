package tasks;

import TaskManager.TaskManager.TaskStatus;

public class SubTask extends Task {
    private final EpicTask epicTask;

    private TaskStatus taskStatus;

    public SubTask(String name, String description, EpicTask epicTask) {
        super(name, description);
        this.epicTask = epicTask;
        this.taskStatus = TaskStatus.NEW;
    }

    public EpicTask getEpicTask() {
        return epicTask;
    }

    @Override
    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    @Override
    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
        epicTask.checkEpicStatus();
        //Если обновляется sub, то проверяем не надо ли обновить epic
        // и обновляем, если надо.
    }

}
