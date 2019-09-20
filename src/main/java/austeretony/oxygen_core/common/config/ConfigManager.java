package austeretony.oxygen_core.common.config;

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

import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.client.CPSyncConfigs;
import austeretony.oxygen_core.common.update.UpdateAdaptersManager;
import austeretony.oxygen_core.common.util.JsonUtils;
import austeretony.oxygen_core.server.config.OxygenConfigServer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayerMP;

public final class ConfigManager {

    private static ConfigManager instance;

    private final List<ConfigHolder> configs = new ArrayList<>(10);

    private final ByteBuf compressed = Unpooled.buffer();

    private ConfigManager() {}

    public static void create() {
        if (instance == null)
            instance = new ConfigManager();
    }

    public static ConfigManager instance() {
        return instance;
    }

    public void registerConfig(ConfigHolder config) {
        this.configs.add(config);
    }

    public void loadConfigs() {
        for (ConfigHolder config : this.configs) 
            load(config.getExternalPath(), config.getInternalPath(), config);
    }

    //server
    public void syncConfigs(EntityPlayerMP playerMP) {
        if (OxygenConfigServer.SYNC_CONFIGS.getBooleanValue() && this.compressed.writerIndex() > 0)
            OxygenMain.network().sendTo(new CPSyncConfigs(this.compressed), playerMP);
    }

    //client
    public void readConfigs(ByteBuf buffer) {
        try {
            for (ConfigHolder configHolder : this.configs) 
                configHolder.read(buffer);
        } finally {
            if (buffer != null)
                buffer.release();
        }
    }

    private void load(String externalFile, String internalFile, ConfigHolder config) {
        Path configPath = Paths.get(externalFile);      
        if (Files.exists(configPath)) {
            try {      
                loadData(updateConfig(JsonUtils.getInternalJsonData(internalFile).getAsJsonObject(), externalFile, config), config);
            } catch (IOException exception) {  
                OxygenMain.LOGGER.error("External configuration file for <{}> damaged!", config.getDomain());
                exception.printStackTrace();
            }       
        } else {                
            try {               
                Files.createDirectories(configPath.getParent());                                
                createExternalCopyAndLoad(JsonUtils.getInternalJsonData(internalFile).getAsJsonObject(), externalFile, config);  
            } catch (IOException exception) {   
                OxygenMain.LOGGER.error("Internal configuration file for <{}> damaged!", config.getDomain());
                exception.printStackTrace();
            }                       
        }
    }

    private void createExternalCopyAndLoad(JsonObject internalConfig, String externalFile, ConfigHolder configHolder) {       
        try {           
            JsonUtils.createExternalJsonFile(externalFile, internalConfig);            
        } catch (IOException exception) {       
            OxygenMain.LOGGER.error("External config file creation failed for <{}>.", configHolder.getDomain());
            exception.printStackTrace();
        }
        loadData(internalConfig, configHolder);
    }

    private void loadData(JsonObject configFile, ConfigHolder config) {  
        OxygenMain.LOGGER.info("Loading config for <{}>...", config.getDomain());
        config.init(configFile);
        for (ConfigValue value : config.values()) {
            switch (value.getType()) {
            case BOOLEAN:
                OxygenMain.LOGGER.info("- {} set to: {}.", value.getKey(), value.getBooleanValue());
                break;
            case INT: 
                OxygenMain.LOGGER.info("- {} set to: {}.", value.getKey(), value.getIntValue());
                break;
            case LONG: 
                OxygenMain.LOGGER.info("- {} set to: {}.", value.getKey(), value.getLongValue());
                break;
            case FLOAT: 
                OxygenMain.LOGGER.info("- {} set to: {}.", value.getKey(), value.getFloatValue());
                break;
            case STRING: 
                OxygenMain.LOGGER.info("- {} set to: {}.", value.getKey(), value.getStringValue());
                break;
            }
        }
        if (config.sync())
            config.write(this.compressed);
        OxygenMain.LOGGER.info("Config loaded.");
        OxygenMain.LOGGER.info("--------------");
    }

    private static JsonObject updateConfig(JsonObject internalConfig, String externalConfigFolder, ConfigHolder configHolder) throws IOException {
        try {            
            JsonObject externalConfigOld, externalConfigNew, externalGroupNew;
            externalConfigOld = JsonUtils.getExternalJsonData(externalConfigFolder).getAsJsonObject();   
            JsonElement versionElement = externalConfigOld.get("version");
            if (versionElement == null 
                    || isOutdated(versionElement.getAsString(), configHolder.getVersion())) {
                OxygenMain.LOGGER.info("Updating <{}> config file...", configHolder.getDomain());
                externalConfigNew = new JsonObject();
                externalConfigNew.add("version", new JsonPrimitive(configHolder.getVersion()));
                Map<String, JsonElement> 
                internalData = new LinkedHashMap<>(),
                externlDataOld = new HashMap<>(),
                internalGroup, externlGroupOld;
                for (Map.Entry<String, JsonElement> entry : internalConfig.entrySet())
                    internalData.put(entry.getKey(), entry.getValue());
                for (Map.Entry<String, JsonElement> entry : externalConfigOld.entrySet())
                    externlDataOld.put(entry.getKey(), entry.getValue());      
                for (String key : internalData.keySet()) {
                    internalGroup = new LinkedHashMap<>();
                    externlGroupOld = new HashMap<>();
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
                if (versionElement != null)
                    UpdateAdaptersManager.moduleUpdated(configHolder.getDomain(), versionElement.getAsString(), configHolder.getVersion());
                return externalConfigNew;
            }
            return externalConfigOld;            
        } catch (IOException exception) {  
            OxygenMain.LOGGER.error("External configuration file for <{}> damaged!", configHolder.getDomain());
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
            OxygenMain.LOGGER.error("Versions comparison failed!");               
            exception.printStackTrace();
        }
        return true;
    }
}
