package com.fewlaps.quitnowcache;

import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SupplierTest {

    private static final String A_KEY = "key";
    private static final String SOME_VALUE = "value";
    private static final String ANOTHER_VALUE = "another_value";

    QNCache<String> cache;
    MockDateProvider dateProvider;

    @Before
    public void init() {
        cache = new QNCacheBuilder().createQNCache();
        dateProvider = new MockDateProvider();
        cache.setDateProvider(dateProvider);
    }

    @Test
    public void testOldValueWhenPresent() throws Exception {
        cache.set(A_KEY, SOME_VALUE);

        String result = cache.getOrElseCache(A_KEY, () -> ANOTHER_VALUE);

        assertEquals(SOME_VALUE, result);
    }

    @Test
    public void testSuppliedValueWhenNotPresent() throws Exception {
        // Empty cache

        String result = cache.getOrElseCache(A_KEY, () -> ANOTHER_VALUE);

        assertEquals(ANOTHER_VALUE, result);
    }

    @Test
    public void testSuppliedIsCached() throws Exception {
        cache.getOrElseCache(A_KEY, () -> ANOTHER_VALUE);

        Optional<String> result = cache.get(A_KEY);

        assertTrue(result.isPresent());
        assertEquals(ANOTHER_VALUE, result.get());
    }
}
