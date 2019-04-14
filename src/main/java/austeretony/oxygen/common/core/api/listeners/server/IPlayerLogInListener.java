package austeretony.oxygen.common.core.api.listeners.server;

import austeretony.oxygen.common.main.IOxygenListener;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public interface IPlayerLogInListener extends IOxygenListener {

    void onPlayerLogIn(EntityPlayerMP playerMP);
}
