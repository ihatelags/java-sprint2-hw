package managers;

import managers.interfaces.HistoryManager;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {
    // HashMap для хранения связи id задачи и объекта Node
    private final HashMap<Integer, Node<Task>> nodeMap = new HashMap<>();
    private Node<Task> head;
    private Node<Task> tail;

    @Override
    public List<Task> getHistory() {
        List<Task> outputOfTasks = new ArrayList<>();
        Node<Task> node = head;
        while (node != null) {
            outputOfTasks.add(node.data);
            node = node.next;
        }
        return outputOfTasks;
    }

    @Override
    public void add(Task task) {
        final Node<Task> node = nodeMap.get(task.getId());
        if (node != null) {
            removeNode(node);
        }
        linkLast(task);

    }

    @Override
    public void remove(Task task) {
        final Node<Task> node = nodeMap.get(task.getId());
        if (node != null) {
            removeNode(node);
        }
    }

    /**
     * Создает node с таском в конце списка.
     */
    public void linkLast(Task task) {
        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<>(oldTail, task, null);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
        nodeMap.put(task.getId(), newNode);
    }

    /**
     * Удаляет узел двусвязного списка
     */
    private void removeNode(Node<Task> node) {
        if (node != null) {
            Node<Task> prevNode = node.prev;
            Node<Task> nextNode = node.next;
            if (prevNode != null) {
                prevNode.next = nextNode;
            } else {
                head = nextNode;
            }
            if (nextNode != null) {
                nextNode.prev = prevNode;
            } else {
                tail = prevNode;
            }
        }
    }

    @Override
    public void update(Task task) {
        Node<Task> oldNode = nodeMap.get(task.getId());
        if (oldNode != null) {
            Node<Task> newNode = new Node<>(oldNode.prev, task, oldNode.next);
            nodeMap.put(task.getId(), newNode);
            if (tail == oldNode) tail = newNode;
            if (head == oldNode) head = newNode;
        }
    }

    /**
     * Вспомогательный класс для работы двусвязного списка
     */
    private static class Node<E> {
        E data;
        Node<E> next;
        Node<E> prev;

        Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }

}