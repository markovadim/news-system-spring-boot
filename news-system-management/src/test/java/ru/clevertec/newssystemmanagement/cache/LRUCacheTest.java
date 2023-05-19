package ru.clevertec.newssystemmanagement.cache;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LRUCacheTest {

    private static BaseSystemCache<Long, Integer> cache;

    @BeforeAll
    static void init() {
        cache = new LRUCache<>();
    }

    @Test
    void checkGetShouldReturnNull() {
        cache.put(1L, 111);
        assertNotNull(cache.get(1L));
    }

    @Test
    void checkGetShouldReturnValue() {
        int expectedValue = 111;
        int actualValue = cache.get(1L);

        assertEquals(expectedValue, actualValue);
    }

    @Test
    void checkPutShouldPutValueInCache() {
        cache.put(2L, 222);
        int expectedValue = 222;
        int actualValue = cache.get(2L);

        assertEquals(expectedValue, actualValue);
    }

    @Test
    void checkPutShouldRemoveLaterValueIfSizeBiggerThanCapacity() {
        cache.clear();
        cache.put(1L, 111);
        cache.put(2L, 222);
        cache.put(3L, 333);
        cache.put(4L, 444);
        cache.put(5L, 555);
        cache.put(6L, 666);

        assertFalse(cache.containsKey(1L));
    }

    @Test
    void checkRemoveByKeyShouldRemoveValue111() {
        cache.removeByKey(1L);

        assertFalse(cache.containsKey(1L));
    }

    @Test
    void checkClearShouldClearCache() {
        cache.clear();

        assertEquals(0, cache.size());
    }

    @Test
    void checkSizeShouldReturnSize2() {
        cache.put(1L, 111);
        cache.put(2L, 222);

        int expectedSize = 2;
        int actualSize = cache.size();

        assertEquals(expectedSize, actualSize);
    }

    @Test
    void checkContainsKeyShouldReturnTrue() {
        cache.put(3L, 333);

        assertTrue(cache.containsKey(3L));
    }
}