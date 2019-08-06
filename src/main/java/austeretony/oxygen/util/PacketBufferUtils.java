package austeretony.oxygen.util;

import java.util.UUID;

import austeretony.oxygen.common.main.OxygenMain;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.network.PacketBuffer;

public class PacketBufferUtils {

    public static void writeString(String value, PacketBuffer buffer) {
        buffer.writeShort(value.length());
        buffer.writeString(value);
    }

    public static void writeUUID(UUID uuid, PacketBuffer buffer) {
        buffer.writeLong(uuid.getMostSignificantBits());
        buffer.writeLong(uuid.getLeastSignificantBits());
    }

    public static String readString(PacketBuffer buffer) {
        return buffer.readString(buffer.readShort());
    }

    public static UUID readUUID(PacketBuffer buffer) {
        return new UUID(buffer.readLong(), buffer.readLong());
    }

    public static void writeItemStack(ItemStack itemStack, PacketBuffer buffer) {
        buffer.writeInt(Item.getIdFromItem(itemStack.getItem()));
        buffer.writeByte(itemStack.getCount());
        buffer.writeByte(itemStack.getMetadata());
        buffer.writeShort(itemStack.getItemDamage());
        PacketBufferUtils.writeString(itemStack.hasTagCompound() ? itemStack.getTagCompound().toString() : "", buffer);
    }

    public static ItemStack readItemStack(PacketBuffer buffer) {
        ItemStack itemStack = new ItemStack(Item.getItemById(buffer.readInt()), buffer.readByte(), buffer.readByte());
        itemStack.setItemDamage(buffer.readShort());
        String nbtStr = PacketBufferUtils.readString(buffer);
        if (!nbtStr.isEmpty()) {
            try {
                itemStack.setTagCompound(JsonToNBT.getTagFromJson(nbtStr));
            } catch (NBTException exception) {
                OxygenMain.OXYGEN_LOGGER.error("ItemStack {} NBT parsing failure!", itemStack.toString());
                exception.printStackTrace();
            }
        }
        return itemStack;
    }
}
