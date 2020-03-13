package austeretony.oxygen_core.client.chat;

import java.util.HashMap;
import java.util.Map;

import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.client.api.EnumBaseClientSetting;
import austeretony.oxygen_core.common.chat.ChatMessagesHandler;
import austeretony.oxygen_core.common.main.OxygenMain;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public class ChatMessagesManagerClient {

    private final Map<Integer, ChatMessagesHandler> statusMessagesHandlers = new HashMap<>(5);

    public void registerStatusMessagesHandler(ChatMessagesHandler handler) {
        this.statusMessagesHandlers.put(handler.getModIndex(), handler);
    }

    public void showStatusMessage(int modIndex, int messageIndex, String... args) {
        if (EnumBaseClientSetting.ENABLE_STATUS_MESSAGES.get().asBoolean()) {
            ChatMessagesHandler handler = this.statusMessagesHandlers.get(modIndex);
            if (handler != null) {
                MessageFormatter formatter = handler.getMessageFormatter();
                ITextComponent message = null; 
                if (formatter == null || (message = formatter.getMessage(messageIndex, (Object[]) args)) == null) {
                    message = new TextComponentTranslation(handler.getMessage(messageIndex), (Object[]) args);
                    message.getStyle().setItalic(true);
                    message.getStyle().setColor(TextFormatting.AQUA);
                }
                ClientReference.showChatMessage(message);    
            } else
                OxygenMain.LOGGER.error("[Core] Chat messages handler for mod index {} is missing!", modIndex);
        }
    }
}
