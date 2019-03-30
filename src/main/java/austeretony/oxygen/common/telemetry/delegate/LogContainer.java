package austeretony.oxygen.common.telemetry.delegate;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import austeretony.oxygen.common.telemetry.api.ILog;

public class LogContainer<T extends ILog> {

    private final Queue<T> 
    bufferQueue = new ConcurrentLinkedQueue<T>(), 
    cacheQueue = new ConcurrentLinkedQueue<T>();

    public void process() {
        T log;
        while ((log = this.bufferQueue.poll()) != null) {
            this.cacheQueue.offer(log);
        }
    }

    public boolean addLog(T log) {
        return this.bufferQueue.offer(log);
    }

    public Queue<T> getCache() {
        return this.cacheQueue;
    }

    public void clearCache() {
        this.cacheQueue.clear();
    }
}
