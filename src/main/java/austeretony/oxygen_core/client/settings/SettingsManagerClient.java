package austeretony.oxygen_core.client.settings;

import austeretony.oxygen_core.client.gui.base.common.WidgetGroup;
import austeretony.oxygen_core.client.util.MinecraftClient;
import austeretony.oxygen_core.common.api.OxygenCommon;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.util.JsonUtils;
import austeretony.oxygen_core.common.util.value.ValueType;
import com.google.gson.JsonObject;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public class SettingsManagerClient {

    private final Map<String, SettingValue> settings = new LinkedHashMap<>();
    private boolean settingsUpdated;

    public Map<String, SettingValue> getSettingsMap() {
        return Collections.unmodifiableMap(settings);
    }

    public <T> SettingValue registerSetting(String domain, SettingType type, String moduleName, String category,
                                            ValueType valueType, String id, T defaultValue,
                                            Function<SettingValue, WidgetGroup> widgetSupplier) {
        SettingValue settingValue = new SettingValueImpl(domain, type, moduleName, category, valueType, id, defaultValue,
                widgetSupplier);
        settings.put(id, settingValue);
        return settingValue;
    }

    @Nullable
    public SettingValue getSetting(String id) {
        return settings.get(id);
    }

    public void loadSettings() {
        String pathStr = OxygenCommon.getConfigFolder() + "/data/client/settings/settings.json";
        Path path = Paths.get(pathStr);
        JsonObject settingsObject;
        if (Files.exists(path)) {
            try {
                settingsObject = JsonUtils.getExternalJsonData(pathStr).getAsJsonObject();
                for (SettingValue setting : settings.values())
                    setting.fromJson(settingsObject);
                JsonUtils.createExternalJsonFile(pathStr, settingsObject);
                OxygenMain.logInfo(1, "[Core] Client setting loaded.");
            } catch (IOException exception) {
                OxygenMain.logError(1, "[Core] Client settings file damaged!", exception);
            }
        } else {
            try {
                Files.createDirectories(path.getParent());
                settingsObject = new JsonObject();
                for (SettingValue setting : settings.values())
                    setting.fromJson(settingsObject);
                JsonUtils.createExternalJsonFile(pathStr, settingsObject);
                OxygenMain.logInfo(1, "[Core] Client setting file created.");
            } catch (IOException exception) {
                OxygenMain.logError(1, "[Core] Failed to create client settings file!", exception);
            }
        }
    }

    public void saveSettings() {
        synchronized (settings) {
            if (!settingsUpdated) return;
            String pathStr = OxygenCommon.getConfigFolder() + "/data/client/settings/settings.json";
            Path path = Paths.get(pathStr);
            try {
                Files.createDirectories(path.getParent());
                JsonObject settingsObject = new JsonObject();
                for (SettingValue setting : settings.values())
                    setting.fromJson(settingsObject);
                JsonUtils.createExternalJsonFile(pathStr, settingsObject);
            } catch (IOException exception) {
                OxygenMain.logError(1, "[Core] Failed to save client settings!", exception);
            }
            settingsUpdated = false;
        }
    }

    public void markSettingsUpdated() {
        synchronized (settings) {
            settingsUpdated = true;
        }
    }

    public static String getCategoryDisplayName(SettingValue settingValue) {
        return MinecraftClient.localize("oxygen.gui.setting.category." + settingValue.getCategory());
    }

    public static String getDisplayName(SettingValue settingValue) {
        return MinecraftClient.localize("oxygen.gui.setting." + settingValue.getId());
    }
}
