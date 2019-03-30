package austeretony.oxygen.common.core;

import java.util.List;
import java.util.Map;

import austeretony.oxygen.common.config.OxygenConfig;
import austeretony.oxygen.common.io.OxygenIOClient;

public class OxygenHooks {

    //Hook to <net.minecraft.client.resources.Locale> class to <loadLocaleDataFiles()> method.
    public static void loadCustomLocalization(List<String> languageList, Map<String, String> properties) {
        if (OxygenConfig.CUSTOM_LOCALIZATION.getBooleanValue())
            OxygenIOClient.loadCustomLocalization(languageList, properties);
    }
}
