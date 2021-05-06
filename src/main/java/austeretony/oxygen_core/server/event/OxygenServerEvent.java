package austeretony.oxygen_core.server.event;

import net.minecraftforge.fml.common.eventhandler.Event;

public class OxygenServerEvent extends Event {

    public static class Starting extends OxygenServerEvent {}

    public static class Stopping extends OxygenServerEvent {}
}
