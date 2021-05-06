package austeretony.oxygen_core.client.persistent;

import austeretony.oxygen_core.client.util.MinecraftClient;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.persistent.PersistentData;
import austeretony.oxygen_core.common.persistent.PersistentDataManager;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PersistentDataManagerClient extends PersistentDataManager {

    public PersistentDataManagerClient(int savePeriodSeconds, ScheduledExecutorService scheduler,
                                       ExecutorService executor) {
        super(savePeriodSeconds, scheduler, executor);
    }

    @Override
    protected void scheduleSave(int savePeriodSeconds, ScheduledExecutorService scheduler) {
        final Runnable schedulerTask = () -> {
            final Runnable task = () -> {
                Map<String, NBTTagCompound> changedDataMap = new HashMap<>();
                for (PersistentData data : persistentData) {
                    if (data.isChanged()) {
                        data.resetChangedMark();

                        NBTTagCompound tagCompound = new NBTTagCompound();
                        data.writeToNBT(tagCompound);
                        changedDataMap.put(data.getPath(), tagCompound);
                    }
                }
                if (changedDataMap.isEmpty()) return;

                executor.submit(() -> {
                    long start = System.currentTimeMillis();
                    OxygenMain.logInfo(1, "[Core] Starting persistent data saving...");

                    for (Map.Entry<String, NBTTagCompound> entry : changedDataMap.entrySet()) {
                        saveNBTData(entry.getKey(), entry.getValue());
                    }

                    long elapsedTime = System.currentTimeMillis() - start;
                    OxygenMain.logInfo(1, "[Core] Data saved in {} ms.", elapsedTime);
                });
            };
            MinecraftClient.delegateToClientThread(task);
        };
        scheduler.scheduleAtFixedRate(schedulerTask, savePeriodSeconds, savePeriodSeconds, TimeUnit.SECONDS);
    }
}
