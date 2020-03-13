package austeretony.oxygen_core.common.chat;

import javax.annotation.Nullable;

import austeretony.oxygen_core.client.chat.MessageFormatter;

public interface ChatMessagesHandler {

    int getModIndex();

    String getMessage(int messageIndex);

    @Nullable
    default MessageFormatter getMessageFormatter() {
        return null;
    }
}
