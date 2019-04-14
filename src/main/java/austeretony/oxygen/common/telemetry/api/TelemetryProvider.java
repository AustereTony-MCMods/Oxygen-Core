package austeretony.oxygen.common.telemetry.api;

import austeretony.oxygen.common.OxygenManagerServer;
import austeretony.oxygen.common.config.OxygenConfig;
import austeretony.oxygen.common.telemetry.ILogType;
import austeretony.oxygen.common.telemetry.TelemetryManager;
import austeretony.oxygen.common.telemetry.io.TelemetryIO;

public class TelemetryProvider {

    public static <T extends ILog> void registerLogType(ILogType<T> logType) {
        if (OxygenConfig.ENABLE_TELEMETRY.getBooleanValue())
            OxygenManagerServer.instance().getTelemetryManager().register(logType);
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
}
