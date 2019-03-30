package austeretony.oxygen.common.api.network;

import java.io.IOException;

import com.google.common.collect.HashBiMap;

import austeretony.oxygen.common.api.OxygenHelperClient;
import austeretony.oxygen.common.api.OxygenHelperServer;
import austeretony.oxygen.common.api.OxygenTask;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.network.ProxyPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLEventChannel;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;

public class OxygenNetworkHandler {

    public final String channelName;

    public final FMLEventChannel channel;

    private HashBiMap<Integer, Class<? extends ProxyPacket>> packets = HashBiMap.<Integer, Class<? extends ProxyPacket>>create();

    private int id;

    private OxygenNetworkHandler(String channelName) {
        this.channelName = channelName;
        this.channel = NetworkRegistry.INSTANCE.newEventDrivenChannel(channelName);
        this.channel.register(this);
    }

    public static OxygenNetworkHandler createNetworkHandler(String channelName) {
        return new OxygenNetworkHandler(channelName);
    }

    public void register(Class<? extends ProxyPacket> packet) {
        this.packets.put(this.id++, packet);
    }

    @SubscribeEvent
    public void onClientPacketRecieve(FMLNetworkEvent.ClientCustomPacketEvent event) throws IOException {
        OxygenHelperClient.addNetworkTaskClient(new OxygenTask() {

            @Override
            public void execute() {
                try {
                    process(event);
                } catch (IOException exception) {
                    OxygenMain.OXYGEN_LOGGER.error("Client packet processing failed for channel <{}>.", event.getPacket().channel());
                    exception.printStackTrace();
                }
            }            
        });
    }

    @SubscribeEvent
    public void onServerPacketRecieve(FMLNetworkEvent.ServerCustomPacketEvent event) throws IOException {
        OxygenHelperServer.addNetworkTaskServer(new OxygenTask() {

            @Override
            public void execute() {
                try {
                    process(event);
                } catch (IOException exception) {
                    OxygenMain.OXYGEN_LOGGER.error("Server packet processing failed for channel <{}>.", event.getPacket().channel());
                    exception.printStackTrace();
                }
            }            
        });
    }

    private FMLProxyPacket pack(ProxyPacket modPacket, INetHandler netHandler) {
        PacketBuffer packetBuffer = new PacketBuffer(Unpooled.buffer());
        packetBuffer.writeByte(this.packets.inverse().get(modPacket.getClass()));
        modPacket.write(packetBuffer, netHandler);
        FMLProxyPacket packet = new FMLProxyPacket(packetBuffer, this.channelName);
        return packet;
    }

    private void process(FMLNetworkEvent.CustomPacketEvent event) throws IOException {
        FMLProxyPacket proxyPacket = event.getPacket();
        if (this.channelName.equals(proxyPacket.channel())) {
            ByteBuf byteBuf = proxyPacket.payload();
            int readableBytes = byteBuf.readableBytes();
            if (readableBytes != 0) {
                PacketBuffer buffer = new PacketBuffer(byteBuf);
                int id = buffer.readByte();
                ProxyPacket.create(this.packets, id).read(buffer, event.getHandler());
            }
        }
    }

    public void sendToServer(ProxyPacket packet) {
        OxygenHelperClient.addNetworkTaskClient(new OxygenTask() {

            @Override
            public void execute() {
                channel.sendToServer(pack(packet, null));
            }            
        });
    }

    public void sendTo(ProxyPacket packet, EntityPlayerMP player) {
        OxygenHelperServer.addNetworkTaskServer(new OxygenTask() {

            @Override
            public void execute() {
                channel.sendTo(pack(packet, player.connection.netManager.getNetHandler()), player);
            }            
        });
    }

    public void sendToAll(ProxyPacket packet) {
        OxygenHelperServer.addNetworkTaskServer(new OxygenTask() {

            @Override
            public void execute() {
                channel.sendToAll(pack(packet, null));
            }            
        });
    }

    public void sendToAllAround(ProxyPacket packet, TargetPoint point) {
        OxygenHelperServer.addNetworkTaskServer(new OxygenTask() {

            @Override
            public void execute() {
                channel.sendToAllAround(pack(packet, null), point);
            }            
        });
    }

    public void sendToAllTracking(ProxyPacket packet, Entity entity) {
        OxygenHelperServer.addNetworkTaskServer(new OxygenTask() {

            @Override
            public void execute() {
                channel.sendToAllTracking(pack(packet, null), entity);
            }            
        });
    }

    public void sendToAllTracking(ProxyPacket packet, TargetPoint point) {
        OxygenHelperServer.addNetworkTaskServer(new OxygenTask() {

            @Override
            public void execute() {
                channel.sendToAllTracking(pack(packet, null), point);
            }            
        });
    }
}
