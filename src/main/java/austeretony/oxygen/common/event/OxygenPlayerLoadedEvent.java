package austeretony.oxygen.common.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event;

public class OxygenPlayerLoadedEvent extends Event {

    public final EntityPlayer player;

    public OxygenPlayerLoadedEvent(EntityPlayer player) {
        this.player = player;
    }
}
