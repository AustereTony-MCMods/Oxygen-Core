package austeretony.oxygen_core.common.item;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;

import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;

public final class ItemStackWrapper {

    @Nonnull
    private final String registryName, stackNBTStr, capNBTStr;
    private final int itemId, damage;

    private ItemStack cachedStack;

    private ItemStackWrapper(int itemId, @Nonnull String registryName, int damage, @Nonnull String stackNBTStr,
                             @Nonnull String capNBTStr) {
        this.itemId = itemId;
        this.registryName = registryName;
        this.damage = damage;
        this.stackNBTStr = stackNBTStr;
        this.capNBTStr = capNBTStr;
    }

    @Nonnull
    public String getRegistryName() {
        return registryName;
    }

    public int getItemId() {
        return itemId;
    }

    public int getItemDamage() {
        return damage;
    }

    @Nonnull
    public String getStackNBT() {
        return stackNBTStr;
    }

    @Nonnull
    public String getCapabilityNBT() {
        return capNBTStr;
    }

    @Nonnull
    public static ItemStackWrapper of(@Nonnull ItemStack itemStack) {
        NBTTagCompound tagCompound = itemStack.serializeNBT();
        String stackNBTStr = itemStack.hasTagCompound() ? itemStack.getTagCompound().toString() : "";
        String capNBTStr = tagCompound.hasKey("ForgeCaps") ? tagCompound.getCompoundTag("ForgeCaps").toString() : "";

        return new ItemStackWrapper(
                Item.getIdFromItem(itemStack.getItem()),
                itemStack.getItem().getRegistryName().toString(),
                itemStack.getItemDamage(),
                stackNBTStr,
                capNBTStr);
    }

    @Nonnull
    public ItemStack createItemStack() {
        NBTTagCompound stackTagCompound = null;
        NBTTagCompound capTagCompound = null;
        Item item = Item.getItemById(itemId);

        if (!stackNBTStr.isEmpty()) {
            try {
                stackTagCompound = JsonToNBT.getTagFromJson(stackNBTStr);
            } catch (NBTException exception) {
                OxygenMain.logError(1, "[Core] ItemStack NBT parsing failure! Item: {}", item.getRegistryName());
                exception.printStackTrace();
            }
        }
        if (!capNBTStr.isEmpty()) {
            try {
                capTagCompound = JsonToNBT.getTagFromJson(capNBTStr);
            } catch (NBTException exception) {
                OxygenMain.logError(1, "[Core] ItemStack Forge Capabilities NBT parsing failure! Item: {}",
                        item.getRegistryName());
                exception.printStackTrace();
            }
        }
        ItemStack itemStack = new ItemStack(item, 1, damage, capTagCompound);
        itemStack.setTagCompound(stackTagCompound);
        return itemStack;
    }

    @Nonnull
    public ItemStack getItemStackCached() {
        if (cachedStack == null) {
            cachedStack = createItemStack();
        }
        return cachedStack;
    }

    public boolean isEquals(@Nonnull ItemStackWrapper other) {
        return itemId == other.itemId
                && damage == other.damage
                && ((stackNBTStr.isEmpty() && other.stackNBTStr.isEmpty()) || stackNBTStr.equals(other.stackNBTStr))
                && ((capNBTStr.isEmpty() && other.capNBTStr.isEmpty()) || capNBTStr.equals(other.capNBTStr));
    }

    public boolean isEquals(@Nonnull ItemStack itemStack) {
        if (itemStack.isEmpty()) return false;
        NBTTagCompound serialized = itemStack.serializeNBT();
        String stackNBT = itemStack.hasTagCompound() ? itemStack.getTagCompound().toString() : "";
        String capNBT = serialized.hasKey("ForgeCaps") ? serialized.getCompoundTag("ForgeCaps").toString() : "";

        return Item.getIdFromItem(itemStack.getItem()) == itemId
                && itemStack.getItemDamage() == damage
                && ((stackNBTStr.isEmpty() && stackNBT.isEmpty()) || stackNBTStr.equals(stackNBT))
                && ((capNBTStr.isEmpty() && capNBT.isEmpty()) || capNBTStr.equals(capNBT));
    }

    public NBTTagCompound writeToNBT() {
        NBTTagCompound tagCompound = new NBTTagCompound();
        tagCompound.setString("registry_name", registryName);
        tagCompound.setShort("damage", (short) damage);
        tagCompound.setString("stack_nbt", stackNBTStr);
        tagCompound.setString("capabilities_nbt", capNBTStr);
        return tagCompound;
    }

    @Nonnull
    public static ItemStackWrapper readFromNBT(NBTTagCompound tagCompound) {
        String registryName = tagCompound.getString("registry_name");
        Item item = Item.getByNameOrId(registryName);
        if (item == null) {
            item = Items.AIR;
        }

        return new ItemStackWrapper(Item.getIdFromItem(item), registryName, tagCompound.getShort("damage"),
                tagCompound.getString("stack_nbt"), tagCompound.getString("capabilities_nbt"));
    }

    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("registry_name", registryName);
        jsonObject.addProperty("damage", damage);
        jsonObject.addProperty("stack_nbt", stackNBTStr);
        jsonObject.addProperty("capabilities_nbt", capNBTStr);
        return jsonObject;
    }

    @Nonnull
    public static ItemStackWrapper fromJson(JsonObject jsonObject) {
        String registryName = jsonObject.get("registry_name").getAsString();
        Item item = Item.getByNameOrId(registryName);
        if (item == null) {
            item = Items.AIR;
        }

        return new ItemStackWrapper(Item.getIdFromItem(item), registryName, jsonObject.get("damage").getAsInt(),
                jsonObject.get("stack_nbt").getAsString(), jsonObject.get("capabilities_nbt").getAsString());
    }

    public void write(ByteBuf buffer) {
        buffer.writeShort(itemId);
        buffer.writeShort(damage);
        ByteBufUtils.writeString(stackNBTStr, buffer);
        ByteBufUtils.writeString(capNBTStr, buffer);
    }

    @Nonnull
    public static ItemStackWrapper read(ByteBuf buffer) {
        int itemId = buffer.readShort();
        return new ItemStackWrapper(itemId, Item.getItemById(itemId).getRegistryName().toString(), buffer.readShort(),
                ByteBufUtils.readString(buffer), ByteBufUtils.readString(buffer));
    }

    @Nonnull
    public ItemStackWrapper copy() {
        return new ItemStackWrapper(itemId, registryName, damage, stackNBTStr, capNBTStr);
    }

    @Override
    public String toString() {
        return "ItemStackWrapper[" +
                "registryName= " + registryName + ", " +
                "damage= " + damage + ", " +
                "stackNBTStr= " + stackNBTStr + ", " +
                "capNBTStr= " + capNBTStr +
                "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + itemId;
        result = prime * result + damage;
        result = prime * result + stackNBTStr.hashCode();
        result = prime * result + capNBTStr.hashCode();
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
        if (itemId == other.itemId
                && damage == other.damage
                && ((stackNBTStr.isEmpty() && other.stackNBTStr.isEmpty()) || stackNBTStr.equals(other.stackNBTStr))
                && ((capNBTStr.isEmpty() && other.capNBTStr.isEmpty()) || capNBTStr.equals(other.capNBTStr)))
            return true;
        return false;
    }
}
