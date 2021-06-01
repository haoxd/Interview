package com.bread.coalquality.sys.tools.lrucache;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Description: lrucache 基于linkedhashmap下的实现
 * @Author: haoxd
 * @Version: 1.0
 */
public class LruLinkedHashMap<K, V> {

    private static final float hashLoadFactory = 0.75f;

    private int size;

    private LinkedHashMap<K, V> cache;


    public LruLinkedHashMap(int size) {

        this.size = size;
        int capacity = (int) Math.ceil(size / hashLoadFactory) + 1;
        cache = new LinkedHashMap<K, V>(capacity, hashLoadFactory, true) {
            private static final long serialVersionUID = 1;

            @Override
            protected boolean removeEldestEntry(Map.Entry eldest) {
                return size() > this.size();
            }
        };

    }


    public synchronized V get(K key) {
        return cache.get(key);
    }

    public synchronized void put(K key, V value) {
        cache.put(key, value);
    }


    public static void main(String[] args) {
        LruLinkedHashMap lruLinkedHashMap = new LruLinkedHashMap(2);
        lruLinkedHashMap.put("1", "1");
        lruLinkedHashMap.put("2", "2");
        lruLinkedHashMap.put("3", "3");
        System.out.println(lruLinkedHashMap.get("1"));


    }


}
