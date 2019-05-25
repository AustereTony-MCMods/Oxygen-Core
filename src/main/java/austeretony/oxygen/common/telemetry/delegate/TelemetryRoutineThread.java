package austeretony.oxygen.common.telemetry.delegate;

import austeretony.oxygen.common.OxygenManagerServer;
import austeretony.oxygen.common.telemetry.TimeCounter;
import austeretony.oxygen.common.telemetry.config.OxygenTelemetryConfig;

public class TelemetryRoutineThread extends Thread {

    private TimeCounter recordCounter;

    private volatile boolean recording;

    public TelemetryRoutineThread(String string) {
        super(string);
        this.setDaemon(true);
        this.recordCounter = new TimeCounter(OxygenTelemetryConfig.RECORDING_INTERVAL.getIntValue() * 1000);
    }

    @Override
    public void run() {
        while (!interrupted()) {            
            if (!this.isRecording()) {
                OxygenManagerServer.instance().getTelemetryManager().process();
                if (this.recordCounter.expired())
                    this.startRecord();
            }
        }
    }

    public boolean isRecording() {
        return this.recording;
    }

    public void startRecord() {
        this.recording = true;
    }

    public void stopRecord() {
        this.recording = false;
    }
}
