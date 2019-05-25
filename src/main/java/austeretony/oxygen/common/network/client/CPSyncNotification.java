package austeretony.oxygen.common.network.client;

import austeretony.oxygen.client.OxygenManagerClient;
import austeretony.oxygen.common.network.ProxyPacket;
import austeretony.oxygen.common.notification.EnumNotifications;
import austeretony.oxygen.common.notification.INotification;
import austeretony.oxygen.common.notification.NotificationClient;
import austeretony.oxygen.common.util.PacketBufferUtils;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;

public class CPSyncNotification extends ProxyPacket {

    private INotification notification;

    public CPSyncNotification() {}

    public CPSyncNotification(INotification notification) {
        this.notification = notification;
    }

    @Override
    public void write(PacketBuffer buffer, INetHandler netHandler) {
        buffer.writeByte(this.notification.getArguments().length);
        if (this.notification.getArguments().length > 0)
            for (String arg : this.notification.getArguments()) 
                PacketBufferUtils.writeString(arg, buffer);
        buffer.writeByte(this.notification.getType().ordinal());
        buffer.writeByte(this.notification.getIndex());
        buffer.writeLong(this.notification.getId());
        buffer.writeShort(this.notification.getExpireTime());
        PacketBufferUtils.writeString(this.notification.getDescription(), buffer);
    }

    @Override
    public void read(PacketBuffer buffer, INetHandler netHandler) {
        String[] args = new String[buffer.readByte()];
        if (args.length > 0)
            for (int i = 0; i < args.length; i++)
                args[i] = PacketBufferUtils.readString(buffer);
        OxygenManagerClient.instance().getNotificationsManager().addNotification(new NotificationClient(
                EnumNotifications.values()[buffer.readByte()], 
                buffer.readByte(),
                buffer.readLong(),
                buffer.readShort(), 
                PacketBufferUtils.readString(buffer),
                args));
    }
}
