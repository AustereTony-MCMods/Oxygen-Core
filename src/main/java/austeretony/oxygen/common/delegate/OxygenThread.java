package austeretony.oxygen.common.delegate;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import austeretony.oxygen.common.main.IOxygenTask;

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
                task = tasks.take();
                task.execute();
            } catch (InterruptedException exception) {}
        }
    }

    public void addTask(IOxygenTask task) {
        this.tasks.add(task);
    }
}
