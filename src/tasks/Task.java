package tasks;

import java.time.Instant;
import java.util.Objects;

public class Task {
    protected String name;
    protected String description;
    protected Integer id; // NULL by default
    protected TaskStatus taskStatus;
    protected TasksTypes taskType = TasksTypes.TASK; //for FileBackEndManager better logic;
    protected Instant startTime;
    protected Long duration;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.id = null;
        this.taskStatus = TaskStatus.NEW;
        this.startTime = null;
        this.duration = 0L;
    }

    // Перегрузил конструкторы, чтобы было удобно создавать объекты в файловом менеджере
    public Task(Integer id, TasksTypes taskType, String name, TaskStatus taskStatus, String description,
                Instant startTime, long duration) {
        this.name = name;
        this.taskType = taskType;
        this.description = description;
        this.id = id;
        this.taskStatus = taskStatus;
        this.startTime = startTime;
        this.duration = duration;
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

    public TasksTypes getTaskType() {
        return taskType;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public long getDuration() {
        return duration;
    }

    public Instant getEndTime() {
        if (startTime == null) {return null;}
        long SECONDS_IN_MINUTE = 60L;
        return startTime.plusSeconds(duration * SECONDS_IN_MINUTE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(name, task.name) && Objects.equals(description, task.description)
                && Objects.equals(id, task.id) && taskStatus == task.taskStatus && taskType == task.taskType
                && Objects.equals(startTime, task.startTime) && Objects.equals(duration, task.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, id, taskStatus, taskType, startTime, duration);
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", taskStatus=" + taskStatus +
                ", taskType=" + taskType +
                ", startTime=" + startTime +
                ", duration=" + duration +
                '}';
    }
}
