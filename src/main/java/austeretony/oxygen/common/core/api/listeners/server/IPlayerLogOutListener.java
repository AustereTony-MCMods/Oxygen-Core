package austeretony.oxygen.common.core.api.listeners.server;

import austeretony.oxygen.common.main.IOxygenListener;
import net.minecraft.entity.player.EntityPlayerMP;

public interface IPlayerLogOutListener extends IOxygenListener {

    void onPlayerLogOut(EntityPlayerMP playerMP);
}
