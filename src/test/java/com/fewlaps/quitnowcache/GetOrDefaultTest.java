package com.fewlaps.quitnowcache;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GetOrDefaultTest extends BaseTest {

    QNCache<String> cache;

    @Before
    public void init() {
        cache = new QNCacheBuilder().createQNCache();
    }

    @Test
    public void valueIsReturnedIfItsPresent() {
        cache.set(A_KEY, A_VALUE, ONE_SECOND);

        String result = cache.getOrDefault(A_KEY, "nothing");

        assertEquals(A_VALUE, result);
    }

    @Test
    public void defaultIsReturnedIfValueIsNotPresent() {
        cache.remove(A_KEY);

        String result = cache.getOrDefault(A_KEY, "nothing");

        assertEquals("nothing", result);
    }
}
