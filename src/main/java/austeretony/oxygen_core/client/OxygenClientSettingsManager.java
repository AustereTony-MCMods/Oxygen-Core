package austeretony.oxygen_core.client;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.JsonObject;

import austeretony.oxygen_core.client.api.OxygenHelperClient;
import austeretony.oxygen_core.common.api.OxygenHelperCommon;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.settings.SettingValue;
import austeretony.oxygen_core.common.util.JsonUtils;

public class OxygenClientSettingsManager {

    private final Map<String, SettingValue> settings = new TreeMap<>();

    private volatile boolean changed;

    protected OxygenClientSettingsManager() {}

    public void register(SettingValue setting) {
        this.settings.put(setting.getKey(), setting);
    }

    public SettingValue getSettingValue(String key) {
        return this.settings.get(key);
    }

    public void loadSettings() {
        OxygenHelperClient.addIOTask(()->this.load());
    }

    private void load() {
        String pathStr = OxygenHelperCommon.getConfigFolder() + "data/client/settings/settings.json";
        Path path =  Paths.get(pathStr);
        JsonObject settingsObject;
        if (Files.exists(path)) {
            try {    
                settingsObject = JsonUtils.getExternalJsonData(pathStr).getAsJsonObject();
                for (SettingValue setting : this.settings.values())
                    setting.load(settingsObject);
                JsonUtils.createExternalJsonFile(pathStr, settingsObject);
                OxygenMain.LOGGER.info("Client setting loaded.");
            } catch (IOException | NullPointerException exception) {  
                OxygenMain.LOGGER.error("Client settings file damaged!", exception);
            }       
        } else {                
            try {               
                Files.createDirectories(path.getParent());                                
                settingsObject = new JsonObject();
                for (SettingValue setting : this.settings.values())
                    setting.save(settingsObject);
                JsonUtils.createExternalJsonFile(pathStr, settingsObject);
                OxygenMain.LOGGER.info("Client setting file created.");
            } catch (IOException exception) {   
                OxygenMain.LOGGER.error("Failed to create client settings file!");
                exception.printStackTrace();
            }     
        }
    }

    public void changed() {
        this.changed = true;
    }

    public void save() {
        OxygenHelperClient.addIOTask(()->{
            if (this.changed) {
                this.changed = false;
                String pathStr = OxygenHelperCommon.getConfigFolder() + "data/client/settings/settings.json";
                Path path =  Paths.get(pathStr);
                if (Files.exists(path)) {
                    try {    
                        JsonObject settingsObject = new JsonObject();
                        for (SettingValue setting : this.settings.values())
                            setting.save(settingsObject);
                        JsonUtils.createExternalJsonFile(pathStr, settingsObject);
                    } catch (IOException | NullPointerException exception) {  
                        OxygenMain.LOGGER.error("Client settings file damaged!", exception);
                    }       
                }
            }
        });
    }
}
