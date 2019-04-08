package austeretony.oxygen.common.privilege.config;

import java.util.Queue;

import austeretony.oxygen.common.api.config.AbstractConfigHolder;
import austeretony.oxygen.common.api.config.ConfigValue;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.reference.CommonReference;

public class OxygenPrivilegeConfig extends AbstractConfigHolder {

    public static final ConfigValue
    SHOW_PREFIX = new ConfigValue(ConfigValue.EnumValueType.BOOLEAN, "main", "show_prefix"),
    SHOW_SUFFIX = new ConfigValue(ConfigValue.EnumValueType.BOOLEAN, "main", "show_suffix"),
    ENABLE_NICKNAME_COLOR = new ConfigValue(ConfigValue.EnumValueType.BOOLEAN, "main", "enable_nickname_color"),
    ENABLE_PREFIX_COLOR = new ConfigValue(ConfigValue.EnumValueType.BOOLEAN, "main", "enable_prefix_color"),
    ENABLE_SUFFIX_COLOR = new ConfigValue(ConfigValue.EnumValueType.BOOLEAN, "main", "enable_suffix_color"),
    ENABLE_CHAT_COLOR = new ConfigValue(ConfigValue.EnumValueType.BOOLEAN, "main", "enable_chat_color");

    @Override
    public String getModId() {
        return OxygenMain.MODID + ":privilege";
    }

    @Override
    public String getExternalPath() {
        return CommonReference.getGameFolder() + "/config/oxygen/privilege/privilege.json";
    }

    @Override
    public String getInternalPath() {
        return "assets/oxygen/privilege.json";
    }

    @Override
    public void getValues(Queue<ConfigValue> values) {
        values.add(SHOW_PREFIX);    
        values.add(SHOW_SUFFIX);        
        values.add(ENABLE_NICKNAME_COLOR);        
        values.add(ENABLE_PREFIX_COLOR);        
        values.add(ENABLE_SUFFIX_COLOR);        
        values.add(ENABLE_CHAT_COLOR);        
    }

    @Override
    public boolean sync() {
        return false;
    }
}
