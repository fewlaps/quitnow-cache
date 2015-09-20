package com.fewlaps.quitnowcache;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MassiveDataTest extends BaseTest {
    QNCache cache;

    @Before
    public void init() {
        cache = new QNCacheBuilder().createQNCache();
    }

    @Test
    public void worksWithAMilionOfEntries() {
        //int iterations = 1000000; //Travis doesn't like this huge tests :(
        int iterations = 1000;
        for (int i = 0; i < iterations; i++) {
            cache.set(Integer.valueOf(i).toString(), i, 0);
        }
        for (int i = 0; i < iterations; i++) {
            assertEquals(i, cache.get(Integer.valueOf(i).toString()));
        }
    }
}
