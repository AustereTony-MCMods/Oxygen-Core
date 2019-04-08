package austeretony.oxygen.common.telemetry.api;

import austeretony.oxygen.common.config.OxygenConfig;
import austeretony.oxygen.common.telemetry.ILogType;
import austeretony.oxygen.common.telemetry.TelemetryManager;
import austeretony.oxygen.common.telemetry.io.TelemetryIO;

public class TelemetryProvider {

    public static <T extends ILog> void registerLogType(ILogType<T> logType) {
        if (OxygenConfig.ENABLE_TELEMETRY.getBooleanValue())
            TelemetryManager.instance().register(logType);
    }

    public static void createLog(ILogType logType, ILog log) {
        if (OxygenConfig.ENABLE_TELEMETRY.getBooleanValue())
            logType.getLogContainer().addLog(log);
    }      

    public static long getLastLogCacheRecordTime() {
        if (!OxygenConfig.ENABLE_TELEMETRY.getBooleanValue())
            return - 1L;
        return TelemetryIO.instance().getLastRecordTime();
    }

    public static long getLastLogCacheAppendTime() {
        if (!OxygenConfig.ENABLE_TELEMETRY.getBooleanValue())
            return - 1L;
        return TelemetryIO.instance().getLastCacheAppendTime();
    }

    public static String getTelemetryDataFolder() {
        if (!OxygenConfig.ENABLE_TELEMETRY.getBooleanValue())
            return null;
        return TelemetryIO.instance().getTelemetryDataFolder();
    }
}
