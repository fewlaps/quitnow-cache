package com.fewlaps.quitnowcache;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SizeTest extends BaseTest {

    QNCache<String> cache;
    MockDateProvider dateProvider;

    @Before
    public void init() {
        cache = new QNCache.Builder().build();
        dateProvider = new MockDateProvider();
        cache.setDateProvider(dateProvider);
    }

    @Test
    public void sizeWorksForZeroElements() {
        assertTrue(cache.isEmpty());
        assertEquals(0, cache.size());
    }

    @Test
    public void sizeWorksForOneElement() {
        cache.set(A_KEY, A_VALUE, ONE_SECOND);

        assertFalse(cache.isEmpty());
        assertEquals(1, cache.size());
    }

    @Test
    public void sizeWorksForTwoElements() {
        cache.set(A_KEY, A_VALUE, ONE_SECOND);
        cache.set(ANOTHER_KEY, ANOTHER_VALUE, THREE_DAYS);

        assertFalse(cache.isEmpty());
        assertEquals(2, cache.size());
    }

    @Test
    public void sizeWorksAfterRemovingAllTheElements() {
        cache.set(A_KEY, A_VALUE, ONE_SECOND);
        cache.set(ANOTHER_KEY, ANOTHER_VALUE, THREE_DAYS);

        cache.clear();

        assertNull(cache.get(A_KEY));
        assertNull(cache.get(ANOTHER_KEY));

        assertEquals(0, cache.size());
        assertEquals(0, cache.sizeAliveElements());
        assertEquals(0, cache.sizeDeadElements());
        assertEquals(0, cache.sizeDeadAndAliveElements());
        assertTrue(cache.isEmpty());
    }

    @Test
    public void sizeWorksAfterRemovingOldElements() {
        cache.set(A_KEY, A_VALUE, ONE_SECOND);
        cache.set(ANOTHER_KEY, ANOTHER_VALUE, THREE_DAYS);
        dateProvider.setFixed(twoHoursFromNow());

        cache.purge();

        assertNull(cache.get(A_KEY));
        assertEquals(ANOTHER_VALUE, cache.get(ANOTHER_KEY));

        assertEquals(1, cache.size());
        assertEquals(1, cache.sizeAliveElements());
        assertEquals(0, cache.sizeDeadElements());
        assertEquals(1, cache.sizeDeadAndAliveElements());
        assertFalse(cache.isEmpty());
    }
}
