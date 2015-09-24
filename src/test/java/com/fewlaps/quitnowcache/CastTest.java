package com.fewlaps.quitnowcache;

import com.fewlaps.quitnowcache.beam.ObjectTestOne;
import com.fewlaps.quitnowcache.beam.ObjectTestTwo;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class CastTest extends BaseTest {
    QNCache cache;

    @Before
    public void init() {
        cache = new QNCacheBuilder().createQNCache();
    }

    @Test
    public void saveObjectAndReturnSameInstance() {
        cache.set(A_KEY, new ObjectTestOne());
        cache.set(ANOTHER_KEY, new ObjectTestTwo());

        assertThat(cache.get(A_KEY), instanceOf(ObjectTestOne.class));
        assertThat(cache.get(ANOTHER_KEY), instanceOf(ObjectTestTwo.class));
    }

    @Test
    public void shouldThrowExceptionOnCastReturnObject() {
        AtomicInteger errors = new AtomicInteger(0);

        cache.set(A_KEY, new ObjectTestOne());
        cache.set(ANOTHER_KEY, new ObjectTestTwo());

        try {
            ObjectTestTwo testTwo = (ObjectTestTwo) cache.get(A_KEY);
        } catch (ClassCastException cle) {
            errors.set(errors.get() + 1);
        }

        try {
            ObjectTestOne testOne = (ObjectTestOne) cache.get(ANOTHER_KEY);
        } catch (ClassCastException cle) {
            errors.set(errors.get() + 1);
        }

        assertEquals(2, errors.get());
    }

    @Test
    public void shouldCastObject() {
        cache.set(A_KEY, new ObjectTestOne());
        cache.set(ANOTHER_KEY, new ObjectTestTwo());

        ObjectTestOne objectTestOne = cache.<ObjectTestOne>get(A_KEY);
        ObjectTestTwo objectTestTwo = cache.<ObjectTestTwo>get(ANOTHER_KEY);

        assertThat(objectTestOne, instanceOf(ObjectTestOne.class));
        assertThat(objectTestTwo, instanceOf(ObjectTestTwo.class));
    }

    @Test
    public void shouldThrowExceptionOnCastWithGenericReturnObject() {
        AtomicInteger errors = new AtomicInteger(0);

        cache.set(A_KEY, new ObjectTestOne());
        cache.set(ANOTHER_KEY, new ObjectTestTwo());

        try {
            ObjectTestTwo testTwo = cache.get(A_KEY);
        } catch (ClassCastException cle) {
            errors.set(errors.get() + 1);
        }

        try {
            ObjectTestOne testOne = cache.get(ANOTHER_KEY);
        } catch (ClassCastException cle) {
            errors.set(errors.get() + 1);
        }

        assertEquals(2, errors.get());
    }
}
