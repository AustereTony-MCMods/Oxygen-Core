package austeretony.oxygen_core.common.config;

import java.util.List;

import austeretony.oxygen_core.common.api.CommonReference;
import austeretony.oxygen_core.common.api.config.AbstractConfig;
import austeretony.oxygen_core.common.main.OxygenMain;

public class PrivilegesConfig extends AbstractConfig {

    public static final ConfigValue
    ENABLE_FORMATTED_CHAT = ConfigValueUtils.getValue("server", "enable_formatted_chat", false, true),
    ENABLE_CHAT_FORMATTING_ROLE_MANAGEMENT = ConfigValueUtils.getValue("server", "enable_chat_formatting_role_management", false, true),
    ENABLE_CUSTOM_FORMATTED_CHAT = ConfigValueUtils.getValue("server", "enable_custom_formatted_chat", false, true),
    FORMATTED_CHAT_PATTERN = ConfigValueUtils.getValue("server", "custom_formatted_chat_pattern", "@username: ", true),
    FORMATTED_CHAT_PREFIX_PATTERN = ConfigValueUtils.getValue("server", "custom_formatted_chat_prefix_pattern", "[@prefix] @username: ", true),
    DEFAULT_CHAT_COLOR = ConfigValueUtils.getValue("server", "custom_formatted_default_chat_color", 7, true);

    @Override
    public String getDomain() {
        return OxygenMain.MODID + ":privileges";
    }

    @Override
    public String getVersion() {
        return OxygenMain.VERSION_CUSTOM;
    }

    @Override
    public String getExternalPath() {
        return CommonReference.getGameFolder() + "/config/oxygen/core-privileges.json";
    }

    @Override
    public void getValues(List<ConfigValue> values) {
        values.add(ENABLE_FORMATTED_CHAT);   
        values.add(ENABLE_CHAT_FORMATTING_ROLE_MANAGEMENT);    
        values.add(ENABLE_CUSTOM_FORMATTED_CHAT);   
        values.add(FORMATTED_CHAT_PATTERN);    
        values.add(FORMATTED_CHAT_PREFIX_PATTERN); 
        values.add(DEFAULT_CHAT_COLOR);         
    }
}
