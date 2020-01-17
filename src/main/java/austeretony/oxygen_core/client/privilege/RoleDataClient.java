package austeretony.oxygen_core.client.privilege;

import net.minecraft.util.text.TextFormatting;

public class RoleDataClient {

    public final String name;

    public final TextFormatting nameColor;

    public RoleDataClient(String name, TextFormatting nameColor) {
        this.name = name;
        this.nameColor = nameColor;
    }
}
