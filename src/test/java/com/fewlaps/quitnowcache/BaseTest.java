package com.fewlaps.quitnowcache;

/**
 * Testing dates is a madness. This class will improve the readability of tests.
 */
public abstract class BaseTest {
    public static final String A_KEY = "aLovelyKey";
    public static final String A_VALUE = "aLovelyValue";
    public static final String ANOTHER_KEY = "anotherLovelyKey";
    public static final String ANOTHER_VALUE = "anotherLovelyValue";
    public static final String JUST_A = "a";

    public static final long FOREVER = 0;
    public static final long ONE_SECOND = 1000;
    public static final long TWO_HOURS = 2 * 60 * 1000;
    public static final long THREE_DAYS = 3 * 24 * 60 * 1000;

    protected long now() {
        return System.currentTimeMillis();
    }

    protected long oneSecondFromNow() {
        return now() + ONE_SECOND;
    }

    protected long twoHoursFromNow() {
        return now() + TWO_HOURS;
    }

    protected long threeDaysFromNow() {
        return now() + THREE_DAYS;
    }
}
