package austeretony.oxygen.common.listener;

import austeretony.oxygen.common.OxygenManagerServer;
import austeretony.oxygen.common.core.api.listeners.server.IPlayerLogInListener;
import austeretony.oxygen.common.core.api.listeners.server.IPlayerLogOutListener;
import austeretony.oxygen.common.main.OxygenMain;
import net.minecraft.entity.player.EntityPlayerMP;

public class OxygenListenerServer implements IPlayerLogInListener, IPlayerLogOutListener {

    @Override
    public String getModId() {
        return OxygenMain.MODID;
    }

    @Override
    public void onPlayerLogIn(EntityPlayerMP playerMP) {        
        OxygenManagerServer.instance().onPlayerLoggedIn(playerMP);
    }

    @Override
    public void onPlayerLogOut(EntityPlayerMP playerMP) {
        OxygenManagerServer.instance().onPlayerLoggedOut(playerMP);
    }
}
