package austeretony.oxygen_core.client.gui.base.event;

import austeretony.oxygen_core.client.gui.base.core.OxygenScreen;
import net.minecraftforge.fml.common.eventhandler.Event;

public class OxygenScreenInitEvent extends Event {

    private final OxygenScreen screen;

    public OxygenScreenInitEvent(OxygenScreen screen) {
        this.screen = screen;
    }

    public OxygenScreen getScreen() {
        return screen;
    }

    public static class Pre extends OxygenScreenInitEvent {

        public Pre(OxygenScreen screen) {
            super(screen);
        }
    }

    public static class Post extends OxygenScreenInitEvent {

        public Post(OxygenScreen screen) {
            super(screen);
        }
    }
}
