package austeretony.oxygen_core.server.preset;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import austeretony.oxygen_core.common.api.CommonReference;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.client.CPSyncPreset;
import austeretony.oxygen_core.common.network.client.CPSyncPresetsVersions;
import austeretony.oxygen_core.server.api.OxygenHelperServer;
import net.minecraft.entity.player.EntityPlayerMP;

public class PresetsManagerServer {

    private final List<PresetServer> presets = new ArrayList<>(5);

    private long[] data;

    public List<PresetServer> getPresets() {
        return this.presets;
    }

    public void registerPreset(PresetServer preset) {
        this.presets.add(preset);
    }

    public void loadPresets() {
        OxygenHelperServer.addIOTask(()->{
            OxygenMain.LOGGER.info("Presets loading started...");
            String folder = CommonReference.getGameFolder() + "/config/oxygen/data/server/";
            for (PresetServer preset : this.presets)
                if (preset.load(folder + "presets/" + preset.getDirectory() + "/"))
                    OxygenMain.LOGGER.info("Preset <{}> loaded successfully.", preset.getName());
                else
                    OxygenMain.LOGGER.error("Failed to load preset <{}>.", preset.getName());
        });          
    }

    public void init() {
        this.data = new long[this.presets.size() + 1];
        this.data[0] = OxygenHelperServer.getWorldId();
        int index = 1;
        for (PresetServer preset : this.presets)
            this.data[index++] = preset.getVersionId();
    }

    public void syncVersions(EntityPlayerMP playerMP) {
        OxygenMain.network().sendTo(new CPSyncPresetsVersions(this.data), playerMP);
    }

    public void syncPreset(EntityPlayerMP playerMP, int presetId) {
        UUID playerUUID = CommonReference.getPersistentUUID(playerMP);
        for (PresetServer preset : this.presets)
            if (preset.getId() == presetId)
                OxygenMain.network().sendTo(new CPSyncPreset(OxygenHelperServer.getWorldId(), preset), playerMP);
    }
}
