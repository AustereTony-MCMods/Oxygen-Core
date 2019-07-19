package austeretony.oxygen.common.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.Set;

import com.google.gson.JsonObject;

import austeretony.oxygen.common.api.config.ConfigValue;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.util.JsonUtils;
import austeretony.oxygen.util.OxygenUtils;

public class ConfigLoader {

    public static final Set<IConfigHolder> CONFIG_HOLDERS = new LinkedHashSet<IConfigHolder>(7);

    public static void addConfig(IConfigHolder config) {
        CONFIG_HOLDERS.add(config);
    }

    public static void loadConfigs() {
        for (IConfigHolder config : CONFIG_HOLDERS) 
            load(config.getExternalPath(), config.getInternalPath(), config);
    }

    private static void load(String externalFile, String internalFile, IConfigHolder configHolder) {
        Path configPath = Paths.get(externalFile);      
        if (Files.exists(configPath)) {
            try {      
                loadData(OxygenUtils.updateConfig(JsonUtils.getInternalJsonData(internalFile).getAsJsonObject(), externalFile, configHolder), configHolder);
            } catch (IOException exception) {  
                OxygenMain.OXYGEN_LOGGER.error("External configuration file for <{}> damaged!", configHolder.getModId());
                exception.printStackTrace();
            }       
        } else {                
            try {               
                Files.createDirectories(configPath.getParent());                                
                createExternalCopyAndLoad(JsonUtils.getInternalJsonData(internalFile).getAsJsonObject(), externalFile, configHolder);  
            } catch (IOException exception) {   
                OxygenMain.OXYGEN_LOGGER.error("Internal configuration file for <{}> damaged!", configHolder.getModId());
                exception.printStackTrace();
            }                       
        }
    }

    private static void createExternalCopyAndLoad(JsonObject internalConfig, String externalFile, IConfigHolder configHolder) {       
        try {           
            JsonUtils.createExternalJsonFile(externalFile, internalConfig);            
        } catch (IOException exception) {       
            OxygenMain.OXYGEN_LOGGER.error("External config file creation failed for <{}>.", configHolder.getModId());
            exception.printStackTrace();
        }
        loadData(internalConfig, configHolder);
    }

    private static void loadData(JsonObject configFile, IConfigHolder configHolder) {  
        OxygenMain.OXYGEN_LOGGER.info("Loading config for <{}>...", configHolder.getModId());
        configHolder.init(configFile);
        for (ConfigValue value : configHolder.values()) {
            switch (value.getType()) {
            case BOOLEAN:
                OxygenMain.OXYGEN_LOGGER.info("- {} set to: {}.", value.configKey, value.getBooleanValue());
                break;
            case INT: 
                OxygenMain.OXYGEN_LOGGER.info("- {} set to: {}.", value.configKey, value.getIntValue());
                break;
            case FLOAT: 
                OxygenMain.OXYGEN_LOGGER.info("- {} set to: {}.", value.configKey, value.getFloatValue());
                break;
            case STRING: 
                OxygenMain.OXYGEN_LOGGER.info("- {} set to: {}.", value.configKey, value.getStringValue());
                break;
            }
        }
        OxygenMain.OXYGEN_LOGGER.info("Config loaded.");
        OxygenMain.OXYGEN_LOGGER.info("--------------");
    }
}
