package austeretony.oxygen_core.common.instant;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;

public interface InstantData<T> {

    int getIndex();

    boolean isValid();

    T getValue();

    void write(EntityLivingBase entityLiving, ByteBuf buffer);

    void read(ByteBuf buffer);

    InstantData<T> copy();
}
