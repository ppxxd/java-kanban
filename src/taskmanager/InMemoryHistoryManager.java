package taskmanager;

import java.util.List;
import java.util.ArrayList;
import tasks.Task;
import java.util.Map;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {

    private List<Task> historyList = new ArrayList<>();
    private final Map<Integer, Node> customLinkedList = new HashMap<>();

    private Node firstNode;
    private Node lastNode;

    public static class Node {
        private Node previous;
        private Node next;
        private final Task value;

        public Node(Node previous, Task value, Node next) {
            this.previous = previous;
            this.value = value;
            this.next = next;
        }

    }

    public Node linkLast(Task task) {
        final Node previuosLast = lastNode;
        final Node newLast = new Node (previuosLast, task, null);
        lastNode = newLast;

        if (previuosLast == null) {
            firstNode = newLast;
        } else {
            previuosLast.next = newLast;
        }

        return newLast;
    }

    private void removeNode(Node node) {

        if (node == lastNode) { //clearing last
            if (lastNode.previous != null) {
                lastNode = lastNode.previous;
            } else {
                lastNode = null;
            }
        }

        if (node == firstNode) { //clearing first
            if (firstNode.next != null) {
                firstNode = firstNode.next;
            } else {
                firstNode = null;
            }
        }

        if (node.previous != null) { //cutting between
            node.previous.next = node.next;
            if (node.next != null) {
                node.next.previous = node.previous;
            }
        }
    }

    @Override
    public void add(Task task) {
        if (customLinkedList.containsKey(task.getId())) {
            removeNode(customLinkedList.get(task.getId()));
            customLinkedList.remove(task.getId());
        }

        Node newNode = linkLast(task);
        customLinkedList.put(task.getId(), newNode);
    }

    @Override
    public void remove(int id) {
         if (customLinkedList.containsKey(id)) {
             removeNode(customLinkedList.get(id));
             customLinkedList.remove(id);
         }
    }

    @Override
    public List<Task> getHistory() {
        historyList = getTasks();
        return new ArrayList<>(historyList);
    }

    private List<Task> getTasks() {
        ArrayList<Task> tasksArrayList = new ArrayList<>();
        Node node = firstNode;
        while (node != null) {
            tasksArrayList.add(node.value);
            node = node.next;
        }
        return tasksArrayList;
    }
}
