package austeretony.oxygen_core.common;

import java.util.ArrayList;
import java.util.Collection;

import austeretony.oxygen_core.common.config.OxygenConfig;
import austeretony.oxygen_core.common.instant.InstantData;
import austeretony.oxygen_core.common.main.OxygenMain;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class InstantDataPotionEffects implements InstantData<Collection<PotionEffect>> {

    private final Collection<PotionEffect> effects = new ArrayList<>();

    @Override
    public int getIndex() {
        return OxygenMain.ACTIVE_EFFECTS_INSTANT_DATA_INDEX;
    }

    @Override
    public boolean isValid() {
        return OxygenConfig.SYNC_ENTITIES_ACTIVE_EFFECTS.asBoolean();
    }

    @Override
    public Collection<PotionEffect> getValue() {
        return this.effects;
    }

    @Override
    public void write(EntityLivingBase entityLiving, ByteBuf buffer) {  
        Collection<PotionEffect> effects = new ArrayList<>(entityLiving.getActivePotionEffects());
        buffer.writeByte(effects.size());
        for (PotionEffect effect : effects) {
            buffer.writeByte((byte) Potion.getIdFromPotion(effect.getPotion()));
            buffer.writeInt(effect.getDuration());
            buffer.writeByte((byte) effect.getAmplifier());
            buffer.writeBoolean(effect.getIsAmbient());
        }
    }

    @Override
    public void read(ByteBuf buffer) {
        this.effects.clear();
        int amount = buffer.readByte();
        for (int i = 0; i < amount; i++)
            this.effects.add(new PotionEffect(
                    Potion.getPotionById(buffer.readByte() & 0xFF),
                    buffer.readInt(),
                    buffer.readByte(),
                    buffer.readBoolean(),
                    false));
    }

    @Override
    public InstantData<Collection<PotionEffect>> copy() {
        return new InstantDataPotionEffects();
    }
}
