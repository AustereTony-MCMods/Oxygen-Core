package austeretony.oxygen.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import austeretony.oxygen.common.api.IOxygenTask;
import austeretony.oxygen.common.api.IPersistentData;
import austeretony.oxygen.common.api.OxygenHelperClient;
import austeretony.oxygen.common.core.api.CommonReference;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.util.JsonUtils;

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

    public static void loadLocalization(List<String> languageList, Map<String, String> properties) {
        String localizationFolder = CommonReference.getGameFolder() + "/config/oxygen/localization/localization.json";
        Path localizationPath = Paths.get(localizationFolder);      
        if (Files.exists(localizationPath)) {
            try {       
                loadLocalization(JsonUtils.getExternalJsonData(localizationFolder).getAsJsonObject(), languageList, properties);
            } catch (IOException exception) {       
                exception.printStackTrace();
                return;
            }
        } else {
            try {               
                Files.createDirectories(localizationPath.getParent());
                JsonObject loclization = JsonUtils.getInternalJsonData("assets/oxygen/localization.json").getAsJsonObject();
                JsonUtils.createExternalJsonFile(localizationFolder, loclization);    
                loadLocalization(loclization, languageList, properties);
            } catch (IOException exception) {               
                exception.printStackTrace();
            }   
        }
    }

    private static void loadLocalization(JsonObject localizationFile, List<String> languageList, Map<String, String> properties) {
        OxygenMain.OXYGEN_LOGGER.info("Searching for custom localization...");
        for (String lang : languageList) {
            JsonElement entriesElement = localizationFile.get(lang.toLowerCase());
            if (entriesElement != null) {
                OxygenMain.OXYGEN_LOGGER.info("Loading custom <{}> localization...", lang);
                JsonObject localizationObject = entriesElement.getAsJsonObject();
                Set<Map.Entry<String, JsonElement>> entrySet = localizationObject.entrySet();
                for (Map.Entry<String, JsonElement> entry : entrySet)
                    properties.put(entry.getKey(), entry.getValue().getAsJsonObject().getAsString());
            } else {
                OxygenMain.OXYGEN_LOGGER.error("Custom localization for <{}> undefined!", lang);
            }
        }
    }
}
