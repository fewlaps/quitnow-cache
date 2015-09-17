package com.fewlaps.quitnowcache;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CacheTest {

    public static final String A_KEY = "akey";
    public static final String A_VALUE = "avalue";

    public static final long FOREVER = 0;

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
