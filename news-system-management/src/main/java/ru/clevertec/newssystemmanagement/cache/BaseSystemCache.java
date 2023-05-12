package ru.clevertec.newssystemmanagement.cache;

/**
 * @param <K> - type of entity id
 * @param <V> - entity class
 */
public interface BaseSystemCache<K, V> {

    int DEFAULT_CAPACITY = 5;

    /**
     * @param key - entity id
     * @return value (object)
     */
    V get(K key);

    /**
     * @param key   - entity id
     * @param value - object which need put in cache
     */
    void put(K key, V value);

    /**
     * cache cleaning
     */
    void clear();

    /**
     * @return object count in cache
     */
    int size();

    /**
     * @param key - entity id
     */
    void removeByKey(K key);

    /**
     * @param key - entity id
     * @return tru if cache has object with id = key
     */
    boolean containsKey(K key);
}
