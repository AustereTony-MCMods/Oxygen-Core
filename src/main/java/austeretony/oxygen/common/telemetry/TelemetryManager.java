package austeretony.oxygen.common.telemetry;

import java.util.Set;

import austeretony.oxygen.common.OxygenManagerServer;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.telemetry.api.ILog;
import austeretony.oxygen.common.telemetry.api.LogType;
import austeretony.oxygen.common.telemetry.delegate.TelemetryIOThread;
import austeretony.oxygen.common.telemetry.delegate.TelemetryRoutineThread;
import austeretony.oxygen.common.telemetry.io.TelemetryIO;
import io.netty.util.internal.ConcurrentSet;

public class TelemetryManager {
    
    private final OxygenManagerServer manager;

    private TelemetryRoutineThread routineThread;

    private TelemetryIOThread ioThread;

    private TelemetryIO telemetryIO;

    private Set<ILogType> containers = new ConcurrentSet<ILogType>();

    public TelemetryManager(OxygenManagerServer manager) {
        this.manager = manager;
        this.routineThread = new TelemetryRoutineThread("Telemetry Routine");
        this.ioThread = new TelemetryIOThread("Telemetry IO");
    }

    public void initIO() {
        this.telemetryIO = TelemetryIO.create();
    }

    public void startTelemetryThreads() {
        OxygenMain.TELEMETRY_LOGGER.info("Starting Routine thread...");
        this.routineThread.start();
        OxygenMain.TELEMETRY_LOGGER.info("Starting IO thread...");
        this.ioThread.start();
    }

    public TelemetryRoutineThread getProcessingThread() {
        return this.routineThread;
    }

    public TelemetryIOThread getIOThread() {
        return this.ioThread;
    }

    public TelemetryIO getIO() {
        return this.telemetryIO;
    }

    public boolean has(ILogType logType) {
        return this.containers.contains(logType);
    }

    public void process() {
        for (ILogType logType : this.containers)
            logType.getLogContainer().process();
    }

    public void record() {
        for (ILogType logType : LogType.getLogTypes())
            TelemetryIO.instance().writeCachedLog(logType, logType.getLogContainer().getCache());
        TelemetryIO.instance().appendCache();
    }

    public <T extends ILog> void register(ILogType<T> logType) {     
        this.containers.add(logType);
    }
}
