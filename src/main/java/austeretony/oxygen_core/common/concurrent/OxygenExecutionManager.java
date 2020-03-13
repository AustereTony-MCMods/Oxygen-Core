package austeretony.oxygen_core.common.concurrent;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import austeretony.oxygen_core.common.main.EnumSide;
import austeretony.oxygen_core.common.main.OxygenMain;

public class OxygenExecutionManager {

    private OxygenExecutors executors;

    public OxygenExecutionManager(EnumSide side, int ioThreads, int networkThreads, int routineThreads, int schedulerThreads) {
        this.executors = new OxygenExecutors(side, ioThreads, networkThreads, routineThreads, schedulerThreads);
    }

    public OxygenExecutors getExecutors() {
        return this.executors;
    }

    public Future<?> addIOTask(final Runnable task) {
        return this.executors.getIOExecutorService().submit(()->{
            try {
                task.run();
            } catch (Exception exception) {
                OxygenMain.LOGGER.error("[Core] IO task execution failed.", exception);
            }
        });
    }

    public Future<?> addNetworkTask(final Runnable task) {
        return this.executors.getNetworkExecutorService().submit(()->{
            try {
                task.run();
            } catch (Exception exception) {
                OxygenMain.LOGGER.error("[Core] Network task execution failed.", exception);
            }
        });
    }

    public Future<?> addRoutineTask(final Runnable task) {
        return this.executors.getRoutineExecutorService().submit(()->{
            try {
                task.run();
            } catch (Exception exception) {
                OxygenMain.LOGGER.error("[Core] Routine task execution failed.", exception);
            }
        });
    }

    public ScheduledFuture<?> scheduleTask(Runnable task, long delay, TimeUnit unit) {
        return this.executors.getSchedulerExecutorService().schedule(task, delay, unit);
    }

    public void shutdown() {
        this.executors.shutdownServices();
    }
}
