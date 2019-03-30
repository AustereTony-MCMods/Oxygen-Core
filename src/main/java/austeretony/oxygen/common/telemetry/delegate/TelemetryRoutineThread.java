package austeretony.oxygen.common.telemetry.delegate;

import austeretony.oxygen.common.telemetry.TelemetryManager;
import austeretony.oxygen.common.telemetry.config.OxygenTelemetryConfig;
import austeretony.oxygen.common.util.TimeCounter;

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
        while (true) {
            if (!this.isRecording()) {
                TelemetryManager.instance().process();
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
