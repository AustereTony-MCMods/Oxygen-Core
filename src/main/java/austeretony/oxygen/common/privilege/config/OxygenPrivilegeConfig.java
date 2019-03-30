package austeretony.oxygen.common.privilege.config;

import java.util.Queue;

import austeretony.oxygen.common.api.config.AbstractConfigHolder;
import austeretony.oxygen.common.api.config.ConfigValue;
import austeretony.oxygen.common.main.OxygenMain;

public class OxygenPrivilegeConfig extends AbstractConfigHolder {

    public static final ConfigValue
    SHOW_PREFIX = new ConfigValue(ConfigValue.EnumValueType.BOOLEAN, "main", "show_prefix"),
    SHOW_SUFFIX = new ConfigValue(ConfigValue.EnumValueType.BOOLEAN, "main", "show_suffix"),
    NICKNAME_COLOR = new ConfigValue(ConfigValue.EnumValueType.BOOLEAN, "main", "enable_nickname_color"),
    PREFIX_COLOR = new ConfigValue(ConfigValue.EnumValueType.BOOLEAN, "main", "enable_prefix_color"),
    SUFFIX_COLOR = new ConfigValue(ConfigValue.EnumValueType.BOOLEAN, "main", "enable_suffix_color"),
    CHAT_COLOR = new ConfigValue(ConfigValue.EnumValueType.BOOLEAN, "main", "enable_chat_color");

    @Override
    public String getModId() {
        return OxygenMain.MODID + ":privilege";
    }

    @Override
    public void getValues(Queue<ConfigValue> values) {
        values.add(SHOW_PREFIX);    
        values.add(SHOW_SUFFIX);        
        values.add(NICKNAME_COLOR);        
        values.add(PREFIX_COLOR);        
        values.add(SUFFIX_COLOR);        
        values.add(CHAT_COLOR);        
    }

    @Override
    public boolean sync() {
        return false;
    }
}
