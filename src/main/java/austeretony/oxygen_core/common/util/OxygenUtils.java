package austeretony.oxygen_core.common.util;

import austeretony.oxygen_core.client.api.ClientReference;
import net.minecraft.util.text.TextFormatting;

public class OxygenUtils {

    public static String formattingCode(TextFormatting enumFormatting) {
        return enumFormatting.getFriendlyName();
    }

    public static TextFormatting formattingFromCode(String code) {
        return TextFormatting.getValueByName(code);
    }

    public static String formatCurrencyValue(String input) {
        int index = 1;
        StringBuilder builder = new StringBuilder();
        for (char c : new StringBuilder(input).reverse().toString().toCharArray()) {
            builder.append(c);
            if (index % 3 == 0 && index != input.length())
                builder.append(',');
            index++;
        }
        return builder.reverse().toString();
    }

    public static String formatDecimalCurrencyValue(String input) {
        int index = 1;
        StringBuilder builder = new StringBuilder();
        String[] array = input.split("[.]");
        for (char c : new StringBuilder(array[0]).reverse().toString().toCharArray()) {
            builder.append(c);
            if (index % 3 == 0 && index != array[0].length())
                builder.append(',');
            index++;
        }
        if (array.length == 2)
            return builder.reverse().append('.').append(array[1]).toString();
        return builder.reverse().toString();
    }

    public static String getTimePassedLocalizedString(long timeStampMillis) {
        if (timeStampMillis > 0L) {
            long 
            delta = System.currentTimeMillis() - timeStampMillis,
            hours = delta / 3_600_000L;        
            if (hours < 24L) {
                if (hours % 10L == 1L)
                    return ClientReference.localize("oxygen_core.gui.hour", hours);
                else {
                    if (hours < 1L) {
                        long minutes = delta / 60_000L;
                        if (minutes % 10L == 1L)
                            return ClientReference.localize("oxygen_core.gui.minute", minutes);
                        else               
                            return ClientReference.localize("oxygen_core.gui.minutes", minutes);
                    }
                    return ClientReference.localize("oxygen_core.gui.hours", hours);
                }
            } else {
                long days = hours / 24L;
                if (days % 10L == 1L)
                    return ClientReference.localize("oxygen_core.gui.day", days);
                else               
                    return ClientReference.localize("oxygen_core.gui.days", days);
            }
        } else
            return ClientReference.localize("oxygen_core.gui.undef");
    }

    public static String getExpirationTimeLocalizedString(long expiresInMillis, long timeStampMillis) {
        if (timeStampMillis > 0L) {
            long 
            deltaPassed = System.currentTimeMillis() - timeStampMillis,
            minutesPassed = deltaPassed / 60_000L,
            expiresInMinutes = expiresInMillis / 60_000L,
            expirationTimeMinutes = expiresInMinutes - minutesPassed;  
            if (expirationTimeMinutes < 0L)
                return ClientReference.localize("oxygen_core.gui.expired");
            if (expirationTimeMinutes < 60L) {
                if (expirationTimeMinutes % 10L == 1L)
                    return ClientReference.localize("oxygen_core.gui.minute", expirationTimeMinutes);
                else
                    return ClientReference.localize("oxygen_core.gui.minutes", expirationTimeMinutes);
            } else {
                minutesPassed /= 60L;
                expiresInMinutes /= 60L;
                expirationTimeMinutes = expiresInMinutes - minutesPassed;  
                if (expirationTimeMinutes < 24L) {
                    if (expirationTimeMinutes % 10L == 1L)
                        return ClientReference.localize("oxygen_core.gui.hour", expirationTimeMinutes);
                    else
                        return ClientReference.localize("oxygen_core.gui.hours", expirationTimeMinutes);
                } else {
                    long expirationTimeDays = expirationTimeMinutes / 24L;
                    if (expirationTimeDays % 10L == 1L)
                        return ClientReference.localize("oxygen_core.gui.day", expirationTimeDays);
                    else               
                        return ClientReference.localize("oxygen_core.gui.days", expirationTimeDays);
                }
            }
        } else
            return ClientReference.localize("oxygen_core.gui.undef");
    }
}
