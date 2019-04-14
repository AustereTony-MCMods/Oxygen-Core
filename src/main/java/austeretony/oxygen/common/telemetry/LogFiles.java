package austeretony.oxygen.common.telemetry;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.telemetry.config.OxygenTelemetryConfig;
import net.minecraft.util.math.MathHelper;

public class LogFiles {

    public static final DateFormat LOG_FILE_DATE_FORMAT = new SimpleDateFormat("MM_dd_yyyy_HH-mm");

    private String baseLogFileName, stampedLogFileName;

    private long logFileTimeCreated;

    private int cacheFilesAmount, prevCacheFileIndex, currentCacheFileIndex;

    private String[] cacheFileNames;

    private boolean isCacheUpdated;

    private static long logFileChangingInterval;

    private LogFiles() {}

    public static LogFiles create() {
        return new LogFiles();
    }

    public void createLogFile(String baseName, long timeCreated) {
        this.baseLogFileName = baseName;
        this.logFileTimeCreated = timeCreated;
        long time = System.currentTimeMillis();
        if (time - timeCreated > logFileChangingInterval) {
            this.stampedLogFileName = baseName + "_" + LOG_FILE_DATE_FORMAT.format(new Date(time));
            this.logFileTimeCreated = time;
        } else {           
            this.stampedLogFileName = baseName + "_" + LOG_FILE_DATE_FORMAT.format(new Date(timeCreated));
        }
        OxygenMain.TELEMETRY_LOGGER.info("Initialized log file: {}.", this.stampedLogFileName);
    }

    public long getLogFileTimeCreated() {
        return this.logFileTimeCreated;
    }

    public String getLogFile() {
        long time = System.currentTimeMillis();
        if (time - this.logFileTimeCreated > logFileChangingInterval) {
            this.stampedLogFileName = this.baseLogFileName + "_" + LOG_FILE_DATE_FORMAT.format(new Date(time));
            this.logFileTimeCreated = time;
            OxygenMain.TELEMETRY_LOGGER.info("Initialized log file: {}.", this.stampedLogFileName);
        }
        return this.stampedLogFileName;
    }

    public String getLatestLogFile() {
        return this.stampedLogFileName;
    }

    public void createCacheFiles(String fileName, int amount) {
        this.cacheFilesAmount = MathHelper.clamp(amount, 1, 10);
        this.cacheFileNames = new String[this.cacheFilesAmount];
        for (int i = 0; i < this.cacheFilesAmount; i++)
            this.cacheFileNames[i] = fileName + "_" + i;
    }

    public String getCacheFile() {
        if (this.currentCacheFileIndex >= this.cacheFilesAmount)
            this.currentCacheFileIndex = 0;
        this.prevCacheFileIndex = this.currentCacheFileIndex;
        this.isCacheUpdated = true;
        return this.cacheFileNames[this.currentCacheFileIndex++];        
    }

    public String getLatestCacheFile() {
        return this.cacheFileNames[this.prevCacheFileIndex];        
    }

    public boolean isCacheFileUpdated() {
        boolean flag = this.isCacheUpdated;
        this.isCacheUpdated = false;
        return flag;
    }

    public static void initLogFileChangingInterval() {
        logFileChangingInterval = OxygenTelemetryConfig.LOG_FILE_CHANGING_INTERVAL.getIntValue() * 1000 * 60 * 60;
    }

    public static long getLogFileChangingInterval() {
        return logFileChangingInterval;
    }
}
