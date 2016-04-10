package com.fewlaps.quitnowcache;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class SetAndGetValuesTest extends BaseTest {

    QNCache<String> cache;
    MockDateProvider dateProvider;

    @Before
    public void init() {
        cache = new QNCacheBuilder().createQNCache();
        dateProvider = new MockDateProvider();
        cache.setDateProvider(dateProvider);
    }

    @Test
    public void savingSomethingForOneSecondsShouldReturnTheSameImmediatelly() {
        cache.set(A_KEY, A_VALUE, ONE_SECOND);

        assertEquals(A_VALUE, cache.getOptional(A_KEY).get());
    }

    @Test
    public void savingSomethingForeverShouldReturnTheSameImmediatelly() {
        cache.set(A_KEY, A_VALUE, FOREVER);

        assertEquals(A_VALUE, cache.getOptional(A_KEY).get());
    }

    @Test
    public void savingSomethingWithoudSpecifyingTheKeepaliveValueShouldReturnTheSameAfterThreeDays() {
        cache.set(A_KEY, A_VALUE);

        dateProvider.setFixed(threeDaysFromNow());

        assertEquals(A_VALUE, cache.getOptional(A_KEY).get());
    }

    @Test
    public void savingSomethingForeverShouldReturnTheSameAfterThreeDays() {
        cache.set(A_KEY, A_VALUE, FOREVER);

        dateProvider.setFixed(threeDaysFromNow());

        assertEquals(A_VALUE, cache.getOptional(A_KEY).get());
    }

    @Test
    public void savingAValueForTwoHoursShouldReturnNullAfterThreeDays() {
        cache.set(A_KEY, A_VALUE, TWO_HOURS);

        dateProvider.setFixed(threeDaysFromNow());

        assertFalse(cache.getOptional(A_KEY).isPresent());
    }

    @Test
    public void savingSomethingForANegativeTimeWillBeIgnored() {
        cache.set(A_KEY, A_VALUE, -1);
        cache.set(ANOTHER_KEY, ANOTHER_VALUE, ONE_SECOND);

        assertFalse(cache.getOptional(A_KEY).isPresent());
        assertEquals(1, cache.size());
        assertEquals(1, cache.sizeCountingDeadAndAliveElements());
    }

    @Test
    public void gettingAIgnoredValueWithGetAndRemoveIfDeadWorks() {
        cache.set(A_KEY, A_VALUE, -1);
        cache.set(ANOTHER_KEY, ANOTHER_VALUE, THREE_DAYS);

        assertFalse(cache.getOptionalAndRemoveIfDead(A_KEY).isPresent());
        assertEquals(1, cache.size());
        assertEquals(1, cache.sizeCountingDeadAndAliveElements());
    }

    @Test
    public void gettingADeadValueWithGetAndRemoveIfDeadWorks() {
        cache.set(A_KEY, A_VALUE, ONE_SECOND);
        cache.set(ANOTHER_KEY, ANOTHER_VALUE, THREE_DAYS);

        dateProvider.setFixed(twoHoursFromNow());

        assertFalse(cache.getOptionalAndRemoveIfDead(A_KEY).isPresent());
        assertEquals(1, cache.size());
        assertEquals(1, cache.sizeCountingDeadAndAliveElements());
    }

    @Test
    public void gettingAnAliveValueWithGetAndRemoveIfDeadWorks() {
        cache.set(A_KEY, A_VALUE, ONE_SECOND);
        cache.set(ANOTHER_KEY, ANOTHER_VALUE, THREE_DAYS);

        dateProvider.setFixed(twoHoursFromNow());

        assertEquals(ANOTHER_VALUE, cache.getOptionalAndRemoveIfDead(ANOTHER_KEY).get());
        assertEquals(1, cache.size());
        assertEquals(2, cache.sizeCountingDeadAndAliveElements());
    }

    @Test
    public void removingAValueWorks() {
        cache.set(A_KEY, A_VALUE, TWO_HOURS);
        cache.set(ANOTHER_KEY, ANOTHER_VALUE, THREE_DAYS);

        cache.remove(A_KEY);

        assertFalse(cache.getOptional(A_KEY).isPresent());
        assertEquals(ANOTHER_VALUE, cache.getOptional(ANOTHER_KEY).get());
    }

    @Test
    public void removingAllValuesWorks() {
        cache.set(A_KEY, A_VALUE, TWO_HOURS);
        cache.set(ANOTHER_KEY, ANOTHER_VALUE, THREE_DAYS);

        cache.removeAll();

        assertFalse(cache.getOptional(A_KEY).isPresent());
        assertFalse(cache.getOptional(ANOTHER_KEY).isPresent());
    }

    @Test
    public void replacingAValueForANewOneReturnTheNewOne() {
        cache.set(A_KEY, A_VALUE, TWO_HOURS);
        cache.set(A_KEY, ANOTHER_VALUE, THREE_DAYS);

        assertEquals(ANOTHER_VALUE, cache.getOptional(A_KEY).get());
    }

    @Test
    public void replacingAValueForANewOneCanMakeItDead() {
        cache.set(A_KEY, A_VALUE, THREE_DAYS);
        cache.set(A_KEY, ANOTHER_VALUE, ONE_SECOND);

        dateProvider.setFixed(twoHoursFromNow());

        assertFalse(cache.getOptional(A_KEY).isPresent());
    }

    @Test
    public void replacingAValueForNullWillRemoveIt() {
        cache.set(A_KEY, A_VALUE, THREE_DAYS);
        cache.set(A_KEY, null, ONE_SECOND);

        assertFalse(cache.getOptional(A_KEY).isPresent());
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
}
