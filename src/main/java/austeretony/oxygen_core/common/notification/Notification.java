package austeretony.oxygen_core.common.notification;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nonnull;
import java.util.UUID;

public interface Notification {

    int getId();

    @Nonnull
    NotificationType getType();

    @Nonnull
    NotificationMode getMode();

    @Nonnull
    String getMessage();

    @Nonnull
    String[] getArguments();

    @Nonnull
    UUID getUUID();

    long getExpirationTimeMillis();

    void accepted(EntityPlayer player);

    void rejected(EntityPlayer player);

    void expired(EntityPlayer player);

    void write(ByteBuf buffer);
}
