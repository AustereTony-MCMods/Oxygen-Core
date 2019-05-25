package austeretony.oxygen.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import austeretony.oxygen.client.api.OxygenHelperClient;
import austeretony.oxygen.common.api.IPersistentData;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.util.StreamUtils;

public class ClientSettingsManager implements IPersistentData {

    private final OxygenManagerClient manager;

    private final Map<Integer, Integer> settings = new HashMap<Integer, Integer>(10);

    public ClientSettingsManager(OxygenManagerClient manager) {
        this.manager = manager;
    }

    public void register(int settingId) {
        this.settings.put(settingId, 0);
    }

    public void set(int settingId, int value) {
        this.settings.put(settingId, value);
    }

    public void set(int settingId, boolean value) {
        this.settings.put(settingId, value ? 1 : 0);
    }

    public int getAsInt(int settingId) {
        return this.settings.get(settingId);
    }

    public boolean getAsBoolean(int settingId) {
        return this.settings.get(settingId) == 1 ? true : false;
    }

    public void load() {
        OxygenHelperClient.loadPlayerDataDelegated(this);
    }

    public void save() {
        OxygenHelperClient.savePlayerDataDelegated(this);
    }

    @Override
    public String getName() {
        return "client settings";
    }

    @Override
    public String getModId() {
        return OxygenMain.MODID;
    }

    @Override
    public String getPath() {
        return "oxygen/client_settings.dat";
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
            this.settings.put((int) StreamUtils.readShort(bis), StreamUtils.readInt(bis));
    }
}
