package tasks;

import TaskManager.TaskManager.TaskStatus;

public class Task {
    protected String name;
    protected String description;

    protected static Integer id = 0;

    private TaskStatus taskStatus;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        id++;
        this.taskStatus = TaskStatus.NEW;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Integer getId() {
        return id;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }


}
