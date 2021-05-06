package austeretony.oxygen_core.common.util.value.custom;

import austeretony.oxygen_core.common.util.value.TypedValue;
import austeretony.oxygen_core.common.util.value.ValueType;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;

public class PotionEffectsListValue implements TypedValue<List<PotionEffect>> {

    protected List<PotionEffect> effectsList = new ArrayList<>(1);

    public PotionEffectsListValue() {}

    public PotionEffectsListValue(List<PotionEffect> initial) {
        effectsList = initial;
    }

    @Override
    public ValueType getType() {
        return ValueType.CUSTOM;
    }

    @Override
    public List<PotionEffect> getValue() {
        return effectsList;
    }

    @Override
    public void setValue(List<PotionEffect> value) {
        this.effectsList = value;
    }

    @Override
    public void fromString(String str) {
        fromJson(new JsonParser().parse(str).getAsJsonArray());
    }

    @Override
    public String toString() {
        return toJson().toString();
    }

    @Override
    public void fromJson(JsonElement valueElement) {
        effectsList.clear();
        JsonArray effectsArray = valueElement.getAsJsonArray();
        for (JsonElement effectElement : effectsArray) {
            JsonObject effectObject = effectElement.getAsJsonObject();

            String registryName = effectObject.get("potion").getAsString();
            Potion potion = Potion.getPotionFromResourceLocation(registryName);
            if (potion == null) continue;

            PotionEffect effect = new PotionEffect(
                    potion,
                    effectObject.get("duration").getAsInt(),
                    effectObject.get("amplifier").getAsInt(),
                    effectObject.get("is_ambient").getAsBoolean(),
                    false);
            effectsList.add(effect);
        }
    }

    @Override
    public JsonElement toJson() {
        JsonArray effectsArray = new JsonArray();
        for (PotionEffect effect : effectsList) {
            JsonObject effectObject  = new JsonObject();
            if (effect.getPotion().getRegistryName() == null) continue;

            effectObject.addProperty("potion", effect.getPotion().getRegistryName().toString());
            effectObject.addProperty("duration", effect.getDuration());
            effectObject.addProperty("amplifier", effect.getAmplifier());
            effectObject.addProperty("is_ambient", effect.getIsAmbient());

            effectsArray.add(effectObject);
        }
        return effectsArray;
    }

    @Override
    public NBTBase toNBT() {
        NBTTagList tagList = new NBTTagList();
        for (PotionEffect effect : effectsList) {
            NBTTagCompound effectTag = new NBTTagCompound();
            effectTag.setByte("id", (byte) Potion.getIdFromPotion(effect.getPotion()));
            effectTag.setInteger("dur", effect.getDuration());
            effectTag.setByte("amp", (byte) effect.getAmplifier());
            effectTag.setBoolean("amb", effect.getIsAmbient());
            tagList.appendTag(effectTag);
        }
        return tagList;
    }

    @Override
    public void fromNBT(NBTBase nbtBase) {
        effectsList.clear();
        NBTTagList tagList = (NBTTagList) nbtBase;
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound effectTag = tagList.getCompoundTagAt(i);

            Potion potion = Potion.getPotionById(effectTag.getByte("id"));
            if (potion == null) continue;

            PotionEffect effect = new PotionEffect(
                    potion,
                    effectTag.getInteger("dur"),
                    effectTag.getByte("amp"),
                    effectTag.getBoolean("amb"),
                    false);
            effectsList.add(effect);
        }
    }

    @Override
    public void write(ByteBuf buffer) {
        buffer.writeByte(effectsList.size());
        for (PotionEffect effect : effectsList) {
            buffer.writeByte(Potion.getIdFromPotion(effect.getPotion()));
            buffer.writeInt(effect.getDuration());
            buffer.writeByte(effect.getAmplifier());
            buffer.writeBoolean(effect.getIsAmbient());
        }
    }

    @Override
    public void read(ByteBuf buffer) {
        effectsList.clear();
        int amount = buffer.readByte();
        for (int i = 0; i < amount; i++) {
            effectsList.add(new PotionEffect(
                    Potion.getPotionById(buffer.readByte() & 0xFF),
                    buffer.readInt(),
                    buffer.readByte(),
                    buffer.readBoolean(),
                    false));
        }
    }

    @Override
    public TypedValue<List<PotionEffect>> copy() {
        return new PotionEffectsListValue(effectsList);
    }
}
