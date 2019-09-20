package austeretony.oxygen_core.client.status;

import java.util.HashMap;
import java.util.Map;

import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.common.status.ChatMessagesHandler;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class ChatMessagesManagerClient {

    private final Map<Integer, ChatMessagesHandler>  statusMessagesHandlers = new HashMap<>(5);

    public void registerStatusMessagesHandler(ChatMessagesHandler handler) {
        this.statusMessagesHandlers.put(handler.getModIndex(), handler);
    }

    public void showStatusMessage(int modIndex, int messageIndex) {
        ITextComponent msg = new TextComponentString(this.statusMessagesHandlers.get(modIndex).getMessage(messageIndex));
        msg.getStyle().setItalic(true);
        msg.getStyle().setColor(TextFormatting.AQUA);
        ClientReference.showChatMessage(msg);
    }
}