# Problem:
Create LRU cache that can hold entries upto capacity **N**, and evict the entries based on the following rules:
1. All the expired entries are removed first.
2. Entries with lower priority are removed first, if we two entries with same priority remove the least recently used 
first.


# Implementation Details:
## 1. Used two Java in-Built priority queues:

**timeStampQueue** for keep tracks of first expiring nodes:
```aidl
 // nodes with older timestamp will appear at the top
        timeStampQueue = new PriorityQueue<Node>(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                return (int)(o1.timeStamp - o2.timeStamp);
            }
        });
```
**priorityQueue** to keep track of lowest priority nodes first:
```aidl
// nodes with lower prority will appear at the top
        priorityQueue = new PriorityQueue<Node>(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                return o1.priority - o2.priority;
            }
        });
```

## 2. Used a circular double-linked List to keep track of the most and least recently used nodes.

```
        mostRecentlyUsed = new Node();
        leastRecentlyUsed = new Node();

        mostRecentlyUsed.next = leastRecentlyUsed;
        mostRecentlyUsed.prev = leastRecentlyUsed;

        leastRecentlyUsed.next = mostRecentlyUsed;
        leastRecentlyUsed.prev = mostRecentlyUsed; 
```
***-Note:I have initialized the linked list with two dummy nodes, to keep removal and addition method simplier. Otherwise code would have to take care of lot edge cases for handling head and tail being null***


# Runtime Complexity:
## 1.Space Complexity:
```aidl
O(N) //We will be storing every entry until we hit the capacity
```

## 2. Time Complexity:
```aidl
O(N lg N)
```
Even though we have highly optimized the least and most recently used operation to ***O(1)*** using double linked list, but every removal or addition to each priority queue will be ***O(lg N)***

# Testing:
LRUCacheTest file test the designed cache for all the edge cases such as:
1. testBasicGetSetOperations() => test basic functionality of setting and retrieveing the key.
2. testExpiredTimeStampsRemoval()  => test that all the expired entries are removed once the cache hit the capacity;
3. testLowerPriorityFirstRemoval() => test that only the lower prority key is removed once the cache hit the limit
4. testSamePriorityLeastRecentlyUsedRemovedFirst() => test that when two keys have same prority only the least recently used is removed

## Running
1. src.zip contains the runnable jar
2. Take the terminal to the location where you have uncompressed the folder., and cd into the 
3. excecute ```Java -jar -ea Tesla-TakeHome-Screening-1.0-SNAPSHOT.jar```