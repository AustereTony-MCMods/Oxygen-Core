package austeretony.oxygen.common.api;

import net.minecraft.client.resources.I18n;

public enum EnumDimensions {

    OVERWORLD(0, "oxygen.dim.overworld"),
    NETHER(- 1, "oxygen.dim.nether"),
    THE_END(1, "oxygen.dim.end"),
    TWILIGHT_FOREST(7, "oxygen.dim.twilightForest");

    public final int dimension;

    public final String nameKey;

    EnumDimensions(int dimension, String nameKey) {
        this.dimension = dimension;
        this.nameKey = nameKey;
    }

    public static String getLocalizedNameFromId(int dimension) {
        String nameKey = "oxygen.dim.unknown";
        for (EnumDimensions names : values())
            if (dimension == names.dimension)
                nameKey = names.nameKey;
        return I18n.format(nameKey);
    }
}
