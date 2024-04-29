package com.example.springbootbasefunctions.LRU;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRULinkedHashMap<K, V> extends LinkedHashMap<K, V> {
    private final int MAX_ENTRIES;

    public LRULinkedHashMap(int maxEntries) {
        // 通过设置容量为 maxEntries + 1，可以确保在添加第 maxEntries + 1 个条目之前，
        // HashMap 不会因为达到其负载因子而扩容。当尝试添加第 maxEntries + 1 个条目时，removeEldestEntry 方法会被调用，
        // 从而移除最老的条目，
        // 保持实际存储的条目数为 maxEntries。这样既避免了不必要的扩容操作，又确保了缓存能够准确地维持预期的最大条目数量。
        super(maxEntries + 1, 1.0f, true);
        this.MAX_ENTRIES = maxEntries;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > MAX_ENTRIES;
    }

    public static void main(String[] args) {
        // 使用LRUCache
        LRULinkedHashMap<String, String> cache = new LRULinkedHashMap<>(2);

        cache.put("Key1", "Value1");
        cache.put("Key2", "Value2");
        cache.put("Key3", "Value3"); // 此时Key1会被移除，因为它是最久未被访问的

        System.out.println(cache); // 显示当前缓存内容，Key1已被移除
    }

}
