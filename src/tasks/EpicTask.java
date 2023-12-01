package tasks;

import java.util.ArrayList;

public class EpicTask extends Task {
    private ArrayList<Integer> subTasksIDs;

    public EpicTask(String name, String description) {
        super(name, description);
        subTasksIDs = new ArrayList<>();
        this.taskStatus = TaskStatus.NEW;
    }

    public ArrayList<Integer> getSubTaskArrayList() {
        return subTasksIDs;
    }

    public void clearSubTaskArrayList() {
        subTasksIDs.clear();
    }

    public void addSubTaskToArrayList(Integer id) {
        this.subTasksIDs.add(id);
    }

    public void removeSubTaskFromArray(SubTask subTask) {
        subTasksIDs.remove(subTask.getId());
    }

    public SubTask createSubTask(String name, String description) {
        //Добавляем айди сабТаски после определения ей её айди в Менеджере (метод createSubTask)
        return new SubTask(name, description, this.id);
    }

}
