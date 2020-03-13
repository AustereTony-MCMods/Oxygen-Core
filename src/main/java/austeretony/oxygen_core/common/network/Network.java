package austeretony.oxygen_core.common.network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import austeretony.oxygen_core.client.api.OxygenHelperClient;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.server.api.OxygenHelperServer;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLEventChannel;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;

public class Network {

    public final String channelName;

    public final FMLEventChannel channel;

    private final List<Class<? extends Packet>> packets = new ArrayList<>();

    private Network(String channelName) {
        this.channelName = channelName;
        this.channel = NetworkRegistry.INSTANCE.newEventDrivenChannel(channelName);
        this.channel.register(this);
        OxygenMain.LOGGER.info("[Core] Network handler <{}> created.", this.channelName);
    }

    public static Network create(String channelName) {
        return new Network(channelName);
    }

    public void registerPacket(Class<? extends Packet> packet) {
        this.packets.add(packet);
    }

    public void sortPackets() {
        OxygenMain.LOGGER.info("[Core] Network channel <{}> holds {} packets.", this.channelName, this.packets.size());
        Collections.sort(this.packets, 
                (c1, c2)->c1.getCanonicalName().toLowerCase().compareTo(c2.getCanonicalName().toLowerCase()));
    }

    @SubscribeEvent
    public void onClientPacketRecieved(final FMLNetworkEvent.ClientCustomPacketEvent event) throws IOException {
        OxygenHelperClient.addNetworkTask(()->this.process(event));
    }

    @SubscribeEvent
    public void onServerPacketRecieved(final FMLNetworkEvent.ServerCustomPacketEvent event) throws IOException {
        OxygenHelperServer.addNetworkTask(()->this.process(event));
    }

    private FMLProxyPacket pack(Packet packet, INetHandler netHandler) {
        PacketBuffer packetBuffer = new PacketBuffer(Unpooled.buffer());
        packetBuffer.writeByte((byte) this.packets.indexOf(packet.getClass()));
        packet.write(packetBuffer, netHandler);
        return new FMLProxyPacket(packetBuffer, this.channelName);
    }

    private void process(FMLNetworkEvent.CustomPacketEvent event) {
        FMLProxyPacket proxyPacket = event.getPacket();
        if (this.channelName.equals(proxyPacket.channel()) 
                && proxyPacket.payload().readableBytes() != 0) {
            Packet packet = Packet.create(this.packets.get(proxyPacket.payload().readByte()));
            if (packet != null)
                packet.read(proxyPacket.payload(), event.getHandler());
        }
    }

    public void sendToServer(final Packet packet) {
        OxygenHelperClient.addNetworkTask(()->this.channel.sendToServer(this.pack(packet, null)));
    }

    public void sendTo(final Packet packet, final EntityPlayerMP player) {
        OxygenHelperServer.addNetworkTask(()->this.channel.sendTo(this.pack(packet, player.connection.netManager.getNetHandler()), player));
    }
}
