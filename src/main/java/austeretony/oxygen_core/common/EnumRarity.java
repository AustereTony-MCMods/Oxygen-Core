package austeretony.oxygen_core.common;

import austeretony.oxygen_core.client.api.ClientReference;

public enum EnumRarity {

    NORMAL(0xffffffff, "normal"),
    FINE(0xff2dc50e, "fine"),
    SUPERIOR(0xff3990fc, "superior"),
    EPIC(0xff9e2df4, "epic"),
    LEGENDARY(0xffeeca2a, "legendary");

    private final int colorHex;

    private final String name;

    EnumRarity(int colorHex, String name) {
        this.colorHex = colorHex;
        this.name = name;
    }

    public int getColor() {
        return this.colorHex;
    }

    public String getName() {
        return this.name;
    }

    public String localizedName() {
        return ClientReference.localize("oxygen_core.rarity." + this.name);
    }
}
