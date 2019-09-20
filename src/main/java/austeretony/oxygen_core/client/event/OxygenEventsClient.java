package austeretony.oxygen_core.client.event;

import austeretony.oxygen_core.client.api.event.OxygenChatMessageEvent;
import austeretony.oxygen_core.common.main.EnumOxygenChatMessage;
import austeretony.oxygen_core.common.main.OxygenMain;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class OxygenEventsClient {

    @SubscribeEvent
    public void onChatMessage(OxygenChatMessageEvent event) {
        if (event.modIndex == OxygenMain.OXYGEN_CORE_MOD_INDEX)
            EnumOxygenChatMessage.values()[event.messageIndex].show(event.args);
    }
}
