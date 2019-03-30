package austeretony.oxygen.common.telemetry.delegate;

import austeretony.oxygen.common.telemetry.TelemetryManager;

public class TelemetryIOThread extends Thread {

    public TelemetryIOThread(String string) {
        super(string);
        this.setDaemon(true);
    }

    @Override
    public void run() {
        while (true) {
            if (TelemetryManager.instance().getProcessingThread().isRecording()) {
                TelemetryManager.instance().record();
                TelemetryManager.instance().getProcessingThread().stopRecord();
            }
        }
    }
}
