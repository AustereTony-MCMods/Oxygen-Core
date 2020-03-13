package austeretony.oxygen_core.common.concurrent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import austeretony.oxygen_core.common.main.EnumSide;
import austeretony.oxygen_core.common.main.OxygenMain;

public class OxygenExecutors {

    private final EnumSide side;

    private final ExecutorService ioService, networkService, routineService;

    private final ScheduledExecutorService schedulerService;

    private final BlockingQueue<Runnable> ioTasksQueue, networkTasksQueue, routineTasksQueue;

    public OxygenExecutors(EnumSide side, int ioThreads, int networkThreads, int routineThreads, int schedulerThreads) {
        this.side = side;
        String sideStr = side.toString();
        this.ioService = new ThreadPoolExecutor(ioThreads, ioThreads, 0L, TimeUnit.MILLISECONDS, this.ioTasksQueue = new LinkedBlockingQueue<>(),
                new ThreadFactoryBuilder()
                .setNameFormat("Oxygen IO #%d " + sideStr)
                .setDaemon(true)
                .build());
        this.networkService = new ThreadPoolExecutor(networkThreads, networkThreads, 0L, TimeUnit.MILLISECONDS, this.networkTasksQueue = new LinkedBlockingQueue<>(),
                new ThreadFactoryBuilder()
                .setNameFormat("Oxygen Network #%d " + sideStr)
                .setDaemon(true)
                .build());
        this.routineService = new ThreadPoolExecutor(routineThreads, routineThreads, 0L, TimeUnit.MILLISECONDS, this.routineTasksQueue = new LinkedBlockingQueue<>(),
                new ThreadFactoryBuilder()
                .setNameFormat("Oxygen Routine #%d " + sideStr)
                .setDaemon(true)
                .build());
        this.schedulerService = Executors.newScheduledThreadPool(schedulerThreads,
                new ThreadFactoryBuilder()
                .setNameFormat("Oxygen Scheduler #%d " + sideStr)
                .setDaemon(true)
                .build());
        ((ScheduledThreadPoolExecutor) this.schedulerService).setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
    }

    public ExecutorService getIOExecutorService() {
        return this.ioService;
    }

    public ExecutorService getNetworkExecutorService() {
        return this.networkService;
    }

    public ExecutorService getRoutineExecutorService() {
        return this.routineService;
    }

    public ScheduledExecutorService getSchedulerExecutorService() {
        return this.schedulerService;
    }

    public BlockingQueue<Runnable> getIOTasksQueue() {
        return this.ioTasksQueue;
    }

    public BlockingQueue<Runnable> getNetworkTasksQueue() {
        return this.networkTasksQueue;
    }

    public BlockingQueue<Runnable> getRoutineTasksQueue() {
        return this.routineTasksQueue;
    }

    public void shutdownServices() {
        OxygenMain.LOGGER.info("[Core] Initiated <{}> executors shutdown. Tasks: {}/{}/{}.", 
                this.side, this.ioTasksQueue.size(), this.networkTasksQueue.size(), this.routineTasksQueue.size());

        this.schedulerService.shutdown();
        this.ioService.shutdown();
        this.networkService.shutdownNow();
        this.routineService.shutdownNow();

        this.awaitSchedulerTasksExecution();
        this.awaitIOTasksExecution();
    }

    private void awaitSchedulerTasksExecution() {
        try {
            boolean executed = this.schedulerService.awaitTermination(10L, TimeUnit.SECONDS);
            if (executed)
                OxygenMain.LOGGER.info("[Core] Successfully executed scheduler tasks at <{}>.", this.side);
            else
                OxygenMain.LOGGER.info("[Core] Failed to execute scheduler tasks at <{}>.", this.side);
        } catch (InterruptedException exception) {
            OxygenMain.LOGGER.info("[Core] <{}> main thread was interrupteed! Failed to execute scheduler tasks.", this.side);
        }
    }

    private void awaitIOTasksExecution() {
        try {
            boolean executed = this.ioService.awaitTermination(10L, TimeUnit.SECONDS);
            if (executed)
                OxygenMain.LOGGER.info("[Core] Successfully executed io tasks at <{}>.", this.side);
            else
                OxygenMain.LOGGER.info("[Core] Failed to execute io tasks at <{}>.", this.side);
        } catch (InterruptedException exception) {
            OxygenMain.LOGGER.info("[Core] <{}> main thread was interrupteed! Failed to execute io tasks.", this.side);
        }
    }
}
