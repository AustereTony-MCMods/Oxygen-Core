package austeretony.oxygen.client.listener;

import austeretony.oxygen.common.core.api.listeners.client.IChatMessageInfoListener;
import austeretony.oxygen.common.main.EnumChatMessages;
import austeretony.oxygen.common.main.OxygenMain;

public class OxygenListenerClient implements IChatMessageInfoListener {

    @Override
    public String getModId() {
        return OxygenMain.MODID;
    }

    @Override
    public void onChatMessage(int mod, int message, String... args) {
        if (mod == OxygenMain.OXYGEN_MOD_INDEX)
            EnumChatMessages.values()[message].show(args);
    }
}
