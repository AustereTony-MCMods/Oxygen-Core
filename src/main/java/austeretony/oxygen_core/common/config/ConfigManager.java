package austeretony.oxygen_core.common.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.client.CPSyncConfigs;
import austeretony.oxygen_core.common.util.JsonUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayerMP;

public final class ConfigManager {

    private static ConfigManager instance;

    private final List<Config> configs = new ArrayList<>();

    private final ByteBuf compressedConfigs = Unpooled.buffer();

    private ConfigManager() {}

    public static void create() {
        if (instance == null)
            instance = new ConfigManager();
    }

    public static ConfigManager instance() {
        return instance;
    }

    public void registerConfig(Config config) {
        this.configs.add(config);
    }

    public void loadConfigs() {
        for (Config config : this.configs) {
            Path path =  Paths.get(config.getExternalPath());
            JsonObject configObject;
            if (Files.exists(path)) {
                try {    
                    configObject = JsonUtils.getExternalJsonData(config.getExternalPath()).getAsJsonObject();
                    configObject.add("version", new JsonPrimitive(OxygenMain.VERSION_CUSTOM));
                    config.load(configObject);
                    config.write(this.compressedConfigs);
                    JsonUtils.createExternalJsonFile(config.getExternalPath(), configObject);
                    OxygenMain.LOGGER.info("Loaded config for <{}>.", config.getDomain());
                } catch (IOException | NullPointerException exception) {  
                    OxygenMain.LOGGER.error("Config file for <{}> damaged!", config.getDomain(), exception);
                }       
            } else {                
                try {               
                    Files.createDirectories(path.getParent());                                
                    configObject = new JsonObject();
                    configObject.add("version", new JsonPrimitive(OxygenMain.VERSION_CUSTOM));
                    config.save(configObject);
                    config.write(this.compressedConfigs);
                    JsonUtils.createExternalJsonFile(config.getExternalPath(), configObject);
                    OxygenMain.LOGGER.info("Created config for <{}>.", config.getDomain());
                } catch (IOException exception) {   
                    OxygenMain.LOGGER.error("Failed to create config for <>!", config.getDomain());
                    exception.printStackTrace();
                }     
            }
        }
    }

    //server
    public void syncConfigs(EntityPlayerMP playerMP) {
        if (OxygenConfig.SYNC_CONFIGS.asBoolean())
            OxygenMain.network().sendTo(new CPSyncConfigs(this.compressedConfigs), playerMP);
    }

    //client
    public void readConfigs(ByteBuf buffer) {
        try {
            for (Config config : this.configs) 
                config.read(buffer);
        } finally {
            if (buffer != null)
                buffer.release();
        }
    }
}
