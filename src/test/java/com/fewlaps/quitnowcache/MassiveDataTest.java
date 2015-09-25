package com.fewlaps.quitnowcache;

import org.joda.time.DateTimeUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MassiveDataTest extends BaseTest {
    QNCache cache;

    @Before
    public void init() {
        DateTimeUtils.setCurrentMillisSystem();
        cache = new QNCacheBuilder().createQNCache();
    }

    @Test
    public void worksWithLotsOfEntries() {
        int iterations = 10000;
        for (int i = 0; i < iterations; i++) {
            cache.set(Integer.valueOf(i).toString(), i, 0);
        }
        for (Integer i = 0; i < iterations; i++) {
            assertEquals(i, cache.get(Integer.valueOf(i).toString()));
        }
    }
}
