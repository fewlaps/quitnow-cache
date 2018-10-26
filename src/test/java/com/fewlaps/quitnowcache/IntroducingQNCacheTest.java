package com.fewlaps.quitnowcache;

import org.junit.Test;

public class IntroducingQNCacheTest {

    @SuppressWarnings("UnusedAssignment")
    @Test
    public void theCodeOfTheReadmeWorks() {
        //##The sample

        QNCache cache = new QNCache.Builder().build();

        cache.set("key", "value", 60 * 1000); // It can store things for a minute,
        cache.set("key", "value", 60 * 60 * 1000); // for an hour,
        cache.set("key", "value", 0); // or forever.
        cache.set("key", "value"); // And also for the short version of forever.

        cache.get("key"); // It can get them again,
        cache.remove("key"); // and remove it if you want.

        cache.get("unExistingKey"); // If something doesn't exists, it returns null
        cache.get("tooOldKey"); // The same if a key is too old

        cache.clear(); // You can also clean it,
        cache.size(); // and ask it how many elements it has

        QNCache<String> stringCache = new QNCache.Builder().build(); //You can also make it typesafe
        //stringCache.set("key", 42); //so this will not compile :)

        //##Let's talk about the memory
        cache = new QNCache.Builder().autoReleaseInSeconds(1).build(); //frees the memory every second
        cache = new QNCache.Builder().autoReleaseInSeconds(60).build(); //frees the memory every minute
        cache = new QNCache.Builder().autoReleaseInSeconds(60*60).build(); //frees the memory every hour
        cache = new QNCache.Builder().build(); //never frees the memory

        //##Are the keys case sensitive?
        cache = new QNCache.Builder().caseSensitiveKeys(true).build(); //"key" and "KEY" will be different items
        cache = new QNCache.Builder().caseSensitiveKeys(false).build(); //"key" and "KEY" will be the same
        cache = new QNCache.Builder().build(); //"key" and "KEY" will be different items

        //##It's possible to change the default keepalive?
        cache = new QNCache.Builder().defaultKeepaliveInMillis(1000).build(); //a keepalive of one second
        cache = new QNCache.Builder().defaultKeepaliveInMillis(1000 * 60).build(); //a keepalive of one minute
        cache = new QNCache.Builder().build(); //the default keepalive: remember it forever!
    }
}
