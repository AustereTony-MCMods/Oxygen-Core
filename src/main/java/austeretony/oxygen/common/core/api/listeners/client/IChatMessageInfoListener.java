package austeretony.oxygen.common.core.api.listeners.client;

import austeretony.oxygen.common.main.IOxygenListener;

public interface IChatMessageInfoListener extends IOxygenListener {

    void onChatMessage(int mod, int message, String... args);
}
