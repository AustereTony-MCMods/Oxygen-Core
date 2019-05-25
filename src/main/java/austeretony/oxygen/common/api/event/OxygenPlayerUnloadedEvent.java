package austeretony.oxygen.common.api.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event;

public class OxygenPlayerUnloadedEvent extends Event {

    public final EntityPlayer player;

    public OxygenPlayerUnloadedEvent(EntityPlayer player) {
        this.player = player;
    }
}
