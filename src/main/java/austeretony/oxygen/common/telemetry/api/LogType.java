package austeretony.oxygen.common.telemetry.api;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import austeretony.oxygen.common.telemetry.ILogType;
import austeretony.oxygen.common.telemetry.config.OxygenTelemetryConfig;
import austeretony.oxygen.common.telemetry.delegate.LogContainer;
import austeretony.oxygen.common.telemetry.io.LogFiles;

public class LogType<T extends ILog> implements ILogType {

    private static final Map<Integer, ILogType> LOG_TYPES = new HashMap<Integer, ILogType>();

    private final int id;

    private final LogContainer<T> logContainer = new LogContainer<T>();

    private final String folderName, logFileName, cacheFileName;

    private LogFiles logFiles = LogFiles.create();

    public LogType(int id, String folderName, String logFileName, String cacheFileName) {
        this.id = id;
        this.folderName = folderName;
        this.logFileName = logFileName;
        this.cacheFileName = cacheFileName;
        this.logFiles.createLogFile(logFileName, System.currentTimeMillis());
        this.logFiles.createCacheFiles(cacheFileName, OxygenTelemetryConfig.CACHE_FILES_AMOUNT.getIntValue());
        LOG_TYPES.put(id, this);
    }

    public static Collection<ILogType> getLogTypes() {
        return LOG_TYPES.values();
    }

    public static ILogType getType(int id) {
        return LOG_TYPES.get(id);
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getFolderName() {
        return this.folderName;
    }

    @Override
    public String getLogFileName() {
        return this.logFileName;
    }

    @Override
    public String getCacheFileName() {
        return this.cacheFileName;
    }

    @Override
    public LogContainer<T> getLogContainer() {
        return this.logContainer;
    }

    @Override
    public LogFiles getLogFiles() {
        return this.logFiles;
    }
}
