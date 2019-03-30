package austeretony.oxygen.common.main;

import austeretony.oxygen.common.api.chat.IChatMessageInfoListener;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class OxygenChatMessagesListener implements IChatMessageInfoListener {

    @Override
    public String getModId() {
        return OxygenMain.MODID;
    }

    @Override
    public void show(int mod, int message, String... args) {
        if (mod == OxygenMain.OXYGEN_MOD_INDEX)
            EnumChatMessages.values()[message].show(args);
    }
}
