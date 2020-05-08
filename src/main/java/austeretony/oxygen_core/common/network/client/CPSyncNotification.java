package austeretony.oxygen_core.common.network.client;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.api.OxygenHelperClient;
import austeretony.oxygen_core.client.notification.NotificationClient;
import austeretony.oxygen_core.common.network.Packet;
import austeretony.oxygen_core.common.notification.EnumNotification;
import austeretony.oxygen_core.common.notification.Notification;
import austeretony.oxygen_core.common.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.INetHandler;

public class CPSyncNotification extends Packet {

    private Notification notification;

    public CPSyncNotification() {}

    public CPSyncNotification(Notification notification) {
        this.notification = notification;
    }

    @Override
    public void write(ByteBuf buffer, INetHandler netHandler) {
        buffer.writeByte(this.notification.getArguments().length);
        for (String arg : this.notification.getArguments()) 
            ByteBufUtils.writeString(arg, buffer);
        buffer.writeByte(this.notification.getType().ordinal());
        buffer.writeShort(this.notification.getIndex());
        buffer.writeLong(this.notification.getId());
        buffer.writeShort(this.notification.getExpireTimeSeconds());
        ByteBufUtils.writeString(this.notification.getDescription(), buffer);
    }

    @Override
    public void read(ByteBuf buffer, INetHandler netHandler) {
        final String[] args = new String[buffer.readByte()];
        for (int i = 0; i < args.length; i++)
            args[i] = ByteBufUtils.readString(buffer);
        final NotificationClient notification = new NotificationClient(
                EnumNotification.values()[buffer.readByte()], 
                buffer.readShort(),
                buffer.readLong(),
                buffer.readShort(), 
                ByteBufUtils.readString(buffer),
                args);
        OxygenHelperClient.addRoutineTask(()->OxygenManagerClient.instance().getNotificationsManager().addNotification(notification));
    }
}
