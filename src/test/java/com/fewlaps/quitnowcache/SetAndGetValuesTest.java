package com.fewlaps.quitnowcache;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class SetAndGetValuesTest extends BaseTest {

    QNCache<String> cache;
    MockDateProvider dateProvider;

    @Before
    public void init() {
        cache = new QNCache.Builder().build();
        dateProvider = new MockDateProvider();
        cache.setDateProvider(dateProvider);
    }

    @Test
    public void savingSomethingForOneSecondsShouldReturnTheSameImmediatelly() {
        cache.set(A_KEY, A_VALUE, ONE_SECOND);

        assertEquals(A_VALUE, cache.get(A_KEY));
    }

    @Test
    public void savingSomethingForeverShouldReturnTheSameImmediatelly() {
        cache.set(A_KEY, A_VALUE, FOREVER);

        assertEquals(A_VALUE, cache.get(A_KEY));
    }

    @Test
    public void savingSomethingWithoudSpecifyingTheKeepaliveValueShouldReturnTheSameAfterThreeDays() {
        cache.set(A_KEY, A_VALUE);

        dateProvider.setFixed(threeDaysFromNow());

        assertEquals(A_VALUE, cache.get(A_KEY));
    }

    @Test
    public void savingSomethingForeverShouldReturnTheSameAfterThreeDays() {
        cache.set(A_KEY, A_VALUE, FOREVER);

        dateProvider.setFixed(threeDaysFromNow());

        assertEquals(A_VALUE, cache.get(A_KEY));
    }

    @Test
    public void savingAValueForTwoHoursShouldReturnNullAfterThreeDays() {
        cache.set(A_KEY, A_VALUE, TWO_HOURS);

        dateProvider.setFixed(threeDaysFromNow());

        assertNull(cache.get(A_KEY));
    }

    @Test
    public void savingSomethingForANegativeTimeWillBeIgnored() {
        cache.set(A_KEY, A_VALUE, -1);
        cache.set(ANOTHER_KEY, ANOTHER_VALUE, ONE_SECOND);

        assertNull(cache.get(A_KEY));
        assertEquals(1, cache.size());
        assertEquals(1, cache.sizeDeadAndAliveElements());
    }

    @Test
    public void gettingAIgnoredValueWithGetAndRemoveIfDeadWorks() {
        cache.set(A_KEY, A_VALUE, -1);
        cache.set(ANOTHER_KEY, ANOTHER_VALUE, THREE_DAYS);

        assertNull(cache.getAndPurgeIfDead(A_KEY));
        assertEquals(1, cache.size());
        assertEquals(1, cache.sizeDeadAndAliveElements());
    }

    @Test
    public void gettingADeadValueWithGetAndRemoveIfDeadWorks() {
        cache.set(A_KEY, A_VALUE, ONE_SECOND);
        cache.set(ANOTHER_KEY, ANOTHER_VALUE, THREE_DAYS);

        dateProvider.setFixed(twoHoursFromNow());

        assertNull(cache.getAndPurgeIfDead(A_KEY));
        assertEquals(1, cache.size());
        assertEquals(1, cache.sizeDeadAndAliveElements());
    }

    @Test
    public void gettingAnAliveValueWithGetAndRemoveIfDeadWorks() {
        cache.set(A_KEY, A_VALUE, ONE_SECOND);
        cache.set(ANOTHER_KEY, ANOTHER_VALUE, THREE_DAYS);

        dateProvider.setFixed(twoHoursFromNow());

        assertEquals(ANOTHER_VALUE, cache.getAndPurgeIfDead(ANOTHER_KEY));
        assertEquals(1, cache.size());
        assertEquals(2, cache.sizeDeadAndAliveElements());
    }

    @Test
    public void removingAValueWorks() {
        cache.set(A_KEY, A_VALUE, TWO_HOURS);
        cache.set(ANOTHER_KEY, ANOTHER_VALUE, THREE_DAYS);

        cache.remove(A_KEY);

        assertNull(cache.get(A_KEY));
        assertEquals(ANOTHER_VALUE, cache.get(ANOTHER_KEY));
    }

    @Test
    public void removingAllValuesWorks() {
        cache.set(A_KEY, A_VALUE, TWO_HOURS);
        cache.set(ANOTHER_KEY, ANOTHER_VALUE, THREE_DAYS);

        cache.clear();

        assertNull(cache.get(A_KEY));
        assertNull(cache.get(ANOTHER_KEY));
    }

    @Test
    public void replacingAValueForANewOneReturnTheNewOne() {
        cache.set(A_KEY, A_VALUE, TWO_HOURS);
        cache.set(A_KEY, ANOTHER_VALUE, THREE_DAYS);

        assertEquals(ANOTHER_VALUE, cache.get(A_KEY));
    }

    @Test
    public void replacingAValueForANewOneCanMakeItDead() {
        cache.set(A_KEY, A_VALUE, THREE_DAYS);
        cache.set(A_KEY, ANOTHER_VALUE, ONE_SECOND);

        dateProvider.setFixed(twoHoursFromNow());

        assertNull(cache.get(A_KEY));
    }

    @Test
    public void replacingAValueForNullWillRemoveIt() {
        cache.set(A_KEY, A_VALUE, THREE_DAYS);
        cache.set(A_KEY, null, ONE_SECOND);

        assertNull(cache.get(A_KEY));
    }

    @Test
    public void containsReturnsTrueIfSomethingExists() {
        cache.set(A_KEY, A_VALUE, THREE_DAYS);

        assertTrue(cache.contains(A_KEY));
    }

    @Test
    public void containsReturnsFalseSomethingDoesntExist() {
        assertFalse(cache.contains(A_KEY));
    }

    @Test
    public void savingSomethingForOneSecondsShouldReturnTheSameImmediatelly_usingSecondsTimeUnit() {
        cache.set(A_KEY, A_VALUE, 1, TimeUnit.SECONDS);

        assertEquals(A_VALUE, cache.get(A_KEY));
    }

    @Test
    public void savingSomethingForOneSecondsShouldReturnTheSameImmediatelly_usingMillisTimeUnit() {
        cache.set(A_KEY, A_VALUE, 1000, TimeUnit.MILLISECONDS);

        assertEquals(A_VALUE, cache.get(A_KEY));
    }
}
