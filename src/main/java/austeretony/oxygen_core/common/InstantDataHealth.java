package austeretony.oxygen_core.common;

import austeretony.oxygen_core.common.config.OxygenConfig;
import austeretony.oxygen_core.common.instant.InstantData;
import austeretony.oxygen_core.common.main.OxygenMain;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;

public class InstantDataHealth implements InstantData<Float> {

    private float health;

    @Override
    public int getIndex() {
        return OxygenMain.HEALTH_INSTANT_DATA_INDEX;
    }

    @Override
    public boolean isValid() {
        return OxygenConfig.SYNC_ENTITIES_HEALTH.asBoolean();
    }

    @Override
    public Float getValue() {
        return this.health;
    }

    @Override
    public void write(EntityLivingBase entityLiving, ByteBuf buffer) { 
        buffer.writeFloat(entityLiving.getHealth());
    }

    @Override
    public void read(ByteBuf buffer) { 
        this.health = buffer.readFloat();
    }

    @Override
    public InstantData<Float> copy() {
        return new InstantDataHealth();
    }
}
