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
                builder.append(",");
            index++;
        }
        return builder.reverse().toString();
    }

    public static String getTimePassedLocalizedString(long timeStampMillis) {
        if (timeStampMillis > 0L) {
            long 
            delta = System.currentTimeMillis() - timeStampMillis,
            hours = delta / 3_600_000L;        
            if (hours < 24L) {
                if (hours % 10L == 1L)
                    return ClientReference.localize("oxygen.gui.hour", hours);
                else {
                    if (hours < 1L) {
                        long minutes = delta / 60_000L;
                        if (minutes % 10L == 1L)
                            return ClientReference.localize("oxygen.gui.minute", minutes);
                        else               
                            return ClientReference.localize("oxygen.gui.minutes", minutes);
                    }
                    return ClientReference.localize("oxygen.gui.hours", hours);
                }
            } else {
                long days = hours / 24L;
                if (days % 10L == 1L)
                    return ClientReference.localize("oxygen.gui.day", days);
                else               
                    return ClientReference.localize("oxygen.gui.days", days);
            }
        } else
            return ClientReference.localize("oxygen.gui.undef");
    }

    public static String getExpirationTimeLocalizedString(long expiresInMillis, long timeStampMillis) {
        if (timeStampMillis > 0L) {
            long 
            deltaPassed = System.currentTimeMillis() - timeStampMillis,
            minutesPassed = deltaPassed / 60_000L,
            expiresInMinutes = expiresInMillis / 60_000L,
            expirationTimeMinutes = expiresInMinutes - minutesPassed;  
            if (expirationTimeMinutes < 0L)
                return ClientReference.localize("oxygen.gui.expired");
            if (expirationTimeMinutes < 60L) {
                if (expirationTimeMinutes % 10L == 1L)
                    return ClientReference.localize("oxygen.gui.minute", expirationTimeMinutes);
                else
                    return ClientReference.localize("oxygen.gui.minutes", expirationTimeMinutes);
            } else {
                minutesPassed /= 60L;
                expiresInMinutes /= 60L;
                expirationTimeMinutes = expiresInMinutes - minutesPassed;  
                if (expirationTimeMinutes < 24L) {
                    if (expirationTimeMinutes % 10L == 1L)
                        return ClientReference.localize("oxygen.gui.hour", expirationTimeMinutes);
                    else
                        return ClientReference.localize("oxygen.gui.hours", expirationTimeMinutes);
                } else {
                    long expirationTimeDays = expirationTimeMinutes / 24L;
                    if (expirationTimeDays % 10L == 1L)
                        return ClientReference.localize("oxygen.gui.day", expirationTimeDays);
                    else               
                        return ClientReference.localize("oxygen.gui.days", expirationTimeDays);
                }
            }
        } else
            return ClientReference.localize("oxygen.gui.undef");
    }
}
