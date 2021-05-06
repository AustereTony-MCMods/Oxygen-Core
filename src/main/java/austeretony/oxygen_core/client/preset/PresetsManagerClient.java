package austeretony.oxygen_core.client.preset;

import austeretony.oxygen_core.client.api.OxygenClient;
import austeretony.oxygen_core.common.api.OxygenCommon;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.packets.server.SPRequestPresetsSync;
import austeretony.oxygen_core.common.preset.Preset;
import io.netty.buffer.ByteBuf;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class PresetsManagerClient {

    public final Map<Integer, Preset> presetsMap = new HashMap<>();

    public void registerPreset(Preset preset) {
        presetsMap.put(preset.getId(), preset);
    }

    public void initPresets(List<String> data) {
        String worldId = data.get(0);
        OxygenMain.logInfo(1, "[Core] Initializing presets for world: {}", worldId);
        List<Integer> outdatedIds = new ArrayList<>(3);

        int index = 1;
        for (Preset preset : presetsMap.values()) {
            preset.loadVersion(worldId);
            if (preset.getVersion() < Long.parseLong(data.get(index++))) {
                OxygenMain.logInfo(1, "[Core] Preset <{}> outdated", preset.getName());
                outdatedIds.add(preset.getId());
            } else {
                OxygenMain.logInfo(1, "[Core] Preset <{}> up-to-date, loading...", preset.getName());
                preset.load(worldId);
            }
        }

        if (!outdatedIds.isEmpty()) {
            OxygenMain.logInfo(1, "[Core] Requesting outdated presets synchronization");
            OxygenMain.network().sendToServer(new SPRequestPresetsSync(outdatedIds));
        }
    }

    public void updatePreset(ByteBuf buffer) {
        try {
            int id = buffer.readByte();
            Preset preset = presetsMap.get(id);
            if (preset != null) {
                preset.read(buffer);

                OxygenMain.logInfo(1, "[Core] Updating preset <{}>", preset.getName());
                preset.save();
            }
        } finally {
            if (buffer != null) {
                buffer.release();
            }
        }
    }

    @Nullable
    public Preset getPreset(int id) {
        return presetsMap.get(id);
    }

    public static long loadVersion(String presetName, String worldId) {
        long version = -1L;
        Path path = Paths.get(OxygenCommon.getConfigFolder() + "/data/client/worlds/" + worldId
                + "/presets/" + presetName + "/version.txt");
        if (Files.exists(path)) {
            try {
                List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
                String versionSrt = lines.get(0).trim();
                version = Long.parseLong(versionSrt);
                OxygenMain.logInfo(1, "[Core] Loaded preset <{}> version: {}", presetName, version);
            } catch (Exception exception) {
                OxygenMain.logError(1, "[Core] Failed to load version preset <{}>", presetName);
            }
        }
        return version;
    }

    public static void saveVersion(String presetName, long version) {
        Path path = Paths.get(OxygenCommon.getConfigFolder() + "/data/client/worlds/" + OxygenClient.getWorldId()
                + "/presets/" + presetName + "/version.txt");
        try {
            Files.createDirectories(path.getParent());
            Files.write(path, Collections.singletonList(String.valueOf(version)), StandardCharsets.UTF_8);
            OxygenMain.logInfo(1, "[Core] Created preset <{}> version file", presetName, version);
        } catch (IOException exception) {
            OxygenMain.logError(1, "[Core] Failed to create <{}> preset version file", presetName);
        }
    }
}
