package austeretony.oxygen_core.server.config;

import java.util.List;

import austeretony.oxygen_core.common.EnumValueType;
import austeretony.oxygen_core.common.api.CommonReference;
import austeretony.oxygen_core.common.api.config.AbstractConfigHolder;
import austeretony.oxygen_core.common.api.config.ConfigValueImpl;
import austeretony.oxygen_core.common.config.ConfigValue;
import austeretony.oxygen_core.common.main.OxygenMain;

public class PrivilegesConfig extends AbstractConfigHolder {

    public static final ConfigValue
    ENABLE_FORMATTED_CHAT = new ConfigValueImpl(EnumValueType.BOOLEAN, "main", "enable_formatted_chat"),
    SHOW_PREFIX = new ConfigValueImpl(EnumValueType.BOOLEAN, "main", "show_prefix"),
    SHOW_SUFFIX = new ConfigValueImpl(EnumValueType.BOOLEAN, "main", "show_suffix"),
    ENABLE_NICKNAME_COLOR = new ConfigValueImpl(EnumValueType.BOOLEAN, "main", "enable_nickname_color"),
    ENABLE_PREFIX_COLOR = new ConfigValueImpl(EnumValueType.BOOLEAN, "main", "enable_prefix_color"),
    ENABLE_SUFFIX_COLOR = new ConfigValueImpl(EnumValueType.BOOLEAN, "main", "enable_suffix_color"),
    ENABLE_CHAT_COLOR = new ConfigValueImpl(EnumValueType.BOOLEAN, "main", "enable_chat_color");

    @Override
    public String getDomain() {
        return OxygenMain.MODID + ":privilege";
    }

    @Override
    public String getVersion() {
        return OxygenMain.VERSION_CUSTOM;
    }

    @Override
    public String getExternalPath() {
        return CommonReference.getGameFolder() + "/config/oxygen/privilege/privileges_server.json";
    }

    @Override
    public String getInternalPath() {
        return "assets/oxygen_core/privileges_server.json";
    }

    @Override
    public void getValues(List<ConfigValue> values) {
        values.add(ENABLE_FORMATTED_CHAT);    
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
