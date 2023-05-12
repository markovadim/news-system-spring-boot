package ru.clevertec.newssystemmanagement.cache;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LFUCacheTest {

    private static BaseSystemCache<Long, Integer> cache;

    @BeforeAll
    static void init() {
        cache = new LFUCache<>();
    }

    @Test
    void checkGetShouldThrowNPE() {
        cache.clear();
        assertThrows(NullPointerException.class, () -> cache.get(1L));
    }

    @Test
    void checkGetShouldReturn1() {
        cache.put(1L, 1);
        int expectedValue = 1;
        int actualValue = cache.get(1L);

        assertEquals(expectedValue, actualValue);
    }

    @Test
    void checkGetShouldRemoveLeastFrequentlyUsed() {
        cache.clear();
        cache.put(1L, 1);
        cache.put(2L, 2);
        cache.put(3L, 3);
        cache.put(4L, 4);
        cache.put(5L, 5);

        cache.get(1L);
        cache.get(2L);
        cache.get(4L);
        cache.get(5L);
        cache.put(6L, 6);

        assertThrows(NullPointerException.class, () -> cache.get(3L));
    }

    @Test
    void checkPutShouldPutValueInCache() {
        cache.put(5L, 5);

        assertTrue(cache.containsKey(5L));
    }

    @Test
    void checkPutShouldUpdateValueInCache() {
        cache.put(5L, 5);
        cache.put(5L, 555);

        int expectedValue = 555;
        int actualValue = cache.get(5L);

        assertEquals(expectedValue, actualValue);
    }

    @Test
    void CheckRemoveByKeyShouldRemoveValueFromCache() {
        cache.put(6L, 6);

        cache.removeByKey(6L);

        assertFalse(cache.containsKey(6L));
    }

    @Test
    void checkClearShouldReturnSize0() {
        cache.clear();

        assertEquals(0, cache.size());
    }
}