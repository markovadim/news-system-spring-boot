package ru.clevertec.newssystemmanagement.cache;

import java.util.Comparator;


public class LFUCache<K, V> extends AbstractCache<K, V> implements BaseSystemCache<K, V> {

    public LFUCache() {
        this(DEFAULT_CAPACITY);
    }

    public LFUCache(int capacity) {
        super(capacity);
    }

    @Override
    public V get(K key) {
        Node node = cache.get(key);
        node.counter++;
        changeNodeValueInMap(key, node, node.value);
        logger.info("VALUE FROM CACHE");
        return node.value;
    }

    @Override
    public void put(K key, V value) {
        Node node = cache.get(key);
        if (node != null) {
            changeNodeValueInMap(key, node, value);
        } else {
            putNewNodeInMap(key, value);
        }
        logger.info("THE VALUE HAS BEEN CACHED");
    }

    @Override
    public void removeByKey(K key) {
        cache.remove(key);
    }

    @Override
    public void clear() {
        super.clear();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean containsKey(K key) {
        return cache.containsKey(key);
    }

    public void changeNodeValueInMap(K key, Node node, V value) {
        node.counter++;
        super.changeNodeValueInMap(key, node, value);
    }

    private void putNewNodeInMap(K key, V value) {
        Node node = basePutNewNodeInMap(key, value);
        node.counter++;
        size++;

        if (size > capacity) {
            removeFromChain(tail.prev);
            cache.remove(getKeyLastNode(Comparator.comparing(a -> a.getValue().counter)));
            size--;
        }
    }
}
