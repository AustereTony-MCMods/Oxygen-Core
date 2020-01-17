package austeretony.oxygen_core.common.network;

import java.io.IOException;

import com.google.common.collect.HashBiMap;

import austeretony.oxygen_core.client.api.OxygenHelperClient;
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

    private final HashBiMap<Integer, Class<? extends Packet>> packets = HashBiMap.create();

    private int id = Byte.MIN_VALUE;

    private Network(String channelName) {
        this.channelName = channelName;
        this.channel = NetworkRegistry.INSTANCE.newEventDrivenChannel(channelName);
        this.channel.register(this);
    }

    public static Network createNetworkHandler(String channelName) {
        return new Network(channelName);
    }

    public void registerPacket(Class<? extends Packet> packet) {
        this.packets.put(this.id++, packet);
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
        int id = this.packets.inverse().get(packet.getClass());
        packetBuffer.writeByte(id);
        packet.write(packetBuffer, netHandler);
        return new FMLProxyPacket(packetBuffer, this.channelName);
    }

    private void process(FMLNetworkEvent.CustomPacketEvent event) {
        FMLProxyPacket proxyPacket = event.getPacket();
        if (this.channelName.equals(proxyPacket.channel()) 
                && proxyPacket.payload().readableBytes() != 0) {
            Packet packet = Packet.create(this.packets, proxyPacket.payload().readByte());
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