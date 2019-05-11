package austeretony.oxygen.client.event;

import austeretony.oxygen.client.OxygenManagerClient;
import austeretony.oxygen.common.main.EnumOxygenChatMessages;
import austeretony.oxygen.common.main.OxygenMain;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class OxygenEventsClient {

    @SubscribeEvent
    public void onChatMessage(OxygenChatMessageEvent event) {
        if (event.modIndex == OxygenMain.OXYGEN_MOD_INDEX)
            EnumOxygenChatMessages.values()[event.messageIndex].show(event.args);
    }

    @SubscribeEvent
    public void onClientTick(ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START && OxygenManagerClient.instance().getNotificationsManager() != null)
            OxygenManagerClient.instance().getNotificationsManager().processNotifications();
    }
}
