[![Coverage Status](https://coveralls.io/repos/Fewlaps/quitnow-cache/badge.svg?branch=master&service=github)](https://coveralls.io/github/Fewlaps/quitnow-cache?branch=master)
[![Gitter](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/Fewlaps/quitnow-cache?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)

# [QuitNow](http://quitnowapp.com)'s cache
A temporary cache for JVM applications.

Before this library, caching data for a limited time was a hard task to do. The developer had to save the last time the data was stored, and then, check it everytime the data was read. So, we decided to return the work to the open source community by writing this really simple cache, allowing developers to keep information for a limited time.

We've done it using TDD, so it's totally tested. [Check the tests!](https://github.com/Fewlaps/quitnow-cache/tree/master/src/test/java/com/fewlaps/quitnowcache) :·)

The sample
----------

```java
QNCache cache = new QNCache.Builder().build();

cache.set("key", "value", 60 * 1000); // It can store things for a minute,
cache.set("key", "value", 60 * 60 * 1000); // for an hour,
cache.set("key", "value", 0); // or forever.
cache.set("key", "value"); // And also for the short version of forever.

cache.get("key"); // It can get them again,
cache.remove("key"); // and remove it if you want.

cache.get("unexistingKey"); // If something doesn't exist, it returns null
cache.get("tooOldKey"); // The same if a key is too old

cache.clear(); // You can also clean it,
cache.size(); // and ask it how many elements it has

QNCache<String> stringCache = new QNCache.Builder().build(); // You can also make it typesafe
stringCache.set("key", 42); // so this won't compile :)
```

Let's talk about the memory
---------------------------
By default, the cache stores a reference to all stored instances, doesn't matter if they're fresh or not. If you plan to store huge instances, like an Android's Bitmap, you can create it with an auto releaser. Then the cache will remove the old elements after the given amount of time.

```java
new QNCache.Builder().autoReleaseInSeconds(1).build(); // frees the memory every second
new QNCache.Builder().autoReleaseInSeconds(60).build(); // frees the memory every minute
new QNCache.Builder().autoReleaseInSeconds(60*60).build(); // frees the memory every hour
new QNCache.Builder().build(); // never frees the memory

```

By the way, if you use the auto releaser, you should know that you can stop it. You should do it when your Servlet container notifies you that it wants to finish the application. It's the same you should do with any database connection, for example. In Android environments you can avoid it, but you might be interested in [this extended explanation](https://github.com/Fewlaps/quitnow-cache/releases/tag/v3.2.0).

```java
cache.shutdown();
```

Are the keys case sensitive?
---------------------------
By default, yes. But you can specify it at building time.

```java
new QNCache.Builder().caseSensitiveKeys(true).build(); // "key" and "KEY" will be different items
new QNCache.Builder().caseSensitiveKeys(false).build(); // "key" and "KEY" will be the same
new QNCache.Builder().build(); // "key" and "KEY" will be different items
```

It's possible to change the default keepalive?
---------------------------
Of course! But, again, you have to specify it at building time.

```java
new QNCache.Builder().defaultKeepaliveInMillis(1000).build(); // a keepalive of one second
new QNCache.Builder().defaultKeepaliveInMillis(1000 * 60).build(); // a keepalive of one minute
new QNCache.Builder().build(); // the default keepalive: remember it forever!
```

Why working with millis and seconds?
---------------------------
Good question! If you prefer to work with TimeUnit, you're free to do it.

```java
new QNCache.Builder().autoRelease(2, TimeUnit.HOURS).build();
new QNCache.Builder().defaultKeepalive(5, TimeUnit.MINUTES).build();
cache.set("key", "value", 42, TimeUnit.SECONDS);
```

# Download

* Grab via Gradle:
```groovy
repositories { jcenter() }
    
compile 'com.fewlaps.quitnowcache:quitnow-cache:3.4.0'
```
* Grab via Maven:
```xml
<repository>
  <id>jcenter</id>
  <url>http://jcenter.bintray.com</url>
</repository>

<dependency>
    <groupId>com.fewlaps.quitnowcache</groupId>
    <artifactId>quitnow-cache</artifactId>
    <version>3.4.0</version>
</dependency>
```


## LICENSE ##

The MIT License (MIT)

Copyright (c) 2018 Fewlaps

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
