package org.LRUCache;



public class LRUCacheTest {

    void testBasicGetSetOperations(){
        System.out.println("Running => testBasicGetSetOperations");
        LRUCache lruCache = new LRUCache(10);
        lruCache.addItem("1", "one", System.currentTimeMillis(), 1);
        lruCache.addItem("2", "two", System.currentTimeMillis(), 2);
        lruCache.addItem("3", "three", System.currentTimeMillis(), 3);

        assert lruCache.getItem("1").equals("one");
        assert lruCache.getItem("2").equals("two");
        assert lruCache.getItem("3").equals("three");
    }


    void testExpiredTimeStampsRemoval() throws InterruptedException {
        System.out.println("Running => testExpiredTimeStampsRemoval");
        LRUCache lruCache = new LRUCache(3);
        long oneSecs = 1000;
        lruCache.addItem("1", "one", System.currentTimeMillis()+oneSecs, 1);
        lruCache.addItem("2", "two", System.currentTimeMillis()+oneSecs, 2);
        lruCache.addItem("3", "three", System.currentTimeMillis()+oneSecs, 3);

        Thread.sleep(2000);

        lruCache.addItem("4", "four", System.currentTimeMillis()+oneSecs, 4);
        lruCache.addItem("5", "five", System.currentTimeMillis()+oneSecs, 5);
        lruCache.addItem("6", "six", System.currentTimeMillis()+oneSecs, 6);

        assert lruCache.getItem("1").equals("");
        assert lruCache.getItem("2").equals("");
        assert lruCache.getItem("3").equals("");
        assert lruCache.getItem("4").equals("four");
    }


    void testLowerPriorityFirstRemoval(){
        System.out.println("Running => testLowerPriorityFirstRemoval");
        LRUCache lruCache = new LRUCache(2);
        long tenSecs = 10000;
        lruCache.addItem("1", "one", System.currentTimeMillis()+tenSecs, 1);
        lruCache.addItem("2", "two", System.currentTimeMillis()+tenSecs, 2);
        lruCache.addItem("3", "three", System.currentTimeMillis()+tenSecs, 3);

        assert lruCache.getItem("1").equals("");
        assert lruCache.getItem("2").equals("two");
        assert lruCache.getItem("3").equals("three");
    }


    void testSamePriorityLeastRecentlyUsedRemovedFirst(){
        System.out.println("Running => testSamePriorityLeastRecentlyUsedRemovedFirst");
        LRUCache lruCache = new LRUCache(2);
        long tenSecs = 10000;
        lruCache.addItem("1", "one", System.currentTimeMillis()+tenSecs, 1);
        lruCache.addItem("2", "two", System.currentTimeMillis()+tenSecs, 1);

        lruCache.getItem("1");

        lruCache.addItem("3", "three", System.currentTimeMillis()+tenSecs, 3);

        assert lruCache.getItem("1").equals("one");
        assert lruCache.getItem("2").equals("");
        assert lruCache.getItem("3").equals("three");
    }
}
