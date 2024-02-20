package tasks;

import java.time.Instant;
import java.util.Objects;

public class SubTask extends Task {
    private final Integer epicTaskID;

    public SubTask(String name, String description, Integer epicTaskID) {
        super(name, description);
        this.epicTaskID = epicTaskID;
        this.taskType = TasksTypes.SUBTASK;
    }

    // Перегрузил конструкторы, чтобы было удобно создавать объекты в файловом менеджере
    public SubTask(Integer id, TasksTypes taskType, String name, TaskStatus taskStatus, String description,
                   Integer epicTaskID, Instant startTime, long duration) {
        super(id, taskType, name, taskStatus, description, startTime, duration);
        this.epicTaskID = epicTaskID;
    }

    public Integer getEpicTaskID() {
        return epicTaskID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SubTask subTask = (SubTask) o;
        return Objects.equals(epicTaskID, subTask.epicTaskID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicTaskID);
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "epicTaskID=" + epicTaskID +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", taskStatus=" + taskStatus +
                '}';
    }
}
