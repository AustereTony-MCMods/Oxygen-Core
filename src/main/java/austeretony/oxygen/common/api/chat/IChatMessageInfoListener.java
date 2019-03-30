package austeretony.oxygen.common.api.chat;

import austeretony.oxygen.common.main.IOxygenListener;

public interface IChatMessageInfoListener extends IOxygenListener {

    void show(int mod, int message, String... args);
}
