package austeretony.oxygen.common.telemetry.api;

import java.util.Set;

import austeretony.oxygen.common.telemetry.ILogType;
import io.netty.util.internal.ConcurrentSet;

public class TelemetryProvider {

    private static final Set<ILogType> CONTAINERS = new ConcurrentSet<ILogType>();

    public static <T extends ILog> void registerLogType(ILogType<T> logType) {
        CONTAINERS.add(logType);
    }

    public static <T extends ILog> void createLog(ILogType<T> logType, T log) {
        logType.getLogContainer().addLog(log);
    }      
}
