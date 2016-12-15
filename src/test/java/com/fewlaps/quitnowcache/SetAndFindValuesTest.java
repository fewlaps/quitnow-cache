package com.fewlaps.quitnowcache;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class SetAndFindValuesTest extends BaseTest {

    QNCache<String> cache;
    MockDateProvider dateProvider;

    @Before
    public void init() {
        cache = new QNCacheBuilder().createQNCache();
        dateProvider = new MockDateProvider();
        cache.setDateProvider(dateProvider);
    }

    @Test
    public void setAndFindisSave() {
        cache.set(A_KEY, A_VALUE);
        cache.set(ANOTHER_KEY, ANOTHER_VALUE, THREE_DAYS);
        cache.clear();

        assertTrue(0 == cache.findStartsWith(A_KEY).size());
    }
    
    @Test
    public void setAndFindNone() {
        cache.set(A_KEY, A_VALUE);
        cache.set(ANOTHER_KEY, ANOTHER_VALUE, THREE_DAYS);

        assertTrue(0 == cache.findStartsWith(A_VALUE).size());
    }
    
    @Test
    public void setAndFindOne() {
        cache.set(A_KEY, A_VALUE);
        cache.set(ANOTHER_KEY, ANOTHER_VALUE, THREE_DAYS);

        assertTrue(1 == cache.findStartsWith(A_KEY).size());
    }

    @Test
    public void setAndFindMoreThanOne() {
        cache.set(A_KEY, A_VALUE);
        cache.set(ANOTHER_KEY, ANOTHER_VALUE, THREE_DAYS);

        assertTrue(1 < cache.findStartsWith("a").size());
    }
 
    
    @Test
    public void setAndFindIfAlive() {
        cache.set(A_KEY, A_VALUE, -1);
        cache.set(ANOTHER_KEY, ANOTHER_VALUE, THREE_DAYS);

        assertTrue(0 == cache.findStartsWithIfAlive(A_KEY).size());
        assertTrue(1 == cache.findStartsWithIfAlive(ANOTHER_KEY).size());
    }

}
