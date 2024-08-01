package org.LRUCache;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        LRUCacheTest lruCacheTest = new LRUCacheTest();
        lruCacheTest.testBasicGetSetOperations();
        lruCacheTest.testExpiredTimeStampsRemoval();
        lruCacheTest.testSamePriorityLeastRecentlyUsedRemovedFirst();
        lruCacheTest.testLowerPriorityFirstRemoval();
    }

}
