package austeretony.oxygen_core.client.preset;

import austeretony.oxygen_core.client.api.OxygenClient;
import austeretony.oxygen_core.common.api.OxygenCommon;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.preset.Preset;
import austeretony.oxygen_core.common.util.FileUtils;
import austeretony.oxygen_core.common.util.JsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import io.netty.buffer.ByteBuf;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class CurrencyPropertiesPresetClient implements Preset {

    private long version;
    private final Map<Integer, CurrencyProperties> propertiesMap = new HashMap<>(3);

    @Nonnull
    public Collection<CurrencyProperties> getPropertiesMap() {
        return propertiesMap.values();
    }

    @Nullable
    public CurrencyProperties getProperties(int currencyIndex) {
        return propertiesMap.get(currencyIndex);
    }

    @Override
    public int getId() {
        return OxygenMain.PRESET_CURRENCY_PROPERTIES;
    }

    @Override
    public String getName() {
        return "currency_properties";
    }

    @Override
    public long getVersion() {
        return version;
    }

    @Override
    public void loadVersion(String worldId) {
        version = PresetsManagerClient.loadVersion(getName(), worldId);
    }

    @Override
    public void load(String worldId) {
        propertiesMap.clear();
        String pathStr = OxygenCommon.getConfigFolder() + "/data/client/worlds/" + worldId + "/presets/" + getName() + "/currency.json";
        Path path = Paths.get(pathStr);
        if (Files.exists(path)) {
            try {
                JsonArray propertiesArray = JsonUtils.getExternalJsonData(pathStr).getAsJsonArray();
                for (JsonElement propertyElement : propertiesArray) {
                    CurrencyProperties properties = CurrencyProperties.fromJson(propertyElement.getAsJsonObject());
                    this.propertiesMap.put(properties.getCurrencyIndex(), properties);
                }
                OxygenMain.logInfo(2, "[Core] Loaded preset <{}> data", getName());
            } catch (IOException exception) {
                exception.printStackTrace();
                OxygenMain.logError(1, "[Core] Failed to load preset <{}> data", getName());
            }
        }
    }

    @Override
    public void save() {
        saveVersion();
        saveData();
    }

    private void saveVersion() {
        PresetsManagerClient.saveVersion(getName(), version);
    }

    private void saveData() {
        String pathStr = OxygenCommon.getConfigFolder() + "/data/client/worlds/" + OxygenClient.getWorldId()
                + "/presets/" + getName() + "/currency.json";
        Path path = Paths.get(pathStr);
        try {
            Files.createDirectories(path.getParent());

            JsonArray propertiesArray = new JsonArray();
            propertiesMap.values()
                    .stream()
                    .sorted(Comparator.comparing(CurrencyProperties::getCurrencyIndex))
                    .forEach(property -> propertiesArray.add(property.toJson()));
            JsonUtils.createExternalJsonFile(pathStr, propertiesArray);

            for (CurrencyProperties properties : propertiesMap.values()) {
                FileUtils.saveImage(OxygenCommon.getConfigFolder() + "/data/client/worlds/"
                                + OxygenClient.getWorldId() + "/presets/currency_properties/currency_icon_"
                                + properties.getCurrencyIndex() + ".png",
                        properties.getIconRaw());
            }

            OxygenMain.logInfo(2, "[Core] Created preset <{}> data files", getName());
        } catch (IOException exception) {
            OxygenMain.logError(1, "[Core] Failed to create <{}> preset data file", getName());
            exception.printStackTrace();
        }
    }

    @Override
    public void write(ByteBuf buffer) {}

    @Override
    public void read(ByteBuf buffer) {
        propertiesMap.clear();
        version = buffer.readLong();
        int amount = buffer.readByte();
        for (int i = 0; i < amount; i++) {
            CurrencyProperties properties = CurrencyProperties.read(buffer);
            this.propertiesMap.put(properties.getCurrencyIndex(), properties);
        }
    }
}
