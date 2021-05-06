package austeretony.oxygen_core.common.network.operation;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nullable;

public interface NetworkOperationsHandler {

    int getId();

    void process(EntityPlayer player, int operationIndex, ByteBuf buffer);

    default @Nullable <T extends Enum> T getEnum(T[] values, int ordinal) {
        if (ordinal >= 0 && ordinal < values.length) {
            return values[ordinal];
        }
        return null;
    }
}
