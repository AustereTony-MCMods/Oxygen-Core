package austeretony.oxygen.common.telemetry.delegate;

import austeretony.oxygen.common.OxygenManagerServer;

public class TelemetryIOThread extends Thread {

    public TelemetryIOThread(String string) {
        super(string);
        this.setDaemon(true);
    }

    @Override
    public void run() {
        while (!interrupted()) {            
            if (OxygenManagerServer.instance().getTelemetryManager().getProcessingThread().isRecording()) {
                OxygenManagerServer.instance().getTelemetryManager().record();
                OxygenManagerServer.instance().getTelemetryManager().getProcessingThread().stopRecord();
            }
        }
    }
}
