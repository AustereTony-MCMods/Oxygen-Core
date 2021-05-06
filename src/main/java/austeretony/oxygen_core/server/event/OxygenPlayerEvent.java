package austeretony.oxygen_core.server.event;

import austeretony.oxygen_core.common.player.ActivityStatus;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Event;

public class OxygenPlayerEvent extends Event {

    private final EntityPlayerMP playerMP;

    public OxygenPlayerEvent(EntityPlayerMP playerMP) {
        this.playerMP = playerMP;
    }

    public EntityPlayerMP getPlayer() {
        return playerMP;
    }

    public static class LoggedIn extends OxygenPlayerEvent {

        public LoggedIn(EntityPlayerMP playerMP) {
            super(playerMP);
        }
    }

    public static class LoggedOut extends OxygenPlayerEvent {

        public LoggedOut(EntityPlayerMP playerMP) {
            super(playerMP);
        }
    }

    public static class ChangedActivityStatus extends OxygenPlayerEvent {

        private final ActivityStatus old, current;

        public ChangedActivityStatus(EntityPlayerMP playerMP, ActivityStatus old, ActivityStatus current) {
            super(playerMP);
            this.old = old;
            this.current = current;
        }

        public ActivityStatus getOldStatus() {
            return old;
        }

        public ActivityStatus getCurrentStatus() {
            return current;
        }
    }
}
