package com.fewlaps.quitnowcache;

import org.junit.Test;

import java.util.ArrayList;

public class IntroducingQNCacheTest {

    @Test
    public void theCodeOfTheReadmeWorks() {
        try {
            QNCache cache; // Let me introduce you: The simplest memcached-like cache!

            //It has a fancy builder with fancy default options.
            cache = new QNCacheBuilder().createQNCache();

            cache.set("Key", "Value"); //you can save things in the cache.
            cache.get("Key"); //get it again
            cache.remove("Key"); //and remove it if you want

            //By the way, you can save only it for a second, as you do with memcached.
            cache.set("Key", "Value", 1000); //save it for a second
            cache.get("Key"); //it will work as usual, but,
            Thread.sleep(1000); //But after a second,
            cache.get("Key"); //it will become null. That's fantastic!

            //In addition, you can save all kinds of Objects:
            cache.set("Mama", new Integer(42));
            cache.set("Papa", new ArrayList());

            //What about the memory? By default, all the instances will be stored unless you remove it
            //But there's a way to autoremove all the instances that are too old to be get: the auto release
            //You can create an autoreleased QNCache using its builder. For example, cleaning it every minute:
            cache = new QNCacheBuilder().setAutoReleaseInSeconds(60).createQNCache();

            //And, what about the keys? Are they case sensitive?
            //By default, yes, as it is the common Java behaviour.
            //But you can also define it at building time:
            cache = new QNCacheBuilder().setCaseSensitiveKeys(false).createQNCache();

            //To end this beautiful writing, let's talk detail the builder default values:
            cache = new QNCacheBuilder()
                    .setAutoReleaseInSeconds(60) //default null: it means autorelease disabled
                    .setCaseSensitiveKeys(false) //default true
                    .createQNCache();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }
}
