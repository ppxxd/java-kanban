package taskmanager;

import java.util.List;
import java.util.ArrayList;
import tasks.Task;

public class InMemoryHistoryManager implements HistoryManager {

    private final static int SIZE_LIST = 10;
    private final List<Task> historyList = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (task != null) {
            historyList.add(task);
            if (historyList.size() > SIZE_LIST) {
                historyList.remove(0);
            }
        }
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(historyList);
    }
}
