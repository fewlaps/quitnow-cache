package com.fewlaps.quitnowcache;

/**
 * Testing dates is a madness. This class will improve the readability of tests
 */
public class BaseTest {
    public static final String A_KEY = "aLovelyKey";
    public static final String A_VALUE = "aLovelyValue";

    public static final long FOREVER = 0;
    public static final long ONE_SECOND = 1000;
    public static final long TWO_SECONDS = 2000;
    public static final long FIVE_SECONDS = 5000;

    protected long now() {
        return System.currentTimeMillis();
    }

    protected long oneSecondFromNow() {
        return now() + ONE_SECOND;
    }

    protected long twoSecondsFromNow() {
        return now() + TWO_SECONDS;
    }

    protected long fiveSecondsFromNow() {
        return now() + FIVE_SECONDS;
    }
}
