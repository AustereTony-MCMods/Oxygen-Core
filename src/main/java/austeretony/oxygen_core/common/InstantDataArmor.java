package austeretony.oxygen_core.common;

import austeretony.oxygen_core.common.config.OxygenConfig;
import austeretony.oxygen_core.common.instant.InstantData;
import austeretony.oxygen_core.common.main.OxygenMain;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;

public class InstantDataArmor implements InstantData<Integer> {

    private int totalArmor;

    @Override
    public int getIndex() {
        return OxygenMain.TOTAL_ARMOR_INSTANT_DATA_INDEX;
    }

    @Override
    public boolean isValid() {
        return OxygenConfig.SYNC_ENTITIES_ARMOR_VALUE.asBoolean();
    }

    @Override
    public Integer getValue() {
        return this.totalArmor;
    }

    @Override
    public void write(EntityLivingBase entityLiving, ByteBuf buffer) { 
        buffer.writeShort(entityLiving.getTotalArmorValue());
    }

    @Override
    public void read(ByteBuf buffer) { 
        this.totalArmor = buffer.readShort();
    }

    @Override
    public InstantData<Integer> copy() {
        return new InstantDataArmor();
    }
}
