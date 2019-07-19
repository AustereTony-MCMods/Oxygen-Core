package austeretony.oxygen.common.api;

import austeretony.oxygen.client.core.api.ClientReference;

public enum EnumDimension {

    OVERWORLD(0, "oxygen.dim.overworld"),
    NETHER(- 1, "oxygen.dim.nether"),
    THE_END(1, "oxygen.dim.end"),
    TWILIGHT_FOREST(7, "oxygen.dim.twilightForest");

    public final int dimension;

    public final String nameKey;

    EnumDimension(int dimension, String nameKey) {
        this.dimension = dimension;
        this.nameKey = nameKey;
    }

    public static String getLocalizedNameFromId(int dimension) {
        String nameKey = "oxygen.dim.unknown";
        for (EnumDimension names : values())
            if (dimension == names.dimension)
                nameKey = names.nameKey;
        return ClientReference.localize(nameKey);
    }
}
