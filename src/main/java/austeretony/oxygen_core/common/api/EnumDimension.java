package austeretony.oxygen_core.common.api;

import austeretony.oxygen_core.client.api.ClientReference;

public enum EnumDimension {

    OVERWORLD(0, "oxygen.dim.overworld"),
    NETHER(- 1, "oxygen.dim.nether"),
    THE_END(1, "oxygen.dim.end"),

    TWILIGHT_FOREST(7, "oxygen.dim.twilightForest"),

    BENEATH(10, "oxygen.dim.beneath"),

    HUNTING_DIMENSION(28885, "oxygen.dim.huntingDimension"),

    BETWEENLANDS(20, "oxygen.dim.betweenlands"),

    STORAGE_CELL(2, "oxygen.dim.storageCell"),

    POCKET_DIMENSION(144, "oxygen.dim.pocketDimension"),

    ABYSSAL_WASTELAND(50, "oxygen.dim.abyssalWasteland"),
    DREADLANDS(51, "oxygen.dim.dreadlands"),
    OMOTHOL(52, "oxygen.dim.omothol"),
    DARK_REALM(53, "oxygen.dim.darkRealm"),

    OVERWORLD_SPACE_STATION(- 27, "oxygen.dim.overworldSpaceStation"),
    OVERWORLD_SPACE_STATION_STATIC(- 26, "oxygen.dim.static"),
    MOON(- 28, "oxygen.dim.moon"),
    MARS(- 29, "oxygen.dim.mars"),
    ASTEROIDS(- 30, "oxygen.dim.asteroids"),
    VENUS(- 31, "oxygen.dim.venus");

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
