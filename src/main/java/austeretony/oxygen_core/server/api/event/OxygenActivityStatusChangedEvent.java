package austeretony.oxygen_core.server.api.event;

import austeretony.oxygen_core.server.OxygenPlayerData.EnumActivityStatus;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Event;

public class OxygenActivityStatusChangedEvent extends Event {

    public final EntityPlayerMP playerMP;

    public final EnumActivityStatus oldStatus, newStatus;

    public OxygenActivityStatusChangedEvent(EntityPlayerMP playerMP, EnumActivityStatus oldStatus, EnumActivityStatus newStatus) {
        this.playerMP = playerMP;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
    }
}
