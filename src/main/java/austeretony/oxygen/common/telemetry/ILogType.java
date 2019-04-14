package austeretony.oxygen.common.telemetry;

import austeretony.oxygen.common.telemetry.api.ILog;

public interface ILogType<T extends ILog> {

    int getId();

    String getFolderName();

    String getLogFileName();

    String getCacheFileName();

    LogContainer<T> getLogContainer();

    LogFiles getLogFiles();
}
