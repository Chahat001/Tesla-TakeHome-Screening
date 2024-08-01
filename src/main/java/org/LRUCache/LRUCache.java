package org.LRUCache;

import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

public class LRUCache {

    private PriorityQueue<Node> timeStampQueue;
    private PriorityQueue<Node> priorityQueue;
    private HashMap<String, Node> keyToNode;
    private Node mostRecentlyUsed;
    private Node leastRecentlyUsed;
    private int capacity;

    /**
     * Construct LRU cache with specified capacity
     * @param capacity the number of items to be stored in the cache
     */
    LRUCache(int capacity){
        // nodes with older timestamp will appear at the top
        timeStampQueue = new PriorityQueue<Node>(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                return (int)(o1.timeStamp - o2.timeStamp);
            }
        });

        // nodes with lower prority will appear at the top
        priorityQueue = new PriorityQueue<Node>(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                return o1.priority - o2.priority;
            }
        });

        // Setting up a circular double linked list to make it easier to handle edge cases later in code
        mostRecentlyUsed = new Node();
        leastRecentlyUsed = new Node();

        mostRecentlyUsed.next = leastRecentlyUsed;
        mostRecentlyUsed.prev = leastRecentlyUsed;

        leastRecentlyUsed.next = mostRecentlyUsed;
        leastRecentlyUsed.prev = mostRecentlyUsed;

        keyToNode = new HashMap<>();

        this.capacity = capacity;
    }

    /**
     * Public api to add the item to the LRU cache
     * @param key string key to search the key
     * @param value corresponding value of the key
     * @param timeStamp of expiration in millisecs
     * @param priority integer value lower priority means less significance and key will be removed first
     */
    public void addItem(String key, String value, long timeStamp, int priority){
        // if key already exist we will delete and re-inset it with updated values
        if(keyToNode.containsKey(key)){
            deleteItemFromDataStructures(keyToNode.get(key));
        }

        if(keyToNode.size() >= this.capacity){
            evictItems();
        }

        Node toBeInserted = new Node(key, value, timeStamp, priority);

        addItemIntoAllDataStructures(toBeInserted);
    }

    /**
     * public function exposing the api to get item
     * @param key string value to search the key for
     * @return found value or "" if key cannot be found
     */
    public String getItem(String key){
        if(keyToNode.containsKey(key)){
            deleteNodeFromList(keyToNode.get(key));
            insertNodeAtHead(keyToNode.get(key));
            return keyToNode.get(key).value;
        }
        return "";
    }
    /**
     * This methods will remove the keys from the LRU based on following rules:
     * 1. Evict all the expired item.
     * 2. If there are no expired items to evict then evict the lowest priority entry.
     * 3. Tie breaking among entries with the same priority is done via least recently used.
     */
    private void evictItems(){
        // Evict all the expired item.
        while(!timeStampQueue.isEmpty() && timeStampQueue.peek().timeStamp < System.currentTimeMillis()){
            Node toBeRemovedNode = timeStampQueue.poll();
            deleteItemFromDataStructures(toBeRemovedNode);
        }

        // Evict the lowest priority entry.
        Node toBeRemovedNode = null;
        Node firstLowestPriorityNode = priorityQueue.poll();
        Node secondLowestPriorityNode = priorityQueue.poll();
        if(firstLowestPriorityNode != null && secondLowestPriorityNode != null && firstLowestPriorityNode.priority == secondLowestPriorityNode.priority){
            toBeRemovedNode = leastRecentlyUsed.prev;
            if(toBeRemovedNode == firstLowestPriorityNode){
                deleteItemFromDataStructures(firstLowestPriorityNode);
                priorityQueue.add(secondLowestPriorityNode);
            }
            else{
                deleteItemFromDataStructures(secondLowestPriorityNode);
                priorityQueue.add(firstLowestPriorityNode);
            }
        }
        else if(firstLowestPriorityNode != null){
            deleteItemFromDataStructures(firstLowestPriorityNode);
            if(secondLowestPriorityNode != null){
                priorityQueue.add(secondLowestPriorityNode);
            }
        }
    }

    /**
     * Since we have about 4 data structures keeping track of our nodes, this is idempotent method will take care of completely
     * removing key from all of them.
     * @param node to be deleted node
     */
    private void deleteItemFromDataStructures(Node node){
        timeStampQueue.remove(node);
        priorityQueue.remove(node);
        keyToNode.remove(node.key);
        deleteNodeFromList(node);
    }

    /**
     * Add the node into all 4 data structures.
     * @param node to be inserted node
     */
    private void addItemIntoAllDataStructures(Node node){
        timeStampQueue.add(node);
        priorityQueue.add(node);
        keyToNode.put(node.key, node);
        insertNodeAtHead(node);
    }

    /**
     * This will insert the node at the head of LRU list, marking it as most recently used.
     * @param node to be inserted node
     */
    private void insertNodeAtHead(Node node){
        Node temp = mostRecentlyUsed.next;

        mostRecentlyUsed.next = node;
        node.prev = mostRecentlyUsed;

        node.next = temp;
        temp.prev = node;
    }

    /**
     * We have double linkedlist keeping track of the least recently used. This method will take care of removing the node
     * from the double linked list.
     * Note -> this method assumes that node is alerady presented int
     * @param node to be deleted node f
     */
    private void deleteNodeFromList(Node node){
        try{
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
        catch (NullPointerException e){
            System.out.println("Null pointer exception, " + node.key + "might not be present in linked List");
        }

        node.next = null;
        node.prev = null;
    }

}
