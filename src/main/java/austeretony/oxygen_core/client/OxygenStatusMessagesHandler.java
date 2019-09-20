package austeretony.oxygen_core.client;

import austeretony.oxygen_core.common.main.EnumOxygenStatusMessage;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.status.ChatMessagesHandler;

public class OxygenStatusMessagesHandler implements ChatMessagesHandler {

    @Override
    public int getModIndex() {
        return OxygenMain.OXYGEN_CORE_MOD_INDEX;
    }

    @Override
    public String getMessage(int messageIndex) {
        return EnumOxygenStatusMessage.values()[messageIndex].localizedName();
    }
}
