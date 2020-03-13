package austeretony.oxygen_core.client.privilege;

import net.minecraft.util.text.TextFormatting;

public class RoleData {

    public final String name;

    public final TextFormatting nameColor;

    public RoleData(String name, TextFormatting nameColor) {
        this.name = name;
        this.nameColor = nameColor;
    }
}
