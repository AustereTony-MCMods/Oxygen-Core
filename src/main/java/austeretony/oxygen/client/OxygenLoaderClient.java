package austeretony.oxygen.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import austeretony.oxygen.client.api.OxygenHelperClient;
import austeretony.oxygen.common.api.IOxygenTask;
import austeretony.oxygen.common.api.IPersistentData;
import austeretony.oxygen.common.main.OxygenMain;

public class OxygenLoaderClient {

    public static void loadPersistentDataDelegated(IPersistentData persistentData) {
        OxygenHelperClient.addIOTask(new IOxygenTask() {

            @Override
            public void execute() {
                loadPersistentData(persistentData);
            }     
        });
    }

    public static void loadPersistentData(IPersistentData persistentData) {
        String folder = OxygenHelperClient.getDataFolder() + "/client/" + persistentData.getPath();
        Path path = Paths.get(folder);
        if (Files.exists(path)) {
            try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(folder))) {    
                persistentData.read(bis);
            } catch (IOException exception) {
                OxygenMain.OXYGEN_LOGGER.error("Client persistent data <{}> loading failed for <{}>, path: {}", persistentData.getName(), persistentData.getModId(), persistentData.getPath());
                exception.printStackTrace();
            }
        }
    }

    public static void savePersistentDataDelegated(IPersistentData persistentData) {
        OxygenHelperClient.addIOTask(new IOxygenTask() {

            @Override
            public void execute() {
                savePersistentData(persistentData);
            }     
        });
    }

    public static void savePersistentData(IPersistentData persistentData) {
        String folder = OxygenHelperClient.getDataFolder() + "/client/" + persistentData.getPath();
        Path path = Paths.get(folder);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path.getParent());
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(folder))) {   
            persistentData.write(bos);
        } catch (IOException exception) {
            OxygenMain.OXYGEN_LOGGER.error("Client persistent data <{}> saving failed for <{}>, path: {}", persistentData.getName(), persistentData.getModId(), persistentData.getPath());
            exception.printStackTrace();
        }
    }
}
