package austeretony.oxygen.common;

import austeretony.oxygen.common.api.process.AbstractPersistentProcess;
import austeretony.oxygen.common.process.IPersistentProcess;

public class StatWatcherSyncProcess extends AbstractPersistentProcess {

    @Override
    public boolean hasDelay() {
        return true;
    }

    @Override
    public int getExecutionDelay() {
        return 20;//every 1 second
    }

    @Override
    public void execute() {
        OxygenStatWatcherManagerServer.instance().sync();
    }
}
