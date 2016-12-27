package com.fewlaps.quitnowcache;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class KeySetTest extends BaseTest {

    QNCache<String> cache;
    MockDateProvider dateProvider;

    @Before
    public void init() {
        cache = new QNCacheBuilder().createQNCache();
        dateProvider = new MockDateProvider();
        cache.setDateProvider(dateProvider);
    }

    @Test
    public void keySetIsEmptyWhenCacheHasBeenJustCreated() {
        assertEquals(0, cache.keySet().size());
    }

    @Test
    public void keySetReturnTheSameNumberAsAddedItems() {
        cache.set(A_KEY, A_VALUE);
        cache.set(ANOTHER_KEY, ANOTHER_VALUE, THREE_DAYS);

        assertEquals(2, cache.keySet().size());
    }

    @Test
    public void keySetAliveReturnsOnlyAliveKeys() {
        cache.set(A_KEY, A_VALUE);
        cache.set(ANOTHER_KEY, ANOTHER_VALUE, ONE_SECOND);

        dateProvider.setFixed(threeDaysFromNow());

        assertEquals(1, cache.keySetAlive().size());
    }

    @Test
    public void keySetIsEmptyWhenClearingTheCache() {
        cache.set(A_KEY, A_VALUE);
        cache.set(ANOTHER_KEY, ANOTHER_VALUE, THREE_DAYS);
        cache.clear();

        assertEquals(0, cache.keySetStartingWith(A_KEY).size());
    }

    @Test
    public void setAndFindNone() {
        cache.set(A_KEY, A_VALUE);
        cache.set(ANOTHER_KEY, ANOTHER_VALUE, THREE_DAYS);

        assertEquals(0, cache.keySetStartingWith(A_VALUE).size());
    }

    @Test
    public void setAndFindOne() {
        cache.set(A_KEY, A_VALUE);
        cache.set(ANOTHER_KEY, ANOTHER_VALUE, THREE_DAYS);

        assertEquals(1, cache.keySetStartingWith(A_KEY).size());
    }

    @Test
    public void setAndFindMoreThanOne() {
        cache.set(A_KEY, A_VALUE);
        cache.set(ANOTHER_KEY, ANOTHER_VALUE, THREE_DAYS);

        assertEquals(2, cache.keySetStartingWith(JUST_A).size());
    }

    @Test
    public void setAndFindIfAlive() {
        cache.set(A_KEY, A_VALUE, -1);
        cache.set(ANOTHER_KEY, ANOTHER_VALUE, THREE_DAYS);

        assertEquals(0, cache.keySetAliveStartingWith(A_KEY).size());
        assertEquals(1, cache.keySetAliveStartingWith(ANOTHER_KEY).size());
    }

    @Test
    public void keySetAlive() {
        cache.set(A_KEY, A_VALUE);
        cache.set(ANOTHER_KEY, ANOTHER_VALUE, ONE_SECOND);

        dateProvider.setFixed(threeDaysFromNow());

        assertTrue(cache.isKeyAlive(A_KEY));
        assertFalse(cache.isKeyAlive(ANOTHER_KEY));
    }

}
