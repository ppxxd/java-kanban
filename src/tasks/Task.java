package tasks;

import java.util.Objects;

public class Task {
    protected String name;
    protected String description;
    protected Integer id; // NULL by default
    protected TaskStatus taskStatus;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.id = null;
        this.taskStatus = TaskStatus.NEW;
    }

    // Перегрузил конструкторы, чтобы было удобно создавать объекты в файловом менеджере
    public Task(Integer id, String name, TaskStatus taskStatus, String description) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.taskStatus = taskStatus;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setId(Integer id) {
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(name, task.name) && Objects.equals(description, task.description)
                && Objects.equals(id, task.id) && taskStatus == task.taskStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, id, taskStatus);
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", taskStatus=" + taskStatus +
                '}';
    }
}
