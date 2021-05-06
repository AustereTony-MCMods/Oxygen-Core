package austeretony.oxygen_core.common.util;

import austeretony.oxygen_core.common.item.ItemStackWrapper;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Collection;
import java.util.Map;

public class CommonUtils {

    public static long createId(long current, Collection<Long> existing) {
        long newId = ++current;
        while (existing.contains(newId)) {
            newId++;
        }
        return newId;
    }

    public static String formatCurrencyValue(long value) {
        return formatCurrencyValue(String.valueOf(value));
    }

    public static String formatCurrencyValue(String input) {
        int index = 1;
        StringBuilder builder = new StringBuilder();
        char[] chars = new StringBuilder(input).reverse().toString().toCharArray();
        for (char c : chars) {
            builder.append(c);
            if (index % 3 == 0 && index != input.length()) {
                builder.append(',');
            }
            index++;
        }
        return builder.reverse().toString();
    }

    public static String formatCurrencyValue(float value, int decimalDigits) {
        return formatCurrencyValue(String.format("%." + decimalDigits + "f", value));
    }

    public static String formatDecimalCurrencyValue(String input) {
        int index = 1;
        StringBuilder builder = new StringBuilder();
        String[] array = input.split("[.]");
        char[] chars = new StringBuilder(array[0]).reverse().toString().toCharArray();
        for (char c : chars) {
            builder.append(c);
            if (index % 3 == 0 && index != array[0].length())
                builder.append(',');
            index++;
        }
        if (array.length == 2) {
            return builder.reverse().append('.').append(array[1]).toString();
        }
        return builder.reverse().toString();
    }

    public static String processCommandSelectors(EntityPlayer player, String commandIn) {
        if (commandIn.contains("@p"))
            commandIn = commandIn.replace("@p", MinecraftCommon.getEntityName(player));

        if (commandIn.contains("@pX"))
            commandIn = commandIn.replace("@pX", String.valueOf((int) player.posX));
        if (commandIn.contains("@pY"))
            commandIn = commandIn.replace("@pY", String.valueOf((int) player.posY));
        if (commandIn.contains("@pZ"))
            commandIn = commandIn.replace("@pZ", String.valueOf((int) player.posZ));
        if (commandIn.contains("@dim"))
            commandIn = commandIn.replace("@dim", String.valueOf(player.dimension));
        return commandIn;
    }

    public static String formatForLogging(Map<ItemStackWrapper, Integer> itemsMap) {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (Map.Entry<ItemStackWrapper, Integer> entry : itemsMap.entrySet()) {
            if (builder.length() > 1) {
                builder.append(", ");
            }

            ItemStackWrapper stackWrapper = entry.getKey();
            builder.append("{");
            builder.append(stackWrapper.getRegistryName()).append(", ");
            builder.append(stackWrapper.getItemDamage()).append(", ");
            builder.append(stackWrapper.getStackNBT()).append(", ");
            builder.append(stackWrapper.getCapabilityNBT());
            builder.append("} - ");
            builder.append(entry.getValue());
        }
        builder.append("]");
        return builder.toString();
    }
}
