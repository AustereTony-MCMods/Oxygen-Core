package austeretony.oxygen_core.server.api.event;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Event;

public class OxygenPlayerLoadedEvent extends Event {

    public final EntityPlayerMP playerMP;

    public OxygenPlayerLoadedEvent(EntityPlayerMP playerMP) {
        this.playerMP = playerMP;
    }
}
