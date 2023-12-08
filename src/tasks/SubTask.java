package tasks;

public class SubTask extends Task {
    private final Integer epicTaskID;

    public SubTask(String name, String description, Integer epicTaskID) {
        super(name, description);
        this.epicTaskID = epicTaskID;
        this.taskStatus = TaskStatus.NEW;
    }

    public Integer getEpicTaskID() {
        return epicTaskID;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
