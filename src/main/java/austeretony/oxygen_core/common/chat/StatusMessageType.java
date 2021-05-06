package austeretony.oxygen_core.common.chat;

import net.minecraft.util.text.TextFormatting;

public enum StatusMessageType {

    COMMON(TextFormatting.AQUA),
    SPECIAL(TextFormatting.YELLOW),
    ERROR(TextFormatting.RED);

    private final TextFormatting formatting;

    StatusMessageType(TextFormatting formatting) {
        this.formatting = formatting;
    }

    public TextFormatting getFormatting() {
        return formatting;
    }
}
