package com.cme.trading.cache;

import com.cme.trading.model.Quote;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;


public class QuoteCache implements Cache<String, Quote> {

    private final ConcurrentMap<String, BlockingQueue<Quote>> internalCache = new ConcurrentHashMap<>();


    private BlockingQueue<Quote> getQueue(String key) {
        return internalCache.computeIfAbsent(key, k -> new LinkedBlockingQueue<>());
    }

    /**
     * Clears the cache and puts the Quote in the cache
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public Quote put(String key, Quote value) {
        getQueue(key).clear();
        return getQueue(key).add(value) ? value : null;
    }

    /**
     * Gets the Quote from the cache but does not remove it
     *
     * @param key
     * @return
     */
    @Override
    public Quote get(String key) {
        BlockingQueue<Quote> queue = getQueue(key);
        return queue.peek();
    }

    /**
     * Takes and removes the Quote from the cache
     *
     * @param key
     * @return
     */
    @Override
    public Quote take(String key) {
        try {
            BlockingQueue<Quote> queue = getQueue(key);
            return queue.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }

    @Override
    public int size() {
        return internalCache.size();
    }

    @Override
    public boolean isEmpty() {
        return internalCache.isEmpty();
    }

    @Override
    public boolean containsKey(String key) {
        return internalCache.containsKey(key);
    }

    @Override
    public boolean containsValue(Quote value) {
        return internalCache.containsValue(value);
    }

    @Override
    public void clear() {
        internalCache.clear();
    }
}