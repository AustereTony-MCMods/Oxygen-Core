package austeretony.oxygen.common.telemetry;

import java.util.Queue;

import austeretony.oxygen.common.telemetry.api.ILog;

public interface ILogContainer<T extends ILog> {

    void process();

    boolean addLog(T log);

    Queue<T> getCache();

    void clearCache();
}
