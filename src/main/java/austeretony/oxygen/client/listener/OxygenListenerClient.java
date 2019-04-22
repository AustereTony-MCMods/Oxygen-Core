package austeretony.oxygen.client.listener;

import austeretony.oxygen.client.OxygenManagerClient;
import austeretony.oxygen.common.core.api.listeners.client.IChatMessageInfoListener;
import austeretony.oxygen.common.core.api.listeners.client.IClientTickListener;
import austeretony.oxygen.common.main.EnumOxygenChatMessages;
import austeretony.oxygen.common.main.OxygenMain;

public class OxygenListenerClient implements IChatMessageInfoListener, IClientTickListener {

    @Override
    public String getModId() {
        return OxygenMain.MODID;
    }

    @Override
    public void onChatMessage(int mod, int message, String... args) {
        if (mod == OxygenMain.OXYGEN_MOD_INDEX)
            EnumOxygenChatMessages.values()[message].show(args);
    }

    @Override
    public void onClientTick() {
        if (OxygenManagerClient.instance().getNotificationsManager() != null)
            OxygenManagerClient.instance().getNotificationsManager().processNotifications();
    }
}
