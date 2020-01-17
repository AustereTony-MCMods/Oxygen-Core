package austeretony.oxygen_core.common;

import austeretony.oxygen_core.common.config.OxygenConfig;
import austeretony.oxygen_core.common.instant.InstantData;
import austeretony.oxygen_core.common.main.OxygenMain;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;

public class InstantDataAbsorption implements InstantData<Float> {

    private float absorption;

    @Override
    public int getIndex() {
        return OxygenMain.ABSORPTION_INSTANT_DATA_INDEX;
    }

    @Override
    public boolean isValid() {
        return OxygenConfig.SYNC_ENTITIES_ABSORPTION_VALUE.asBoolean();
    }

    @Override
    public Float getValue() {
        return this.absorption;
    }

    @Override
    public void write(EntityLivingBase entityLiving, ByteBuf buffer) { 
        buffer.writeFloat(entityLiving.getAbsorptionAmount());
    }

    @Override
    public void read(ByteBuf buffer) { 
        this.absorption = buffer.readFloat();
    }

    @Override
    public InstantData<Float> copy() {
        return new InstantDataAbsorption();
    }
}
