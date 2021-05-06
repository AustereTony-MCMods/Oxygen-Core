package austeretony.oxygen_core.common.persistent;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import austeretony.oxygen_core.common.main.OxygenMain;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PersistentDataManager {

    protected final ExecutorService executor;

    protected final List<PersistentData> persistentData = new ArrayList<>();
    protected final List<Runnable> suppliers = new ArrayList<>();

    public PersistentDataManager(int savePeriodSeconds, ScheduledExecutorService scheduler, ExecutorService executor) {
        this.executor = executor;
        scheduleSave(savePeriodSeconds, scheduler);
    }

    protected void scheduleSave(int savePeriodSeconds, ScheduledExecutorService scheduler) {
        final Runnable task = () -> executor.submit(() -> {
            OxygenMain.logInfo(1, "[Core] Starting persistent data saving...");
            long start = System.currentTimeMillis();

            for (PersistentData data : persistentData) {
                if (data.isChanged()) {
                    data.resetChangedMark();
                    savePersistentData(data);
                }
            }
            for (Runnable runnable : suppliers) {
                runnable.run();
            }

            long elapsedTime = System.currentTimeMillis() - start;
            OxygenMain.logInfo(1, "[Core] Data saved in {} ms.", elapsedTime);
        });
        scheduler.scheduleAtFixedRate(task, savePeriodSeconds, savePeriodSeconds, TimeUnit.SECONDS);
    }

    public void registerPersistentData(@Nonnull PersistentData data) {
        persistentData.add(data);
        OxygenMain.logInfo(1, "[Core] Registered <{}> persistent data.", data.getName());
    }

    public void registerPersistentData(final @Nonnull Runnable task) {
        suppliers.add(task);
    }

    public void serverStopping() {
        OxygenMain.logInfo(1, "[Core] Forcing persistent data save on world unload...");
        long start = System.currentTimeMillis();

        for (PersistentData data : persistentData) {
            if (data.isChanged()) {
                data.resetChangedMark();
                savePersistentData(data);
            }
        }
        for (Runnable runnable : suppliers) {
            runnable.run();
        }

        long elapsedTime = System.currentTimeMillis() - start;
        OxygenMain.logInfo(1, "[Core] Data saved in {} ms.", elapsedTime);
    }

    public void loadPersistentData(PersistentData data) {
        synchronized (data) {
            data.resetChangedMark();
            data.reset();

            NBTTagCompound tagCompound = loadNBTData(data.getPath());
            if (tagCompound != null) {
                data.readFromNBT(tagCompound);
            }
        }
    }

    protected @Nullable NBTTagCompound loadNBTData(String pathStr) {
        NBTTagCompound tagCompound = null;
        Path path = Paths.get(pathStr);
        if (Files.exists(path)) {
            try (FileInputStream fis = new FileInputStream(pathStr)) {
                tagCompound = CompressedStreamTools.readCompressed(fis);
            } catch (IOException exception) {
                OxygenMain.logError(1, "[Core] Persistent data loading failed. Path: {}", pathStr);
                exception.printStackTrace();
            }
        }
        return tagCompound;
    }

    public Future<?> loadPersistentDataAsync(PersistentData data) {
        return executor.submit(() -> loadPersistentData(data));
    }

    public void savePersistentData(PersistentData data) {
        synchronized (data) {
            NBTTagCompound tagCompound = new NBTTagCompound();
            data.writeToNBT(tagCompound);
            saveNBTData(data.getPath(), tagCompound);
        }
    }

    protected void saveNBTData(String pathStr, NBTTagCompound tagCompound) {
        Path path = Paths.get(pathStr);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path.getParent());
            } catch (IOException exception) {
                OxygenMain.logError(1, "[Core] Failed to create directory. Path: {}", pathStr);
                exception.printStackTrace();
            }
        }

        try (FileOutputStream fos = new FileOutputStream(pathStr)) {
            CompressedStreamTools.writeCompressed(tagCompound, fos);
        } catch (IOException exception) {
            OxygenMain.logError(1, "[Core] Persistent data saving failed. Path: {}", pathStr);
            exception.printStackTrace();
        }
    }

    public Future<?> savePersistentDataAsync(PersistentData data) {
        return executor.submit(() -> savePersistentData(data));
    }
}