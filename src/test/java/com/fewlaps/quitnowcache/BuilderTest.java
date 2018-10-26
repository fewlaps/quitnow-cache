package com.fewlaps.quitnowcache;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static com.fewlaps.quitnowcache.QNCache.KEEPALIVE_FOREVER;
import static com.fewlaps.quitnowcache.QNCache.WITHOUT_AUTORELEASE;
import static org.junit.Assert.*;

public class BuilderTest {

    @Test
    public void testDefaultBuilder() {
        QNCache<Object> cache = new QNCache.Builder().build();

        assertTrue(cache.getCaseSensitiveKeys());
        assertEquals(WITHOUT_AUTORELEASE, cache.getAutoReleaseInSeconds());
        assertEquals(KEEPALIVE_FOREVER, cache.getDefaultKeepaliveInMillis());
    }

    @Test
    public void testSettingTrueCaseSensitiveKeysBuilder() {
        QNCache<Object> cache = new QNCache.Builder().caseSensitiveKeys(true).build();

        assertTrue(cache.getCaseSensitiveKeys());
    }

    @Test
    public void testSettingFalseCaseSensitiveKeysBuilder() {
        QNCache<Object> cache = new QNCache.Builder().caseSensitiveKeys(false).build();

        assertFalse(cache.getCaseSensitiveKeys());
    }

    @Test
    public void testSettingMinusOneAutoReleaseSecondsBuilder() {
        QNCache<Object> cache = new QNCache.Builder().autoReleaseInSeconds(-1).build();

        assertEquals(WITHOUT_AUTORELEASE, cache.getAutoReleaseInSeconds());
    }

    @Test
    public void testSetting10AutoReleaseSecondsBuilder() {
        QNCache<Object> cache = new QNCache.Builder().autoReleaseInSeconds(10).build();

        assertEquals(10, cache.getAutoReleaseInSeconds());
    }

    @Test
    public void testSetting10AutoReleaseSecondsBuilder_usingSecondsTimeUnit() {
        QNCache<Object> cache = new QNCache.Builder().autoRelease(10, TimeUnit.SECONDS).build();

        assertEquals(10, cache.getAutoReleaseInSeconds());
    }

    @Test
    public void testSetting10AutoReleaseSecondsBuilder_usingMillisTimeUnit() {
        QNCache<Object> cache = new QNCache.Builder().autoRelease(10000, TimeUnit.MILLISECONDS).build();

        assertEquals(10, cache.getAutoReleaseInSeconds());
    }

    @Test
    public void testSetting10AutoReleaseSecondsBuilder_usingMicrosTimeUnit() {
        QNCache<Object> cache = new QNCache.Builder().autoRelease(10000000, TimeUnit.MICROSECONDS).build();

        assertEquals(10, cache.getAutoReleaseInSeconds());
    }

    @Test
    public void testSettingDefaultKeepaliveBuilder_with10() {
        QNCache<Object> cache = new QNCache.Builder().defaultKeepaliveInMillis(10).build();

        assertEquals(10, cache.getDefaultKeepaliveInMillis());
    }

    @Test
    public void testSettingDefaultKeepaliveBuilder_with10000MillisTimeUnits() {
        QNCache<Object> cache = new QNCache.Builder().defaultKeepalive(10000, TimeUnit.MILLISECONDS).build();

        assertEquals(10000, cache.getDefaultKeepaliveInMillis());
    }

    @Test
    public void testSettingDefaultKeepaliveBuilder_with10SecondsTimeUnits() {
        QNCache<Object> cache = new QNCache.Builder().defaultKeepalive(10, TimeUnit.SECONDS).build();

        assertEquals(10000, cache.getDefaultKeepaliveInMillis());
    }

    @Test
    public void testSettingDefaultKeepaliveBuilder_withZero() {
        QNCache<Object> cache = new QNCache.Builder().defaultKeepaliveInMillis(0).build();

        assertEquals(KEEPALIVE_FOREVER, cache.getDefaultKeepaliveInMillis());
    }

    @Test
    public void testSettingDefaultKeepaliveBuilder_withZeroSecondsTimeUnit() {
        QNCache<Object> cache = new QNCache.Builder().defaultKeepalive(0, TimeUnit.SECONDS).build();

        assertEquals(KEEPALIVE_FOREVER, cache.getDefaultKeepaliveInMillis());
    }

    @Test
    public void testSettingDefaultKeepaliveBuilder_withMinusTen() {
        QNCache<Object> cache = new QNCache.Builder().defaultKeepaliveInMillis(-10).build();

        assertEquals(KEEPALIVE_FOREVER, cache.getDefaultKeepaliveInMillis());
    }

    @Test
    public void testSettingDefaultKeepaliveBuilder_withMinusTenSecondsTimeUnit() {
        QNCache<Object> cache = new QNCache.Builder().defaultKeepalive(-10, TimeUnit.SECONDS).build();

        assertEquals(KEEPALIVE_FOREVER, cache.getDefaultKeepaliveInMillis());
    }

    @Test
    public void testQNCacheDefaultKeepaliveIsForever() {
        QNCache<Object> cache = new QNCache.Builder().defaultKeepaliveInMillis(KEEPALIVE_FOREVER).build();

        assertEquals(KEEPALIVE_FOREVER, cache.getDefaultKeepaliveInMillis());
    }
}
