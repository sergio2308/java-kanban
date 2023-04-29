package manager;
import task.*;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private final CustomLinkedList<Node> historyList = new CustomLinkedList<>();
    private Map<Integer, Node> history = new HashMap<>();


    class CustomLinkedList<T> {
        public Node head;
        public Node tail;

        private void linkLast(Task task) {
            if (task != null) {
                final Node current = tail;
                final Node newNode = new Node(current, task, null);
                tail = newNode;
                if (head == null)
                    head = newNode;
                else
                    current.next = newNode;
            }
        }
        private List<Task> getTasks() {
            List<Task> tasks = new ArrayList<>();
            Node current = head;
            while (current != null) {
                if (current.task != null) {
                    tasks.add(current.task);
                    current = current.next;
                }
            }
            return tasks;
        }

        public void removeNode(Node node) {
            if (node != null) {
                if (node.equals(head)) {
                    head = node.next;
                    if (head != null) {
                        head.prev = null;
                    }
                } else {
                    node.prev.next = node.next;
                    if (node.next != null) {
                        head.prev = node.prev;
                    }
                }
            }
        }
    }
    @Override
    public void add(Task task) {
        if (task != null) {
            remove(task);
            historyList.linkLast(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyList.getTasks();
    }

    @Override
    public void remove(Task task) {
        if (task != null) {
                historyList.removeNode(history.get(task.getId()));
                history.remove(task.getId());
        }
    }
}