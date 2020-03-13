package austeretony.oxygen_core.common.util;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import austeretony.oxygen_core.common.main.OxygenMain;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;

public class ByteBufUtils {

    public static void writeString(String value, ByteBuf buffer) {
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        buffer.writeShort(bytes.length);
        buffer.writeBytes(bytes);
    }

    public static String readString(ByteBuf buffer) {
        byte[] bytes = new byte[buffer.readShort()];
        buffer.readBytes(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static void writeUUID(UUID uuid, ByteBuf buffer) {
        buffer.writeLong(uuid.getMostSignificantBits());
        buffer.writeLong(uuid.getLeastSignificantBits());
    }

    public static UUID readUUID(ByteBuf buffer) {
        return new UUID(buffer.readLong(), buffer.readLong());
    }

    public static void writeItemStack(ItemStack itemStack, ByteBuf buffer) {
        if (itemStack.isEmpty())
            buffer.writeShort(- 1);
        else {
            buffer.writeShort(Item.getIdFromItem(itemStack.getItem()));
            buffer.writeByte(itemStack.getCount());
            buffer.writeShort(itemStack.getItemDamage());
            NBTTagCompound tag = itemStack.getItem().getNBTShareTag(itemStack);
            writeString(tag != null ? tag.toString() : "", buffer);
        }
    }

    public static ItemStack readItemStack(ByteBuf buffer) {
        int itemId = buffer.readShort();
        if (itemId < 0)
            return ItemStack.EMPTY;
        else {
            ItemStack itemStack = new ItemStack(Item.getItemById(itemId), buffer.readByte(), buffer.readShort());
            String nbtStr = readString(buffer);
            if (!nbtStr.isEmpty()) {
                try {
                    itemStack.getItem().readNBTShareTag(itemStack, JsonToNBT.getTagFromJson(nbtStr));
                } catch (NBTException exception) {
                    OxygenMain.LOGGER.error("[Core] ItemStack {} NBT parsing failure!", itemStack.toString());
                    exception.printStackTrace();
                }
            }
            return itemStack;
        }
    }
}
