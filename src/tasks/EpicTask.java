package tasks;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Objects;

public class EpicTask extends Task {
    private final ArrayList<Integer> subTasksIDs;
    private Instant endTime;

    public EpicTask(String name, String description) {
        super(name, description);
        subTasksIDs = new ArrayList<>();
        taskStatus = TaskStatus.NEW;
        this.taskType = TasksTypes.EPICTASK;
    }

    // Перегрузил конструкторы, чтобы было удобно создавать объекты в файловом менеджере
    public EpicTask(Integer id, TasksTypes taskType, String name, TaskStatus taskStatus, String description,
                    Instant startTime, long duration) {
        super(id, taskType, name, taskStatus, description, startTime, duration);
        this.endTime = super.getEndTime();
        subTasksIDs = new ArrayList<>();
    }

    public ArrayList<Integer> getSubTaskArrayList() {
        return subTasksIDs;
    }

    public void clearSubTaskArrayList() {
        subTasksIDs.clear();
    }

    public void addSubTaskToArrayList(Integer id) {
        subTasksIDs.add(id);
    }

    public void removeSubTaskFromArray(SubTask subTask) {
        subTasksIDs.remove(subTask.getId());
    }

    public SubTask createSubTask(String name, String description) {
        //Добавляем айди сабТаски после определения ей её айди в Менеджере (метод createSubTask)
        return new SubTask(name, description, id);
    }

    @Override
    public Instant getEndTime() {
        return endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        EpicTask epicTask = (EpicTask) o;
        return Objects.equals(subTasksIDs, epicTask.subTasksIDs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subTasksIDs);
    }

    @Override
    public String toString() {
        return "EpicTask{" +
                "subTasksIDs=" + subTasksIDs +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", taskStatus=" + taskStatus +
                '}';
    }
}
