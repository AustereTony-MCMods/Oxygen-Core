package austeretony.oxygen.common.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import austeretony.oxygen.common.api.config.ConfigValue;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.util.JsonUtils;

public class ConfigLoader {

    public static final List<IConfigHolder> CONFIGS = new ArrayList<IConfigHolder>(7);

    public static void addConfig(IConfigHolder config) {
        CONFIGS.add(config);
    }

    public static void loadConfigs() {
        for (IConfigHolder config : CONFIGS) 
            load(config.getExternalPath(), config.getInternalPath(), config);
    }

    private static void load(String externalFile, String internalFile, IConfigHolder configHolder) {
        Path configPath = Paths.get(externalFile);      
        if (Files.exists(configPath)) {
            try {      
                loadData(updateConfig(JsonUtils.getInternalJsonData(internalFile).getAsJsonObject(), externalFile, configHolder), configHolder);
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

    private static JsonObject updateConfig(JsonObject internalConfig, String externalConfigFolder, IConfigHolder configHolder) throws IOException {
        try {            
            JsonObject externalConfigOld, externalConfigNew, externalGroupNew;
            externalConfigOld = JsonUtils.getExternalJsonData(externalConfigFolder).getAsJsonObject();   
            JsonElement versionElement = externalConfigOld.get("version");
            if (versionElement == null || isOutdated(versionElement.getAsString(), configHolder.getVersion())) {
                OxygenMain.OXYGEN_LOGGER.info("Updating <{}> config file...", configHolder.getModId());
                externalConfigNew = new JsonObject();
                externalConfigNew.add("version", new JsonPrimitive(configHolder.getVersion()));
                Map<String, JsonElement> 
                internalData = new LinkedHashMap<String, JsonElement>(),
                externlDataOld = new HashMap<String, JsonElement>(),
                internalGroup, externlGroupOld;
                for (Map.Entry<String, JsonElement> entry : internalConfig.entrySet())
                    internalData.put(entry.getKey(), entry.getValue());
                for (Map.Entry<String, JsonElement> entry : externalConfigOld.entrySet())
                    externlDataOld.put(entry.getKey(), entry.getValue());      
                for (String key : internalData.keySet()) {
                    internalGroup = new LinkedHashMap<String, JsonElement>();
                    externlGroupOld = new HashMap<String, JsonElement>();
                    externalGroupNew = new JsonObject();
                    for (Map.Entry<String, JsonElement> entry : internalData.get(key).getAsJsonObject().entrySet())
                        internalGroup.put(entry.getKey(), entry.getValue());
                    if (externlDataOld.containsKey(key)) {                    
                        for (Map.Entry<String, JsonElement> entry : externlDataOld.get(key).getAsJsonObject().entrySet())
                            externlGroupOld.put(entry.getKey(), entry.getValue());   
                        for (String k : internalGroup.keySet()) {
                            if (externlGroupOld.containsKey(k))
                                externalGroupNew.add(k, externlGroupOld.get(k));
                            else 
                                externalGroupNew.add(k, internalGroup.get(k));
                        }
                    } else {
                        for (String k : internalGroup.keySet())
                            externalGroupNew.add(k, internalGroup.get(k));
                    }
                    externalConfigNew.add(key, externalGroupNew);
                    JsonUtils.createExternalJsonFile(externalConfigFolder, externalConfigNew);
                }
                return externalConfigNew;
            }
            return externalConfigOld;            
        } catch (IOException exception) {  
            OxygenMain.OXYGEN_LOGGER.error("External configuration file for <{}> damaged!", configHolder.getModId());
            exception.printStackTrace();
        }
        return null;
    }

    public static boolean isOutdated(String currentVersion, String availableVersion) {        
        try {
            String[] 
                    cSplitted = currentVersion.split("[:]"),
                    aSplitted = availableVersion.split("[:]");    
            String 
            cVer = cSplitted[0],
            cType = cSplitted[1],
            cRev = cSplitted[2],
            aVer = aSplitted[0],
            aType = aSplitted[1],
            aRev = aSplitted[2];
            String[]
                    cVerSplitted = cVer.split("[.]"),
                    aVerSplitted = aVer.split("[.]");
            int verDiff, revDiff;               
            for (int i = 0; i < 3; i++) {                                                             
                verDiff = Integer.parseInt(aVerSplitted[i]) - Integer.parseInt(cVerSplitted[i]);                                                                                           
                if (verDiff > 0)
                    return true;                                
                if (verDiff < 0)
                    return false;
            }  
            if (aType.equals("release") && (cType.equals("beta") || cType.equals("alpha")))
                return true;
            if (aType.equals("beta") && cType.equals("alpha"))
                return true;
            revDiff = Integer.parseInt(aRev) - Integer.parseInt(cRev);                                                                                           
            if (revDiff > 0)
                return true;                                
            if (revDiff < 0)
                return false;
            return false;
        } catch (Exception exception) { 
            OxygenMain.OXYGEN_LOGGER.error("Versions comparison failed!");               
            exception.printStackTrace();
        }
        return true;
    }
}