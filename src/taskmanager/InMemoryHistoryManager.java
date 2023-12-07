package taskmanager;

import java.util.List;
import java.util.ArrayList;
import tasks.Task;

public class InMemoryHistoryManager implements HistoryManager {

    private final static int SIZE_LIST = 10;
    private List<Task> historyList = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (task != null) {
            this.historyList.add(task);
        }
        if (this.historyList.size() > SIZE_LIST) {
            this.historyList.remove(0);
        }
    }

    @Override
    public List<Task> getHistory() {
        return this.historyList;
    }
}
