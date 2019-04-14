package austeretony.oxygen.common.core.api.listeners.server;

import net.minecraft.entity.player.EntityPlayer;

public interface IPlayerChangedDimensionListener {

    void onPlayerChangedDimension(EntityPlayer player, int fromDim, int toDim);
}
