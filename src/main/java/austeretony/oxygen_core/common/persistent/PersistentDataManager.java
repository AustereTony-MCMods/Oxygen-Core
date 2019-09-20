package austeretony.oxygen_core.common.persistent;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import austeretony.oxygen_core.common.concurrent.OxygenExecutionManager;
import austeretony.oxygen_core.common.main.OxygenMain;

public class PersistentDataManager {

    private final OxygenExecutionManager executionManager;

    private final OxygenIOManager ioManager;

    private final Set<PersistentData> globalData = new HashSet<PersistentData>();

    public PersistentDataManager(OxygenExecutionManager executionManager, OxygenIOManager ioManager) {
        this.executionManager = executionManager;
        this.ioManager = ioManager;
    }

    public void registerPersistentData(PersistentData data) {
        this.globalData.add(data);
        Runnable task = ()->{
            if (data.isChanged()) {
                data.setChanged(false);
                this.ioManager.savePersistentDataAsync(data);
            }
        };
        this.executionManager.getExecutors().getSchedulerExecutorService().scheduleAtFixedRate(task, data.getSaveDelayMinutes(), data.getSaveDelayMinutes(), TimeUnit.MINUTES);
        OxygenMain.LOGGER.info("Registered <{}> persistent data. Scheduled save every <{}> minutes.", data.getDisplayName(), data.getSaveDelayMinutes());
    }

    public void worldUnloaded() {
        OxygenMain.LOGGER.info("Forcing persistent data save on world unload...");
        for (PersistentData data : this.globalData) {
            if (data.isChanged()) {
                data.setChanged(false);
                this.ioManager.savePersistentDataAsync(data);
                OxygenMain.LOGGER.info("Persistent data <{}> saved.", data.getDisplayName());
            }
        }
    }
}