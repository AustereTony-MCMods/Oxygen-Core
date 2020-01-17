package austeretony.oxygen_core.client.chat;

import net.minecraft.util.text.TextFormatting;

public interface ChatChannelProperties {

    String getName();

    TextFormatting getColor();

    boolean available();
}
