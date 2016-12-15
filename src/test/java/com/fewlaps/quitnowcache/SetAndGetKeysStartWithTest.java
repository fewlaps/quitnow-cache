package com.fewlaps.quitnowcache;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class SetAndGetKeysStartWithTest extends BaseTest {

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

        assertEquals(0, cache.listCachedKeysStartingWith(A_KEY).size());
    }
    
    @Test
    public void setAndFindNone() {
        cache.set(A_KEY, A_VALUE);
        cache.set(ANOTHER_KEY, ANOTHER_VALUE, THREE_DAYS);

        assertEquals(0, cache.listCachedKeysStartingWith(A_VALUE).size());
    }
    
    @Test
    public void setAndFindOne() {
        cache.set(A_KEY, A_VALUE);
        cache.set(ANOTHER_KEY, ANOTHER_VALUE, THREE_DAYS);

        assertEquals(1, cache.listCachedKeysStartingWith(A_KEY).size());
    }

    @Test
    public void setAndFindMoreThanOne() {
        cache.set(A_KEY, A_VALUE);
        cache.set(ANOTHER_KEY, ANOTHER_VALUE, THREE_DAYS);

        assertEquals(2, cache.listCachedKeysStartingWith(JUST_A).size());
    }
    
    @Test
    public void setAndFindIfAlive() {
        cache.set(A_KEY, A_VALUE, -1);
        cache.set(ANOTHER_KEY, ANOTHER_VALUE, THREE_DAYS);

        assertEquals(0, cache.listCachedKeysStartingWithIfAlive(A_KEY).size());
        assertEquals(1, cache.listCachedKeysStartingWithIfAlive(ANOTHER_KEY).size());
    }

}
