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

    private final OxygenManagerClient manager;

    public OxygenLoaderClient(OxygenManagerClient manager) {
        this.manager = manager;
    }

    public void loadPlayerDataDelegated(IPersistentData persistentData) {
        OxygenHelperClient.addIOTask(new IOxygenTask() {

            @Override
            public void execute() {
                loadPlayerData(persistentData);
            }     
        });
    }

    public void loadPlayerData(IPersistentData persistentData) {
        String folder = OxygenHelperClient.getDataFolder() + "/client/players/" + OxygenHelperClient.getPlayerUUID() + "/" + persistentData.getPath();
        Path path = Paths.get(folder);
        if (Files.exists(path)) {
            try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(folder))) {    
                persistentData.read(bis);
            } catch (IOException exception) {
                OxygenMain.OXYGEN_LOGGER.error("Client player data <{}> loading failed for <{}>.", persistentData.getName(), persistentData.getModId());
                exception.printStackTrace();
            }
        }
    }

    public void savePlayerDataDelegated(IPersistentData persistentData) {
        OxygenHelperClient.addIOTask(new IOxygenTask() {

            @Override
            public void execute() {
                savePlayerData(persistentData);
            }     
        });
    }

    public void savePlayerData(IPersistentData persistentData) {
        String folder = OxygenHelperClient.getDataFolder() + "/client/players/" + OxygenHelperClient.getPlayerUUID() + "/" + persistentData.getPath();
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
            OxygenMain.OXYGEN_LOGGER.error("Client player data <{}> saving failed for <{}>.", persistentData.getName(), persistentData.getModId());
            exception.printStackTrace();
        }
    }

    public void loadWorldDataDelegated(IPersistentData persistentData) {
        OxygenHelperClient.addIOTask(new IOxygenTask() {

            @Override
            public void execute() {
                loadWorldData(persistentData);
            }     
        });
    }

    public void loadWorldData(IPersistentData persistentData) {
        String folder = OxygenHelperClient.getDataFolder() + "/client/world/" + persistentData.getPath();
        Path path = Paths.get(folder);
        if (Files.exists(path)) {
            try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(folder))) {    
                persistentData.read(bis);
            } catch (IOException exception) {
                OxygenMain.OXYGEN_LOGGER.error("Client wolrd data <{}> loading failed for <{}>.", persistentData.getName(), persistentData.getModId());
                exception.printStackTrace();
            }
        }
    }

    public void saveWorldDataDelegated(IPersistentData persistentData) {
        OxygenHelperClient.addIOTask(new IOxygenTask() {

            @Override
            public void execute() {
                saveWorldData(persistentData);
            }     
        });
    }

    public void saveWorldData(IPersistentData persistentData) {
        String folder = OxygenHelperClient.getDataFolder() + "/client/world/" + persistentData.getPath();
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
            OxygenMain.OXYGEN_LOGGER.error("Client world data <{}> saving failed for <{}>.", persistentData.getName(), persistentData.getModId());
            exception.printStackTrace();
        }
    }
}
