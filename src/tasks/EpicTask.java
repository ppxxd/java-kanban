package tasks;

import java.util.ArrayList;

public class EpicTask extends Task {
    private final ArrayList<Integer> subTasksIDs;

    public EpicTask(String name, String description) {
        super(name, description);
        subTasksIDs = new ArrayList<>();
        taskStatus = TaskStatus.NEW;
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
