package com.fewlaps.quitnowcache;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CacheTest extends BaseTest {

    QNCache cache;

    @Before
    public void init() {
        cache = new QNCache();
    }

    @Test
    public void savingSomethingShouldReturnTheSameInmediatelly() {
        cache.set(A_KEY, A_VALUE, FOREVER);
        assertEquals(A_VALUE, cache.get(A_KEY));
    }
}
