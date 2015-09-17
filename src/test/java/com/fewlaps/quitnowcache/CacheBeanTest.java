package com.fewlaps.quitnowcache;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CacheBeanTest extends BaseTest {

    @Test
    public void aJustCreatedBeanWithOneSecondOfKeepaliveIsAliveRightNow() {
        QNCacheBean bean = new QNCacheBean(A_VALUE, now(), ONE_SECOND);
        assertTrue(bean.isAlive(now()));
    }

    @Test
    public void aJustCreatedBeanWithTwoSecondsOfKeepaliveIsAliveAfterASecond() {
        QNCacheBean bean = new QNCacheBean(A_VALUE, now(), TWO_SECONDS);
        assertTrue(bean.isAlive(oneSecondFromNow()));
    }

    @Test
    public void aJustCreatedBeanWithOneSecondOfKeepaliveIsNotAliveAfterTwoSeconds() {
        QNCacheBean bean = new QNCacheBean(A_VALUE, now(), ONE_SECOND);
        assertFalse(bean.isAlive(twoSecondsFromNow()));
    }
}
