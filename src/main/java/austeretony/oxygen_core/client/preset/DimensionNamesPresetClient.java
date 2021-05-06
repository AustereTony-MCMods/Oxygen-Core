package austeretony.oxygen_core.client.preset;

import austeretony.oxygen_core.client.api.OxygenClient;
import austeretony.oxygen_core.common.api.OxygenCommon;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.preset.Preset;
import austeretony.oxygen_core.common.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class DimensionNamesPresetClient implements Preset {

    private long version;
    private final Map<Integer, String> dimensionsMap = new HashMap<>();

    @Nonnull
    public String getDimensionName(int dimensionId) {
        return dimensionsMap.getOrDefault(dimensionId, "Unknown");
    }

    @Override
    public int getId() {
        return OxygenMain.PRESET_DIMENSION_NAMES;
    }

    @Override
    public String getName() {
        return "dimension_names";
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
        dimensionsMap.clear();
        Path path = Paths.get(OxygenCommon.getConfigFolder() + "/data/client/worlds/" + worldId
                + "/presets/" + getName() + "/dimensions.txt");
        if (Files.exists(path)) {
            try {
                List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
                String regex = Pattern.quote("=");
                for (String line : lines) {
                    if (line.isEmpty() || line.startsWith("#")) continue;
                    String[] data = line.split(regex);
                    dimensionsMap.put(Integer.parseInt(data[0].trim()), data[1].trim());
                }
                OxygenMain.logInfo(2, "[Core] Loaded preset <{}> data", getName());
            } catch (Exception exception) {
                OxygenMain.logError(1, "[Core] Failed to load preset <{}> data", getName());
                exception.printStackTrace();
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
        Path path = Paths.get(OxygenCommon.getConfigFolder() + "/data/client/worlds/" + OxygenClient.getWorldId()
                + "/presets/" + getName() + "/dimensions.txt");
        try {
            Files.createDirectories(path.getParent());

            List<String> lines = new ArrayList<>(3);
            for (Map.Entry<Integer, String> entry : dimensionsMap.entrySet()) {
                lines.add(entry.getKey() + "=" + entry.getValue());
            }

            Files.write(path, lines, StandardCharsets.UTF_8);
            OxygenMain.logInfo(2, "[Core] Created preset <{}> data file", getName());
        } catch (IOException exception) {
            OxygenMain.logError(1, "[Core] Failed to create <{}> preset data file", getName());
            exception.printStackTrace();
        }
    }

    @Override
    public void write(ByteBuf buffer) {}

    @Override
    public void read(ByteBuf buffer) {
        dimensionsMap.clear();
        version = buffer.readLong();
        int amount = buffer.readShort();
        for (int i = 0; i < amount; i++) {
            dimensionsMap.put(buffer.readInt(), ByteBufUtils.readString(buffer));
        }
    }
}
