package austeretony.oxygen.common.config;

import java.util.List;

import austeretony.oxygen.common.api.config.AbstractConfigHolder;
import austeretony.oxygen.common.api.config.ConfigValue;
import austeretony.oxygen.common.core.api.CommonReference;
import austeretony.oxygen.common.main.OxygenMain;

public class OxygenConfig extends AbstractConfigHolder {

    public static final ConfigValue
    SYNC_CONFIGS = new ConfigValue(ConfigValue.EnumValueType.BOOLEAN, "main", "sync_configs"),
    SYNC_PRIVILEGES = new ConfigValue(ConfigValue.EnumValueType.BOOLEAN, "main", "sync_privileges"),
    ENABLE_PRIVILEGES = new ConfigValue(ConfigValue.EnumValueType.BOOLEAN, "main", "enable_privileges"),
    INTERACT_WITH_RMB = new ConfigValue(ConfigValue.EnumValueType.BOOLEAN, "main", "interact_with_rmb");

    @Override
    public String getModId() {
        return OxygenMain.MODID;
    }

    @Override
    public String getVersion() {
        return OxygenMain.VERSION_CUSTOM;
    }

    @Override
    public String getExternalPath() {
        return CommonReference.getGameFolder() + "/config/oxygen/oxygen.json";
    }

    @Override
    public String getInternalPath() {
        return "assets/oxygen/oxygen.json";
    }

    @Override
    public void getValues(List<ConfigValue> values) {
        values.add(SYNC_CONFIGS);
        values.add(SYNC_PRIVILEGES);
        values.add(ENABLE_PRIVILEGES);
        values.add(INTERACT_WITH_RMB);
    }

    @Override
    public boolean sync() {
        return true;
    }
}
