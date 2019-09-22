package austeretony.oxygen_core.client.preset;

import java.util.ArrayList;
import java.util.List;

import austeretony.oxygen_core.client.api.OxygenHelperClient;
import austeretony.oxygen_core.common.api.CommonReference;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.client.CPRequestPresetSync;
import io.netty.buffer.ByteBuf;

public class PresetsManagerClient {

    private final List<PresetClient> presets = new ArrayList<>(5);

    public void registerPreset(PresetClient preset) {
        this.presets.add(preset);
    }

    public void presetsVersionsReceived(long[] data) {
        OxygenHelperClient.addIOTask(()->{
            OxygenMain.LOGGER.info("Presets loading started...");
            String folder = CommonReference.getGameFolder() + "/config/oxygen/data/client/worlds/" + data[0] + "/";
            for (PresetClient preset : this.presets) {
                if (preset.loadVersionId(folder + preset.getDomain() + "/presets/"))
                    OxygenMain.LOGGER.info("Preset <{}> version id loaded successfully.", preset.getDisplayName());
                else
                    OxygenMain.LOGGER.error("Failed to load preset <{}> version id.", preset.getDisplayName());
            }
            int index = 1;
            for (PresetClient preset : this.presets) {
                if (preset.getVersionId() != data[index++]) {
                    OxygenMain.network().sendToServer(new CPRequestPresetSync(preset.getId()));
                    OxygenMain.LOGGER.info("Preset <{}> is outdated, sync requested.", preset.getDisplayName());
                } else {
                    OxygenMain.LOGGER.info("Preset <{}> is up-to-date, loading...", preset.getDisplayName());
                    if (preset.load(folder + preset.getDomain() + "/presets/"))
                        OxygenMain.LOGGER.info("Preset <{}> loaded successfully.", preset.getDisplayName());
                    else
                        OxygenMain.LOGGER.error("Failed to load preset <{}>.", preset.getDisplayName());                        
                }
            }  
        });  
    }

    public void rawPresetReceived(ByteBuf buffer) {
        try {
            long worldId = buffer.readLong();
            int presetId = buffer.readByte();
            String folder = CommonReference.getGameFolder() + "/config/oxygen/data/client/worlds/" + worldId + "/";
            for (PresetClient preset : this.presets)
                if (preset.getId() == presetId) {
                    OxygenMain.LOGGER.info("Received raw preset <{}> data, processing...", preset.getDisplayName());
                    preset.read(buffer);
                    OxygenHelperClient.addIOTask(()->{
                        if (preset.save(folder + preset.getDomain() + "/presets/"))
                            OxygenMain.LOGGER.info("Preset <{}> saved successfully.", preset.getDisplayName());
                        else
                            OxygenMain.LOGGER.error("Failed to save preset <{}>.", preset.getDisplayName());
                        if (preset.reloadAfterSave()) {
                            OxygenMain.LOGGER.info("Reloading preset <{}>...", preset.getDisplayName());
                            if (preset.load(folder + preset.getDomain() + "/presets/"))
                                OxygenMain.LOGGER.info("Preset <{}> loaded successfully.", preset.getDisplayName());
                            else
                                OxygenMain.LOGGER.error("Failed to load preset <{}>.", preset.getDisplayName());  
                        }
                    });
                }
        } finally {
            if (buffer != null)
                buffer.release();
        }
    }
}