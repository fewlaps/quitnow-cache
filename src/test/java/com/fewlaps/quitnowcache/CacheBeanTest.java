package com.fewlaps.quitnowcache;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CacheBeanTest extends BaseTest {

    @Test
    public void aJustCreatedBeanWithMaxKeepaliveIsAliveRightNow() {
        QNCacheBean<Object> bean = new QNCacheBean<Object>(A_VALUE, now(), FOREVER);
        assertTrue(bean.isAlive(now()));
    }

    @Test
    public void aJustCreatedBeanWithMaxKeepaliveIsAliveAfterThreeDays() {
        QNCacheBean<Object> bean = new QNCacheBean<Object>(A_VALUE, now(), FOREVER);
        assertTrue(bean.isAlive(threeDaysFromNow()));
    }

    @Test
    public void aJustCreatedBeanWithOneSecondOfKeepaliveIsAliveRightNow() {
        QNCacheBean<Object> bean = new QNCacheBean<Object>(A_VALUE, now(), ONE_SECOND);
        assertTrue(bean.isAlive(now()));
    }

    @Test
    public void aJustCreatedBeanWithTwoHoursOfKeepaliveIsAliveAfterASecond() {
        QNCacheBean<Object> bean = new QNCacheBean<Object>(A_VALUE, now(), TWO_HOURS);
        assertTrue(bean.isAlive(oneSecondFromNow()));
    }

    @Test
    public void aJustCreatedBeanWithOneSecondOfKeepaliveIsNotAliveAfterTwoSeconds() {
        QNCacheBean<Object> bean = new QNCacheBean<Object>(A_VALUE, now(), ONE_SECOND);
        assertFalse(bean.isAlive(twoHoursFromNow()));
    }
}
