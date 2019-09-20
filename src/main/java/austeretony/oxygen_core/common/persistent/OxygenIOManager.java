package austeretony.oxygen_core.common.persistent;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import austeretony.oxygen_core.common.concurrent.OxygenExecutionManager;
import austeretony.oxygen_core.common.main.OxygenMain;

public class OxygenIOManager {

    private final OxygenExecutionManager executionManager;

    public OxygenIOManager(OxygenExecutionManager executionManager) {
        this.executionManager = executionManager;
    }

    public void loadPersistentData(PersistentData data) {
        String pathStr = data.getPath();
        Path path = Paths.get(pathStr);
        data.setChanged(false);
        data.reset();
        if (Files.exists(path)) {
            try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(pathStr))) {  
                data.read(bis);
            } catch (IOException exception) {
                OxygenMain.LOGGER.error("Persistent data <{}> loading failed. Path: {}", data.getDisplayName(), pathStr);
                exception.printStackTrace();
            }
        }
    }

    public void loadPersistentDataAsync(PersistentData data) {
        this.executionManager.addIOTask(()->this.loadPersistentData(data));
    }

    public void savePersistentData(PersistentData data) {
        String pathStr = data.getPath();
        Path path = Paths.get(pathStr);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path.getParent());
            } catch (IOException exception) {
                OxygenMain.LOGGER.error("Failed to create directoy. Path: {}", pathStr);
                exception.printStackTrace();
            }
        }
        synchronized (data) {//prevents concurrent data writing for specific IPersistentData
            try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(pathStr))) {   
                data.write(bos);
            } catch (IOException exception) {
                OxygenMain.LOGGER.error("Persistent data <{}> saving failed. Path: {}", data.getDisplayName(), pathStr);
                exception.printStackTrace();
            }
        }
    }

    public void savePersistentDataAsync(PersistentData data) {
        this.executionManager.addIOTask(()->this.savePersistentData(data));
    }
}