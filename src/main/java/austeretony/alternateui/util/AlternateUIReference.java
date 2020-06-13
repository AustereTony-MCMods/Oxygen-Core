package austeretony.alternateui.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;

public class AlternateUIReference {

    public static Minecraft getMinecraft() {
        return Minecraft.getMinecraft();
    }

    public static RenderItem getRenderItem() {
        return getMinecraft().getRenderItem();
    }
}
