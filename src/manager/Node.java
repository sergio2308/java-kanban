package manager;

public class Node<E> {
    public E task;
    public Node<E> next;
    public Node<E> prev;

    public Node(E task, Node<E> next, Node<E> prev) {
        this.task = task;
        this.next = next;
        this.prev = prev;
    }
}
