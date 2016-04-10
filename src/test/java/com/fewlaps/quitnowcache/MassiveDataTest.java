package com.fewlaps.quitnowcache;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MassiveDataTest extends BaseTest {
    QNCache<Integer> cache;

    @Before
    public void init() {
        cache = new QNCacheBuilder().createQNCache();
    }

    @Test
    public void worksWithLotsOfEntries() {
        int iterations = 10000;
        for (int i = 0; i < iterations; i++) {
            cache.set(String.valueOf(i), i, 0);
        }
        for (Integer i = 0; i < iterations; i++) {
            assertEquals(i, cache.get(String.valueOf(i)).get());
        }
    }
}
