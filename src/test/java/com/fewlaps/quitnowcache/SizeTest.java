package com.fewlaps.quitnowcache;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SizeTest extends BaseTest {

    QNCache cache;
    MockDateProvider dateProvider;

    @Before
    public void init() {
        cache = new QNCacheBuilder().createQNCache();
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

        cache.removeAll();

        assertNull(cache.get(A_KEY));
        assertNull(cache.get(ANOTHER_KEY));

        assertEquals(0, cache.size());
        assertEquals(0, cache.sizeCountingDeadAndAliveElements());
        assertEquals(0, cache.sizeCountingOnlyAliveElements());
        assertTrue(cache.isEmpty());
    }

    @Test
    public void sizeWorksAfterRemovingOldElements() {
        cache.set(A_KEY, A_VALUE, ONE_SECOND);
        cache.set(ANOTHER_KEY, ANOTHER_VALUE, THREE_DAYS);
        dateProvider.setFixed(twoHoursFromNow());

        cache.removeTooOldValues();

        assertNull(cache.get(A_KEY));
        assertEquals(ANOTHER_VALUE, cache.get(ANOTHER_KEY));

        assertEquals(1, cache.size());
        assertEquals(1, cache.sizeCountingDeadAndAliveElements());
        assertEquals(1, cache.sizeCountingOnlyAliveElements());
        assertFalse(cache.isEmpty());
    }
}
