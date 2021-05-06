package austeretony.oxygen_core.common.notification;

import austeretony.oxygen_core.common.util.ByteBufUtils;
import austeretony.oxygen_core.common.util.nbt.NBTUtils;
import austeretony.oxygen_core.server.api.OxygenServer;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

public class NotificationImpl implements Notification {

    private int id, expirationTimeMillis;
    private NotificationType type;
    private NotificationMode mode;
    private String message;
    private String[] messageArgs;
    private UUID notificationUUID;
    private @Nullable Consumer<EntityPlayer> acceptTask, rejectTask, expireTask;

    public NotificationImpl() {}

    public NotificationImpl(int id, @Nonnull NotificationType type, @Nonnull NotificationMode mode,
                            @Nonnull UUID notificationUUID, @Nonnull String message, @Nonnull String[] messageArgs,
                            int expirationTimeMillis, @Nullable Consumer<EntityPlayer> acceptTask,
                            @Nullable Consumer<EntityPlayer> rejectTask, @Nullable Consumer<EntityPlayer> expireTask) {
        this.id = id;
        this.type = type;
        this.mode = mode;
        this.message = message;
        this.messageArgs = messageArgs;
        this.expirationTimeMillis = expirationTimeMillis;
        this.notificationUUID = notificationUUID;
        this.acceptTask = acceptTask;
        this.rejectTask = rejectTask;
        this.expireTask = expireTask;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public NotificationType getType() {
        return type;
    }

    @Override
    public NotificationMode getMode() {
        return mode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String[] getArguments() {
        return messageArgs;
    }

    @Override
    public UUID getUUID() {
        return notificationUUID;
    }

    @Override
    public long getExpirationTimeMillis() {
        return expirationTimeMillis;
    }

    @Override
    public void accepted(EntityPlayer player) {
        if (acceptTask != null) {
            acceptTask.accept(player);
        }
    }

    @Override
    public void rejected(EntityPlayer player) {
        if (rejectTask != null) {
            rejectTask.accept(player);
        }
    }

    @Override
    public void expired(EntityPlayer player) {
        if (expireTask != null) {
            expireTask.accept(player);
        }
    }

    public static Builder builder(int id, String message, String... messageArgs) {
        return new NotificationImpl().new Builder(id, message, messageArgs);
    }

    public static Builder builder(int id, String message) {
        return new NotificationImpl().new Builder(id, message, new String[0]);
    }

    @Override
    public void write(ByteBuf buffer) {
        buffer.writeShort(id);
        buffer.writeByte(type.ordinal());
        buffer.writeByte(mode.ordinal());
        ByteBufUtils.writeUUID(notificationUUID, buffer);

        ByteBufUtils.writeString(message, buffer);
        buffer.writeByte(messageArgs.length);
        for (String arg : messageArgs) {
            ByteBufUtils.writeString(arg, buffer);
        }

        if (type != NotificationType.NOTIFICATION) {
            buffer.writeInt(expirationTimeMillis);
        }
    }

    public static NotificationImpl read(ByteBuf buffer) {
        NotificationImpl notification = new NotificationImpl();

        notification.id = buffer.readShort();
        notification.type = NotificationType.values()[buffer.readByte()];
        notification.mode = NotificationMode.values()[buffer.readByte()];
        notification.notificationUUID = ByteBufUtils.readUUID(buffer);

        notification.message = ByteBufUtils.readString(buffer);
        notification.messageArgs = new String[buffer.readByte()];
        for (int i = 0; i < notification.messageArgs.length; i++) {
            notification.messageArgs[i] = ByteBufUtils.readString(buffer);
        }

        if (notification.type != NotificationType.NOTIFICATION) {
            notification.expirationTimeMillis = buffer.readInt();
        }

        return notification;
    }

    public NBTTagCompound writeToNBT() {
        NBTTagCompound tagCompound = new NBTTagCompound();
        tagCompound.setShort("id", (short) id);
        tagCompound.setByte("type_ordinal", (byte) type.ordinal());
        tagCompound.setByte("mode_ordinal", (byte) mode.ordinal());
        tagCompound.setTag("notification_uuid", NBTUtils.toNBTUUID(notificationUUID));

        tagCompound.setString("message", message);
        NBTTagList argsList = new NBTTagList();
        for (String arg : messageArgs) {
            argsList.appendTag(new NBTTagString(arg));
        }
        tagCompound.setTag("message_args_list", argsList);

        tagCompound.setInteger("expiration_time_millis", expirationTimeMillis);
        return tagCompound;
    }

    public static NotificationImpl readFromNBT(NBTTagCompound tagCompound) {
        NotificationImpl notification = new NotificationImpl();

        notification.id = tagCompound.getShort("id");
        notification.type = NotificationType.values()[tagCompound.getByte("type_ordinal")];
        notification.mode = NotificationMode.values()[tagCompound.getByte("mode_ordinal")];
        notification.notificationUUID = NBTUtils.fromNBTUUID(tagCompound.getTag("notification_uuid"));

        notification.message = tagCompound.getString("message");
        NBTTagList messageArgsList = tagCompound.getTagList("message_args_list", 8);
        notification.messageArgs = new String[messageArgsList.tagCount()];
        for (int i = 0; i < messageArgsList.tagCount(); i++) {
            notification.messageArgs[i] = messageArgsList.getStringTagAt(i);
        }

        notification.expirationTimeMillis = tagCompound.getInteger("expiration_time_millis");

        return notification;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationImpl that = (NotificationImpl) o;
        return notificationUUID.equals(that.notificationUUID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(notificationUUID);
    }

    public class Builder {

        Builder(int id, String message, String[] messageArgs) {
            NotificationImpl.this.id = id;
            NotificationImpl.this.message = message;
            NotificationImpl.this.messageArgs = messageArgs;

            NotificationImpl.this.type = NotificationType.NOTIFICATION;
            NotificationImpl.this.mode = NotificationMode.STANDARD;
            NotificationImpl.this.notificationUUID = UUID.randomUUID();
        }

        public Builder withType(NotificationType type) {
            NotificationImpl.this.type = type;
            return this;
        }

        public Builder withMode(NotificationMode mode) {
            NotificationImpl.this.mode = mode;
            return this;
        }

        public Builder withExpirationTimeMillis(int millis) {
            NotificationImpl.this.expirationTimeMillis = millis;
            return this;
        }

        public Builder withUUID(UUID notificationUUID) {
            NotificationImpl.this.notificationUUID = notificationUUID;
            return this;
        }

        public Builder withAcceptTask(Consumer<EntityPlayer> consumer) {
            NotificationImpl.this.acceptTask = consumer;
            return this;
        }

        public Builder withRejectTask(Consumer<EntityPlayer> consumer) {
            NotificationImpl.this.rejectTask = consumer;
            return this;
        }

        public Builder withExpireTask(Consumer<EntityPlayer> consumer) {
            NotificationImpl.this.expireTask = consumer;
            return this;
        }

        public NotificationImpl build() {
            return NotificationImpl.this;
        }

        public boolean send(EntityPlayerMP targetMP) {
            return OxygenServer.sendNotification(targetMP, build());
        }

        public boolean send(EntityPlayerMP senderMP, EntityPlayerMP targetMP) {
            return OxygenServer.sendNotification(senderMP, targetMP, build());
        }
    }
}
