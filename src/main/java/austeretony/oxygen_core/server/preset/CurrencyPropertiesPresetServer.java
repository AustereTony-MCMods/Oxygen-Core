package austeretony.oxygen_core.server.preset;

import austeretony.oxygen_core.common.api.OxygenCommon;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.preset.Preset;
import austeretony.oxygen_core.common.util.ByteBufUtils;
import austeretony.oxygen_core.common.util.FileUtils;
import austeretony.oxygen_core.common.util.JsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class CurrencyPropertiesPresetServer implements Preset {

    private long version;
    private final Map<Integer, CurrencyProperties> propertiesMap = new HashMap<>(3);

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
        version = PresetsManagerServer.loadOrCreateVersion(getName());
    }

    @Override
    public void load(String worldId) {
        propertiesMap.clear();
        String pathStr = OxygenCommon.getConfigFolder() + "/data/server/presets/" + getName() + "/currency.json";
        Path path = Paths.get(pathStr);
        if (Files.exists(path)) {
            try {
                JsonArray propertiesArray = JsonUtils.getExternalJsonData(pathStr).getAsJsonArray();
                for (JsonElement propertyElement : propertiesArray) {
                    CurrencyProperties currencyProperties = CurrencyProperties.fromJson(propertyElement.getAsJsonObject());
                    propertiesMap.put(currencyProperties.index, currencyProperties);
                }
                OxygenMain.logInfo(2, "[Core] Loaded preset <{}> data", getName());
            } catch (IOException exception) {
                exception.printStackTrace();
                OxygenMain.logError(1, "[Core] Failed to load preset <{}> data", getName());
            }
        } else {
            defaultData();
            try {
                Files.createDirectories(path.getParent());

                JsonArray propertiesArray = new JsonArray();
                propertiesMap.values().stream()
                        .sorted(Comparator.comparing(e -> e.index))
                        .forEach(property -> propertiesArray.add(property.toJson()));
                JsonUtils.createExternalJsonFile(pathStr, propertiesArray);

                for (CurrencyProperties currencyProperties : propertiesMap.values()) {
                    FileUtils.saveImage(OxygenCommon.getConfigFolder()
                            + "/data/server/presets/currency_properties/currency_icon_" + currencyProperties.index + ".png",
                            currencyProperties.iconRaw);
                }

                OxygenMain.logInfo(2, "[Core] Created preset <{}> data files", getName());
            } catch (IOException exception) {
                OxygenMain.logError(1, "[Core] Failed to create <{}> preset data file", getName());
                exception.printStackTrace();
            }
        }
    }

    private void defaultData() {
        propertiesMap.put(OxygenMain.CURRENCY_COINS, new CurrencyProperties(OxygenMain.CURRENCY_COINS, "Coins",
                FileUtils.loadImageBytesFromJar("assets/oxygen_core/textures/icons/currency/coin.png"),
                8, 8, 0, 0));
        propertiesMap.put(OxygenMain.CURRENCY_SHARDS, new CurrencyProperties(OxygenMain.CURRENCY_SHARDS, "Shards",
                FileUtils.loadImageBytesFromJar("assets/oxygen_core/textures/icons/currency/shard.png"),
                8, 8, 0, 0));
        propertiesMap.put(OxygenMain.CURRENCY_VOUCHERS, new CurrencyProperties(OxygenMain.CURRENCY_VOUCHERS, "Vouchers",
                FileUtils.loadImageBytesFromJar("assets/oxygen_core/textures/icons/currency/voucher.png"),
                8, 8, 0, 0));
    }

    @Override
    public void save() {}

    @Override
    public void write(ByteBuf buffer) {
        buffer.writeLong(version);
        buffer.writeByte(propertiesMap.size());
        for (CurrencyProperties currencyProperties : propertiesMap.values()) {
            currencyProperties.write(buffer);
        }
    }

    @Override
    public void read(ByteBuf buffer) {}

    public static class CurrencyProperties {

        public final int index;
        public final String name;
        public final byte[] iconRaw;
        public final int iconWidth, iconHeight, iconXOffset, iconYOffset;

        CurrencyProperties(int index, String name, byte[] iconRaw, int iconWidth, int iconHeight, int iconXOffset,
                           int iconYOffset) {
            this.index = index;
            this.name = name;
            this.iconRaw = iconRaw;
            this.iconWidth = iconWidth;
            this.iconHeight = iconHeight;
            this.iconXOffset = iconXOffset;
            this.iconYOffset = iconYOffset;
        }

        static CurrencyProperties fromJson(JsonObject jsonObject) {
            int index = jsonObject.get("index").getAsInt();
            String iconPath = OxygenCommon.getConfigFolder() + "/data/server/presets/currency_properties/currency_icon_"
                    + index + ".png";
            return new CurrencyProperties(index, jsonObject.get("name").getAsString(), FileUtils.loadImageBytes(iconPath),
                    jsonObject.get("icon_width").getAsInt(), jsonObject.get("icon_height").getAsInt(),
                    jsonObject.get("icon_x_offset").getAsInt(), jsonObject.get("icon_y_offset").getAsInt());
        }

        JsonObject toJson() {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("index", index);
            jsonObject.addProperty("name", name);
            jsonObject.addProperty("icon_width", iconWidth);
            jsonObject.addProperty("icon_height", iconHeight);
            jsonObject.addProperty("icon_x_offset", iconXOffset);
            jsonObject.addProperty("icon_y_offset", iconYOffset);
            return jsonObject;
        }

        void write(ByteBuf buffer) {
            buffer.writeInt(iconRaw.length);
            buffer.writeBytes(iconRaw);

            buffer.writeByte(index);
            ByteBufUtils.writeString(name, buffer);
            buffer.writeByte(iconWidth);
            buffer.writeByte(iconHeight);
            buffer.writeByte(iconXOffset);
            buffer.writeByte(iconYOffset);
        }
    }
}
