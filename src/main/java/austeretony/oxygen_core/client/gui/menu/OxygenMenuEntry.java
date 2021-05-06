package austeretony.oxygen_core.client.gui.menu;

import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public interface OxygenMenuEntry {

    int getScreenId();

    String getDisplayName();

    int getPriority();

    @Nullable
    ResourceLocation getIconTexture();

    int getKeyCode();

    boolean isValid();
}
