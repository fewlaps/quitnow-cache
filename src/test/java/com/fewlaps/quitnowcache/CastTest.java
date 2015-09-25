package com.fewlaps.quitnowcache;

import com.fewlaps.quitnowcache.beam.ObjectTestOne;
import com.fewlaps.quitnowcache.beam.ObjectTestTwo;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertNotNull;
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

        assertThat(cache.get(A_KEY), instanceOf(ObjectTestOne.class));
    }

    @Test
    public void shouldThrowExceptionOnCastReturnObject() {
        cache.set(A_KEY, new ObjectTestOne());

        Exception e = null;

        try {
            ObjectTestTwo o = (ObjectTestTwo) cache.get(A_KEY);
        } catch (ClassCastException cle) {
            e = cle;
        }

        assertNotNull(e);
    }

    @Test
    public void shouldCastObject() {
        cache.set(A_KEY, new ObjectTestOne());

        ObjectTestOne objectTestOne = cache.get(A_KEY);

        assertThat(objectTestOne, instanceOf(ObjectTestOne.class));
    }

    @Test
    public void shouldThrowExceptionOnCastWithGenericReturnObject() {
        cache.set(A_KEY, new ObjectTestOne());

        Exception e = null;

        try {
            ObjectTestTwo o = cache.get(A_KEY);
        } catch (ClassCastException cle) {
            e = cle;
        }

        assertNotNull(e);
    }
}
