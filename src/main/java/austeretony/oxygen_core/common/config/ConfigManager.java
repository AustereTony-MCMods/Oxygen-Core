package austeretony.oxygen_core.common.config;

import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.packets.client.CPSyncConfigs;
import austeretony.oxygen_core.common.util.MinecraftCommon;
import austeretony.oxygen_core.common.util.JsonUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayerMP;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public final class ConfigManager {

    private static ConfigManager instance;

    private final List<Config> configs = new ArrayList<>(5);

    private final ByteBuf compressedConfigs = Unpooled.buffer();

    private ConfigManager() {}

    public static ConfigManager instance() {
        if (instance == null)
            instance = new ConfigManager();
        return instance;
    }

    public void registerConfig(Config config) {
        configs.add(config);
    }

    public void loadConfigs() {
        for (Config config : configs) {
            Path path =  Paths.get(MinecraftCommon.getGameFolder() + "/config/oxygen/" + config.getFileName());
            if (Files.exists(path)) {
                try {
                    JsonObject configObject = JsonUtils.getExternalJsonData(MinecraftCommon.getGameFolder() + "/config/oxygen/"
                            + config.getFileName()).getAsJsonObject();
                    configObject.add("version", new JsonPrimitive(config.getVersion()));

                    if (config.load(configObject)) {
                        JsonUtils.createExternalJsonFile(MinecraftCommon.getGameFolder() + "/config/oxygen/"
                                + config.getFileName(), configObject);
                    }
                    config.write(compressedConfigs);

                    OxygenMain.logInfo(1, "[Core] Loaded config for <{}>.", config.getDomain());
                } catch (IOException | NullPointerException exception) {
                    OxygenMain.logError(1, "[Core] Config file for <{}> damaged!", config.getDomain(), exception);
                }
            } else {
                try {
                    Files.createDirectories(path.getParent());

                    JsonObject configObject = new JsonObject();
                    configObject.add("version", new JsonPrimitive(config.getVersion()));
                    config.save(configObject);
                    config.write(compressedConfigs);

                    JsonUtils.createExternalJsonFile(MinecraftCommon.getGameFolder() + "/config/oxygen/"
                            + config.getFileName(), configObject);
                    OxygenMain.logInfo(1, "[Core] Created config for <{}>.", config.getDomain());
                } catch (IOException exception) {
                    OxygenMain.logError(1, "[Core] Failed to create config for <{}>!", config.getDomain());
                    exception.printStackTrace();
                }
            }
        }
    }

    public void syncConfigs(EntityPlayerMP playerMP) {
        OxygenMain.network().sendTo(new CPSyncConfigs(compressedConfigs), playerMP);
    }

    public void readConfigs(ByteBuf buffer) {
        try {
            for (Config config : configs) {
                config.read(buffer);
            }
        } finally {
            if (buffer != null) {
                buffer.release();
            }
        }
    }
}
