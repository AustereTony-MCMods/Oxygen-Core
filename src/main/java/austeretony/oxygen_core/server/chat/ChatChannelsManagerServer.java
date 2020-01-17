package austeretony.oxygen_core.server.chat;

import java.util.HashSet;
import java.util.Set;

import austeretony.oxygen_core.common.api.CommonReference;
import austeretony.oxygen_core.server.OxygenManagerServer;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public class ChatChannelsManagerServer {

    private final OxygenManagerServer manager;

    private final Set<ChatChannel> channels = new HashSet<>(5);

    public ChatChannelsManagerServer(OxygenManagerServer manager) {
        this.manager = manager;
    }

    public void registerChannel(ChatChannel channel) {
        this.channels.add(channel);
    }

    public void initChannels(FMLServerStartingEvent event) {
        for (ChatChannel channel : this.channels)
            if (channel.isEnabled())
                CommonReference.registerCommand(event, channel);
    }
}
