package org.LRUCache;

public class Node {
    String key;
    String value;
    long timeStamp;
    int priority;
    Node next;
    Node prev;

    Node(String key, String value, long timeStamp, int priority){
        this.key = key;
        this.value = value;
        this.timeStamp = timeStamp;
        this.priority = priority;
    }

    Node() {

    }
}
