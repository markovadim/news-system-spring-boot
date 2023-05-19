package ru.clevertec.newssystemmanagement.cache;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import ru.clevertec.newssystemmanagement.controllers.CommentController;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;


public abstract class AbstractCache<K, V> {

    final Logger logger = (Logger) LoggerFactory.getLogger(CommentController.class);

    Map<K, Node> cache;
    int capacity;
    int size;
    Node head;
    Node tail;

    public AbstractCache() {
    }

    public AbstractCache(int capacity) {
        this.capacity = capacity;
        cache = new HashMap<>(capacity);

        head = new Node();
        tail = new Node();

        head.next = tail;
        tail.prev = head;
    }

    public class Node {
        V value;
        Node prev;
        Node next;
        LocalDateTime localDateTime;
        K key;
        int counter;
    }

    public void changeNodeValueInMap(K key, Node node, V value){
        node.value = value;
        node.key = key;
        removeFromChain(node);
        cache.put(key, node);
        addToHead(node);
    }

    private void addToHead(Node node) {
        Node currentFirstNode = head.next;

        node.prev = head;
        head.next = node;

        node.next = currentFirstNode;
        currentFirstNode.prev = node;
    }

    public void removeFromChain(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    public Node basePutNewNodeInMap(K key, V value){
        Node node = new Node();
        node.value = value;
        node.key = key;
        cache.put(key, node);
        addToHead(node);
        return node;
    }

    public K getKeyLastNode(Comparator<? super Map.Entry<K, Node>> comparator) {
        return cache.entrySet()
                .stream()
                .sorted(comparator)
                .map(Map.Entry::getKey).findFirst()
                .orElseThrow();
    }

    public void clear(){
        cache.clear();
        head = new Node();
        tail = new Node();

        head.next = tail;
        tail.prev = head;
        size = 0;
    }
}
