package austeretony.oxygen_core.common.api;

import austeretony.oxygen_core.common.config.Config;
import austeretony.oxygen_core.common.config.ConfigManager;
import austeretony.oxygen_core.common.item.ItemStackWrapper;
import austeretony.oxygen_core.common.util.MinecraftCommon;

public final class OxygenCommon {

    public static void registerConfig(Config config) {
        ConfigManager.instance().registerConfig(config);
    }

    public static String getConfigFolder() {
        return MinecraftCommon.getGameFolder() + "/config/oxygen";
    }

    public static int getMaxItemStackSize(ItemStackWrapper stackWrapper) {
        return stackWrapper.getItemStackCached().getMaxStackSize();
    }
}
