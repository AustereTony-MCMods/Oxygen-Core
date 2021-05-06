package austeretony.oxygen_core.common.network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import austeretony.oxygen_core.client.util.MinecraftClient;
import austeretony.oxygen_core.common.main.OxygenMain;
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
        channel = NetworkRegistry.INSTANCE.newEventDrivenChannel(channelName);
        channel.register(this);

        OxygenMain.logInfo(1, "[Core] Network handler <{}> created.", channelName);
    }

    public static Network create(String channelName) {
        return new Network(channelName);
    }

    public void registerPacket(Class<? extends Packet> packet) {
        packets.add(packet);
    }

    @SubscribeEvent
    public void onClientPacket(final FMLNetworkEvent.ClientCustomPacketEvent event) throws IOException {
        try {
            process(event);
        } catch (Exception exception) {
            OxygenMain.logError(1, "[Core] Failed client-side packet processing.", exception);
        }
    }

    @SubscribeEvent
    public void onServerPacket(final FMLNetworkEvent.ServerCustomPacketEvent event) throws IOException {
        try {
            process(event);
        } catch (Exception exception) {
            OxygenMain.logError(1, "[Core] Failed server-side packet processing.", exception);
        }
    }

    private FMLProxyPacket pack(Packet packet, INetHandler netHandler) {
        PacketBuffer packetBuffer = new PacketBuffer(Unpooled.buffer());
        packetBuffer.writeByte((byte) packets.indexOf(packet.getClass()));
        packet.write(packetBuffer, netHandler);
        return new FMLProxyPacket(packetBuffer, channelName);
    }

    private void process(FMLNetworkEvent.CustomPacketEvent event) {
        FMLProxyPacket proxyPacket = event.getPacket();
        if (channelName.equals(proxyPacket.channel()) && proxyPacket.payload().readableBytes() != 0) {
            Packet packet = Packet.create(packets.get(proxyPacket.payload().readByte()));
            if (packet != null) {
                packet.read(proxyPacket.payload(), event.getHandler());
            }
        }
    }

    public void sendToServer(Packet packet) {
        channel.sendToServer(pack(packet, MinecraftClient.getClientPlayer().connection.getNetworkManager().getNetHandler()));
    }

    public void sendTo(Packet packet, EntityPlayerMP player) {
        channel.sendTo(pack(packet, player.connection.netManager.getNetHandler()), player);
    }
}
