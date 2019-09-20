package austeretony.oxygen_core.server.api.event;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Event;

public class OxygenPlayerUnloadedEvent extends Event {

    public final EntityPlayerMP playerMP;

    public OxygenPlayerUnloadedEvent(EntityPlayerMP playerMP) {
        this.playerMP = playerMP;
    }
}
