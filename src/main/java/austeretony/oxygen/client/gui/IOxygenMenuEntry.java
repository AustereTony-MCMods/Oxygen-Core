package austeretony.oxygen.client.gui;

import net.minecraft.util.ResourceLocation;

public interface IOxygenMenuEntry {

    String getName();

    ResourceLocation getIcon();

    boolean isValid();

    void open();
}