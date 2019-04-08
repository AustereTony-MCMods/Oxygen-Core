package austeretony.oxygen.common.delegate;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import austeretony.oxygen.common.api.IOxygenTask;
import austeretony.oxygen.common.main.OxygenMain;

public class OxygenThread extends Thread {

    private BlockingQueue<IOxygenTask> tasks = new LinkedBlockingQueue<IOxygenTask>();

    public OxygenThread(String threadName) {
        super(threadName);
        this.setDaemon(true);
    }

    @Override
    public void run() {
        IOxygenTask task;
        while (!interrupted()) {            
            try {
                task = this.tasks.take();
                try {
                    task.execute();
                } catch (Exception exception) {
                    OxygenMain.OXYGEN_LOGGER.error("An error intercepted from thread {}.", this.getName());
                    exception.printStackTrace();
                }
            } catch (InterruptedException exception) {
                OxygenMain.OXYGEN_LOGGER.info("Interrupted thread {}.", this.getName());
            }
        }
    }

    public void addTask(IOxygenTask task) {
        this.tasks.add(task);
    }
}
