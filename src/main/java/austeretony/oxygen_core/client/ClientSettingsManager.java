package austeretony.oxygen_core.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import austeretony.oxygen_core.client.config.OxygenConfigClient;
import austeretony.oxygen_core.common.api.OxygenHelperCommon;
import austeretony.oxygen_core.common.persistent.AbstractPersistentData;
import austeretony.oxygen_core.common.util.StreamUtils;

public class ClientSettingsManager extends AbstractPersistentData {

    private final Map<Integer, Integer> settings = new HashMap<>(5);

    public void register(int settingId) {
        this.settings.put(settingId, 0);
    }

    public void set(int settingId, int value) {
        this.settings.put(settingId, value);
        this.setChanged(true);
    }

    public void set(int settingId, boolean value) {
        this.settings.put(settingId, value ? 1 : 0);
        this.setChanged(true);
    }

    public int getAsInt(int settingId) {
        return this.settings.get(settingId);
    }

    public boolean getAsBoolean(int settingId) {
        return this.settings.get(settingId) == 1 ? true : false;
    }

    @Override
    public String getDisplayName() {
        return "client_settings";
    }

    @Override
    public long getSaveDelayMinutes() {
        return OxygenConfigClient.CLIENT_SETTINGS_SAVE_DELAY_MINUTES.getIntValue();
    }

    @Override
    public String getPath() {
        return OxygenHelperCommon.getConfigFolder() + "data/client/client_settings.dat";
    }

    @Override
    public void write(BufferedOutputStream bos) throws IOException {
        StreamUtils.write((short) this.settings.size(), bos);
        for (Map.Entry<Integer, Integer> entry : this.settings.entrySet()) {
            StreamUtils.write(entry.getKey().shortValue(), bos);
            StreamUtils.write(entry.getValue(), bos);
        }
    }

    @Override
    public void read(BufferedInputStream bis) throws IOException {
        int amount = StreamUtils.readShort(bis);
        for (int i = 0; i < amount; i++)
            this.settings.put(StreamUtils.readShort(bis), StreamUtils.readInt(bis));
    }

    @Override
    public void reset() {}
}