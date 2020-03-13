package austeretony.oxygen_core.common.item;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.util.ByteBufUtils;
import austeretony.oxygen_core.common.util.StreamUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;

public class ItemStackWrapper {

    private final String registryName, stackNBTStr, capNBTStr;

    private final int itemId, damage;

    //cache

    protected ItemStack cachedStack;

    private ItemStackWrapper(int itemId, String registryName, int damage, String stackNBTStr, String capNBTStr) {
        this.itemId = itemId;
        this.registryName = registryName;
        this.damage = damage;   
        this.stackNBTStr = stackNBTStr;   
        this.capNBTStr = capNBTStr;
    }

    @Nonnull
    public String getRegistryName() {
        return this.registryName;
    }

    public int getItemId() {
        return this.itemId;
    }

    public int getItemDamage() {
        return this.damage;
    }

    @Nonnull
    public String getStackNBT() {
        return this.stackNBTStr;
    }

    @Nonnull
    public String getCapabilityNBT() {
        return this.capNBTStr;
    }

    public static ItemStackWrapper of(ItemStack itemStack) {
        NBTTagCompound serialized = itemStack.serializeNBT();
        String 
        stackNBTStr = itemStack.hasTagCompound() ? itemStack.getTagCompound().toString() : "",
                capNBTStr = serialized.hasKey("ForgeCaps") ? serialized.getCompoundTag("ForgeCaps").toString() : "";
                return new ItemStackWrapper(
                        Item.getIdFromItem(itemStack.getItem()), 
                        itemStack.getItem().getRegistryName().toString(), 
                        itemStack.getItemDamage(), 
                        stackNBTStr, 
                        capNBTStr);
    }

    @Nonnull
    public ItemStack getItemStack() {
        NBTTagCompound 
        stackTagCompound = null,
        capTagCompound = null;
        Item item = Item.getItemById(this.itemId);
        if (!this.stackNBTStr.isEmpty()) {
            try {
                stackTagCompound = JsonToNBT.getTagFromJson(this.stackNBTStr);
            } catch (NBTException exception) {
                OxygenMain.LOGGER.error("[Core] ItemStack NBT parsing failure! Item: {}", item.getRegistryName());
                exception.printStackTrace();    
            }
        }
        if (!this.capNBTStr.isEmpty()) {
            try {
                capTagCompound = JsonToNBT.getTagFromJson(this.capNBTStr);
            } catch (NBTException exception) {
                OxygenMain.LOGGER.error("[Core] ItemStack Forge Capabilities NBT parsing failure! Item: {}", item.getRegistryName());
                exception.printStackTrace();    
            }
        }
        ItemStack itemStack = new ItemStack(item, 1, this.damage, capTagCompound);
        itemStack.setTagCompound(stackTagCompound);
        return itemStack;
    }

    @Nonnull
    public ItemStack getCachedItemStack() {
        if (this.cachedStack == null)
            this.cachedStack = this.getItemStack();
        return this.cachedStack;
    }

    public boolean isEquals(ItemStackWrapper other) {
        return this.itemId == other.itemId 
                && this.damage == other.damage
                && ((this.stackNBTStr.isEmpty() && other.stackNBTStr.isEmpty()) || this.stackNBTStr.equals(other.stackNBTStr))
                && ((this.capNBTStr.isEmpty() && other.capNBTStr.isEmpty()) || this.capNBTStr.equals(other.capNBTStr));     
    }

    public boolean isEquals(ItemStack itemStack) {
        if (itemStack.isEmpty())
            return false;
        NBTTagCompound serialized = itemStack.serializeNBT();
        String 
        stackNBT = itemStack.hasTagCompound() ? itemStack.getTagCompound().toString() : "",
                capNBT = serialized.hasKey("ForgeCaps") ? serialized.getCompoundTag("ForgeCaps").toString() : "";
                return Item.getIdFromItem(itemStack.getItem()) == this.itemId
                        && itemStack.getItemDamage() == this.damage
                        && ((this.stackNBTStr.isEmpty() && stackNBT.isEmpty()) || this.stackNBTStr.equals(stackNBT))
                        && ((this.capNBTStr.isEmpty() && capNBT.isEmpty()) || this.capNBTStr.equals(capNBT));     
    }

    public void write(BufferedOutputStream bos) throws IOException {
        StreamUtils.write(this.registryName, bos);
        StreamUtils.write((short) this.damage, bos);
        StreamUtils.write(this.stackNBTStr, bos);
        StreamUtils.write(this.capNBTStr, bos);
    }

    public static ItemStackWrapper read(BufferedInputStream bis) throws IOException {
        String registryName = StreamUtils.readString(bis);
        Item item = Item.getByNameOrId(registryName);
        if (item == null)
            item = Items.AIR;
        return new ItemStackWrapper(
                Item.getIdFromItem(item), 
                registryName,
                StreamUtils.readShort(bis), 
                StreamUtils.readString(bis), 
                StreamUtils.readString(bis));
    }

    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("registry_name", new JsonPrimitive(this.registryName));
        jsonObject.add("damage", new JsonPrimitive(this.damage));
        jsonObject.add("itemstack_nbt", new JsonPrimitive(this.stackNBTStr));
        jsonObject.add("capabilities_nbt", new JsonPrimitive(this.capNBTStr));
        return jsonObject;
    }

    public static ItemStackWrapper fromJson(JsonObject jsonObject) {
        String registryName = jsonObject.get("registry_name").getAsString();
        Item item = Item.getByNameOrId(registryName);
        if (item == null)
            item = Items.AIR;
        return new ItemStackWrapper(
                Item.getIdFromItem(item), 
                registryName,
                jsonObject.get("damage").getAsInt(), 
                jsonObject.get("itemstack_nbt").getAsString(), 
                jsonObject.get("capabilities_nbt").getAsString());
    }

    public void write(ByteBuf buffer) {
        buffer.writeShort(this.itemId);
        buffer.writeShort(this.damage);
        ByteBufUtils.writeString(this.stackNBTStr, buffer);
        ByteBufUtils.writeString(this.capNBTStr, buffer);
    }

    public static ItemStackWrapper read(ByteBuf buffer) {
        int itemId = buffer.readShort();
        return new ItemStackWrapper(
                itemId, 
                Item.getItemById(itemId).getRegistryName().toString(),
                buffer.readShort(), 
                ByteBufUtils.readString(buffer), 
                ByteBufUtils.readString(buffer));
    }

    public ItemStackWrapper copy() {
        return new ItemStackWrapper(this.itemId, this.registryName, this.damage, this.stackNBTStr, this.capNBTStr);
    }

    @Override
    public String toString() {
        return String.format("[registry name: %s, damage: %d, stack NBT: %s, capability NBT: %s]",
                this.registryName,
                this.damage,
                this.stackNBTStr,
                this.capNBTStr);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.itemId;
        result = prime * result + this.damage;
        result = prime * result + this.stackNBTStr.hashCode();
        result = prime * result + this.capNBTStr.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object == null)
            return false;
        if (!(object instanceof ItemStackWrapper))
            return false;
        ItemStackWrapper other = (ItemStackWrapper) object;
        if (this.itemId == other.itemId 
                && this.damage == other.damage
                && ((this.stackNBTStr.isEmpty() && other.stackNBTStr.isEmpty()) || this.stackNBTStr.equals(other.stackNBTStr))
                && ((this.capNBTStr.isEmpty() && other.capNBTStr.isEmpty()) || this.capNBTStr.equals(other.capNBTStr)))
            return true;
        return false;
    }
}
