package austeretony.oxygen_core.common.persistent;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import austeretony.oxygen_core.common.concurrent.OxygenExecutionManager;
import austeretony.oxygen_core.common.main.OxygenMain;

public class PersistentDataManager {

    private final OxygenExecutionManager executionManager;

    private final OxygenIOManager ioManager;

    private final int savePeriodSeconds;

    private final Set<PersistentData> containers = new HashSet<>();

    private final Set<Runnable> tasks = new HashSet<>();

    public PersistentDataManager(OxygenExecutionManager executionManager, OxygenIOManager ioManager, int savePeriodSeconds) {
        this.executionManager = executionManager;
        this.ioManager = ioManager;
        this.savePeriodSeconds = savePeriodSeconds;

        Runnable task = ()->{
            for (PersistentData container : this.containers) {
                if (container.isChanged()) {
                    container.setChanged(false);
                    this.ioManager.savePersistentDataAsync(container);
                }
            }
            for (Runnable r : this.tasks)
                r.run();
        };
        this.executionManager.getExecutors().getSchedulerExecutorService().scheduleAtFixedRate(task, this.savePeriodSeconds, this.savePeriodSeconds, TimeUnit.SECONDS);
    }

    public void registerPersistentData(PersistentData data) {
        this.containers.add(data);
        OxygenMain.LOGGER.info("[Core] Registered <{}> persistent data.", data.getDisplayName());
    }

    public void registerPersistentData(Runnable task) {
        this.tasks.add(task);
    }

    public void worldUnloaded() {
        OxygenMain.LOGGER.info("[Core] Forcing persistent data save on world unload...");
        for (PersistentData data : this.containers) {
            if (data.isChanged()) {
                data.setChanged(false);
                this.ioManager.savePersistentDataAsync(data);
                OxygenMain.LOGGER.info("[Core] Persistent data <{}> saved.", data.getDisplayName());
            }
        }
        for (Runnable r : this.tasks)
            r.run();
    }
}