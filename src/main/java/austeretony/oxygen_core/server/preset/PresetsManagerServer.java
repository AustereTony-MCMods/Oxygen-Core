package austeretony.oxygen_core.server.preset;

import austeretony.oxygen_core.common.api.OxygenCommon;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.packets.client.CPSyncPreset;
import austeretony.oxygen_core.common.network.packets.client.CPSyncPresetsVersions;
import austeretony.oxygen_core.common.preset.Preset;
import austeretony.oxygen_core.common.util.MinecraftCommon;
import austeretony.oxygen_core.server.api.OxygenServer;
import net.minecraft.entity.player.EntityPlayerMP;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class PresetsManagerServer {

    private final Map<Integer, Preset> presetsMap = new HashMap<>();
    private final List<String> data = new ArrayList<>();

    public void registerPreset(Preset preset) {
        presetsMap.put(preset.getId(), preset);
    }

    public void loadPresets() {
        data.clear();
        OxygenMain.logInfo(1, "[Core] Loading presets...");

        String worldId = OxygenServer.getWorldId();
        data.add(worldId);

        for (Preset preset : presetsMap.values()) {
            preset.loadVersion(worldId);
            preset.load(worldId);

            data.add(String.valueOf(preset.getVersion()));
        }
    }

    @Nullable
    public Preset getPreset(int id) {
        return presetsMap.get(id);
    }

    public void syncVersions(EntityPlayerMP playerMP) {
        OxygenMain.logInfo(2, "[Core] Sending presets versions to: {}", MinecraftCommon.getEntityName(playerMP));
        OxygenMain.network().sendTo(new CPSyncPresetsVersions(data), playerMP);
    }

    public void syncPreset(EntityPlayerMP playerMP, int id) {
        Preset preset = presetsMap.get(id);
        if (preset != null) {
            OxygenMain.logInfo(2, "[Core] Synchronizing preset <{}> with: {}",
                    preset.getName(), MinecraftCommon.getEntityName(playerMP));
            OxygenMain.network().sendTo(new CPSyncPreset(preset), playerMP);
        }
    }

    public static long loadOrCreateVersion(String presetName) {
        long version = 1L;
        Path path = Paths.get(OxygenCommon.getConfigFolder() + "/data/server/presets/" + presetName + "/version.txt");
        if (Files.exists(path)) {
            try {
                List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
                String versionSrt = lines.get(0).trim();
                version = Long.parseLong(versionSrt);
            } catch (Exception exception) {
                OxygenMain.logError(1, "[Core] Failed to load version preset <{}>", presetName);
            }
        } else {
            try {
                Files.createDirectories(path.getParent());
                Files.write(path, Collections.singletonList(String.valueOf(version)), StandardCharsets.UTF_8);
            } catch (IOException exception) {
                OxygenMain.logError(1, "[Core] Failed to create preset <{}> version file", presetName);
            }
        }
        return version;
    }
}
