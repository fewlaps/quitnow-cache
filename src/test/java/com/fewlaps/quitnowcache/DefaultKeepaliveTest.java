package com.fewlaps.quitnowcache;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DefaultKeepaliveTest extends BaseTest {

    @Test
    public void shouldHaveInfiniteKeepaliveByDefault() {
        QNCache<String> cache = new QNCacheBuilder().createQNCache();
        MockDateProvider dateProvider = new MockDateProvider();
        cache.setDateProvider(dateProvider);

        cache.set(A_KEY, A_VALUE);

        dateProvider.setFixed(threeDaysFromNow());

        assertNotNull(cache.get(A_KEY).get());
    }

    @Test
    public void testOneSecondKeepaliveRightNow() {
        QNCache<String> cache = new QNCacheBuilder().setDefaultKeepaliveInMillis(ONE_SECOND).createQNCache();
        MockDateProvider dateProvider = new MockDateProvider();
        cache.setDateProvider(dateProvider);

        cache.set(A_KEY, A_VALUE);

        assertNotNull(cache.get(A_KEY).get());
    }

    @Test
    public void testOneSecondKeepaliveAfterOneSecond() {
        QNCache<String> cache = new QNCacheBuilder().setDefaultKeepaliveInMillis(ONE_SECOND).createQNCache();
        MockDateProvider dateProvider = new MockDateProvider();
        cache.setDateProvider(dateProvider);

        cache.set(A_KEY, A_VALUE);

        dateProvider.setFixed(oneSecondFromNow());

        assertFalse(cache.get(A_KEY).isPresent());
    }

    @Test
    public void testOneSecondKeepaliveAfterTwoHours() {
        QNCache<String> cache = new QNCacheBuilder().setDefaultKeepaliveInMillis(ONE_SECOND).createQNCache();
        MockDateProvider dateProvider = new MockDateProvider();
        cache.setDateProvider(dateProvider);

        cache.set(A_KEY, A_VALUE);

        dateProvider.setFixed(twoHoursFromNow());

        assertFalse(cache.get(A_KEY).isPresent());
    }

    @Test
    public void testTwoSecondsKeepaliveAfterOneSecond() {
        QNCache<String> cache = new QNCacheBuilder().setDefaultKeepaliveInMillis(2 * ONE_SECOND).createQNCache();
        MockDateProvider dateProvider = new MockDateProvider();
        cache.setDateProvider(dateProvider);

        cache.set(A_KEY, A_VALUE);

        dateProvider.setFixed(oneSecondFromNow());

        assertNotNull(cache.get(A_KEY).get());
    }
}
