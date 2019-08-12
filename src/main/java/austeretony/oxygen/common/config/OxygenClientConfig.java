package austeretony.oxygen.common.config;

import java.util.List;

import austeretony.oxygen.common.api.config.AbstractConfigHolder;
import austeretony.oxygen.common.api.config.ConfigValue;
import austeretony.oxygen.common.core.api.CommonReference;
import austeretony.oxygen.common.main.OxygenMain;

public class OxygenClientConfig extends AbstractConfigHolder {

    public static final ConfigValue INTERACT_WITH_RMB = new ConfigValue(ConfigValue.EnumValueType.BOOLEAN, "main", "interact_with_rmb");

    @Override
    public String getModId() {
        return OxygenMain.MODID + ":client";
    }

    @Override
    public String getVersion() {
        return OxygenMain.VERSION_CUSTOM;
    }

    @Override
    public String getExternalPath() {
        return CommonReference.getGameFolder() + "/config/oxygen/oxygen_client.json";
    }

    @Override
    public String getInternalPath() {
        return "assets/oxygen/oxygen_client.json";
    }

    @Override
    public void getValues(List<ConfigValue> values) {
        values.add(INTERACT_WITH_RMB);
    }

    @Override
    public boolean sync() {
        return false;
    }
}