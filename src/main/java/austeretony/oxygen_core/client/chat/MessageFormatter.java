package austeretony.oxygen_core.client.chat;

import net.minecraft.util.text.ITextComponent;

@FunctionalInterface
public interface MessageFormatter {

    ITextComponent getMessage(int messageIndex, Object... args);
}
