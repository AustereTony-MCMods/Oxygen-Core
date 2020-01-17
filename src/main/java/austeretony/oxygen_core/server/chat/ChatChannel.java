package austeretony.oxygen_core.server.chat;

import net.minecraft.command.ICommand;
import net.minecraft.util.text.TextFormatting;

public interface ChatChannel extends ICommand {

    TextFormatting getColor();
    
    boolean isEnabled();
}
