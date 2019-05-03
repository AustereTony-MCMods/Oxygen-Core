package austeretony.oxygen.client.event;

import net.minecraftforge.fml.common.eventhandler.Event;

public class OxygenChatMessageEvent extends Event {

    public final int modIndex, messageIndex;

    public final String[] args;

    public OxygenChatMessageEvent(int modIndex, int messageIndex, String... args) {
        this.modIndex = modIndex;
        this.messageIndex = messageIndex;
        this.args = args;
    }
}
