package austeretony.oxygen.common.api.network;

import java.io.IOException;

import com.google.common.collect.HashBiMap;

import austeretony.oxygen.common.api.IOxygenTask;
import austeretony.oxygen.common.delegate.OxygenThread;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.network.ProxyPacket;
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

public class OxygenNetwork {

    public final String channelName;

    public final FMLEventChannel channel;

    private final OxygenThread networkThread;

    private final HashBiMap<Integer, Class<? extends ProxyPacket>> packets = HashBiMap.<Integer, Class<? extends ProxyPacket>>create();

    private int id;

    private OxygenNetwork(String channelName) {
        this.channelName = channelName;
        this.channel = NetworkRegistry.INSTANCE.newEventDrivenChannel(channelName);
        this.channel.register(this);
        this.networkThread = new OxygenThread(channelName + " network");
        this.networkThread.start();
    }

    public static OxygenNetwork createNetworkHandler(String channelName) {
        return new OxygenNetwork(channelName);
    }

    public void registerPacket(Class<? extends ProxyPacket> packet) {
        this.packets.put(this.id++, packet);
    }

    @SubscribeEvent
    public void onClientPacketRecieve(FMLNetworkEvent.ClientCustomPacketEvent event) throws IOException {
        this.networkThread.addTask(new IOxygenTask() {

            @Override
            public void execute() {
                try {
                    process(event);
                } catch (Exception exception) {
                    OxygenMain.OXYGEN_LOGGER.error("Client packet processing failed for channel <{}>.", event.getPacket().channel());
                    exception.printStackTrace();
                }
            }            
        });
    }

    @SubscribeEvent
    public void onServerPacketRecieve(FMLNetworkEvent.ServerCustomPacketEvent event) throws IOException {
        this.networkThread.addTask(new IOxygenTask() {

            @Override
            public void execute() {
                try {
                    process(event);
                } catch (Exception exception) {
                    OxygenMain.OXYGEN_LOGGER.error("Server packet processing failed for channel <{}>.", event.getPacket().channel());
                    exception.printStackTrace();
                }
            }            
        });
    }

    private FMLProxyPacket pack(ProxyPacket packet, INetHandler netHandler) {
        PacketBuffer packetBuffer = new PacketBuffer(Unpooled.buffer());
        packetBuffer.writeByte(this.packets.inverse().get(packet.getClass()));
        packet.write(packetBuffer, netHandler);
        return new FMLProxyPacket(packetBuffer, this.channelName);
    }

    private void process(FMLNetworkEvent.CustomPacketEvent event) throws IOException {
        FMLProxyPacket packet = event.getPacket();
        if (this.channelName.equals(packet.channel())) {
            if (packet.payload().readableBytes() != 0) {
                PacketBuffer buffer = new PacketBuffer(packet.payload());
                ProxyPacket.create(this.packets, buffer.readByte()).read(buffer, event.getHandler());
            }
        }
    }

    public void sendToServer(ProxyPacket packet) {
        this.networkThread.addTask(new IOxygenTask() {

            @Override
            public void execute() {
                channel.sendToServer(pack(packet, null));
            }            
        });
    }

    public void sendTo(ProxyPacket packet, EntityPlayerMP player) {
        this.networkThread.addTask(new IOxygenTask() {

            @Override
            public void execute() {
                channel.sendTo(pack(packet, player.connection.netManager.getNetHandler()), player);
            }            
        });
    }

    public void sendToAll(ProxyPacket packet) {
        this.networkThread.addTask(new IOxygenTask() {

            @Override
            public void execute() {
                channel.sendToAll(pack(packet, null));
            }            
        });
    }

    public void sendToAllAround(ProxyPacket packet, TargetPoint point) {
        this.networkThread.addTask(new IOxygenTask() {

            @Override
            public void execute() {
                channel.sendToAllAround(pack(packet, null), point);
            }            
        });
    }

    public void sendToAllTracking(ProxyPacket packet, Entity entity) {
        this.networkThread.addTask(new IOxygenTask() {

            @Override
            public void execute() {
                channel.sendToAllTracking(pack(packet, null), entity);
            }            
        });
    }

    public void sendToAllTracking(ProxyPacket packet, TargetPoint point) {
        this.networkThread.addTask(new IOxygenTask() {

            @Override
            public void execute() {
                channel.sendToAllTracking(pack(packet, null), point);
            }            
        });
    }
}
