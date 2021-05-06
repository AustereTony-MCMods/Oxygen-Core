package austeretony.oxygen_core.common.util.nbt;

import net.minecraft.nbt.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public class NBTUtils {

    public static NBTBase toNBTBoolean(boolean flag) {
        return new NBTTagByte((byte) (flag ? 1 : 0));
    }

    public static NBTBase toNBTByte(int value) {
        return new NBTTagByte((byte) value);
    }

    public static NBTBase toNBTShort(int value) {
        return new NBTTagShort((short) value);
    }

    public static NBTBase toNBTInteger(int value) {
        return new NBTTagInt(value);
    }

    public static NBTBase toNBTLong(long value) {
        return new NBTTagLong(value);
    }

    public static NBTBase toNBTFloat(float value) {
        return new NBTTagFloat(value);
    }

    public static NBTBase toNBTDouble(double value) {
        return new NBTTagDouble(value);
    }

    public static NBTBase toNBTString(String value) {
        return new NBTTagString(value);
    }

    public static NBTBase toNBTUUID(UUID value) {
        NBTTagList tagList = new NBTTagList();
        tagList.appendTag(toNBTLong(value.getMostSignificantBits()));
        tagList.appendTag(toNBTLong(value.getLeastSignificantBits()));
        return tagList;
    }

    public static boolean fromNBTBoolean(NBTBase nbtBase) {
        return ((NBTTagByte) nbtBase).getByte() == 1;
    }

    public static int fromNBTByte(NBTBase nbtBase) {
        return ((NBTTagByte) nbtBase).getByte();
    }

    public static int fromNBTShort(NBTBase nbtBase) {
        return ((NBTTagShort) nbtBase).getShort();
    }

    public static int fromNBTInteger(NBTBase nbtBase) {
        return ((NBTTagInt) nbtBase).getInt();
    }

    public static long fromNBTLong(NBTBase nbtBase) {
        return ((NBTTagLong) nbtBase).getLong();
    }

    public static float fromNBTFloat(NBTBase nbtBase) {
        return ((NBTTagFloat) nbtBase).getFloat();
    }

    public static double fromNBTDouble(NBTBase nbtBase) {
        return ((NBTTagDouble) nbtBase).getDouble();
    }

    public static String fromNBTString(NBTBase nbtBase) {
        return ((NBTTagString) nbtBase).getString();
    }

    public static UUID fromNBTUUID(NBTBase nbtBase) {
        NBTTagList tagList = (NBTTagList) nbtBase;
        return new UUID(fromNBTLong(tagList.get(0)), fromNBTLong(tagList.get(1)));
    }

    public static <T, V> NBTBase writeMapToNBT(Map<T, V> map, Function<T, NBTBase> keyConverter, Function<V, NBTBase> valueConverter) {
        NBTTagList tagList = new NBTTagList();
        for (Map.Entry<T, V> entry : map.entrySet()) {
            tagList.appendTag(keyConverter.apply(entry.getKey()));
            tagList.appendTag(valueConverter.apply(entry.getValue()));
        }
        return tagList;
    }

    public static <T, V> Map<T, V> readMapFromNBT(NBTBase mapTag, Function<NBTBase, T> keyExtractor,
                                                  Function<NBTBase, V> valueExtractor) {
        Map<T, V> map = new HashMap<>();
        NBTTagList tagList = (NBTTagList) mapTag;
        Iterator<NBTBase> iterator = tagList.iterator();
        int amount = tagList.tagCount() / 2;
        for (int i = 0; i < amount; i++) {
            T key = keyExtractor.apply(iterator.next());
            V value = valueExtractor.apply(iterator.next());
            map.put(key, value);
        }
        return map;
    }
}
